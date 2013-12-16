package unyuho.graffiti;

import java.util.logging.Level;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import unyuho.graffiti.block.GraffitiBlock;
import unyuho.graffiti.client.RenderGameOverlayEventHooks;
import unyuho.graffiti.item.ItemBlockGraffiti;
import unyuho.graffiti.item.ItemGraffiti;
import unyuho.graffiti.item.ItemSharpener;
import unyuho.graffiti.util.PacketHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "Graffiti", name = "Graffiti Block", version = "1.1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {"graffiti_tile", "graffiti_gui"}, packetHandler = PacketHandler.class)
public class Graffiti
{
	private static int blockId = 2180;
	private static int graffitiItemId = 30821;
	private static int sharpenerItemId = 30822;

	private int posionX = 0;
	private int posionY = 0;
	private int color = 0xFFFFFF;
	private int maxLength = 15;

	private static Configuration cfg;
	private static int lineId = 0;
	private static int connectId = 0;

	public static GraffitiBlock graffitiBlock = null;
	public static ItemBlockGraffiti graffitBlockiItem = null;
	public static ItemGraffiti graffitiItem = null;
	public static ItemSharpener sharpenerItem = null;

	@SidedProxy(clientSide = "unyuho.graffiti.client.ClientProxy", serverSide = "unyuho.graffiti.CommonProxy")
	public static CommonProxy proxy;

	@Instance("Graffiti")
	public static Graffiti instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		cfg = new Configuration(event.getSuggestedConfigurationFile());

		try
		{
			cfg.load();
			blockId = cfg.getBlock("GraffitiBlockID", blockId).getInt();
			graffitiItemId = cfg.getItem("GraffitiItemID", graffitiItemId).getInt();
			sharpenerItemId = cfg.getItem("SharpenerItemID", sharpenerItemId).getInt();

			lineId = cfg.get("lineID", "lineId", lineId).getInt();
			connectId = cfg.get("lineID", "connectId", connectId).getInt();

			posionX = cfg.get("displayinfo", "PosionX", posionX).getInt();
			posionY = cfg.get("displayinfo", "PosionY", posionY).getInt();
			color = cfg.get("displayinfo", "Color", color).getInt();

			maxLength = cfg.get("maxLength", "maxLength", maxLength).getInt();
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Error Massage");
		}
		finally
		{
			cfg.save();
		}

		int renderType = proxy.getBlockNewRenderType();
		graffitiBlock = new GraffitiBlock(blockId, renderType);
		GameRegistry.registerBlock(graffitiBlock, ItemBlockGraffiti.class , "graffitiBlock");

		graffitiItem = new ItemGraffiti(graffitiItemId, blockId, posionX, posionY, color, maxLength);
		GameRegistry.registerItem(graffitiItem, "GraffitiItem");

		sharpenerItem = new ItemSharpener(sharpenerItemId);
		GameRegistry.registerItem(sharpenerItem, "SharpenerItem");
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.registerRenderInformation();

		LanguageRegistry.addName(graffitiBlock, "Graffiti Block");
		LanguageRegistry.instance().addNameForObject(graffitiBlock, "ja_JP", "落書きブロック");


		LanguageRegistry.addName(sharpenerItem, "sharpener");
		LanguageRegistry.instance().addNameForObject(sharpenerItem, "ja_JP", "鉛筆削りっぽいの");


		LanguageRegistry.addName(graffitiItem, "Graffiti Item");
		LanguageRegistry.instance().addNameForObject(graffitiItem, "ja_JP", "鉛筆っぽいの");

		GameRegistry.addRecipe(
			new ItemStack(graffitiItem, 1, 0),
			new Object[] {	"YYY",
							"YXY",
							"YYY",
							'X', new ItemStack(Item.silk),
							'Y', new ItemStack(Item.dyePowder, 1, -1)
						}
		);

		GameRegistry.addRecipe(
				new ItemStack(graffitiItem, 1, 0),
				new Object[] {	"YYY",
								"YXY",
								"YYY",
								'X', new ItemStack(Item.silk),
								'Y', new ItemStack(Item.dyePowder, 1, -1)
							}
			);


		NetworkRegistry.instance().registerGuiHandler(instance, proxy);

		//MinecraftForge.EVENT_BUS.register(new DrawBlockHighlightEventHooks());

		MinecraftForge.EVENT_BUS.register(new RenderGameOverlayEventHooks());
	}

	public static int getNewLineID()
	{
		if(lineId == Integer.MAX_VALUE)
		{
			lineId = 0;
		}
		else
		{
			lineId++;
		}

		try
		{
			cfg.load();
			cfg.get("lineID", "lineId", lineId).set(lineId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			FMLLog.log(Level.SEVERE, e, "Error Massage");
		}
		finally
		{
			cfg.save();
		}

		return lineId;
	}

	public static int getNewConnectID()
	{
		if(connectId == Integer.MAX_VALUE)
		{
			connectId = 0;
		}
		else
		{
			connectId++;
		}

		try
		{
			cfg.load();
			cfg.get("lineID", "connectId", connectId).set(connectId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			FMLLog.log(Level.SEVERE, e, "Error Massage");
		}
		finally
		{
			cfg.save();
		}

		return connectId;
	}

}
