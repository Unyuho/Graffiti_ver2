package unyuho.graffiti.gui;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class SlotDefaultColor extends Slot
{
	private ContainerColorPalette container;
	private Icon icon;
	private int color;

    public SlotDefaultColor(ContainerColorPalette container, int metadata, int slotIndex, int xDisplayPosition, int yDisplayPosition)
    {
        super(null, slotIndex, xDisplayPosition, yDisplayPosition);

        BlockColored cloth = (BlockColored)Block.cloth;
        this.color = ItemDye.dyeColors[cloth.getBlockFromDye(metadata)];
        this.icon = cloth.getIcon(0, metadata);
        this.container = container;
    }

    public Icon getIcon()
    {
    	return icon;
    }

    public int getColor()
    {
    	return color;
    }

    public void onSlotClicked()
    {
    	container.setColor(color);
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }

    @Override
    public ItemStack getStack()
    {
        return null;
    }

    @Override
    public void putStack(ItemStack par1ItemStack)
    {
    }

    @Override
    public void onSlotChanged()
    {
    }

    @Override
    public int getSlotStackLimit()
    {
        return 0;
    }

    @Override
    public ItemStack decrStackSize(int par1)
    {
        return null;
    }

    @Override
    public boolean isSlotInInventory(IInventory par1IInventory, int par2)
    {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return false;
    }
}
