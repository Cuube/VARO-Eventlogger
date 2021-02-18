package net.infinitygrid.cuube.config

import com.google.gson.GsonBuilder
import java.io.File

class ConfigFile(private val dir: File) {

    companion object {
        val gson = GsonBuilder().setPrettyPrinting().serializeNulls().setVersion(.2).create()!!
        const val fileName = "config.json"
    }

    private val configFile = File("$dir/$fileName")

    init {
        if (!configFile.exists()) save(Config())
    }

    private fun save(config: Config) {
        if (!dir.exists()) dir.mkdirs()
        if (!configFile.exists()) configFile.createNewFile()
        val jsonString = gson.toJson(config)
        configFile.writeText(jsonString)
    }

    fun get(): Config {
        val fileContent = configFile.readText()
        val configClass = gson.fromJson(fileContent, Config::class.java)
        save(configClass)
        return configClass
    }

}