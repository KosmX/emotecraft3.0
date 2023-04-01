package dev.kosmx.emotecraft3

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier

object ServerNetwork {

    val s2cIdentifier = Identifier(MODID, "s2c")
    val c2sIdentifier = Identifier(MODID, "c2s")
    fun init() {
        ServerPlayNetworking.registerGlobalReceiver(c2sIdentifier)
        { _, sourcePlayer, _, _, _ ->

            val buf = PacketByteBufs.create().apply {
                writeUuid(sourcePlayer.uuid)
            }

            PlayerLookup.tracking(sourcePlayer.world as ServerWorld, sourcePlayer.blockPos)
                .filter { it !== sourcePlayer }
                .forEach { player ->
                    ServerPlayNetworking.send(player, s2cIdentifier, buf)
                }
        }
    }
}