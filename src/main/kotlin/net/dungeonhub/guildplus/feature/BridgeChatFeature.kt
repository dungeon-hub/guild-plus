package net.dungeonhub.guildplus.feature

import net.dungeonhub.guildplus.config.categories.AppearanceCategory
import net.dungeonhub.guildplus.config.categories.FeaturesCategory
import net.dungeonhub.guildplus.overlay.FiveOptionsTriviaOverlay
import net.dungeonhub.guildplus.overlay.FourOptionsTriviaOverlay
import net.dungeonhub.guildplus.util.MessageUtil.sendDevDebug
import net.dungeonhub.guildplus.util.MessageUtil.sendMessage
import net.dungeonhub.promptoverlay.PromptOverlayApi
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory
import java.util.regex.Pattern

object BridgeChatFeature {
    private val logger = LoggerFactory.getLogger(BridgeChatFeature::class.java)
    private val pattern = Pattern.compile("^(?<type>Guild|Officer) > (?:\\[[A-Z]+\\+*] )?(?<bot>(?:[A-z]|[0-9]|_){3,16})(?: \\[(?:[A-z]|[0-9]|_)+])?: ((?<user>[^:> ]+)(?::| >) )?(?<message>(?:\\s|.)*)")

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
                    handleBotMessage(message)

                    messageComponent
                }
            )
    }

    private fun handleBotMessage(message: String) {
        val triviaRegex = Regex("Quick Trivia: (?<question>.*?) (?<options>[A-E]\\..*)")

        val match = triviaRegex.matchEntire(message) ?: return

        val question = match.groups["question"]?.value ?: return
        val optionsText = match.groups["options"]?.value ?: return

        val optionsRegex = Regex("""[A-E]\.\s*(.*?)(?=\s+[A-E]\.\s|$)""")

        val options = optionsRegex.findAll(optionsText).map { it.groupValues[1] }.toList()

        when (options.size) {
            4 -> {
                PromptOverlayApi.setOverlay(FourOptionsTriviaOverlay(question, options))
            }
            5 -> {
                PromptOverlayApi.setOverlay(FiveOptionsTriviaOverlay(question, options))
            }
            else -> {
                Minecraft.getInstance().sendMessage(Component.literal("[G+] Encountered a trivia quiz with an incorrect amount of options. Please report this!").withStyle(ChatFormatting.RED))
            }
        }
    }
}