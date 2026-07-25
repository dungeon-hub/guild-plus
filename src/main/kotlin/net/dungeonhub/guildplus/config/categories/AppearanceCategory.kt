package net.dungeonhub.guildplus.config.categories

import com.teamresourceful.resourcefulconfig.api.annotations.Comment
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigObject
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption
import com.teamresourceful.resourcefulconfig.api.types.info.ListEntryInfoProvider
import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfig.common.loader.elements.ParsedListEntryElement
import com.teamresourceful.resourcefulconfig.common.loader.entries.ParsedListEntry
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import net.dungeonhub.guildplus.feature.BridgeChatFeature
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import java.awt.Color
import kotlin.reflect.jvm.javaField

object AppearanceCategory : CategoryKt("appearance") {
    override val name: TranslatableValue
        get() = Literal("Appearance")

    init {
        button {
            title = "Example appearance"
            description = "See an example in your chat."
            text = "Send example"
            onClick {
                Minecraft.getInstance().execute {
                    Minecraft.getInstance().setScreen(null)
                    Minecraft.getInstance().gui.chat.addClientSystemMessage(
                        BridgeChatFeature.buildBridgeMessage("Example $separator ", prefixColor, "Taubsie", userColor, "Hello there!", messageColor)
                    )
                }
            }
        }
    }

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

    val separator by string("separator", ">") {
        name = Literal("Separator")
        description = Literal("The separator between the bridge prefix and the message.")
    }

    @JvmField
    @ConfigEntry(id = "bridge_message_overwrite_by_tag", translation = "Message overwrite by tag")
    @Comment(value = "If your bridge bot supports origin tags, you can choose to overwrite the colors and other settings per origin tag.")
    var bridgeMessageOverwriteByTags: MutableList<BridgeMessageOverwrite> = mutableListOf(BridgeMessageOverwrite())

    init {
        element(
            ParsedListEntryElement("bridge_message_overwrite_by_tag", ParsedListEntry(
                ::bridgeMessageOverwriteByTags.javaField, BridgeMessageOverwrite::class.java, mutableListOf<BridgeMessageOverwrite>()
            ))
        )
    }

    @ConfigObject
    class BridgeMessageOverwrite : ListEntryInfoProvider {
        @JvmField
        @ConfigEntry(id = "tag", translation = "Origin Tag")
        @Comment(value = "If the origin tag is e.g.: \"[DC] Taubsie\", you would just set \"DC\" here")
        var tag: String = ""

        @JvmField
        @ConfigEntry(id = "prefix_color", translation = "Prefix color")
        @ConfigOption.Color
        var prefixColor: Int = Color(0x00AAAA).rgb

        @JvmField
        @ConfigEntry(id = "user_color", translation = "User color")
        @ConfigOption.Color
        var userColor: Int = Color(0xFF55FF).rgb

        @JvmField
        @ConfigEntry(id = "message_color", translation = "Message color")
        @ConfigOption.Color
        var messageColor: Int = Color(0xFFFFFF).rgb

        @JvmField
        @ConfigEntry(id = "prefix", translation = "Prefix")
        @Comment(value = "Enter the message prefix.")
        var prefix: String = ""

        @JvmField
        @ConfigEntry(id = "display_tag", translation = "Display tag")
        @Comment(value = "Select if you still want to see the tag or not.")
        var displayTag: Boolean = true

        @JvmField
        @ConfigEntry(id = "enabled", translation = "Enabled")
        var enabled: Boolean = true

        override fun getTitle(index: Int): Component {
            return Component.literal("[$tag]")
        }

        override fun getDescription(index: Int): Component {
            return Component.literal("").append(
                BridgeChatFeature.buildBridgeMessage(
                    "${prefix.takeIf { it.isNotBlank() } ?: "Bridge"} $separator ",
                    prefixColor,
                    "${if (displayTag) "[$tag] " else ""}Taubsie",
                    userColor,
                    "Hello there!",
                    messageColor
                )
            )
        }
    }
}