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