package net.dungeonhub.guildplus.overlay

import net.dungeonhub.promptoverlay.api.render.FourActionsOverlay
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color

open class FourOptionsTriviaOverlay(question: String, options: List<String>, isOfficer: Boolean): FourActionsOverlay {
    override val firstText = "[${firstOptionKey()}] ${options[0]}"
    override val secondText = "[${secondOptionKey()}] ${options[1]}"
    override val thirdText = "[${thirdOptionKey()}] ${options[2]}"
    override val fourthText = "[${fourthOptionKey()}] ${options[3]}"

    val command = if(isOfficer) "oc" else "gc"

    override fun firstOption() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("$command A")
        }
    }

    override fun secondOption() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("$command B")
        }
    }

    override fun thirdOption() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("$command C")
        }
    }

    override fun fourthOption() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("$command D")
        }
    }

    override val borderColor: Color = Color.GREEN
    override val message = Component.literal(question)
}