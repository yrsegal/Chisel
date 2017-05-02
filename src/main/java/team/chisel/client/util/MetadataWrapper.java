package team.chisel.client.util;

import com.google.common.collect.Lists;
import gnu.trove.map.hash.TLongObjectHashMap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@MethodsReturnNonnullByDefault
public final class MetadataWrapper implements IBakedModel {

    private final IBakedModel parent;

    public final boolean anyQuads;

    public MetadataWrapper(IBakedModel parent) {
        this.parent = parent;

        boolean anyQuads = false;
        for (EnumFacing facing : EnumFacing.VALUES) {
            List<BakedQuad> filtered = getQuads(null, facing, 0);
            anyQuads |= !filtered.isEmpty();
        }

        List<BakedQuad> filtered = getQuads(null, null, 0);
        anyQuads |= !filtered.isEmpty();

        this.anyQuads = anyQuads;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> parentQuads = parent.getQuads(state, side, rand);
        //noinspection ConstantConditions
        if (parentQuads == null) return Lists.newArrayList();
        return parentQuads.stream()
                .filter(this::predicateQuad)
                .collect(Collectors.toList());
    }

    private boolean predicateQuad(BakedQuad quad) {
        return true; // TODO
    }

    @Override
    public boolean isAmbientOcclusion() {
        return parent.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return parent.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return parent.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return parent.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return parent.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return parent.getOverrides();
    }
}
