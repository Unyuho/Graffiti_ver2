package unyuho.graffiti;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import unyuho.graffiti.block.TileEntityGraffitiBlock;
import unyuho.graffiti.gui.ContainerColorPalette;
import unyuho.graffiti.gui.GuiColorPalette;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler
{
    public int getBlockNewRenderType()
    {
        return RenderingRegistry.getNextAvailableRenderId();
    }

    public World getClientWorld()
    {
        return null;
    }

    public void registerRenderInformation()
    {
    	GameRegistry.registerTileEntity(TileEntityGraffitiBlock.class, "graffitiBlock");
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return new ContainerColorPalette(player.inventory, world);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return new GuiColorPalette(player.inventory, world);
    }
}