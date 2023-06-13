package xyz.tildejustin.ctm_srigt_integration.mixin;

import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.ctm_srigt_integration.CTMCategory;
import xyz.tildejustin.ctm_srigt_integration.CTMTimer;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class WorldMixin extends World {

    protected WorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
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
            if (blockPos.equals(criterion.position)) {
                if (this.checkCriteria(category)) {
                    InGameTimer.complete();
                    return;
                }
            }
        }
    }

    private boolean checkCriteria(CTMCategory category) {
        for (CTMCategory.Entry entry : category.criteria.toArray(new CTMCategory.Entry[0])) {
            boolean correctBlock = this.getBlockState(entry.position).getBlock().equals(Registries.BLOCK.get(entry.identifier));
            if (correctBlock) {
                continue;
            }
            return false;
        }
        return true;
    }
}