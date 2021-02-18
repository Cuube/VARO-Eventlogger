package net.infinitygrid.cuube

import net.dv8tion.jda.api.OnlineStatus
import net.infinitygrid.cuube.config.ConfigFile
import net.infinitygrid.cuube.discord.DiscordBot
import net.infinitygrid.cuube.event.Listeners
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class VAROEventLogger : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: VAROEventLogger

    }

    private val pluginManager = Bukkit.getPluginManager()
    private val configFile = ConfigFile(this.dataFolder)
    val pluginConfig = configFile.get()
    var discordBot: DiscordBot? = null

    override fun onLoad() {
        INSTANCE = this
    }

    override fun onEnable() {
        val value = initDiscordBot()
        if (!value) {
            disable()
        } else {
            registerListener(Listeners())
            logger.log(Level.INFO, "Plugin enabled!")
        }
    }

    override fun onDisable() {
        discordBot?.presence!!.setStatus(OnlineStatus.OFFLINE)
        logger.log(Level.INFO, "Plugin disabled.")
    }

    fun disable() {
        pluginManager.disablePlugin(this)
    }

    private fun registerListener(vararg listeners: Listener) {
        listeners.forEach {
            pluginManager.registerEvents(it, this)
        }
    }

    private fun initDiscordBot(): Boolean {
        val token = pluginConfig.botToken
        return if (token != null) {
            discordBot = DiscordBot(token)
            true
        } else {
            logger.log(Level.WARNING, "No bot token specified in configuration. Disabling plugin.")
            false
        }
    }

}