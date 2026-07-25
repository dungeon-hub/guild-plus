package net.dungeonhub.guildplus.config.categories

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt

object FeaturesCategory : CategoryKt("features") {
    override val name: TranslatableValue
        get() = Literal("Features")

    val formatBridgeChat by boolean("format_bridge_chat", true) {
        name = Literal("Format Bridge messages")
        description = Literal("Enable this if you want guild chat messages by your bridge bot to be formatted.")
    }

    val bridgeUsers by stringsWithId("bridge_bots") {
        name = Literal("Bridge Bot Users")
        description = Literal("Add the bridge bots in your guild here.")
    }
}