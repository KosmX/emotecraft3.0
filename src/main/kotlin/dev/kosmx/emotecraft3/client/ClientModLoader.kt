package dev.kosmx.emotecraft3.client

import dev.kosmx.emotecraft3.ServerNetwork
import dev.kosmx.playerAnim.api.layered.IAnimation
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer
import dev.kosmx.playerAnim.api.layered.ModifierLayer
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier
import dev.kosmx.playerAnim.core.data.KeyframeAnimation
import dev.kosmx.playerAnim.core.util.Ease
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry
import dev.kosmx.playerAnim.minecraftApi.layers.LeftHandedHelperModifier
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

object ClientModLoader : ClientModInitializer {

    val layerIdentifier = Identifier("emotecraft3", "waving")

    val waving: KeyframeAnimation by lazy {
        PlayerAnimationRegistry.getAnimation(Identifier("emotecraft3", "waving"))!!
    }

    private val waveKeyBind = KeyBinding("key.emotecraft3", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "category.emotecraft3")
    override fun onInitializeClient() {

        // Initialize client-side network listener
        ClientNetwork.init()

        // player animation lib handler
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(layerIdentifier, 16) {
            // Demo the left handed helper modifier
            ModifierLayer<IAnimation>(null, LeftHandedHelperModifier(it))
        }

        // key bind register and handler
        KeyBindingHelper.registerKeyBinding(waveKeyBind)

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            while (waveKeyBind.wasPressed()) {
                client.player?.playWaving()
                ClientPlayNetworking.send(ServerNetwork.c2sIdentifier, PacketByteBufs.empty())
            }
        }
    }

}

fun AbstractClientPlayerEntity.playWaving() {
    (PlayerAnimationAccess.getPlayerAssociatedData(this)[ClientModLoader.layerIdentifier] as? ModifierLayer<IAnimation>)
        ?.replaceAnimationWithFade(
            AbstractFadeModifier.standardFadeIn(12, Ease.INOUTCUBIC),
            KeyframeAnimationPlayer(ClientModLoader.waving)
        )
}
