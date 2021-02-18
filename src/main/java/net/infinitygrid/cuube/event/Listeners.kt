package net.infinitygrid.cuube.event

import net.infinitygrid.cuube.VAROEventLogger
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class Listeners : Listener {

    private val bot = VAROEventLogger.INSTANCE.discordBot

    @EventHandler fun onJoin(e: PlayerJoinEvent) {
        bot?.announce(e)
    }

    @EventHandler fun onQuit(e: PlayerQuitEvent) {
        bot?.announce(e)
    }

    @EventHandler fun onDeath(e: PlayerDeathEvent) {
        bot?.announce(e)
    }

}