package ca.fireball1725.simplygrindstone.common.blocks.misc;

import ca.fireball1725.mods.firelib2.common.blocks.BlockBase;
import ca.fireball1725.mods.firelib2.util.*;
import ca.fireball1725.simplygrindstone.common.tileentities.misc.TileEntityCrank;
import ca.fireball1725.simplygrindstone.util.ICrankable;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ItemExistsCondition;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Consumer;

public class BlockCrank extends BlockBase implements IProvideEvent, IProvideRecipe {
  private final VoxelShape crankTop = VoxelShapes.create(7 / 16d, 8 / 16d, 3 / 16d, 9 / 16d, 10 / 16d, 13 / 16d);
  private final VoxelShape crankShaft = VoxelShapes.create(7 / 16d, 0 / 16d, 7 / 16d, 9 / 16d, 8 / 16d, 9 / 16d);
  private final CrankMaterial crankMaterial;

  public BlockCrank(CrankMaterial crankMaterial) {
    super(Properties.create(Material.WOOD)
      .sound(SoundType.WOOD)
      .hardnessAndResistance(2.0f)
    );
    setRegistryName("blockcrank" + crankMaterial.name().toLowerCase());
    setCanRotate(false);
    setTileEntity(() -> new TileEntityCrank((getTileEntityType())));
    this.crankMaterial = crankMaterial;
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

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void drawBlockHighlight(DrawBlockHighlightEvent event) {
    RayTraceResult target = event.getTarget();
    World world = event.getInfo().getRenderViewEntity().getEntityWorld();
    BlockPos blockPos = new BlockPos(target.getHitVec());
    BlockState blockState = world.getBlockState(blockPos);

    if (!(target.getType() == RayTraceResult.Type.BLOCK))
      return;

    TileEntityCrank tileEntity = TileHelper.getTileEntity(world, blockPos, TileEntityCrank.class);

    if (tileEntity != null && tileEntity.isRotating())
      event.setCanceled(true);
  }

  public CrankMaterial getCrankMaterial() {
    return crankMaterial;
  }

  @Override
  public void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(Blocks.PUMPKIN)
      .patternLine("xxx")
      .patternLine("x#x")
      .patternLine("xxx")
      .key('x', Blocks.COBBLESTONE)
      .key('#', Tags.Items.DYES_RED)
      .setGroup("mytutorial")
      .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
      .build(consumer);

    ShapedRecipeBuilder.shapedRecipe(ca.fireball1725.simplygrindstone.common.blocks.Blocks.CRANK_WOOD.getBlock())
      .patternLine("xxx")
      .patternLine("x#x")
      .patternLine("xxx")
      .key('x', Blocks.COBBLESTONE)
      .key('#', Tags.Items.DYES_RED)
      .setGroup("mytutorial")
      .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
      .build(consumer);

    switch (this.getCrankMaterial()) {
      case WOOD:
      default:
        ShapedRecipeBuilder.shapedRecipe(this)
          .patternLine("xxx")
          .patternLine(" x ")
          .patternLine(" x ")
          .key('x', Items.STICK)
          .setGroup("simplygrindstone")
          .addCriterion("has_sticks", InventoryChangeTrigger.Instance.forItems(Items.STICK))
          .build(consumer);
        break;
      case IRON:
        ShapedRecipeBuilder.shapedRecipe(this)
          .patternLine("xxx")
          .patternLine(" x ")
          .patternLine(" x ")
          .key('x', Items.IRON_INGOT)
          .setGroup("simplygrindstone")
          .addCriterion("has_iron", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
          .build(consumer);
        break;
    }
  }

  public static enum CrankMaterial {
    WOOD,
    IRON
  }
}