package net.dungeonhub.guildplus.mixin;

import net.dungeonhub.guildplus.feature.BridgeChatFeature;
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
        Component result = BridgeChatFeature.INSTANCE.handleBridgeMessage(message);

        if(result != null) {
            addClientSystemMessage(result);
            ci.cancel();
        }
    }
}
