package xyz.tildejustin.ctm_srigt_integration.mixin;

import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.minecraft.block.Block;
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

    @Shadow public abstract Block getBlock(int x, int y, int z);

    @Shadow public abstract int getBlockData(int x, int y, int z);

    @Inject(
            method = "setBlock",
            at = @At(
                    value = "HEAD"
            )
    )
    private void ctm_srigt_integration$setBlockState(int x, int y, int z, Block block, int flags, int data, CallbackInfoReturnable<Boolean> cir) {
        InGameTimer timer = InGameTimer.getInstance();
        CTMCategory category = CTMTimer.categoryLinker.get(timer.getCategory());
        if (category == null || flags != 3) {
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
    private boolean checkCriteria(CTMCategory category) {
        for (CTMCategory.Entry entry : category.criteria.toArray(new CTMCategory.Entry[0])) {
            boolean correctBlock = this.getBlock(entry.position.x, entry.position.y, entry.position.z).equals(Block.REGISTRY.get(entry.identifier));
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