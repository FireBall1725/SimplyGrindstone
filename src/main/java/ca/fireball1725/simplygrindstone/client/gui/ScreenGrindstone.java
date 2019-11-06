package ca.fireball1725.simplygrindstone.client.gui;

import ca.fireball1725.mods.firelib2.client.gui.ScreenBase;
import ca.fireball1725.simplygrindstone.common.container.ContainerGrindstone;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import sun.rmi.log.LogHandler;

public class ScreenGrindstone extends ScreenBase<ContainerGrindstone> {
  private ResourceLocation GUI = new ResourceLocation("simplygrindstone", "textures/gui/grindstone.png");

  public ScreenGrindstone(ContainerGrindstone screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);

    this.xSize = 175;
    this.ySize = 149;
  }

  @Override
  public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
    this.renderBackground();
    super.render(p_render_1_, p_render_2_, p_render_3_);
    this.renderHoveredToolTip(p_render_1_, p_render_2_);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    drawString(Minecraft.getInstance().fontRenderer, this.title.getString(), 5, 5, 0xffffff);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
    this.minecraft.getTextureManager().bindTexture(GUI);
    int relX = (this.width - this.xSize) / 2;
    int relY = (this.height - this.ySize) / 2;
    this.blit(relX, relY, 0, 0, this.xSize, this.ySize);

    float percent = (((float)container.getPercentProcessed() / 100) * 22);
    this.blit(relX + 77, relY + 48, 176, 0, (int)percent, 15);
  }
}
