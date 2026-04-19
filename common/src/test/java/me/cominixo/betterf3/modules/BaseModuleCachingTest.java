package me.cominixo.betterf3.modules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.utils.DebugLine;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

class BaseModuleCachingTest {

    @Test
    void cachesFormattedLinesWhenOptimizationEnabled() {
        GeneralOptions.enablePerformanceOptimizations = true;
        final CountingModule module = new CountingModule();

        final List<Component> first = module.cachedLinesFormatted(false);
        final List<Component> second = module.cachedLinesFormatted(false);

        assertEquals(1, module.formatCalls);
        assertSame(first, second);
    }

    @Test
    void invalidatesCachedLinesWhenMarkedDirty() {
        GeneralOptions.enablePerformanceOptimizations = true;
        final CountingModule module = new CountingModule();

        module.cachedLinesFormatted(false);
        module.markDirty();
        module.cachedLinesFormatted(false);

        assertEquals(2, module.formatCalls);
    }

    @Test
    void recomputesWhenReducedDebugStateChanges() {
        GeneralOptions.enablePerformanceOptimizations = true;
        final CountingModule module = new CountingModule();

        module.cachedLinesFormatted(false);
        module.cachedLinesFormatted(true);

        assertEquals(2, module.formatCalls);
    }

    @Test
    void bypassesCacheWhenOptimizationDisabled() {
        GeneralOptions.enablePerformanceOptimizations = false;
        final CountingModule module = new CountingModule();

        final List<Component> first = module.cachedLinesFormatted(false);
        final List<Component> second = module.cachedLinesFormatted(false);

        assertEquals(2, module.formatCalls);
        assertNotSame(first, second);
    }

    @Test
    void marksDirtyWhenStateChangesAfterUpdate() {
        GeneralOptions.enablePerformanceOptimizations = true;
        final IncrementingModule module = new IncrementingModule();

        module.nextFrame();
        module.refreshDirtyFromState();
        module.cachedLinesFormatted(false);

        module.nextFrame();
        module.refreshDirtyFromState();
        module.cachedLinesFormatted(false);

        assertEquals(2, module.formatCalls);
    }

    @Test
    void keepsCacheWhenStateDoesNotChangeAfterUpdate() {
        GeneralOptions.enablePerformanceOptimizations = true;
        final StableModule module = new StableModule();

        module.nextFrame();
        module.refreshDirtyFromState();
        module.cachedLinesFormatted(false);

        module.nextFrame();
        module.refreshDirtyFromState();
        module.cachedLinesFormatted(false);

        assertEquals(1, module.formatCalls);
    }

    @Test
    void identifiesPerFrameAndNonPerFrameModules() {
        assertTrue(new CoordsModule().updatesEveryFrame());
        assertTrue(new LocationModule().updatesEveryFrame());
        assertTrue(new TargetModule().updatesEveryFrame());
        assertTrue(new ServerModule().updatesEveryFrame());
        assertFalse(new MinecraftModule().updatesEveryFrame());
        assertFalse(new GraphicsModule().updatesEveryFrame());
        assertFalse(new EmptyModule(true).updatesEveryFrame());
    }

    private static final class CountingModule extends BaseModule {
        private int formatCalls = 0;

        @Override
        public void update(final @NonNull Minecraft client) {
            // not needed for these tests
        }

        @Override
        public @NonNull List<Component> linesFormatted(final boolean reducedDebug) {
            this.formatCalls++;
            return List.of(Component.nullToEmpty("render-" + this.formatCalls + "-" + reducedDebug));
        }
    }

    private static final class IncrementingModule extends BaseModule {
        private int formatCalls = 0;
        private int value = 0;

        private IncrementingModule() {
            this.lines.add(new DebugLine("counter"));
        }

        private void nextFrame() {
            this.value++;
            this.lines.getFirst().value(this.value);
        }

        @Override
        public void update(final @NonNull Minecraft client) {
            this.nextFrame();
        }

        @Override
        public @NonNull List<Component> linesFormatted(final boolean reducedDebug) {
            this.formatCalls++;
            return super.linesFormatted(reducedDebug);
        }
    }

    private static final class StableModule extends BaseModule {
        private int formatCalls = 0;

        private StableModule() {
            this.lines.add(new DebugLine("stable"));
            this.lines.getFirst().value("constant");
        }

        private void nextFrame() {
            this.lines.getFirst().value("constant");
        }

        @Override
        public void update(final @NonNull Minecraft client) {
            this.nextFrame();
        }

        @Override
        public @NonNull List<Component> linesFormatted(final boolean reducedDebug) {
            this.formatCalls++;
            return super.linesFormatted(reducedDebug);
        }
    }
}
