package net.dungeonhub.guildplus.config.categories

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt

object FeaturesCategory : CategoryKt("features") {
    override val name: TranslatableValue
        get() = Literal("Features")

    val bridgeUsers by stringsWithId("bridge_bots") {
        name = Literal("Bridge Bot Users")
        description = Literal("Add the bridge bots in your guild here.")
    }
}