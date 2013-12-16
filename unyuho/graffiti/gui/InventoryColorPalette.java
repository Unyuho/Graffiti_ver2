package unyuho.graffiti.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class InventoryColorPalette implements IInventory
{
    private ItemStack itemstack;
    private int inventoryWidth;

    private Container eventHandler;
    private World world;

    public InventoryColorPalette(Container par1Container, World world)
    {
        this.itemstack = null;
        this.eventHandler = par1Container;
        this.world = world;
    }

	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return itemstack;
	}

	@Override
	public ItemStack decrStackSize(int numSlot, int size)
	{
        if (itemstack != null)
        {
            ItemStack newItemStack;

            if (itemstack.stackSize <= size)
            {
                newItemStack = itemstack;
                itemstack = null;
                eventHandler.onCraftMatrixChanged(this);
                return newItemStack;
            }
            else
            {
                newItemStack = itemstack.splitStack(size);

                if (itemstack.stackSize == 0)
                {
                	itemstack = null;
                }

                eventHandler.onCraftMatrixChanged(this);
                return newItemStack;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return itemstack;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		this.itemstack = itemstack;

		if(itemstack != null)
		{
	    	NBTTagCompound stackTagCompound;
	    	if(!itemstack.hasTagCompound()){
	    		stackTagCompound = new NBTTagCompound("clickPos");
	    		stackTagCompound.setInteger("lineSize", 1);
	    		stackTagCompound.setInteger("color", 0);
	    		itemstack.setTagCompound(stackTagCompound);
	    	}else{
	    		stackTagCompound = itemstack.getTagCompound();
	    	}

			System.out.println("??????? : " + stackTagCompound.getInteger("color") + ", world = " + world.isRemote);
		}

		if(world.isRemote)
		{
			eventHandler.onCraftMatrixChanged(this);
		}
	}

	@Override
	public String getInvName()
	{
		return "container.colorpalette";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void onInventoryChanged()
	{

	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void openChest()
	{

	}

	@Override
	public void closeChest()
	{
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}
}
