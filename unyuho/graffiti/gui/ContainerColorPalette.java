package unyuho.graffiti.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import unyuho.common.gui.IScrollable;
import unyuho.graffiti.util.ItemGraffitiNBTHelper;
import unyuho.graffiti.util.PacketHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerColorPalette extends Container implements IScrollable
{
    private IInventory invColorPalette;

    private int colorRed;
    private int colorGreen;
    private int colorBlue;
    private int fontSize;
    private boolean existItemStack;

    public ContainerColorPalette(InventoryPlayer inventoryplayer, World world)
    {
    	existItemStack = false;
    	colorRed = 0;
    	colorGreen = 0;
    	colorBlue = 0;
    	fontSize = 1;

    	int slotIndex = 0;

        for (int cnt = 0; cnt < 9; cnt++)
        {
            addSlotToContainer(new Slot(inventoryplayer, slotIndex++, 8 + cnt * 18, 176));
        }

        for (int cntY = 0; cntY < 3; cntY++)
        {
            for (int cntX = 0; cntX < 9; cntX++)
            {
                addSlotToContainer(new Slot(inventoryplayer, slotIndex++, 8 + cntX * 18, 118 + cntY * 18));
            }
        }

        invColorPalette = new InventoryColorPalette(this, world);
        addSlotToContainer(new SlotColorPalette(invColorPalette, slotIndex++, 8, 10));

        for(int cntY = 0 ; cntY < 2 ;cntY++)
        {
        	for(int cntX = 0 ; cntX < 8;cntX++)
            {
        		addSlotToContainer(new SlotDefaultColor(this, (8*cntY)+cntX, slotIndex++, 29+(cntX*18), 10+(cntY*18)));
            }
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
    	//他のユーザーのスロットも更新する場合
    }


    /**
     * 線の色、太さを設定
     * @param color
     * @param fontSize
     */
    public void setItemStack(int color, int fontSize)
    {
        ItemStack itemstack = invColorPalette.getStackInSlotOnClosing(0);

        if (itemstack != null)
        {
        	ItemGraffitiNBTHelper.setColor(itemstack, color);
        	ItemGraffitiNBTHelper.setLineSize(itemstack, fontSize);
        }
    }

    @SideOnly(Side.CLIENT)
    public void sendPacket()
    {
    	int color = ItemGraffitiNBTHelper.convertRGBToColor(colorRed, colorGreen, colorBlue);

    	setItemStack(color, fontSize);

        FMLClientHandler.instance().sendPacket(PacketHandler.getPacket(color, fontSize));
    }

    /**
     * 色をソイヤソイヤするアイテムがあるか
     * @return
     */
    public boolean existItemStack()
    {
    	return existItemStack;
    }

    /**
     * 赤いの
     * @return
     */
    public int getColorRed()
    {
    	return colorRed;
    }

    /**
     * 緑の
     * @return
     */
    public int getColorGreen()
    {
    	return colorGreen;
    }

    /**
     * 青いの
     * @return
     */
    public int getColorBlue()
    {
    	return colorBlue;
    }

    /**
     * 極太
     * @return
     */
    public int getFontSize()
    {
    	return fontSize;
    }

    /**
     * 色をソイヤソイヤしてサーバーにもソイヤソイヤするやつ
     * @param fontSize
     * @param color
     */
    public void setColor(int color)
    {
    	if(existItemStack)
    	{
    		int rgb[] = ItemGraffitiNBTHelper.convertColorToRGB(color);
    		setColor(rgb[0], rgb[1], rgb[2], fontSize);
    	}
    }

    /**
     * 色をソイヤソイヤしてサーバーにもソイヤソイヤするやつ
     * @param colorRed
     * @param colorGreen
     * @param colorBlue
     * @param fontSize
     */
    public void setColor(int colorRed, int colorGreen, int colorBlue, int fontSize)
    {
    	this.colorRed = colorRed;
    	this.colorGreen = colorGreen;
    	this.colorBlue = colorBlue;
    	this.fontSize = fontSize;

    	sendPacket();
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1iInventory)
    {
    	super.onCraftMatrixChanged(par1iInventory);

    	ItemStack itemstack = par1iInventory.getStackInSlot(0);

    	if(itemstack != null)
    	{
    		existItemStack = true;

        	fontSize = ItemGraffitiNBTHelper.getLineSize(itemstack);
    		int rgb[] = ItemGraffitiNBTHelper.getRGB(itemstack);
        	colorRed = rgb[0];
        	colorGreen = rgb[1];
        	colorBlue = rgb[2];
    	}
    	else
    	{
    		existItemStack = false;

    		fontSize = ItemGraffitiNBTHelper.VALUE_DEFAULT_LINE_SIZE;

    		int rgb[] = ItemGraffitiNBTHelper.convertColorToRGB(ItemGraffitiNBTHelper.VALUE_DEFAULT_COLOR);
        	colorRed = rgb[0];
        	colorGreen = rgb[1];
        	colorBlue = rgb[2];
    	}

    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        ItemStack itemstack = invColorPalette.getStackInSlotOnClosing(0);

        if (itemstack != null)
        {
        	entityplayer.dropPlayerItem(itemstack);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (i == 36)
            {
                if (!mergeItemStack(itemstack1, 0, 36, true))
                {
                    return null;
                }
            }
            else
            {
            	Slot slotColorPalette = (Slot)inventorySlots.get(36);
            	if(!slotColorPalette.isItemValid(itemstack1))
            	{
            		return null;
            	}

            	if(!mergeItemStack(itemstack1, 36, 37, true))
            	{
            		return null;
            	}
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
            	return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }



    @Override
	public void scrollPerformed(int scrollID, int value)
    {
		if(scrollID == 0)
		{
			if(colorRed != value)
			{
				colorRed = value;
			}
		}
		else if(scrollID == 1)
		{
			if(colorGreen != value)
			{
				colorGreen = value;
			}
		}
		else if(scrollID == 2)
		{
			if(colorBlue != value)
			{
				colorBlue = value;
			}
		}
		else if(scrollID == 3)
		{
			if(fontSize != value)
			{
				fontSize = value;
			}
		}

		sendPacket();
	}

}
