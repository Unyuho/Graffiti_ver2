package unyuho.graffiti.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import unyuho.graffiti.block.GraffitiBlock;
import unyuho.graffiti.block.TileEntityGraffitiBlock;
import unyuho.graffiti.util.LinePoint;
import unyuho.graffiti.util.PosLinePoint;
import unyuho.kawo.Point;
import unyuho.kawo.Vec2DImpl;
import unyuho.kawo.Vec2DIntersectSupport;

public class GraffitiBlockRenderer extends TileEntitySpecialRenderer
{
    public GraffitiBlockRenderer()
    {
    }

    @Override
    public void renderTileEntityAt(TileEntity par1tileentity, double d, double d1, double d2, float f)
    {
        float f1 = 1.0F;
        GL11.glPushMatrix();
        GL11.glTranslated(d, d1, d2);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScaled(f1, f1, f1);
        RenderHelper.disableStandardItemLighting();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        bindTexture(TextureMap.locationBlocksTexture);

        GraffitiBlock block = (GraffitiBlock)Block.blocksList[par1tileentity.worldObj.getBlockId(par1tileentity.xCoord, par1tileentity.yCoord, par1tileentity.zCoord)];

        render(par1tileentity.worldObj, block, par1tileentity.xCoord, par1tileentity.yCoord, par1tileentity.zCoord);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }



    private boolean render(World world, GraffitiBlock block, int posX, int posY, int posZ)
    {
        Tessellator tessellator = Tessellator.instance;

        //int l = block.getMixedBrightnessForBlock(this.blockAccess, posX, posY, oosZ);
        //block.setBlockBoundsBasedOnState(blockAccess, posX, posY, oosZ);

        Icon icon = block.getBlockTexture(world, posX, posY, posZ, 0);

        TileEntityGraffitiBlock tileentity = (TileEntityGraffitiBlock)world.getBlockTileEntity(posX, posY, posZ);
        LinePoint[] points = tileentity.getPoints();

        int[] renderPosition = new int[6];
        LinePoint beforePoint = null;

        if(points.length > 0)
        {
        	for(LinePoint point : points)
        	{
             	int side = point.getSide();
             	if(beforePoint != null)
             	{
             		if(!point.isConnect() || beforePoint.getID() != point.getID() || beforePoint.getConnectID()+1 != point.getConnectID() || beforePoint.getSide() != side)
             		{
             			beforePoint = null;
             		}
             	}

        		ForgeDirection direction = ForgeDirection.getOrientation(side);

        		if(ForgeDirection.DOWN == direction)
        		{
        			//tessellator.setBrightness(this.renderMinY > 0.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX, posY - 1, oosZ));

        			if(beforePoint != null)
        			{
            			startDrawingQuads(point);
            			GL11.glTranslated(0.0D, 1.0D - getPositon(renderPosition[side]++), 0.0D);
            			renderLineConnectY(icon , point, beforePoint);
            			draw();
        			}

        			startDrawingQuads(point);
        			GL11.glTranslated(0.0D, 1.0D - getPositon(renderPosition[side]), 0.0D);
        			renderLineFaceY(icon , point, beforePoint);
        			draw();
        		}
        		else if(ForgeDirection.UP == direction)
        		{
                    //tessellator.setBrightness(this.renderMaxY < 1.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX, posY + 1, oosZ));

        			if(beforePoint != null)
        			{
            			startDrawingQuads(point);
            			GL11.glTranslated(0.0D, getPositon(renderPosition[side]++), 0.0D);
            			renderLineConnectY(icon , point, beforePoint);
            			draw();
        			}

        			startDrawingQuads(point);
        			GL11.glTranslated(0.0D, getPositon(renderPosition[side]), 0.0D);
        			renderLineFaceY(icon , point, beforePoint);
        			draw();
        		}
        		else if(ForgeDirection.NORTH == direction)
        		{
                    //tessellator.setBrightness(this.renderMinZ > 0.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX, posY, oosZ - 1));

        			if(beforePoint != null)
        			{
            			startDrawingQuads(point);
            			GL11.glTranslated(0.0D, 0.0D, 1.0D - getPositon(renderPosition[side]++));
            			renderLineConnectZ(icon , point, beforePoint);
            			draw();
        			}

        			startDrawingQuads(point);
        			GL11.glTranslated(0.0D, 0.0D, 1.0D - getPositon(renderPosition[side]));
        			renderLineFaceZ(icon , point, beforePoint);
        			draw();
        		}
        		else if(ForgeDirection.SOUTH == direction)
        		{
                    //tessellator.setBrightness(this.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX - 1, posY, oosZ));

        			if(beforePoint != null)
        			{
            			startDrawingQuads(point);
            			GL11.glTranslated(0.0D, 0.0D, getPositon(renderPosition[side]++));
            			renderLineConnectZ(icon , point, beforePoint);
            			draw();
        			}

        			startDrawingQuads(point);
        			GL11.glTranslated(0.0D, 0.0D, getPositon(renderPosition[side]));
        			renderLineFaceZ(icon , point, beforePoint);
        			draw();
        		}
        		else if(ForgeDirection.WEST == direction)
        		{
                    //tessellator.setBrightness(this.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX - 1, posY, oosZ));

        			if(beforePoint != null)
        			{
            			startDrawingQuads(point);
            			GL11.glTranslated(1.0D - getPositon(renderPosition[side]++), 0.0D, 0.0D);
            			renderLineConnectX(icon , point, beforePoint);
            			draw();
        			}

        			startDrawingQuads(point);
        			GL11.glTranslated(1.0D - getPositon(renderPosition[side]), 0.0D, 0.0D);
        			renderLineFaceX(icon , point, beforePoint);
        			draw();
        		}
        		else if(ForgeDirection.EAST == direction)
        		{
                    //tessellator.setBrightness(this.renderMaxX < 1.0D ? l : block.getMixedBrightnessForBlock(this.blockAccess, posX + 1, posY, oosZ));

        			if(beforePoint != null)
        			{
            			startDrawingQuads(point);
            			GL11.glTranslated(getPositon(renderPosition[side]++), 0.0D, 0.0D);
            			renderLineConnectX(icon , point, beforePoint);
            			draw();
        			}

        			startDrawingQuads(point);
        			GL11.glTranslated(getPositon(renderPosition[side]), 0.0D, 0.0D);
        			renderLineFaceX(icon , point, beforePoint);
        			draw();
        		}

        		renderPosition[side]++;
        		beforePoint = point;
        	}
        }

        return true;
    }



    private void renderLineConnectY(Icon icon , LinePoint point, LinePoint beforePoint)
    {
        Tessellator tessellator = Tessellator.instance;

        //線の角度、長さ
        double lineSize = point.getLineSize();
        double size = lineSize / 2.0D;


        double pX = point.isReverse() ? point.getMaxX() : point.getMinX();
        double pZ = point.isReverse() ? point.getMaxY() : point.getMinY();


        //右端
        double angle = point.getAngle();
        double exRadiansRight = Math.toRadians(angle + 90.0D);
        double cosRight = Math.cos(exRadiansRight) * size;
        double sinRight = Math.sin(exRadiansRight) * size;

        //左端
        double exRadiansLeft = Math.toRadians(angle - 90.0D);
        double cosLeft = Math.cos(exRadiansLeft) * size;
        double sinLeft = Math.sin(exRadiansLeft) * size;

        //X座標
        double minXright = pX + (point.isReverse() ? cosLeft : cosRight);
        double minXleft = pX + (point.isReverse() ? cosRight : cosLeft);

        //Z座標
        double minZright = pZ + (point.isReverse() ? sinLeft : sinRight);
        double minZleft = pZ + (point.isReverse() ? sinRight : sinLeft);


        //右端
        double beforeAngle = beforePoint.getAngle();
        double beforeRadiansRight = Math.toRadians(beforeAngle + 90.0D);
        double beforeCosRight = Math.cos(beforeRadiansRight) * size;
        double beforeSinRight = Math.sin(beforeRadiansRight) * size;

        //左端
        double beforeRadiansLeft = Math.toRadians(beforeAngle - 90.0D);
        double beforeCosLeft = Math.cos(beforeRadiansLeft) * size;
        double beforeSinLeft = Math.sin(beforeRadiansLeft) * size;

        //X座標
        double maxXright = pX + (beforePoint.isReverse() ? beforeCosLeft : beforeCosRight);
        double maxXleft = pX + (beforePoint.isReverse() ? beforeCosRight : beforeCosLeft);

        //Z座標
        double maxZright = pZ + (beforePoint.isReverse() ? beforeSinLeft : beforeSinRight);
        double maxZleft = pZ + (beforePoint.isReverse() ? beforeSinRight : beforeSinLeft);

/*
        double bvX = beforePoint.isReverse() ? (beforePoint.getMinX() - beforePoint.getMaxX()) : (beforePoint.getMaxX() - beforePoint.getMinX());
        double bvY = beforePoint.isReverse() ? (beforePoint.getMinY() - beforePoint.getMaxY()) : (beforePoint.getMaxY() - beforePoint.getMinY());
        double vX = point.isReverse() ? (point.getMaxX() - point.getMinX()) : (point.getMinX() - point.getMaxX());
        double vY = point.isReverse() ? (point.getMaxY() - point.getMinY()) : (point.getMinY() - point.getMaxY());

        double atan2 = Math.atan2((bvY + vY),(bvX + vX));
    	double vAngle = (atan2 * 180.0D / Math.PI) - beforeAngle;;


    	//テクスチャのUV計算はゆるしてくれっつってる
        double minU = (double)icon.getMinU();
        double maxU = (double)icon.getMaxU();
        double minV = (double)icon.getMinV();
        double maxV = (double)icon.getMaxV();

        if(vAngle < -90D)
        {
            tessellator.addVertexWithUV(minXright, 0.0D, minZright, minU, maxV);
            tessellator.addVertexWithUV(pX, 0.0D, pZ, maxU, maxV);
            tessellator.addVertexWithUV(maxXright, 0.0D, maxZright, maxU, minV);
            tessellator.addVertexWithUV(minXright, 0.0D, minZright, minU, maxV);
        }
        else if(vAngle < 0)
        {
            tessellator.addVertexWithUV(minXleft, 0.0D, minZleft, minU, maxV);
            tessellator.addVertexWithUV(pX, 0.0D, pZ, maxU, maxV);
            tessellator.addVertexWithUV(maxXleft, 0.0D, maxZleft, maxU, minV);
            tessellator.addVertexWithUV(minXleft, 0.0D, minZleft, minU, maxV);
        }
        else if(vAngle < 90)
        {
            tessellator.addVertexWithUV(minXright, 0.0D, minZright, minU, maxV);
            tessellator.addVertexWithUV(pX, 0.0D, pZ, maxU, maxV);
            tessellator.addVertexWithUV(maxXright, 0.0D, maxZright, maxU, minV);
            tessellator.addVertexWithUV(minXright, 0.0D, minZright, minU, maxV);
        }
        else
        {
            tessellator.addVertexWithUV(minXleft, 0.0D, minZleft, minU, maxV);
            tessellator.addVertexWithUV(pX, 0.0D, pZ, maxU, maxV);
            tessellator.addVertexWithUV(maxXleft, 0.0D, maxZleft, maxU, minV);
            tessellator.addVertexWithUV(minXleft, 0.0D, minZleft, minU, maxV);
        }
*/
    	//テクスチャのUV計算はゆるしてくれっつってる
        double minU = (double)icon.getMinU();
        double maxU = (double)icon.getMaxU();
        double minV = (double)icon.getMinV();
        double maxV = (double)icon.getMaxV();

        tessellator.addVertexWithUV(minXleft, 0.0D, minZleft, minU, maxV);
        tessellator.addVertexWithUV(minXright, 0.0D, minZright, maxU, maxV);
        tessellator.addVertexWithUV(maxXright, 0.0D, maxZleft, maxU, minV);
        tessellator.addVertexWithUV(maxXleft, 0.0D, maxZleft, minU, maxV);
    }

    private void renderLineConnectX(Icon icon , LinePoint point, LinePoint beforePoint)
    {
        Tessellator tessellator = Tessellator.instance;

        //線の角度、長さ
        double lineSize = point.getLineSize();
        double size = lineSize / 2.0D;


        double pZ = point.isReverse() ? point.getMaxX() : point.getMinX();
        double pY = point.isReverse() ? point.getMaxY() : point.getMinY();


        //右端
        double angle = point.getAngle();
        double exRadiansRight = Math.toRadians(angle + 90.0D);
        double cosRight = Math.cos(exRadiansRight) * size;
        double sinRight = Math.sin(exRadiansRight) * size;

        //左端
        double exRadiansLeft = Math.toRadians(angle - 90.0D);
        double cosLeft = Math.cos(exRadiansLeft) * size;
        double sinLeft = Math.sin(exRadiansLeft) * size;

        //X座標
        double minZright = pZ + (point.isReverse() ? cosLeft : cosRight);
        double minZleft = pZ + (point.isReverse() ? cosRight : cosLeft);

        //Z座標
        double minYright = pY + (point.isReverse() ? sinLeft : sinRight);
        double minYleft = pY + (point.isReverse() ? sinRight : sinLeft);


        //右端
        double beforeAngle = beforePoint.getAngle();
        double beforeRadiansRight = Math.toRadians(beforeAngle + 90.0D);
        double beforeCosRight = Math.cos(beforeRadiansRight) * size;
        double beforeSinRight = Math.sin(beforeRadiansRight) * size;

        //左端
        double beforeRadiansLeft = Math.toRadians(beforeAngle - 90.0D);
        double beforeCosLeft = Math.cos(beforeRadiansLeft) * size;
        double beforeSinLeft = Math.sin(beforeRadiansLeft) * size;

        //X座標
        double maxZright = pZ + (beforePoint.isReverse() ? beforeCosLeft : beforeCosRight);
        double maxZleft = pZ + (beforePoint.isReverse() ? beforeCosRight : beforeCosLeft);

        //Z座標
        double maxYright = pY + (beforePoint.isReverse() ? beforeSinLeft : beforeSinRight);
        double maxYleft = pY + (beforePoint.isReverse() ? beforeSinRight : beforeSinLeft);

/*
        double bvX = beforePoint.isReverse() ? (beforePoint.getMinX() - beforePoint.getMaxX()) : (beforePoint.getMaxX() - beforePoint.getMinX());
        double bvY = beforePoint.isReverse() ? (beforePoint.getMinY() - beforePoint.getMaxY()) : (beforePoint.getMaxY() - beforePoint.getMinY());
        double vX = point.isReverse() ? (point.getMaxX() - point.getMinX()) : (point.getMinX() - point.getMaxX());
        double vY = point.isReverse() ? (point.getMaxY() - point.getMinY()) : (point.getMinY() - point.getMaxY());

        double atan2 = Math.atan2((bvY + vY),(bvX + vX));
    	double vAngle = (atan2 * 180.0D / Math.PI) - beforeAngle;;


    	//テクスチャのUV計算はゆるしてくれっつってる
        double minU = (double)icon.getMinU();
        double maxU = (double)icon.getMaxU();
        double minV = (double)icon.getMinV();
        double maxV = (double)icon.getMaxV();

        if(vAngle < -90D)
        {
            tessellator.addVertexWithUV(0.0D, minYright, minZright, minU, maxV);
            tessellator.addVertexWithUV(0.0D, pY, pZ, maxU, maxV);
            tessellator.addVertexWithUV(0.0D, maxYright, maxZright, maxU, minV);
            tessellator.addVertexWithUV(0.0D, minYright, minZright, minU, maxV);
        }
        else if(vAngle < 0)
        {
            tessellator.addVertexWithUV(0.0D, minYleft, minZleft, minU, maxV);
            tessellator.addVertexWithUV(0.0D, pY, pZ, maxU, maxV);
            tessellator.addVertexWithUV(0.0D, maxYleft, maxZleft, maxU, minV);
            tessellator.addVertexWithUV(0.0D, minYleft, minZleft, minU, maxV);
        }
        else if(vAngle < 90)
        {
            tessellator.addVertexWithUV(0.0D, minYright, minZright, minU, maxV);
            tessellator.addVertexWithUV(0.0D, pY, pZ, maxU, maxV);
            tessellator.addVertexWithUV(0.0D, maxYright, maxZright, maxU, minV);
            tessellator.addVertexWithUV(0.0D, minYright, minZright, minU, maxV);
        }
        else
        {
            tessellator.addVertexWithUV(0.0D, minYleft, minZleft, minU, maxV);
            tessellator.addVertexWithUV(0.0D, pY, pZ, maxU, maxV);
            tessellator.addVertexWithUV(0.0D, maxYleft, maxZleft, maxU, minV);
            tessellator.addVertexWithUV(0.0D, minYleft, minZleft, minU, maxV);
        }
*/
    	//テクスチャのUV計算はゆるしてくれっつってる
        double minU = (double)icon.getMinU();
        double maxU = (double)icon.getMaxU();
        double minV = (double)icon.getMinV();
        double maxV = (double)icon.getMaxV();

        tessellator.addVertexWithUV(0.0D, minYleft, minZleft, minU, maxV);
        tessellator.addVertexWithUV(0.0D, minYright, minZright, maxU, maxV);
        tessellator.addVertexWithUV(0.0D, maxYright, maxZright, maxU, minV);
        tessellator.addVertexWithUV(0.0D, maxYleft, maxZleft, minU, maxV);
    }


    private void renderLineConnectZ(Icon icon , LinePoint point, LinePoint beforePoint)
    {
        Tessellator tessellator = Tessellator.instance;

        //線の角度、長さ
        double lineSize = point.getLineSize();
        double size = lineSize / 2.0D;

        double angle = point.getAngle();
        double beforeAngle = beforePoint.getAngle();

        double pX = point.isReverse() ? point.getMaxX() : point.getMinX();
        double pY = point.isReverse() ? point.getMaxY() : point.getMinY();


        double bvX = beforePoint.isReverse() ? (beforePoint.getMinX() - beforePoint.getMaxX()) : (beforePoint.getMaxX() - beforePoint.getMinX());
        double bvY = beforePoint.isReverse() ? (beforePoint.getMinY() - beforePoint.getMaxY()) : (beforePoint.getMaxY() - beforePoint.getMinY());
        double vX = point.isReverse() ? (point.getMaxX() - point.getMinX()) : (point.getMinX() - point.getMaxX());
        double vY = point.isReverse() ? (point.getMaxY() - point.getMinY()) : (point.getMinY() - point.getMaxY());

        double atan2 = Math.atan2((bvY + vY),(bvX + vX));
    	double vAngle = (atan2 * 180.0D / Math.PI) - beforeAngle;



        //右端
        double exRadiansRight = Math.toRadians(angle + 90.0D);
        double cosRight = Math.cos(exRadiansRight) * size;
        double sinRight = Math.sin(exRadiansRight) * size;

        //左端
        double exRadiansLeft = Math.toRadians(angle - 90.0D);
        double cosLeft = Math.cos(exRadiansLeft) * size;
        double sinLeft = Math.sin(exRadiansLeft) * size;

        //X座標
        double minXright = pX + (point.isReverse() ? cosLeft : cosRight);
        double minXleft = pX + (point.isReverse() ? cosRight : cosLeft);

        //Z座標
        double minYright = pY + (point.isReverse() ? sinLeft : sinRight);
        double minYleft = pY + (point.isReverse() ? sinRight : sinLeft);


        //右端
        double beforeRadiansRight = Math.toRadians(beforeAngle + 90.0D);
        double beforeCosRight = Math.cos(beforeRadiansRight) * size;
        double beforeSinRight = Math.sin(beforeRadiansRight) * size;

        //左端
        double beforeRadiansLeft = Math.toRadians(beforeAngle - 90.0D);
        double beforeCosLeft = Math.cos(beforeRadiansLeft) * size;
        double beforeSinLeft = Math.sin(beforeRadiansLeft) * size;

        //X座標
        double maxXright = pX + (beforePoint.isReverse() ? beforeCosLeft : beforeCosRight);
        double maxXleft = pX + (beforePoint.isReverse() ? beforeCosRight : beforeCosLeft);

        //Z座標
        double maxYright = pY + (beforePoint.isReverse() ? beforeSinLeft : beforeSinRight);
        double maxYleft = pY + (beforePoint.isReverse() ? beforeSinRight : beforeSinLeft);

/*
		いらんかったなｗ
    	System.out.println("vAngle = " + vAngle);
    	System.out.println("pX : + " + pX + ", angle = " + angle + ", beforeAngle = " + beforeAngle);
    	System.out.println("min(" + minXright + ", " + minXleft + ", " + minYright + ", " + minYright + ")");
    	System.out.println("max(" + maxXright + ", " + maxXleft + ", " + maxYright + ", " + maxYleft + ")");
    	System.out.println("p(" + pX + ", " + pY + ")");


        double minU = (double)icon.getMinU();
        double maxU = (double)icon.getMaxU();
        double minV = (double)icon.getMinV();
        double maxV = (double)icon.getMaxV();

        if(vAngle < -90D)
        {
            tessellator.addVertexWithUV(minXright, minYright, 0.0D, minU, maxV);
            tessellator.addVertexWithUV(pX, pY, 0.0D, maxU, maxV);
            tessellator.addVertexWithUV(maxXright, maxYright, 0.0D, maxU, minV);
            tessellator.addVertexWithUV(minXright, minYright, 0.0D, minU, maxV);
        }
        else if(vAngle < 0)
        {
            tessellator.addVertexWithUV(minXleft, minYleft, 0.0D, minU, maxV);
            tessellator.addVertexWithUV(pX, pY, 0.0D, maxU, maxV);
            tessellator.addVertexWithUV(maxXleft, maxYleft, 0.0D, maxU, minV);
            tessellator.addVertexWithUV(minXleft, minYleft, 0.0D, minU, maxV);
        }
        else if(vAngle < 90)
        {
            tessellator.addVertexWithUV(minXright, minYright, 0.0D, minU, maxV);
            tessellator.addVertexWithUV(pX, pY, 0.0D, maxU, maxV);
            tessellator.addVertexWithUV(maxXright, maxYright, 0.0D, maxU, minV);
            tessellator.addVertexWithUV(minXright, minYright, 0.0D, minU, maxV);
        }
        else
        {
            tessellator.addVertexWithUV(minXleft, minYleft, 0.0D, minU, maxV);
            tessellator.addVertexWithUV(pX, pY, 0.0D, maxU, maxV);
            tessellator.addVertexWithUV(maxXleft, maxYleft, 0.0D, maxU, minV);
            tessellator.addVertexWithUV(minXleft, minYleft, 0.0D, minU, maxV);
        }
*/

        //テクスチャのUV計算はゆるしてくれっつってる
        double minU = (double)icon.getMinU();
        double maxU = (double)icon.getMaxU();
        double minV = (double)icon.getMinV();
        double maxV = (double)icon.getMaxV();

        tessellator.addVertexWithUV(minXleft, minYleft, 0.0D, minU, maxV);
        tessellator.addVertexWithUV(minXright, minYright, 0.0D, maxU, maxV);
        tessellator.addVertexWithUV(maxXright, maxYright, 0.0D, maxU, minV);
        tessellator.addVertexWithUV(maxXleft, maxYleft, 0.0D, minU, maxV);

    }

    private void renderLineFaceY(Icon icon , LinePoint point, LinePoint beforePoint)
    {
        Tessellator tessellator = Tessellator.instance;

        //線の角度、長さ
        double lineSize = point.getLineSize();
        double size = lineSize / 2.0D;
        double angle = point.getAngle();

        double minX = 0.0D;
        double maxX = point.getAngleLength();
        double minZ = 0.0D;
        double maxZ = lineSize;

        //線の右側部分計算
        double exRadiansRight = Math.toRadians(angle + 90.0D);

        //X座標の右端
        double cosRight = Math.cos(exRadiansRight) * size;
        double minXright = point.getMinX() + cosRight;
        double maxXright = point.getMaxX() + cosRight;

        //Z座標の右端
        double sinRight = Math.sin(exRadiansRight) * size;
        double minZright = point.getMinY() + sinRight;
        double maxZright = point.getMaxY() + sinRight;

        //線の左側部分計算
        double exRadiansLeft = Math.toRadians(angle - 90.0D);

        //X座標の左端
        double cosLeft = Math.cos(exRadiansLeft) * size;
        double minXleft = point.getMinX() + cosLeft;
        double maxXleft = point.getMaxX() + cosLeft;

        //Z座標の左端
        double sinLeft = Math.sin(exRadiansLeft) * size;
        double minZleft = point.getMinY() + sinLeft;
        double maxZleft = point.getMaxY() + sinLeft;


        //テクスチャのUV計算
        if(point.isSide())
        {
        	maxZ = maxX;
        	maxX = lineSize;
        }

        double minU = (double)icon.getInterpolatedU(minX * 16.0D);
        double maxU = (double)icon.getInterpolatedU(maxX * 16.0D);
        double maxV = (double)icon.getInterpolatedV(minZ * 16.0D);
        double minV = (double)icon.getInterpolatedV(maxZ * 16.0D);

        if (maxX > 1.0D)
        {
            minU = (double)icon.getMinU();
            maxU = (double)icon.getMaxU();
        }

        if (maxZ > 1.0D)
        {
        	minV = (double)icon.getMinV();
            maxV = (double)icon.getMaxV();
        }

        if(point.isSide())
        {
            tessellator.addVertexWithUV(maxXleft, 0.0D, maxZleft, minU, maxV);
            tessellator.addVertexWithUV(minXleft, 0.0D, minZleft, maxU, maxV);
            tessellator.addVertexWithUV(minXright, 0.0D, minZright, maxU, minV);
            tessellator.addVertexWithUV(maxXright, 0.0D, maxZright, minU, minV);
        }
        else
        {
            tessellator.addVertexWithUV(maxXleft, 0.0D, maxZleft, minU, minV);
            tessellator.addVertexWithUV(minXleft, 0.0D, minZleft, minU, maxV);
            tessellator.addVertexWithUV(minXright, 0.0D, minZright, maxU, maxV);
            tessellator.addVertexWithUV(maxXright, 0.0D, maxZright, maxU, minV);
        }
    }

    private void renderLineFaceX(Icon icon , LinePoint point, LinePoint beforePoint)
    {
        Tessellator tessellator = Tessellator.instance;

        //線の角度、長さ
        double lineSize = point.getLineSize();
        double size = lineSize / 2.0D;
        double angle = point.getAngle();

        double minY = 0.0D;
        double maxY = point.getAngleLength();
        double minZ = 0.0D;
        double maxZ = lineSize;

        //線の右側部分計算
        double exRadiansRight = Math.toRadians(angle + 90.0D);

        //Z座標の右端
        double cosRight = Math.cos(exRadiansRight) * size;
        double minZright = point.getMinX() + cosRight;
        double maxZright = point.getMaxX() + cosRight;

        //Y座標の右端
        double sinRight = Math.sin(exRadiansRight) * size;
        double minYright = point.getMinY() + sinRight;
        double maxYright = point.getMaxY() + sinRight;


        //線の左側部分計算
        double exRadiansLeft = Math.toRadians(angle - 90.0D);

        //Z座標の左端
        double cosLeft = Math.cos(exRadiansLeft) * size;
        double minZleft = point.getMinX() + cosLeft;
        double maxZleft = point.getMaxX() + cosLeft;

        //Y座標の左端
        double sinLeft = Math.sin(exRadiansLeft) * size;
        double minYleft = point.getMinY() + sinLeft;
        double maxYleft = point.getMaxY() + sinLeft;


        //テクスチャのUV計算
        if(point.isSide())
        {
        	maxZ = maxY;
        	maxY = lineSize;
        }
        double minU = (double)icon.getInterpolatedU(minZ * 16.0D);
        double maxU = (double)icon.getInterpolatedU(maxZ * 16.0D);
        double maxV = (double)icon.getInterpolatedV(minY * 16.0D);
        double minV = (double)icon.getInterpolatedV(maxY * 16.0D);

        if (maxZ > 1.0D)
        {
            minU = (double)icon.getMinU();
            maxU = (double)icon.getMaxU();
        }
        if (maxY > 1.0D)
        {
        	minV = (double)icon.getMinV();
            maxV = (double)icon.getMaxV();
        }


        if(point.isSide())
        {
            tessellator.addVertexWithUV(0.0D, maxYleft, maxZleft, minU, maxV);
            tessellator.addVertexWithUV(0.0D, minYleft, minZleft, maxU, maxV);
            tessellator.addVertexWithUV(0.0D, minYright, minZright, maxU, minV);
            tessellator.addVertexWithUV(0.0D, maxYright, maxZright, minU, minV);
        }
        else
        {
            tessellator.addVertexWithUV(0.0D, maxYleft, maxZleft, minU, minV);
            tessellator.addVertexWithUV(0.0D, minYleft, minZleft, minU, maxV);
            tessellator.addVertexWithUV(0.0D, minYright, minZright, maxU, maxV);
            tessellator.addVertexWithUV(0.0D, maxYright, maxZright, maxU, minV);
        }

    }

    private void renderLineFaceZ(Icon icon , LinePoint point, LinePoint beforePoint)
    {
        Tessellator tessellator = Tessellator.instance;

        //線の角度、長さ
        double lineSize = point.getLineSize();
        double size = lineSize / 2.0D;
        double angle = point.getAngle();

        double minX = 0.0D;
        double maxX = lineSize;
        double minY = 0.0D;
        double maxY = point.getAngleLength();


        //線の右側部分計算
        double exRadiansRight = Math.toRadians(angle + 90.0D);

        //Z座標の右端
        double cosRight = Math.cos(exRadiansRight) * size;
        double minXright = point.getMinX() + cosRight;
        double maxXright = point.getMaxX() + cosRight;

        //Y座標の右端
        double sinRight = Math.sin(exRadiansRight) * size;
        double minYright = point.getMinY() + sinRight;
        double maxYright = point.getMaxY() + sinRight;


        //線の左側部分計算
        double exRadiansLeft = Math.toRadians(angle - 90.0D);

        //Z座標の左端
        double cosLeft = Math.cos(exRadiansLeft) * size;
        double minXleft = point.getMinX() + cosLeft;
        double maxXleft = point.getMaxX() + cosLeft;

        //Y座標の左端
        double sinLeft = Math.sin(exRadiansLeft) * size;
        double minYleft = point.getMinY() + sinLeft;
        double maxYleft = point.getMaxY() + sinLeft;


        //テクスチャのUV計算
        if(point.isSide())
        {
        	maxX = maxY;
        	maxY = lineSize;
        }

        double minU = (double)icon.getInterpolatedU(minX * 16.0D);
        double maxU = (double)icon.getInterpolatedU(maxX * 16.0D);
        double maxV = (double)icon.getInterpolatedV(minY * 16.0D);
        double minV = (double)icon.getInterpolatedV(maxY * 16.0D);

        if (maxX > 1.0D)
        {
            minU = (double)icon.getMinU();
            maxU = (double)icon.getMaxU();
        }
        if (maxY > 1.0D)
        {
        	minV = (double)icon.getMinV();
            maxV = (double)icon.getMaxV();
        }

        if(point.isSide())
        {
            tessellator.addVertexWithUV(maxXleft, maxYleft, 0.0D, minU, maxV);
            tessellator.addVertexWithUV(minXleft, minYleft, 0.0D, maxU, maxV);
            tessellator.addVertexWithUV(minXright, minYright, 0.0D, maxU, minV);
            tessellator.addVertexWithUV(maxXright, maxYright, 0.0D, minU, minV);
        }
        else
        {
            tessellator.addVertexWithUV(maxXleft, maxYleft, 0.0D, minU, minV);
            tessellator.addVertexWithUV(minXleft, minYleft, 0.0D, minU, maxV);
            tessellator.addVertexWithUV(minXright, minYright, 0.0D, maxU, maxV);
            tessellator.addVertexWithUV(maxXright, maxYright, 0.0D, maxU, minV);
        }
    }


    private void startDrawingQuads(LinePoint point)
    {
		GL11.glPushMatrix();
		Tessellator.instance.startDrawingQuads();
		//影設定っぽいの
		setColorOpaque(point);
    }

    private void draw()
    {
    	Tessellator.instance.draw();
    	GL11.glPopMatrix();
    }


    /**
     * 影っぽい感じのやつ
     * @param point
     */
    private void setColorOpaque(LinePoint point)
    {
        int color = point.getColor();
        float rr = (float)(color >> 16 & 0xff) / 255F;
        float gg = (float)(color >> 8 & 0xff) / 255F;
        float bb = (float)(color & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
        	rr = (rr * 30F + gg * 59F + bb * 11F) / 100F;
        	gg = (rr * 30F + gg * 70F) / 100F;
        	bb = (rr * 30F + bb * 70F) / 100F;
        }

        Tessellator tessellator = Tessellator.instance;

        float opa100 = 1.0F;
        float opa80 = 0.8F;
        float opa60 = 0.6F;
        float opa50 = 0.5F;

        float opa100Red = opa100 * rr;
        float opa100Green = opa100 * gg;
        float opa100Blue = opa100 * bb;

        float opa80Red = opa80 * rr;
        float opa80Green = opa80 * gg;
        float opa80Blue = opa80 * bb;

        float opa60Red = opa60 * rr;
        float opa60Green = opa60 * gg;
     	float opa60Blue = opa60 * bb;

        float opa50Red = opa50 * rr;
        float opa50Green = opa50 * gg;
        float opa50Blue = opa50 * bb;

     	ForgeDirection direction = ForgeDirection.getOrientation(point.getSide());

		if(ForgeDirection.DOWN == direction)
		{
            tessellator.setColorOpaque_F(opa50Red, opa50Green, opa50Blue);
		}
		else if(ForgeDirection.UP == direction)
		{
            tessellator.setColorOpaque_F(opa100Red, opa100Green, opa100Blue);
		}
		else if(ForgeDirection.NORTH == direction)
		{
            tessellator.setColorOpaque_F(opa80Red, opa80Green, opa80Blue);
		}
		else if(ForgeDirection.SOUTH == direction)
		{
            tessellator.setColorOpaque_F(opa60Red, opa60Green, opa60Blue);
		}
		else if(ForgeDirection.WEST == direction)
		{
            tessellator.setColorOpaque_F(opa60Red, opa60Green, opa60Blue);
		}
		else if(ForgeDirection.EAST == direction)
		{
            tessellator.setColorOpaque_F(opa60Red, opa60Green, opa60Blue);
		}
    }

    private double getPositon(int cnt)
    {
    	return 0.001D + 0.00001D * cnt;
    }
}
