package net.dungeonhub.guildplus.overlay

import net.dungeonhub.promptoverlay.api.render.FiveActionsOverlay
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color

class FiveOptionsTriviaOverlay(question: String, options: List<String>, isOfficer: Boolean) : FourOptionsTriviaOverlay(question, options, isOfficer), FiveActionsOverlay {
    override val fifthText = "[${fifthOptionKey()}] ${options[4]}"

    override fun fifthOption() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().player?.connection?.sendCommand("$command E")
        }
    }

    override val borderColor: Color = Color.GREEN
    override val message = Component.literal(question)
}