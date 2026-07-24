package net.dungeonhub.guildplus.config.categories

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import java.awt.Color

object AppearanceCategory : CategoryKt("appearance") {
    override val name: TranslatableValue
        get() = Literal("Apperance")

    val prefixColor by color("prefix_color", Color(0x00AAAA).rgb) {
        name = Literal("Prefix color")
        description = Literal("Change the prefix color.")
    }

    val userColor by color("user_color", Color(0xFF55FF).rgb) {
        name = Literal("User color")
        description = Literal("Change the username color.")
    }

    val messageColor by color("message_color", Color(0xFFFFFF).rgb) {
        name = Literal("Message color")
        description = Literal("Change the message color.")
    }
}