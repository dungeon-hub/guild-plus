package net.dungeonhub.guildplus.mixin;

import net.dungeonhub.guildplus.feature.BridgeChatFeature;
import net.dungeonhub.guildplus.feature.DiscordWarningRemover;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ChatComponent.class, priority = 800)
public abstract class BridgeChatMixin {
    @Shadow
    public abstract void addClientSystemMessage(Component message);

    @Inject(method = "addServerSystemMessage", at = @At("HEAD"), cancellable = true)
    public void addServerSystemMessage(Component message, CallbackInfo ci) {
        if (BridgeChatFeature.INSTANCE.shouldBlockBridgeMessage(message)) {
            ci.cancel();
            return;
        }

        Component discordWarningResult = DiscordWarningRemover.INSTANCE.handleMessage(message);
        Component bridgeResult = BridgeChatFeature.INSTANCE.handleBridgeMessage(discordWarningResult != null ? discordWarningResult : message);

        if(bridgeResult != null) {
            addClientSystemMessage(bridgeResult);
            ci.cancel();

            return;
        }

        if(discordWarningResult != null) {
            addClientSystemMessage(discordWarningResult);
            ci.cancel();
        }
    }
}
