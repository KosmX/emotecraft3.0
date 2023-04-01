package dev.kosmx.emotecraft3.client

import dev.kosmx.emotecraft3.ServerNetwork
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.network.AbstractClientPlayerEntity
import java.util.*

object ClientNetwork {
    fun init() {

        // listen on plugin channel
        ClientPlayNetworking.registerGlobalReceiver(ServerNetwork.s2cIdentifier)
        { client, _, buf, _ ->

            val uuid: UUID = buf.readUuid()
            client.execute {

                client.world?.getPlayerByUuid(uuid)?.let { player ->
                    (player as AbstractClientPlayerEntity).playWaving()
                }
            }
        }
    }
}