package net.dungeonhub.guildplus.overlay

import net.dungeonhub.promptoverlay.api.render.FiveActionsOverlay
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color

class FiveOptionsTriviaOverlay(question: String, options: List<String>) : FiveActionsOverlay {
    override val firstText = "[${firstOptionKey()}] ${options[0]}"
    override val secondText = "[${secondOptionKey()}] ${options[1]}"
    override val thirdText = "[${thirdOptionKey()}] ${options[2]}"
    override val fourthText = "[${fourthOptionKey()}] ${options[3]}"
    override val fifthText = "[${fifthOptionKey()}] ${options[4]}"

    override fun firstOption() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("gc A")
        }
    }

    override fun secondOption() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("gc B")
        }
    }

    override fun thirdOption() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("gc C")
        }
    }

    override fun fourthOption() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("gc D")
        }
    }

    override fun fifthOption() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("gc E")
        }
    }

    override val borderColor: Color = Color.GREEN
    override val message = Component.literal(question)
}