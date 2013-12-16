package unyuho.graffiti.client;

import org.lwjgl.opengl.GL11;

import unyuho.graffiti.item.ItemGraffiti;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;

public class RenderGameOverlayEventHooks
{
    @ForgeSubscribe
    public void PostWei(RenderGameOverlayEvent event)
    {
    	if(ElementType.ALL == event.type)
    	{
    		EntityPlayer entityplayer = Minecraft.getMinecraft().thePlayer;

    		// アイテム情報表示
    		ItemStack itemstack = entityplayer.inventory.getCurrentItem();

    		if (itemstack != null) {
    			Item item = itemstack.getItem();

    			if (item instanceof ItemGraffiti)
    			{
    				((ItemGraffiti)item).displayItemInfo(itemstack, event.resolution);
    			}
    		}
    	}
    }
}