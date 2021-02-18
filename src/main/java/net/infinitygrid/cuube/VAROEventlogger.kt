/*
 * Copyright (c) 2021 Christopher Käding
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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