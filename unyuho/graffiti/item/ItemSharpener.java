package unyuho.graffiti.item;

import unyuho.graffiti.Graffiti;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemSharpener extends Item
{
    public ItemSharpener(int itemID)
    {
		super(itemID);

		setCreativeTab(CreativeTabs.tabDecorations);
		setMaxStackSize(1);

		//後で変える
		setMaxDamage(0);
		setHasSubtypes(false);

		setUnlocalizedName("SharpenerItem");
		setTextureName("unyuho:Sharpener");
	}

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	par3EntityPlayer.openGui(Graffiti.instance, 1, par2World, 0 , 0, 0);
        return par1ItemStack;
    }

}