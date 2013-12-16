package unyuho.graffiti.util;

import java.util.ArrayList;
import java.util.List;

import unyuho.graffiti.Graffiti;
import unyuho.graffiti.block.TileEntityGraffitiBlock;
import unyuho.kawo.Point;
import unyuho.kawo.Vec2DImpl;
import unyuho.kawo.Vec2DIntersectSupport;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * BlockGraffiti用のわいわいクラス
 * @author Unyuho
 *
 */
public class BlockGraffitiHelper
{
	private static final float BLOCK_WIDTH = 0.0125F;
	private static final float LINE_WIDTH = 0.01F;


    private BlockGraffitiHelper(){}

    /**
     * ブロックの当たり判定の幅
     * @return
     */
	public static float getBlockWidth()
	{
		return BLOCK_WIDTH;
	}

	/**
	 * 線の太さ
	 * @return
	 */
	public static float getLineWidth()
	{
		return LINE_WIDTH;
	}

	/**
	 * 線のID
	 * @return
	 */
	public static int getNewLineID()
	{
		return Graffiti.getNewLineID();
	}

	/**
	 * 結合のID
	 * @return
	 */
	public static int getNewConnectID()
	{
		return Graffiti.getNewConnectID();
	}
}
