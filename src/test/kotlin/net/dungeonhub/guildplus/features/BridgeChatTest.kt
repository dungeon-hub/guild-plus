package net.dungeonhub.guildplus.features

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import net.dungeonhub.guildplus.GuildPlus
import net.dungeonhub.guildplus.config.categories.FeaturesCategory
import net.dungeonhub.guildplus.feature.BridgeChatFeature
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BridgeChatTest {
    private val logger = LoggerFactory.getLogger("BridgeChatTest")
    private val tempConfigDir: Path = Files.createTempDirectory("guildplus-test")

    @Test
    fun testBridgeChatParsing() {
        val formatResult = BridgeChatFeature.formatBridgeMessage(Component.literal("Guild > [VIP] DHMain [Admin]: [DC] Taubsie: test2 @qX"))

        assertNotNull(formatResult)
        assertEquals("Bridge > [DC] Taubsie: test2 @qX", formatResult.string)
    }

    @BeforeTest
    fun setup() {
        mockkStatic(FabricLoader::class)
        val mockLoader = mockk<FabricLoader>(relaxed = true)
        every { FabricLoader.getInstance() } returns mockLoader
        every { mockLoader.isDevelopmentEnvironment } returns false
        every { mockLoader.configDir } returns tempConfigDir

        mockkObject(GuildPlus)
        every { GuildPlus.isDev } returns false

        mockkObject(FeaturesCategory)
        every { FeaturesCategory.bridgeUsers } returns arrayOf("DHMain")
    }
}
