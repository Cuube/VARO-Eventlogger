package net.infinitygrid.cuube.discord

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.infinitygrid.cuube.VAROEventLogger
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.logging.Level


fun String.insertPlayer(p: Player): String {
    return this.replace("%p", p.displayName)
}

class DiscordBot(private val token: String) {

    private val jda = connect()
    val presence = jda.presence
    private val plugin = VAROEventLogger.INSTANCE
    private val config = plugin.pluginConfig
    private val logger = plugin.logger
    private val mainChannel = jda.getTextChannelById(config.channels.main)
    private val coordinateChannel = jda.getTextChannelById(config.channels.coordinates)

    init {
        if (mainChannel == null) {
            logger.log(Level.SEVERE, "A channel with the ID ${config.channels.main} does not exist.")
            plugin.disable()
        }
        if (coordinateChannel == null) {
            logger.log(Level.SEVERE, "A channel with the ID ${config.channels.coordinates} does not exist.")
            plugin.disable()
        }
    }

    private fun connect(): JDA {
        val builder = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.ONLINE)
        val jda = builder.build()
        jda.awaitReady()
        return jda
    }

    fun announce(e: Event) {
        var p: Player? = null
        var type: BotMessageType? = null
        var extra: String? = null
        var coords = false
        when (e) {
            is PlayerJoinEvent -> {
                p = e.player
                type = BotMessageTypes.LOGON
            }
            is PlayerQuitEvent -> {
                p = e.player
                type = BotMessageTypes.LOGOFF
                coords = true
            }
            is PlayerDeathEvent -> {
                p = e.entity
                type = BotMessageTypes.DEATH
                extra = e.deathMessage
            }
        }
        if (p != null && type != null) {
            sendMessage(p, type, extra, coords)
        }
    }

    private fun sendMessage(p: Player, type: BotMessageType, extra: String? = null, coordinates: Boolean = false) {
        val headURL = "https://crafatar.com/renders/head/${p.uniqueId}?overlay&scale=2"
        val mainEmbed = EmbedBuilder()
                .setAuthor(type.message.insertPlayer(p), null, headURL)
                .setColor(type.hex.toInt(16))
        if (extra != null) {
            mainEmbed.setDescription(extra)
        }

        mainChannel!!.sendMessage(mainEmbed.build()).queue()
        if (coordinates) {
            val loc = p.location
            mainEmbed.addField(config.messages.worldName, "`${loc.world.name}`", true)
                    .addField(config.messages.worldType, "`${loc.world.environment.name}`", true)
                    .addBlankField(true)
                    .addField("X", "`${loc.blockX}`", true)
                    .addField("Y", "`${loc.blockY}`", true)
                    .addField("Z", "`${loc.blockZ}`", true)
                    .addField("HP", getEmojiHealthBar(p), false)
            coordinateChannel!!.sendMessage(mainEmbed.build()).queue()
        }
    }

    private fun getEmojiHealthBar(p: Player): String {
        val s = StringBuilder()
        val fullEmote = Activity.Emoji(":heart:")
        val halfEmote = Activity.Emoji(":broken_heart:")
        val emptyEmote = Activity.Emoji(":black_heart:")
        val health = p.health / 2
        val scale = p.healthScale / 2
        val healthDecimal = health % 1
        println(healthDecimal)
        val healthGone = scale - health
        for (i in 1..health.toInt()) {
            s.append(fullEmote.asMention)
        }
        if (healthDecimal >= .5) s.append(halfEmote.asMention);
        for (i in 1..healthGone.toInt()) {
            s.append(emptyEmote.asMention)
        }
        return s.toString()
    }

}