package net.dungeonhub.guildplus.features

import io.mockk.*
import net.dungeonhub.guildplus.GuildPlus
import net.dungeonhub.guildplus.config.categories.AppearanceCategory
import net.dungeonhub.guildplus.config.categories.FeaturesCategory
import net.dungeonhub.guildplus.feature.BridgeChatFeature
import net.dungeonhub.guildplus.overlay.FiveOptionsTriviaOverlay
import net.dungeonhub.promptoverlay.PromptOverlayApi
import net.dungeonhub.promptoverlay.api.KeyMappingProvider
import net.dungeonhub.promptoverlay.api.render.Overlay
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.chat.Component
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BridgeChatTest {
    private val tempConfigDir: Path = Files.createTempDirectory("guildplus-test")

    @Test
    fun testBridgeChatParsing() {
        val formatResult = BridgeChatFeature.handleBridgeMessage(Component.literal("Guild > [VIP] DHMain [Admin]: [DC] Taubsie: test2 @qX"))

        assertNotNull(formatResult)
        assertEquals("Bridge > [DC] Taubsie: test2 @qX", formatResult.string)
    }

    @Test
    fun testUsernameDetection() {
        val overwrite = AppearanceCategory.BridgeMessageOverwrite()
        overwrite.tag = "TAG"
        overwrite.prefix = "OverwrittenPrefix"
        overwrite.displayTag = false

        mockkObject(AppearanceCategory)
        AppearanceCategory.bridgeMessageOverwriteByTags = mutableListOf(overwrite)

        val formatResult = BridgeChatFeature.handleBridgeMessage(Component.literal("Guild > [VIP] DHMain [Admin]: [TAG] Ploik: Party > [MVP+] GoldenSword6: some chat message"))

        assertNotNull(formatResult)
        assertEquals("OverwrittenPrefix > Ploik: Party > [MVP+] GoldenSword6: some chat message", formatResult.string)
    }

    @Test
    fun testMessageOverwrite() {
        val overwrite = AppearanceCategory.BridgeMessageOverwrite()
        overwrite.tag = "TAG"
        overwrite.prefix = "OverwrittenPrefix"

        mockkObject(AppearanceCategory)
        AppearanceCategory.bridgeMessageOverwriteByTags = mutableListOf(overwrite)

        val testMessage = "Guild > [VIP] DHMain [Admin]: [TAG] Taubsie: test2 @qX"

        assertEquals("OverwrittenPrefix > [TAG] Taubsie: test2 @qX", BridgeChatFeature.handleBridgeMessage(Component.literal(testMessage))?.string)
    }

    @Test
    fun testTriviaOverlay() {
        mockkObject(BridgeChatFeature, recordPrivateCalls = true)

        val testMessage = "Guild > [VIP] DHMain [Admin]: Quick Trivia: Which desert stretches across northern China and southern Mongolia? " +
                "A. Sahara " +
                "B. Atacama " +
                "C. Mojave " +
                "D. Kalahari " +
                "E. Gobi"

        BridgeChatFeature.handleBridgeMessage(Component.literal(testMessage))

        verify(exactly = 1) { BridgeChatFeature["handleBotMessage"](any<String>(), any<Boolean>()) }
        verify(exactly = 1) { PromptOverlayApi.setOverlay(any<FiveOptionsTriviaOverlay>()) }
    }

    @BeforeTest
    fun setup() {
        mockkStatic(FabricLoader::class)
        val mockLoader = mockk<FabricLoader>(relaxed = true)
        every { FabricLoader.getInstance() } returns mockLoader
        every { mockLoader.isDevelopmentEnvironment } returns false
        every { mockLoader.configDir } returns tempConfigDir
        every { mockLoader.isModLoaded(any<String>()) } returns true

        mockkObject(GuildPlus)
        every { GuildPlus.isDev } returns false

        mockkObject(FeaturesCategory)
        every { FeaturesCategory.formatBridgeChat } returns true
        every { FeaturesCategory.bridgeUsers } returns arrayOf("DHMain")

        mockkObject(PromptOverlayApi)
        every { PromptOverlayApi["getKeyMappingProvider"]() }.answers {
            object : KeyMappingProvider {
                override val acceptKeyName = "Y"
                override val denyKeyName = "N"
                override val dismissKeyName = "X"
                override val firstOptionKeyName = "1"
                override val secondOptionKeyName = "2"
                override val thirdOptionKeyName = "3"
                override val fourthOptionKeyName = "4"
                override val fifthOptionKeyName = "5"
            }
        }

        mockkStatic(PromptOverlayApi::class)
        every { PromptOverlayApi.setOverlay(any<Overlay>()) }.returns(true)
    }
}
