package xyz.tildejustin.ctm_srigt_integration.mixin;

import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.ctm_srigt_integration.CTMCategory;
import xyz.tildejustin.ctm_srigt_integration.CTMTimer;

import java.util.function.BiFunction;

@Mixin(ClientWorld.class)
public abstract class WorldMixin extends World {
    protected WorldMixin(LevelProperties levelProperties, DimensionType dimensionType, BiFunction<World, Dimension, ChunkManager> chunkManagerProvider, Profiler profiler, boolean isClient) {
        super(levelProperties, dimensionType, chunkManagerProvider, profiler, isClient);
    }

    @Inject(
            method = "updateListeners",
            at = @At(
                    value = "TAIL"
            )
    )
    private void ctm_srigt_integration$setBlockState(BlockPos blockPos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        InGameTimer timer = InGameTimer.getInstance();
        CTMCategory category = CTMTimer.categoryLinker.get(timer.getCategory());
        if (category == null || flags != 11) {
            return;
        }
        for (CTMCategory.Entry criterion : category.criteria) {
            System.out.println(blockPos);
            System.out.println(criterion.position);
            System.out.println();
            if (blockPos.equals(criterion.position)) {
                if (this.checkCriteria(category)) {
                    InGameTimer.complete();
                    return;
                }
            }
        }
    }

    // this runs four times on every correct positional block update, I don't know why
    // doesn't impact performance, just unnecessary
    private boolean checkCriteria(CTMCategory category) {
        for (CTMCategory.Entry entry : category.criteria.toArray(new CTMCategory.Entry[0])) {
            boolean correctBlock = this.getBlockState(entry.position).getBlock().equals(Registry.BLOCK.get(entry.identifier));
            System.out.println(this.getBlockState(entry.position).getBlock());
            if (correctBlock) {
                continue;
            }
            return false;
        }
        return true;
    }
}