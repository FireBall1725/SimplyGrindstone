package ca.fireball1725.firelib2;

import net.minecraft.block.Block;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class FireMod {
  private final Logger LOGGER;
  private final String MODID;
  private final EventBus EVENTBUS;

  public abstract ArrayList<Block> getBlocks();

  public FireMod(Logger logger, String modID) {
    this.LOGGER = logger;
    this.MODID = modID;

    FMLJavaModLoadingContext.get().getModEventBus().addListener();
  }
}
