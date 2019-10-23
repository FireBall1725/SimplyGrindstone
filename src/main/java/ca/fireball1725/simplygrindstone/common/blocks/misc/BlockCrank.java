package ca.fireball1725.simplygrindstone.common.blocks.misc;

import ca.fireball1725.mods.firelib2.common.blocks.BlockBase;
import ca.fireball1725.mods.firelib2.util.IProvideEvent;
import ca.fireball1725.mods.firelib2.util.RotationHelper;
import ca.fireball1725.mods.firelib2.util.RotationType;
import ca.fireball1725.mods.firelib2.util.TileHelper;
import ca.fireball1725.simplygrindstone.common.blocks.Blocks;
import ca.fireball1725.simplygrindstone.common.tileentities.misc.TileEntityCrank;
import ca.fireball1725.simplygrindstone.util.ICrankable;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLXEXTStereoTree;

import javax.annotation.Nullable;

public class BlockCrank extends BlockBase implements IProvideEvent {
  private final VoxelShape crankTop = VoxelShapes.create(7 / 16d, 8 / 16d, 3 / 16d, 9 / 16d, 10 / 16d, 13 / 16d);
  private final VoxelShape crankShaft = VoxelShapes.create(7 / 16d, 0 / 16d, 7 / 16d, 9 / 16d, 8 / 16d, 9 / 16d);

  public BlockCrank() {
    super(Properties.create(Material.WOOD)
      .sound(SoundType.WOOD)
      .hardnessAndResistance(2.0f)
    );
    setRegistryName("crank");
    setCanRotate(false);
    setTileEntity(TileEntityCrank::new);
  }

  @Override
  public RotationType getRotationType() {
    return RotationType.NONE;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {

    World world = null;

    if (context.getEntity() != null)
      world = context.getEntity().world;

    if (world == null)
      return VoxelShapes.or(crankShaft, crankTop);

    TileEntity tileEntity = TileHelper.getTileEntity(world, pos.down(), TileEntity.class);
    if (!(tileEntity != null && tileEntity instanceof ICrankable))
      return VoxelShapes.or(crankShaft, crankTop);

    BlockState blockState = tileEntity.getBlockState();
    if (!(blockState.has(BlockStateProperties.HORIZONTAL_FACING)))
      return VoxelShapes.or(crankShaft, crankTop);

    VoxelShape crankTopRotated = RotationHelper.rotateShape(crankTop, blockState.get(BlockStateProperties.HORIZONTAL_FACING));

    return VoxelShapes.or(crankShaft, crankTopRotated);
  }

  @Override
  public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    TileEntityCrank crank = TileHelper.getTileEntity(worldIn, pos, TileEntityCrank.class);
    if (crank == null)
      return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);

    crank.doCrank();

    return true;
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.ENTITYBLOCK_ANIMATED;
  }

  @Override
  public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
    World world = worldIn.getDimension().getWorld();

    TileEntity tileEntity = TileHelper.getTileEntity(world, pos.down(), TileEntity.class);
    return (tileEntity != null && tileEntity instanceof ICrankable);
  }

  @SubscribeEvent
  public void drawBlockHighlight(DrawBlockHighlightEvent event) {
    RayTraceResult target = event.getTarget();
    World world = event.getInfo().getRenderViewEntity().getEntityWorld();
    BlockPos blockPos = new BlockPos(target.getHitVec());
    BlockState blockState = world.getBlockState(blockPos);
    EntityRendererManager rendererManager = Minecraft.getInstance().getRenderManager();

    if (!(target.getType() == RayTraceResult.Type.BLOCK && blockState.getBlock() == Blocks.CRANK.getBlock()))
      return;

    TileEntity tileEntity = TileHelper.getTileEntity(world, blockPos, TileEntity.class);

    event.setCanceled(true);

    Vec3d playerPos = rendererManager.info.getLookDirection();

    GL11.glPushMatrix();
    GlStateManager.translated(blockPos.getX() - playerPos.getX() + 0.5, blockPos.getY() - playerPos.getY() + 0.5, blockPos.getZ() - playerPos.getZ() + 0.5);
    assert tileEntity != null;

    //if (tileEntity.isRotating())
    //  GlStateManager.rotatef(tileEntity.getRotation() + 15 * event.getPartialTicks() + 90, 0, 1, 0);

    //if (!tileEntity.isRotating())
      GlStateManager.rotatef(0 + 90, 0, 1, 0);

    GlStateManager.enableBlend();
    GlStateManager.blendFuncSeparate(770, 771, 1, 0);
    GlStateManager.color4f(0.0F, 0.0F, 0.0F, 0.4F);
    GL11.glLineWidth(2.0F);
    //GlStateManager.disableTexture2D();
    GlStateManager.disableTexture();
    GlStateManager.depthMask(false);

    AxisAlignedBB crankShaftBB = crankShaft.getBoundingBox();
    AxisAlignedBB crankTopBB = crankTop.getBoundingBox();

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder worldrenderer = tessellator.getBuffer();
    worldrenderer.begin(3, DefaultVertexFormats.POSITION);
    worldrenderer.pos(crankShaftBB.maxX, crankShaftBB.maxY, crankShaftBB.maxZ).endVertex();
    worldrenderer.pos(crankTopBB.maxX, crankTopBB.minY, crankTopBB.maxZ).endVertex();
    worldrenderer.pos(crankTopBB.minX, crankTopBB.minY, crankTopBB.maxZ).endVertex();
    worldrenderer.pos(crankShaftBB.minX, crankShaftBB.maxY, crankShaftBB.maxZ).endVertex();
    tessellator.draw();
    worldrenderer.begin(3, DefaultVertexFormats.POSITION);
    worldrenderer.pos(crankShaftBB.minX, crankShaftBB.maxY, crankShaftBB.minZ).endVertex();
    worldrenderer.pos(crankTopBB.minX, crankTopBB.minY, crankTopBB.minZ).endVertex();
    worldrenderer.pos(crankTopBB.maxX, crankTopBB.minY, crankTopBB.minZ).endVertex();
    worldrenderer.pos(crankShaftBB.maxX, crankShaftBB.maxY, crankShaftBB.minZ).endVertex();
    tessellator.draw();
    worldrenderer.begin(3, DefaultVertexFormats.POSITION);
    worldrenderer.pos(crankTopBB.minX, crankTopBB.maxY, crankTopBB.minZ).endVertex();
    worldrenderer.pos(crankTopBB.maxX, crankTopBB.maxY, crankTopBB.minZ).endVertex();
    worldrenderer.pos(crankTopBB.maxX, crankTopBB.maxY, crankTopBB.maxZ).endVertex();
    worldrenderer.pos(crankTopBB.minX, crankTopBB.maxY, crankTopBB.maxZ).endVertex();
    worldrenderer.pos(crankTopBB.minX, crankTopBB.maxY, crankTopBB.minZ).endVertex();
    tessellator.draw();
    worldrenderer.begin(1, DefaultVertexFormats.POSITION);
    worldrenderer.pos(crankTopBB.minX, crankTopBB.minY, crankTopBB.minZ).endVertex();
    worldrenderer.pos(crankTopBB.minX, crankTopBB.maxY, crankTopBB.minZ).endVertex();
    worldrenderer.pos(crankTopBB.maxX, crankTopBB.minY, crankTopBB.minZ).endVertex();
    worldrenderer.pos(crankTopBB.maxX, crankTopBB.maxY, crankTopBB.minZ).endVertex();
    worldrenderer.pos(crankTopBB.maxX, crankTopBB.minY, crankTopBB.maxZ).endVertex();
    worldrenderer.pos(crankTopBB.maxX, crankTopBB.maxY, crankTopBB.maxZ).endVertex();
    worldrenderer.pos(crankTopBB.minX, crankTopBB.minY, crankTopBB.maxZ).endVertex();
    worldrenderer.pos(crankTopBB.minX, crankTopBB.maxY, crankTopBB.maxZ).endVertex();
    tessellator.draw();

    worldrenderer.begin(3, DefaultVertexFormats.POSITION);
    worldrenderer.pos(crankShaftBB.minX, crankShaftBB.minY, crankShaftBB.minZ).endVertex();
    worldrenderer.pos(crankShaftBB.maxX, crankShaftBB.minY, crankShaftBB.minZ).endVertex();
    worldrenderer.pos(crankShaftBB.maxX, crankShaftBB.minY, crankShaftBB.maxZ).endVertex();
    worldrenderer.pos(crankShaftBB.minX, crankShaftBB.minY, crankShaftBB.maxZ).endVertex();
    worldrenderer.pos(crankShaftBB.minX, crankShaftBB.minY, crankShaftBB.minZ).endVertex();
    tessellator.draw();
    worldrenderer.begin(3, DefaultVertexFormats.POSITION);
    worldrenderer.pos(crankShaftBB.minX, crankShaftBB.maxY, crankShaftBB.minZ).endVertex();
    worldrenderer.pos(crankShaftBB.maxX, crankShaftBB.maxY, crankShaftBB.minZ).endVertex();
    tessellator.draw();
    worldrenderer.begin(3, DefaultVertexFormats.POSITION);
    worldrenderer.pos(crankShaftBB.maxX, crankShaftBB.maxY, crankShaftBB.maxZ).endVertex();
    worldrenderer.pos(crankShaftBB.minX, crankShaftBB.maxY, crankShaftBB.maxZ).endVertex();
    tessellator.draw();
    worldrenderer.begin(1, DefaultVertexFormats.POSITION);
    worldrenderer.pos(crankShaftBB.minX, crankShaftBB.minY, crankShaftBB.minZ).endVertex();
    worldrenderer.pos(crankShaftBB.minX, crankShaftBB.maxY, crankShaftBB.minZ).endVertex();
    worldrenderer.pos(crankShaftBB.maxX, crankShaftBB.minY, crankShaftBB.minZ).endVertex();
    worldrenderer.pos(crankShaftBB.maxX, crankShaftBB.maxY, crankShaftBB.minZ).endVertex();
    worldrenderer.pos(crankShaftBB.maxX, crankShaftBB.minY, crankShaftBB.maxZ).endVertex();
    worldrenderer.pos(crankShaftBB.maxX, crankShaftBB.maxY, crankShaftBB.maxZ).endVertex();
    worldrenderer.pos(crankShaftBB.minX, crankShaftBB.minY, crankShaftBB.maxZ).endVertex();
    worldrenderer.pos(crankShaftBB.minX, crankShaftBB.maxY, crankShaftBB.maxZ).endVertex();
    tessellator.draw();

    GlStateManager.depthMask(true);
    GlStateManager.enableTexture();
    //GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();

    GL11.glPopMatrix();
  }
}