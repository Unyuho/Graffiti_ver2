package unyuho.graffiti.gui;

import unyuho.graffiti.item.ItemGraffiti;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class SlotColorPalette extends Slot
{
    private final IInventory craftMatrix;

    public SlotColorPalette(IInventory par1IInventory, int par4, int par5, int par6)
    {
        super(par1IInventory, par4, par5, par6);
        this.craftMatrix = par1IInventory;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
    	if(par1ItemStack == null)
    	{
    		return false;
    	}

    	Item item = Item.itemsList[par1ItemStack.itemID];

        return (item instanceof ItemGraffiti);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
    	super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
    }
}
