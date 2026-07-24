package net.dungeonhub.guildplus.util

import net.dungeonhub.guildplus.GuildPlus
import net.dungeonhub.guildplus.config.categories.DevCategory
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import org.slf4j.Logger

object MessageUtil {
    fun Logger.sendDevError(message: String) {
        if (GuildPlus.isDev) {
            throw RuntimeException(message)
        } else if (DevCategory.extendedDebug) {
            Minecraft.getInstance().execute {
                Minecraft.getInstance().gui.chat.addClientSystemMessage(
                    Component.literal(message).setStyle(Style.EMPTY.withColor(ChatFormatting.RED))
                )
            }
        } else {
            error(message)
        }
    }

    fun Logger.sendDevDebug(message: Component) {
        if (GuildPlus.isDev || DevCategory.extendedDebug) {
            Minecraft.getInstance().execute {
                Minecraft.getInstance().gui.chat.addClientSystemMessage(
                    message
                )
            }
        } else {
            debug(message.string)
        }
    }

    fun Logger.sendDevDebug(message: String) {
        if (GuildPlus.isDev || DevCategory.extendedDebug) {
            Minecraft.getInstance().execute {
                Minecraft.getInstance().gui.chat.addClientSystemMessage(
                    Component.literal(message).setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
                )
            }
        } else {
            debug(message)
        }
    }

    fun Logger.sendDebug(message: String) {
        if (GuildPlus.isDev || DevCategory.extendedDebug) {
            Minecraft.getInstance().execute {
                Minecraft.getInstance().gui.chat.addClientSystemMessage(
                    Component.literal(message).setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
                )
            }
        }

        debug(message)
    }

    /**
     * Sends a chat message and optionally runs additional logic on the main thread.
     * Both the message display and the additionalLogic callback run inside Minecraft.execute {},
     * ensuring they execute on the main Minecraft thread.
     */
    fun Minecraft.sendMessage(component: Component, additionalLogic: () -> Unit = {}) {
        execute {
            gui.chat.addClientSystemMessage(component)
            additionalLogic()
        }
    }
}