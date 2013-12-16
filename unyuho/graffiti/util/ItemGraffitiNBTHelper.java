package unyuho.graffiti.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * ItemGraffiti用のNBT操作クラス
 * @author Unyuho
 *
 */
public class ItemGraffitiNBTHelper
{
	private static final String TAG_NAME = "clickPos";

	private static final String KEY_LINE_SIZE = "lineSize";
	private static final String KEY_COLOR = "color";
	private static final String KEY_SIDE = "side";
	private static final String KEY_COORD_X = "xCoord";
	private static final String KEY_COORD_Y = "yCoord";
	private static final String KEY_COORD_Z = "zCoord";
	private static final String KEY_POS_X = "posX";
	private static final String KEY_POS_Y = "posY";
	private static final String KEY_POS_Z = "posZ";
	private static final String KEY_LINE_ID = "lineID";
	private static final String KEY_LINE_DRAW = "draw";


	public static final int RANGE_MIN_COLOR = 0x00;
	public static final int RANGE_MAX_COLOR = 0xFF;

	public static final int VALUE_DEFAULT_COLOR = 0x000000;
	public static final int VALUE_MAX_COLOR = 0xFFFFFF;
	public static final int VALUE_DEFAULT_LINE_SIZE = 1;
	public static final int VALUE_MAX_LINE_SIZE = 50;
	public static final int VALUE_DEFAULT_SIDE = -1;


    private ItemGraffitiNBTHelper(){}


    /*
     *******************************************************************************************
     *
     * NBT操作的なやつ
     *
     *******************************************************************************************
     */


    /**
     * NBTの初期設定
     * @param itemstack
     */
    public static void createNBTTagCompound(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = new NBTTagCompound(TAG_NAME);
		stackTagCompound.setInteger(KEY_LINE_SIZE, VALUE_DEFAULT_LINE_SIZE);
		stackTagCompound.setInteger(KEY_SIDE, VALUE_DEFAULT_SIDE);

    	itemstack.setTagCompound(stackTagCompound);
    }

    /**
     * NBT取得
     * @param itemstack
     * @return
     */
    private static NBTTagCompound getNBTTagCompound(ItemStack itemstack)
    {
    	if(!itemstack.hasTagCompound()){
    		createNBTTagCompound(itemstack);
    	}

    	NBTTagCompound stackTagCompound = itemstack.getTagCompound();

    	return stackTagCompound;
    }


    /*
     *******************************************************************************************
     *
     * getter
     *
     *******************************************************************************************
     */

    /**
     * 線を引き始めているか
     * @param itemstack
     * @return
     */
    public static boolean isState(ItemStack itemstack)
    {
    	return getSide(itemstack) != VALUE_DEFAULT_SIDE;
    }

    /**
     * 線の描写面
     * @param itemstack
     * @return
     */
    public static int getSide(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	return stackTagCompound.getInteger(KEY_SIDE);
    }

    /**
     * ブロックの位置(X座標)
     * @param itemstack
     * @return
     */
    public static int getXPos(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	return stackTagCompound.getInteger(KEY_POS_X);
    }

    /**
     * ブロックの位置(Y座標)
     * @param itemstack
     * @return
     */
    public static int getYPos(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	return stackTagCompound.getInteger(KEY_POS_Y);
    }

    /**
     * ブロックの位置(Z座標)
     * @param itemstack
     * @return
     */
    public static int getZPos(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	return stackTagCompound.getInteger(KEY_POS_Z);
    }

    /**
     * 線の位置(X座標)
     * @param itemstack
     * @return
     */
    public static float getXCoord(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	return stackTagCompound.getFloat(KEY_COORD_X);
    }

    /**
     * 線の位置(Y座標)
     * @param itemstack
     * @return
     */
    public static float getYCoord(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	return stackTagCompound.getFloat(KEY_COORD_Y);
    }

    /**
     * 線の位置(Z座標)
     * @param itemstack
     * @return
     */
    public static float getZCoord(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	return stackTagCompound.getFloat(KEY_COORD_Z);
    }

    /**
     * 線の太さを取得
     * @param itemstack
     * @return
     */
    public static int getLineSize(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	return stackTagCompound.getInteger(KEY_LINE_SIZE);
    }

    /**
     * 線の色を取得
     * @param itemstack
     * @return
     */
    public static int getColor(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	return stackTagCompound.getInteger(KEY_COLOR);
    }

    /**
     * 線の色(RGB)を取得
     * @param itemstack
     * @return
     */
    public static int[] getRGB(ItemStack itemstack)
    {
    	int color = getColor(itemstack);
    	return convertColorToRGB(color);
    }

    /**
     * 線のIDを取得
     * @param itemstack
     * @return
     */
    public static int getLineID(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	return stackTagCompound.getInteger(KEY_LINE_ID);
    }

    /**
     * 線の描写フラグ
     * @param itemstack
     * @return
     */
    public static boolean getDrawFlg(ItemStack itemstack)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	return stackTagCompound.getBoolean(KEY_LINE_DRAW);
    }

    /*
     *******************************************************************************************
     *
     * setter
     *
     *******************************************************************************************
     */


    /**
     * 線の太さを上書き設定
     * @param itemstack
     * @param size
     */
    public static void setLineSize(ItemStack itemstack, int size)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	if(size > VALUE_MAX_LINE_SIZE)
    	{
    		size = VALUE_MAX_LINE_SIZE;
    	}
    	stackTagCompound.setInteger(KEY_LINE_SIZE, size);
    }

    /**
     * 線の太さを加算設定
     * @param itemstack
     * @param additemstack
     */
    public static void addLineSize(ItemStack itemstack, ItemStack additemstack)
    {
    	int size = getLineSize(itemstack) + getLineSize(additemstack);
    	setLineSize(itemstack, size);
    }

    /**
     * 線の色を設定
     * @param itemstack
     * @param color
     */
    public static void setColor(ItemStack itemstack, int color)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	stackTagCompound.setInteger(KEY_COLOR, color);
    }

    /**
     * 線の色を設定(RGB)
     * @param itemstack
     * @param red
     * @param green
     * @param blue
     */
    public static void setColor(ItemStack itemstack, int red, int green, int blue)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	stackTagCompound.setInteger(KEY_COLOR, convertRGBToColor(red, green, blue));
    }


    /**
     * ブロックの位置(X座標)を設定
     * @param itemstack
     * @param posX
     */
    public static void setXPos(ItemStack itemstack, int posX)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	stackTagCompound.setInteger(KEY_POS_X, posX);
    }

    /**
     * ブロックの位置(Y座標)を設定
     * @param itemstack
     * @param posY
     */
    public static void setYPos(ItemStack itemstack, int posY)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	stackTagCompound.setInteger(KEY_POS_Y, posY);
    }

    /**
     * ブロックの位置(Z座標)を設定
     * @param itemstack
     * @param posZ
     */
    public static void setZPos(ItemStack itemstack, int posZ)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	stackTagCompound.setInteger(KEY_POS_Z, posZ);
    }

    /**
     * 線の位置(X座標)を設定
     * @param itemstack
     * @param xCoord
     */
    public static void setXCoord(ItemStack itemstack, float xCoord)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	stackTagCompound.setFloat(KEY_COORD_X, xCoord);
    }

    /**
     * 線の位置(Y座標)を設定
     * @param itemstack
     * @param yCoord
     */
    public static void setYCoord(ItemStack itemstack, float yCoord)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	stackTagCompound.setFloat(KEY_COORD_Y, yCoord);
    }

    /**
     * 線の位置(Z座標)を設定
     * @param itemstack
     * @param zCoord
     */
    public static void setZCoord(ItemStack itemstack, float zCoord)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	stackTagCompound.setFloat(KEY_COORD_Z, zCoord);
    }

    /**
     * 設置面を設定
     * @param itemstack
     * @param side
     */
    public static void setSize(ItemStack itemstack, int side)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	stackTagCompound.setInteger(KEY_SIDE, side);
    }

    /**
     * 設置面を初期化
     * @param itemstack
     */
    public static void clearSide(ItemStack itemstack)
    {
    	setSize(itemstack,VALUE_DEFAULT_SIDE);
    }


    /**
     * 線のIDを設定
     * @param itemstack
     * @param lineID
     */
    public static void setLineID(ItemStack itemstack, int lineID)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	stackTagCompound.setInteger(KEY_LINE_ID, lineID);
    }

    /**
     * 線のIDを設定
     * @param itemstack
     * @param lineID
     */
    public static void setDrawFlg(ItemStack itemstack, boolean flg)
    {
    	NBTTagCompound stackTagCompound = getNBTTagCompound(itemstack);
    	stackTagCompound.setBoolean(KEY_LINE_DRAW, flg);
    }


    /*
     *******************************************************************************************
     *
     * util
     *
     *******************************************************************************************
     */
    /**
     * RGB ⇒ Color
     * @param colorRed
     * @param colorGreen
     * @param colorBlue
     * @return
     */
    public static int convertRGBToColor(int colorRed, int colorGreen, int colorBlue)
    {
        return ((colorRed << 16) | (colorGreen << 8) | colorBlue);
    }

    /**
     * Color ⇒ RGB
     * @param color
     * @return
     */
    public static int[] convertColorToRGB(int color)
    {
    	int rgb[] = new int[3];
    	rgb[0] = (color >> 16 & 0xff);
    	rgb[1] = (color >> 8 & 0xff);
    	rgb[2] = (color & 0xff);
    	return rgb;
    }
}
