package net.dungeonhub.guildplus.feature

import net.dungeonhub.guildplus.config.categories.FeaturesCategory
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

object DiscordWarningRemover {
    fun handleMessage(component: Component): Component? {
        if(!FeaturesCategory.hideDiscordWarning) {
            return null
        }

        val cleanedMessage = ChatFormatting.stripFormatting(component.string) ?: return null

        if (!cleanedMessage.startsWith("Guild >") || !cleanedMessage.endsWith("\n$DISCORD_WARNING")) {
            return null
        }

        return copyWithoutWarning(component)
    }

    private fun copyWithoutWarning(component: Component): Component {
        val copy = component.plainCopy().setStyle(component.style)

        component.siblings
            .filterNot { it.string == "\n" || it.string == DISCORD_WARNING }
            .map(::copyWithoutWarning)
            .forEach(copy::append)

        return copy
    }

    private const val DISCORD_WARNING =
        "Please be mindful of Discord links in chat as they may pose a security risk"
}
