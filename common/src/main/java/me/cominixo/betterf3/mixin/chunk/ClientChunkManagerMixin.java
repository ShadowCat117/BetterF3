package me.cominixo.betterf3.mixin.chunk;

import me.cominixo.betterf3.ducks.ClientChunkManagerAccess;
import net.minecraft.client.multiplayer.ClientChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Mixin to access volatile "chunks" field in ClientChunkManager.
 */
@Mixin(ClientChunkCache.class)
@SuppressWarnings("NullAway.Init")
public class ClientChunkManagerMixin implements ClientChunkManagerAccess {
    @Shadow
    private volatile ClientChunkCache.Storage storage;

    @Override
    public ClientChunkCache.Storage betterF3$getChunks() {
        return this.storage;
    }
}
