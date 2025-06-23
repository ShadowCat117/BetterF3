package me.cominixo.betterf3.mixin.debugcrosshair;

import me.cominixo.betterf3.config.GeneralOptions;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Main debug crosshair removal mixin.
 */
@Mixin(value = Gui.class, priority = 1100)
public class DebugCrosshairMixin {

  /**
   * Removes the debug crosshair if the option is enabled.
   *
   * @param cir Callback info returnable
   */
  @Inject(method = "shouldRenderDebugCrosshair", at = @At(value = "HEAD"), cancellable = true)
  public void removeDebugCrosshair(final CallbackInfoReturnable<Boolean> cir) {
    if (!GeneralOptions.disableMod && GeneralOptions.hideDebugCrosshair) {
      cir.setReturnValue(false);
      cir.cancel();
    }
  }
}
