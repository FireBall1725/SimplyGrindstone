package ca.fireball1725.simplygrindstone.client.render;
import ca.fireball1725.mods.firelib2.client.util.BlockModelRotator;
import ca.fireball1725.mods.firelib2.client.util.BufferManipulator;
import ca.fireball1725.simplygrindstone.common.blocks.Blocks;
import ca.fireball1725.simplygrindstone.common.tileentities.misc.TileEntityCrank;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.animation.TileEntityRendererFast;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.lwjgl.opengl.GL11;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
public class RenderCrank extends TileEntityRendererFast<TileEntityCrank> {
  protected static Map<BlockState, BlockModelRotator> cachedBuffers;
  public RenderCrank() {
    cachedBuffers = new HashMap<>();
  }
  @Override
  public void renderTileEntityFast(TileEntityCrank te, double x, double y, double z, float partialTicks,
                                   int destroyStage, BufferBuilder buffer) {
    final BlockState state = getRenderedBlockState(te);
    cacheIfMissing(state, BlockModelRotator::new);
    float angle = (float) Math.toRadians(te.getRotation());
    if(te.isRotating()) {
      angle = (float) Math.toRadians(te.getRotation() + 15 * partialTicks);
    }
    renderFromCache(buffer, state, (float) x, (float) y, (float) z, te.getPos(), Axis.Y, -angle);
  }
  protected void renderFromCache(BufferBuilder buffer, BlockState state, float x, float y, float z, BlockPos pos,
                                 Axis axis, float angle) {
    int packedLightmapCoords = state.getPackedLightmapCoords(getWorld(), pos);
    buffer.putBulkData((cachedBuffers.get(state)).getTransformed(x, y, z, angle, axis, packedLightmapCoords));
  }
  protected void cacheIfMissing(final BlockState state, Function<ByteBuffer, BlockModelRotator> factory) {
    if (!cachedBuffers.containsKey(state)) {
      BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
      BlockModelRenderer blockRenderer = dispatcher.getBlockModelRenderer();
      IBakedModel originalModel = dispatcher.getModelForState(state);
      BufferBuilder builder = new BufferBuilder(0);
      Random random = new Random();
      builder.setTranslation(0, 1, 0);
      builder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
      blockRenderer.renderModelFlat(getWorld(), originalModel, state, BlockPos.ZERO.down(), builder, true, random, 42,
        EmptyModelData.INSTANCE);
      builder.finishDrawing();
      cachedBuffers.put(state, factory.apply(builder.getByteBuffer()));
    }
  }
  protected BlockState getRenderedBlockState(TileEntityCrank te) {
    return te.getBlockState();
  }
  public static void invalidateCache() {
    if (cachedBuffers != null)
      cachedBuffers.clear();
  }
}
