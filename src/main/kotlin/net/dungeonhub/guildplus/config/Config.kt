package net.dungeonhub.guildplus.config

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.ConfigKt
import net.dungeonhub.guildplus.GuildPlus
import net.dungeonhub.guildplus.GuildPlus.MOD_ID
import net.dungeonhub.guildplus.config.categories.AppearanceCategory
import net.dungeonhub.guildplus.config.categories.DevCategory
import net.dungeonhub.guildplus.config.categories.FeaturesCategory
import net.minecraft.util.Util
import java.time.LocalDate
import java.time.Month

object Config : ConfigKt("$MOD_ID/config") {
    override val name: TranslatableValue
        get() = Literal("Guild+ ${GuildPlus.version}")

    init {
        val isAprilFools = LocalDate.now().month == Month.APRIL && LocalDate.now().dayOfMonth == 1

        if(isAprilFools) {
            separator {
                title = "Thanks for using Guild++ - your new subscription-based mod!"
                description = "Now only 7.99 for a limited time! No new features, but free and open source didn't make us any money!"
            }
        } else {
            separator {
                title = "Thanks for using Guild+"
            }
        }

        button {
            title = "GitHub"
            description = "This is open source!"
            text = "Open"
            onClick {
                Util.getPlatform().openUri("https://github.com/dungeon-hub/guild-plus")
            }
        }

        button {
            title = "Connect with us"
            description = "For questions and support, check out our discord"
            text = "Join"
            onClick {
                Util.getPlatform().openUri("https://discord.dungeon-hub.net/")
            }
        }

        button {
            title = "Support us"
            description = "Support our development costs and keep the servers running"
            text = "Patreon"
            onClick {
                Util.getPlatform().openUri("https://www.patreon.com/dungeon_hub/")
            }
        }
    }

    var developer by boolean("developer", false) {
        name = Literal("Developer Mode")
        description = Literal("Reopen the config after updating this value.")
    }

    init {
        category(FeaturesCategory)
        category(AppearanceCategory)
        category(DevCategory)
    }
}