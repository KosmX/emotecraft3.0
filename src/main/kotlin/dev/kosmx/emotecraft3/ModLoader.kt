package dev.kosmx.emotecraft3

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// This logger is used to write text to the console and the log file.
// It is considered best practice to use your mod id as the logger's name.
// That way, it's clear which mod wrote info, warnings, and errors.


@JvmField
val LOGGER: Logger = LoggerFactory.getLogger("modid")

const val MODID = "emotecraft3"

@Suppress("unused")
object ModLoader : ModInitializer {
    override fun onInitialize() {
        ServerNetwork.init()
    }
}