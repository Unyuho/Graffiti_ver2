package unyuho.graffiti.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import unyuho.graffiti.block.TileEntityGraffitiBlock;
import unyuho.graffiti.gui.ContainerColorPalette;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
    	if(packet.channel.equals("graffiti_gui"))
    	{
    		onPacketDataGui(manager, packet, player);
    	}
    	else if(packet.channel.equals("graffiti_tile"))
    	{
    		onPacketDataTileEntity(manager, packet, player);
    	}
    }

    private void onPacketDataGui(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
    	ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);

		int color = data.readInt();
		int fontSize = data.readInt();

		EntityPlayerMP entityplayer = (EntityPlayerMP)player;
		ContainerColorPalette container = (ContainerColorPalette)entityplayer.openContainer;

		container.setItemStack(color, fontSize);
    }


    private void onPacketDataTileEntity(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
		ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);

		int x = data.readInt();
		int y = data.readInt();
		int z = data.readInt();

		TileEntity tileentity = null;

		World worldClient = FMLClientHandler.instance().getClient().theWorld;
		World worldServer = ((EntityPlayer)player).worldObj;

		if(worldClient != null && worldServer == null)
		{
			tileentity = worldClient.getBlockTileEntity(x, y, z);
		}

		if(worldServer != null)
		{
			tileentity = worldServer.getBlockTileEntity(x, y, z);
		}

		if (tileentity != null) {
			if(tileentity instanceof TileEntityGraffitiBlock)
			{
				((TileEntityGraffitiBlock)tileentity).readToPacket(data);
			}
		}
    }


    /**
     * GUI用
     * @param color
     * @param fontSize
     * @return
     */
    public static Packet getPacket(int color, int fontSize)
    {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		try
		{
			dos.writeInt(color);
			dos.writeInt(fontSize);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();
		packet.isChunkDataPacket = true;
		packet.channel = "graffiti_gui";

		return packet;
    }


    /**
     * TileEntity同期用
     * @param tileentity
     * @return
     */
    public static Packet getPacket(TileEntityGraffitiBlock tileentity)
    {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		int x = tileentity.xCoord;
		int y = tileentity.yCoord;
		int z = tileentity.zCoord;

		try
		{
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			tileentity.writeToPacket(dos);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();
		packet.isChunkDataPacket = true;
		packet.channel = "graffiti_tile";

		return packet;
    }
}
