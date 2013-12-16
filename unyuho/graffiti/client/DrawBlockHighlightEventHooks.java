package unyuho.graffiti.client;

import org.lwjgl.opengl.GL11;
import net.minecraft.block.Block;
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
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;

public class DrawBlockHighlightEventHooks
{
    @ForgeSubscribe
    public void onDrawBlockHighlight(DrawBlockHighlightEvent event)
    {
        EntityPlayer player = event.player;
        ItemStack itemstack = event.currentItem;
        MovingObjectPosition target = event.target;

        if(player == null)
        {
        	return;
        }

        if(itemstack == null)
        {
        	return;
        }

        if(!(Item.itemsList[itemstack.itemID] instanceof ItemBlock))
        {
        	return;
        }

        if(target.typeOfHit != EnumMovingObjectType.TILE)
        {
        	return;
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        float f1 = 0.002F;

    	double x = target.blockX;
    	double y = target.blockY;
    	double z = target.blockZ;

        int blockID = player.worldObj.getBlockId(target.blockX, target.blockY, target.blockZ);
        Block block = Block.blocksList[blockID];

        if(block.isBlockReplaceable(player.worldObj, target.blockX, target.blockY, target.blockZ))
        {
            //選択しているブロックの選択範囲は描写させない
            event.setCanceled(true);
        }
        else
        {
        	ForgeDirection offset = ForgeDirection.getOrientation(target.sideHit);
        	x += offset.offsetX;
        	y += offset.offsetY;
        	z += offset.offsetZ;
        }


        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)event.partialTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)event.partialTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)event.partialTicks;

        AxisAlignedBB axisAlignedBB =  AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);
        axisAlignedBB = axisAlignedBB.expand((double)f1, (double)f1, (double)f1).getOffsetBoundingBox(-d0, -d1, -d2);
        this.drawOutlinedBoundingBox(axisAlignedBB);


        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB)
    {
        Tessellator tessellator = Tessellator.instance;


        //下っぽい
        tessellator.startDrawing(3);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.draw();

        //上かもしれない
        tessellator.startDrawing(3);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tessellator.draw();

        //側面だったら良いな
        tessellator.startDrawing(1);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        tessellator.draw();
    }
}