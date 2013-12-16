package unyuho.graffiti.client;

import net.minecraft.world.World;
import unyuho.graffiti.CommonProxy;
import unyuho.graffiti.block.TileEntityGraffitiBlock;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void registerRenderInformation()
    {
        ClientRegistry.registerTileEntity(TileEntityGraffitiBlock.class, "graffitiBlock", new GraffitiBlockRenderer());
    }
}