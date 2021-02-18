package net.infinitygrid.cuube.discord

import net.infinitygrid.cuube.VAROEventLogger

data class BotMessageType(val message: String, val hex: String)

object BotMessageTypes {

    private val config = VAROEventLogger.INSTANCE.pluginConfig
    val LOGON = BotMessageType(config.messages.logon, "4bfa85")
    val LOGOFF = BotMessageType(config.messages.logoff, "ed4c4c")
    val DEATH = BotMessageType(config.messages.death, "993131")

}