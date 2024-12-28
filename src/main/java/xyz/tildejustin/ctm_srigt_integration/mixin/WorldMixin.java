package xyz.tildejustin.ctm_srigt_integration.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.tildejustin.ctm_srigt_integration.*;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow
    public abstract int getBlock(int x, int y, int z);

    @Shadow
    public abstract int getBlockData(int x, int y, int z);

    @Dynamic
    @Inject(method = {"method_3601(IIIIIZ)Z", "method_4721(IIIIII)Z"}, at = @At(value = "RETURN"), require = 1)
    private void setBlockState(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true, ordinal = 0) int x, @Local(argsOnly = true, ordinal = 1) int y, @Local(argsOnly = true, ordinal = 2) int z) {
        InGameTimer timer = InGameTimer.getInstance();
        CTMCategory category = CTMTimer.categoryLinker.get(timer.getCategory());
        if (category == null) {
            return;
        }
        for (CTMCategory.Entry criterion : category.criteria) {
            if (new BlockPos(x, y, z).equals(criterion.position)) {
                if (this.checkCriteria(category)) {
                    InGameTimer.complete();
                }
                return;
            }
        }
    }

    // this runs four times on every correct positional block update, I don't know why
    // doesn't impact performance, just unnecessary
    @Unique
    private boolean checkCriteria(CTMCategory category) {
        for (CTMCategory.Entry entry : category.criteria) {
            boolean correctBlock = this.getBlock(entry.position.x, entry.position.y, entry.position.z) == entry.identifier;
            if (correctBlock) {
                boolean correctData = this.getBlockData(entry.position.x, entry.position.y, entry.position.z) == entry.dataValue;
                if (correctData) {
                    continue;
                }
                return false;
            }
            return false;
        }
        return true;
    }
}