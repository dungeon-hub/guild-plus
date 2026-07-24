package net.dungeonhub.guildplus.feature

import net.dungeonhub.guildplus.config.categories.AppearanceCategory
import net.dungeonhub.guildplus.config.categories.FeaturesCategory
import net.dungeonhub.guildplus.util.MessageUtil.sendDevDebug
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

object BridgeChatFeature {
    private val logger = LoggerFactory.getLogger(BridgeChatFeature::class.java)
    private val pattern = Pattern.compile("^(?<type>Guild|Officer) > (?:\\[[A-Z]+\\+*] )?(?<bot>(?:[A-z]|[0-9]|_){3,16})(?: \\[(?:[A-z]|[0-9]|_)+])?: ((?<user>[^:>]+)(?::| >) )?(?<message>.*)")

    fun formatBridgeMessage(component: Component): Component? {
        val text = ChatFormatting.stripFormatting(component.string) ?: return null
        val matcher = pattern.matcher(text)

        if(!matcher.find() || matcher.groupCount() < 3) return null

        val type = matcher.group("type")
        val isOfficer = type == "Officer"
        val bot = matcher.group("bot")
        val user = matcher.group("user")
        val message = matcher.group("message")

        if(FeaturesCategory.bridgeUsers.none { it.equals(bot, true) }) return null

        logger.sendDevDebug("Original message:")
        logger.sendDevDebug(component)

        val prefix = if (isOfficer) {
            "Bridge (Staff) > "
        } else {
            "Bridge > "
        }

        val messageComponent = Component.literal(message)
            .withColor(AppearanceCategory.messageColor)

        return Component.literal(prefix)
            .withColor(AppearanceCategory.prefixColor)
            .append(
                if(user != null) {
                    Component.literal(user)
                        .withColor(AppearanceCategory.userColor)
                        .append(
                            Component.literal(": ").withStyle(ChatFormatting.GRAY)
                                .append(
                                    messageComponent
                                )
                        )
                } else {
                    messageComponent
                }
            )
    }
}