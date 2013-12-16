package unyuho.graffiti.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import unyuho.graffiti.block.TileEntityGraffitiBlock;
import unyuho.graffiti.util.BlockGraffitiHelper;
import unyuho.graffiti.util.ItemGraffitiNBTHelper;
import unyuho.graffiti.util.LinePoint;
import unyuho.graffiti.util.PosLinePoint;
import unyuho.kawo.Point;
import unyuho.kawo.Vec2DImpl;
import unyuho.kawo.Vec2DIntersectSupport;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGraffiti extends Item
{
	private int graffitiBlockID;
	private ItemBlock graffitiBlock = null;
	private int posionX = 0;
	private int posionY = 0;
	private int color = 0xFFFFFF;
	private int maxLength = 0;

    public ItemGraffiti(int itemID , int blockID, int posionX, int posionY, int color, int maxLength)
    {
		super(itemID);

		setCreativeTab(CreativeTabs.tabDecorations);

		setUnlocalizedName("GraffitiItem");
		setTextureName("unyuho:Graffiti");

		//後で変える
		setMaxDamage(0);
		setHasSubtypes(false);

		graffitiBlockID = blockID;
		graffitiBlock = (ItemBlock)Item.itemsList[blockID];

		this.posionX = posionX;
		this.posionY = posionY;
		this.color = color;
		this.maxLength = maxLength;
	}

    @Override
    public void addInformation(ItemStack par1ItemStack,EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);

    	par3List.add("LineWidth : " + ItemGraffitiNBTHelper.getLineSize(par1ItemStack));

    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
    	if(par2 == 0)
    	{
    		return ItemGraffitiNBTHelper.getColor(par1ItemStack);
    	}
    	else
    	{
    		return ItemGraffitiNBTHelper.getColor(par1ItemStack);
    	}
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(this.getIconString());
    }
    */

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
    	return 10;
    }



    @Override
    public void onUsingItemTick(ItemStack stack, EntityPlayer player, int count)
    {
    	//クライアントは帰れ
    	if(player.worldObj.isRemote)
    	{
    		return;
    	}

    	//右クリック話したら終わり
    	if(!Mouse.isButtonDown(1))
    	{
    		player.clearItemInUse();
    		return;
    	}

        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
        if (movingobjectposition == null)
        {
        	player.clearItemInUse();
            return ;
        }
        else
        {
            if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
            {
                int posX = movingobjectposition.blockX;
                int posY = movingobjectposition.blockY;
                int posZ = movingobjectposition.blockZ;
                int side = movingobjectposition.sideHit;
                float xCoord = (float)movingobjectposition.hitVec.xCoord - posX;
                float yCoord = (float)movingobjectposition.hitVec.yCoord - posY;
                float zCoord = (float)movingobjectposition.hitVec.zCoord - posZ;

                if(count == getMaxItemUseDuration(stack))
                {
                	ItemGraffitiNBTHelper.clearSide(stack);
                }

                if(count % 2 == 0)
                {
                    player.clearItemInUse();
                    player.setItemInUse(stack, getMaxItemUseDuration(stack)-1);

                    onItemUseTest(stack, player, player.worldObj, posX, posY, posZ, side, xCoord, yCoord, zCoord);
                }
            }
        }

    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
    	return EnumAction.none;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World,EntityPlayer par3EntityPlayer, int par4)
    {
    	super.onPlayerStoppedUsing(par1ItemStack, par2World, par3EntityPlayer, par4);
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	if(!par2World.isRemote)
    	{
       		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
    	}

    	return par1ItemStack;
    };

    /*
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int posX, int posY, int posZ, int side, float xCoord, float yCoord, float zCoord)
     */

    public boolean onItemUseTest(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int posX, int posY, int posZ, int side, float xCoord, float yCoord, float zCoord)
    {
    	if(par3World.isRemote)
    	{
    		return true;
    	}

    	//設置位置を計算
    	ForgeDirection offset;
    	if(par3World.getBlockId(posX, posY, posZ) != graffitiBlock.getBlockID())
    	{
    		offset = ForgeDirection.getOrientation(side);
    	}
    	else
    	{
    		offset = ForgeDirection.UNKNOWN;
    	}

    	int placePosX = posX + offset.offsetX;
    	int placePosY = posY + offset.offsetY;
    	int placePosZ = posZ + offset.offsetZ;


    	int beforePosX = ItemGraffitiNBTHelper.getXPos(par1ItemStack);
    	int beforePosY = ItemGraffitiNBTHelper.getYPos(par1ItemStack);
    	int beforePosZ = ItemGraffitiNBTHelper.getZPos(par1ItemStack);
		int beforeside = ItemGraffitiNBTHelper.getSide(par1ItemStack);
		float beforeXCoord = ItemGraffitiNBTHelper.getXCoord(par1ItemStack);
		float beforeYCoord = ItemGraffitiNBTHelper.getYCoord(par1ItemStack);
		float beforeZCoord = ItemGraffitiNBTHelper.getZCoord(par1ItemStack);


    	//以下の場合は、位置情報の更新
    	//　座標の全てが不一致である場合
    	//　設置面情報を未保持
    	if( (beforePosX != placePosX && beforePosY != placePosY & beforePosZ != placePosZ) ||
    		(!ItemGraffitiNBTHelper.isState(par1ItemStack)))
    	{
    		//位置情報更新
    		ItemGraffitiNBTHelper.setXPos(par1ItemStack, placePosX);
    		ItemGraffitiNBTHelper.setYPos(par1ItemStack, placePosY);
    		ItemGraffitiNBTHelper.setZPos(par1ItemStack, placePosZ);
    		ItemGraffitiNBTHelper.setSize(par1ItemStack, side);

    		ItemGraffitiNBTHelper.setXCoord(par1ItemStack, xCoord);
    		ItemGraffitiNBTHelper.setYCoord(par1ItemStack, yCoord);
    		ItemGraffitiNBTHelper.setZCoord(par1ItemStack, zCoord);

    		ItemGraffitiNBTHelper.setDrawFlg(par1ItemStack, false);
    		System.out.println("placePosX?");
    		return false;
    	}

    	//10マス以上離れている場合は処理停止
    	if( Math.abs(placePosX - beforePosX) > 10 || Math.abs(placePosY - beforePosY) > 10 || Math.abs(placePosZ - beforePosZ) > 10)
    	{
    		ItemGraffitiNBTHelper.setDrawFlg(par1ItemStack, false);
    		System.out.println("10?");
    		return false;
    	}

    	if( Math.abs(beforeXCoord - xCoord) < 0.02F & Math.abs(beforeYCoord - yCoord) < 0.02F && Math.abs(beforeZCoord - zCoord) < 0.02F)
        {
    		System.out.println("xCoord?");
        	return false;
        }

    	//設置面が異なる場合は処理停止
   		if(beforeside != side)
		{
   			System.out.println("side?");
   			ItemGraffitiNBTHelper.clearSide(par1ItemStack);
   			ItemGraffitiNBTHelper.setDrawFlg(par1ItemStack, false);
   			return false;
		}


		//位置情報更新
		ItemGraffitiNBTHelper.setXPos(par1ItemStack, placePosX);
		ItemGraffitiNBTHelper.setYPos(par1ItemStack, placePosY);
		ItemGraffitiNBTHelper.setZPos(par1ItemStack, placePosZ);
		ItemGraffitiNBTHelper.setSize(par1ItemStack, side);

		ItemGraffitiNBTHelper.setXCoord(par1ItemStack, xCoord);
		ItemGraffitiNBTHelper.setYCoord(par1ItemStack, yCoord);
		ItemGraffitiNBTHelper.setZCoord(par1ItemStack, zCoord);


   		//めんどいから基点をベースに座標反転
   		ForgeDirection sideDirection = ForgeDirection.getOrientation(side);

   		boolean reverse;
   		if(ForgeDirection.WEST == sideDirection || ForgeDirection.EAST == sideDirection)
   		{
   			reverse = (placePosZ < beforePosZ);
   		}
   		else
   		{
   			reverse = (placePosX < beforePosX);
   		}

   		if(reverse)
   		{
   			int tmpPosX = placePosX;
   			int tmpPosY = placePosY;
   			int tmpPosZ = placePosZ;
   			float tmpXCoord = xCoord;
   			float tmpYCoord = yCoord;
   			float tmpZCoord = zCoord;

   			placePosX = beforePosX;
   			placePosY = beforePosY;
   			placePosZ = beforePosZ;
   			xCoord = beforeXCoord;
   			yCoord = beforeYCoord;
   			zCoord = beforeZCoord;

   			beforePosX = tmpPosX;
   			beforePosY = tmpPosY;
   			beforePosZ = tmpPosZ;
   			beforeXCoord = tmpXCoord;
   			beforeYCoord = tmpYCoord;
   			beforeZCoord = tmpZCoord;
   		}

   		//複数ブロックの配置判定
   		List<PosLinePoint> list;
		if(ForgeDirection.DOWN == sideDirection || ForgeDirection.UP == sideDirection)
		{
	   		list = getPosLinePoint(placePosX, beforePosX, placePosZ, beforePosZ, xCoord, beforeXCoord, zCoord, beforeZCoord);
		}
		else if(ForgeDirection.NORTH == sideDirection || ForgeDirection.SOUTH == sideDirection)
		{
	   		list = getPosLinePoint(placePosX, beforePosX, placePosY, beforePosY, xCoord, beforeXCoord, yCoord, beforeYCoord);
		}
		else
		{
			//if(ForgeDirection.WEST == sideDirection || ForgeDirection.EAST == sideDirection)
	   		list = getPosLinePoint(placePosZ, beforePosZ, placePosY, beforePosY, zCoord, beforeZCoord, yCoord, beforeYCoord);
		}

		//設置
		if(!tryPlaceBlock(par3World, par2EntityPlayer, par1ItemStack, posX, posY, posZ, sideDirection, offset, list, reverse))
		{
			return false;
		}

    	return false;
    }


    private boolean isYPos(ForgeDirection sideDirection)
    {
    	return (ForgeDirection.DOWN == sideDirection || ForgeDirection.UP == sideDirection);
    }

    private boolean isZPos(ForgeDirection sideDirection)
    {
    	return (ForgeDirection.NORTH == sideDirection || ForgeDirection.SOUTH == sideDirection);
    }

    private boolean isXPos(ForgeDirection sideDirection)
    {
    	return (ForgeDirection.WEST == sideDirection || ForgeDirection.EAST == sideDirection);
    }

    /**
     * 闇だ…
     * @param world
     * @param entityPlayer
     * @param itemstack
     * @param posX
     * @param posY
     * @param posZ
     * @param sideDirection
     * @param offset
     * @param list
     * @return
     */
    private boolean tryPlaceBlock(World world, EntityPlayer entityPlayer, ItemStack itemstack, int posX, int posY, int posZ, ForgeDirection sideDirection, ForgeDirection offset, List<PosLinePoint> list, boolean reverse)
    {
   		int placeSide = sideDirection.ordinal();

   		//配置チェック
   		Iterator<PosLinePoint> iterator = list.iterator();
   		while(iterator.hasNext())
   		{
   			PosLinePoint plpoint = iterator.next();

   			int xPos = isXPos(sideDirection) ? posX : plpoint.getPosX();
   			int yPos = isYPos(sideDirection) ? posY : plpoint.getPosY();
   			int zPos = isZPos(sideDirection) ? posZ : isXPos(sideDirection) ? plpoint.getPosX() : plpoint.getPosY();

   			if(!existsBlock(world, xPos + offset.offsetX, yPos + offset.offsetY, zPos + offset.offsetZ))
   			{
   				return false;
   			}
   			if(!enablePlace(world, xPos, yPos, zPos, placeSide))
   			{
   				return false;
   			}
   		}

   		int lineID = getLineID(itemstack);
   		int connectID = BlockGraffitiHelper.getNewConnectID();

   		//配置
   		int index = 0;
   		int lastIndex = 0;
   		if(reverse)
   		{
   			lastIndex = list.size()-1;
   		}

   		iterator = list.iterator();
   		while(iterator.hasNext())
   		{
   			PosLinePoint plpoint = iterator.next();
   			LinePoint lpoint = new LinePoint(plpoint, placeSide, itemstack, lineID, connectID, (index++ == lastIndex), reverse);

   			int xPos = isXPos(sideDirection) ? posX : plpoint.getPosX();
   			int yPos = isYPos(sideDirection) ? posY : plpoint.getPosY();
   			int zPos = isZPos(sideDirection) ? posZ : isXPos(sideDirection) ? plpoint.getPosX() : plpoint.getPosY();

   			placeBlock(entityPlayer, world, xPos + offset.offsetX, yPos + offset.offsetY, zPos + offset.offsetZ, placeSide, lpoint);
   		}

   		return true;
    }


    /**
     * 線をブロック単位で分割
     * @param checkPosX
     * @param beforePosX
     * @param checkPosY
     * @param beforePosY
     * @param xCoord
     * @param beforeXCoord
     * @param yCoord
     * @param beforeYCoord
     * @return
     */
    private List<PosLinePoint> getPosLinePoint(int checkPosX, int beforePosX, int checkPosY, int beforePosY, float xCoord, float beforeXCoord, float yCoord, float beforeYCoord)
    {
    	List<PosLinePoint> list = new ArrayList<PosLinePoint>();

   		Vec2DImpl vec2 = new Vec2DImpl(beforePosX + beforeXCoord , beforePosY + beforeYCoord, checkPosX + xCoord , checkPosY + yCoord);

   		int addY = (checkPosY < beforePosY ? -1 : 1);
   		int heightY = (checkPosY < beforePosY ? 0 : 1);
   		int tmpPosX = beforePosX;
   		int tmpPosY = beforePosY;

   		Vec2DImpl tmpVec2;
   		Point p;

   		Point pointX;
   		Point pointY;

   		do
   		{
	   		Vec2DImpl tmpVecX = new Vec2DImpl(tmpPosX +1, tmpPosY + 0, tmpPosX +1, tmpPosY + 1);
	   		pointX = Vec2DIntersectSupport.getIntercectPoint(vec2, tmpVecX);

	   		Vec2DImpl tmpVecY = new Vec2DImpl(tmpPosX + 0 , tmpPosY + heightY, tmpPosX + 1 , tmpPosY + heightY);
	   		pointY = Vec2DIntersectSupport.getIntercectPoint(vec2, tmpVecY);

	   		PosLinePoint lpoint = new PosLinePoint(beforePosX, beforePosY, beforeXCoord, 1.0F, beforeYCoord, (float)heightY);


	   		if(pointX == null && pointY == null)
	   		{
	   			lpoint.setEndCoordY(yCoord);
	   			lpoint.setEndCoordX(xCoord);
	   		}
	   		else if(pointX != null && pointY != null)
	   		{
	   			lpoint.setEndCoordY(1.0F);
	   			lpoint.setEndCoordX((float)heightY);
	   			tmpPosX++;
	   			tmpPosY += addY;

	   			beforeXCoord = 0.0F;
	   			beforeYCoord = (float)heightY - (float)addY;
	   		}
	   		else if(pointX != null)
		   	{
	   			lpoint.setEndCoordY((float)(pointX.y - tmpPosY));
		   		tmpPosX++;

		   		beforeXCoord = 0.0F;
		   		beforeYCoord = (float)(pointX.y - tmpPosY);
		   	}
	   		else if(pointY != null)
		   	{
		   		lpoint.setEndCoordX((float)(pointY.x - tmpPosX));
		   		tmpPosY += addY;

		   		beforeXCoord = (float)(pointY.x - tmpPosX);
		   		beforeYCoord = (float)heightY - (float)addY;
	   		}

	    	beforePosX = tmpPosX;
	    	beforePosY = tmpPosY;

	    	list.add(lpoint);

   		}while(pointX != null || pointY != null);

    	return list;
    }

    /**
     * 設置位置にブロックがあるか
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    private boolean existsBlock(World world, int x, int y, int z)
    {
		int tmpBlockID = world.getBlockId(x, y, z);
		if(tmpBlockID != 0 && tmpBlockID != graffitiBlockID)
		{
			return false;
		}
		return true;
    }

    /**
     * 設置位置にブロックを置けるか
     * @param world
     * @param x
     * @param y
     * @param z
     * @param side
     * @return
     */
    private boolean enablePlace(World world, int x, int y, int z, int side)
    {
		int tmpBlockID = world.getBlockId(x, y, z);
		if(tmpBlockID != graffitiBlockID)
		{
	    	Block block = Block.blocksList[tmpBlockID];
	    	if(!isEnable(block , side))
	    	{
	    		//特殊形状はめんどいので対象外
	    		//そのうちチェストとかは考える
	    		return false;
	    	}
		}
		return true;
    }

    /**
     * ブロック設置
     * @param entityPlayer
     * @param world
     * @param x
     * @param y
     * @param z
     * @param side
     * @param xCoord
     * @param yCoord
     * @param zCoord
     * @param lpoint
     * @return
     */
    private boolean placeBlock(EntityPlayer entityPlayer, World world, int x, int y, int z, int side, LinePoint lpoint)
    {
    	//private boolean placeBlock(EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float xCoord, float yCoord, float zCoord, LinePoint lpoint)

		int tmpBlockID = world.getBlockId(x, y, z);
		if(tmpBlockID == 0)
		{
			ItemStack itemstack = new ItemStack(graffitiBlock);
			if(!graffitiBlock.onItemUse(itemstack, entityPlayer, world, x, y, z, side, 0.0F, 0.0F, 0.0F))
			{
				return false;
			}
		}

		TileEntityGraffitiBlock tileEntity = (TileEntityGraffitiBlock)world.getBlockTileEntity(x, y, z);
		if(tileEntity != null)
		{
			tileEntity.clickPos(lpoint);
			world.markBlockForUpdate(x, y, z);
		}

		return true;
    }

    /**
     * 置けるブロック
     * @param block
     * @param side
     * @return
     */
    public boolean isEnable(Block block, int side)
    {
    	return (block == null) || block.isOpaqueCube() || (block.blockID == Block.glass.blockID);
    }

    /**
     * 線のID
     * @param par1ItemStack
     * @return
     */
    private int getLineID(ItemStack par1ItemStack)
    {
    	int lineID;

		if(ItemGraffitiNBTHelper.getDrawFlg(par1ItemStack))
		{
			lineID = ItemGraffitiNBTHelper.getLineID(par1ItemStack);
		}
		else
		{
			lineID = BlockGraffitiHelper.getNewLineID();
			ItemGraffitiNBTHelper.setLineID(par1ItemStack, lineID);
		}

		ItemGraffitiNBTHelper.setDrawFlg(par1ItemStack, true);

		return lineID;
    }


    @SideOnly(Side.CLIENT)
    public void displayItemInfo(ItemStack itemstack, ScaledResolution scaledresolution)
    {
    	String info = "LineWidth : " + ItemGraffitiNBTHelper.getLineSize(itemstack) + " , State : ";
    	if(ItemGraffitiNBTHelper.isState(itemstack))
    	{
    		ForgeDirection offset = ForgeDirection.getOrientation(ItemGraffitiNBTHelper.getSide(itemstack));
    		info += "Side( " + offset.toString() + " )";
    	}
    	else
    	{
    		info += "Empty";
    	}

        int w = scaledresolution.getScaledWidth();
        int h = scaledresolution.getScaledHeight();
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(info, w - 190 + ((-1) * posionY), h - 40 + ((-1) * posionX), color);
    }
}