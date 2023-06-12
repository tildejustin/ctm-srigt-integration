package xyz.tildejustin.ctm_srigt_integration.mixin;

import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.tildejustin.ctm_srigt_integration.CTMCategory;
import xyz.tildejustin.ctm_srigt_integration.CTMTimer;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow
    public abstract BlockState getBlockState(BlockPos pos);

    @Inject(
            method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
            at = @At(
                    value = "HEAD"
            )
    )
    private void ctm_srigt_integration$setBlockState(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<Boolean> cir) {
        InGameTimer timer = InGameTimer.getInstance();
        CTMCategory category = CTMTimer.categoryLinker.get(timer.getCategory());
        if (category == null || flags != 3) {
            return;
        }
        for (CTMCategory.Entry criterion : category.criteria) {
            if (pos.equals(criterion.position)) {
                if (this.checkCriteria(category)) {
                    InGameTimer.complete();
                }
                return;
            }
        }
    }

    // this runs four times on every correct positional block update, I don't know why
    // doesn't impact performance, just unnecessary
    private boolean checkCriteria(CTMCategory category) {
        for (CTMCategory.Entry entry : category.criteria.toArray(new CTMCategory.Entry[0])) {
            boolean correctBlock = this.getBlockState(entry.position).getBlock().equals(Block.REGISTRY.get(entry.identifier));
            if (correctBlock) {
                boolean correctData = this.getBlockState(entry.position).getBlock().getMeta(this.getBlockState(entry.position)) == entry.dataValue;
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