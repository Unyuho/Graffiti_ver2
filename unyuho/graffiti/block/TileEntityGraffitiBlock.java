package unyuho.graffiti.block;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import unyuho.graffiti.util.LinePoint;
import unyuho.graffiti.util.LinePointManager;
import unyuho.graffiti.util.PacketHandler;

import com.google.common.io.ByteArrayDataInput;

public class TileEntityGraffitiBlock extends TileEntity
{
	private LinePointManager manager;
	private boolean bCollision;

    public TileEntityGraffitiBlock()
    {
		manager = new LinePointManager();
		bCollision = false;
    }


	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		manager.readFromNBT(par1NBTTagCompound);

		if(par1NBTTagCompound.hasKey("bCollision"))
		{
			bCollision = par1NBTTagCompound.getBoolean("bCollision");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		manager.writeToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setBoolean("bCollision", bCollision);
	}

    public boolean[] getSideArray()
    {
    	return manager.getSideArray();
	}
/*
    public void clickPos(float posXstart, float posYstart, float posZstart, float posXend, float posYend, float posZend, int side, int color, int size, int lineID, boolean connect)
    {
    	manager.addPoint(posXstart, posYstart, posZstart, posXend, posYend, posZend, side, color, size, lineID, connect);
	}
*/
    public void clickPos(LinePoint point)
    {
    	manager.addPoint(point);
	}

    public boolean removePos()
    {
    	return manager.removePoint();
	}

    public LinePoint[] getPoints()
    {
    	return manager.getArray();
    }

    public boolean isConnect()
    {
    	return manager.isConnect();
    }

	public void readToPacket(ByteArrayDataInput data)
	{
		manager.readToPacket(data);

		try {
			bCollision = data.readBoolean();
		} catch (Exception e) {
			e.printStackTrace();
			bCollision = true;
		}
	}


	public void writeToPacket(DataOutputStream dos)
	{
		manager.writeToPacket(dos);

		try {
			dos.writeBoolean(bCollision);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void changeCollision()
	{
		bCollision = !bCollision;
	}

	public boolean getCollision()
	{
		return bCollision;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		//パケットの取得
		return PacketHandler.getPacket(this);
	}

}
