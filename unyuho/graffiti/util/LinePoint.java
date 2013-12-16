package unyuho.graffiti.util;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.google.common.io.ByteArrayDataInput;

public class LinePoint
{
	private float lineWidth = 0.1F;
	private float cubeSize = 1.0F;
	private float posXstart;
	private float posYstart;
	private float posXend;
	private float posYend;
	private int side;
	private int color;
	private int size;
	private int lineID;
	private boolean connect;
	private int connectID;
	private boolean reverse;

    private LinePoint()
    {
        this(0F, 0F, 0F, 0F, 0, 0, 1, 0, 0, false, false);
    }

    public LinePoint(NBTTagCompound nbttagcompound1)
    {
        this(0F, 0F, 0F, 0F, 0, 0, 1, 0, 0, false, false);
        readFromNBT(nbttagcompound1);
    }

    public LinePoint(ByteArrayDataInput data)
    {
        this(0F, 0F, 0F, 0F, 0, 0, 1, 0, 0, false, false);
        readToPacket(data);
    }

    public LinePoint(PosLinePoint plpoint, int side, ItemStack itemstack, int lineID, int connectID, boolean connect, boolean reverse)
    {
        this(plpoint.getXCoordFrom(), plpoint.getYCoordFrom(), plpoint.getXCoordTo(), plpoint.getYCoordTo(), side, ItemGraffitiNBTHelper.getColor(itemstack), ItemGraffitiNBTHelper.getLineSize(itemstack), lineID, connectID, connect, reverse);
    }


    public LinePoint(float posXstart, float posYstart, float posXend, float posYend, int side, int color, int size, int lineID, int connectID, boolean connect, boolean reverse)
    {
        this.posXstart = posXstart;
        this.posYstart = posYstart;
        this.posXend = posXend;
        this.posYend = posYend;
        this.side = side;
        this.color = color;
        this.size = size;
        this.lineID = lineID;
        this.connect = connect;
        this.connectID = connectID;
        this.reverse = reverse;
    }

    public int getSide()
    {
    	return side;
    }

    /**
     * NBTへ保存
     * @return
     */
    public void writeToNBT(NBTTagList nbttaglist)
    {
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setFloat("posXstart", posXstart);
		nbttagcompound1.setFloat("posYstart", posYstart);
		nbttagcompound1.setFloat("posXend", posXend);
		nbttagcompound1.setFloat("posYend", posYend);
		nbttagcompound1.setInteger("side", side);
		nbttagcompound1.setInteger("color", color);
		nbttagcompound1.setInteger("size", size);
		nbttagcompound1.setInteger("lineID", lineID);
		nbttagcompound1.setBoolean("connect", connect);
		nbttagcompound1.setInteger("connectID", connectID);
		nbttagcompound1.setBoolean("reverse", reverse);

		nbttaglist.appendTag(nbttagcompound1);
    }

    /**
     * NBTから読み込み
     * @return
     */
    public void readFromNBT(NBTTagCompound nbttagcompound1)
    {
    	posXstart = nbttagcompound1.getFloat("posXstart");
    	posYstart = nbttagcompound1.getFloat("posYstart");
    	posXend = nbttagcompound1.getFloat("posXend");
    	posYend = nbttagcompound1.getFloat("posYend");
    	side = nbttagcompound1.getInteger("side");
    	color = nbttagcompound1.getInteger("color");
    	size = nbttagcompound1.getInteger("size");
    	lineID = nbttagcompound1.getInteger("lineID");
    	connect = nbttagcompound1.getBoolean("connect");
    	connectID = nbttagcompound1.getInteger("connectID");
    	reverse = nbttagcompound1.getBoolean("reverse");
    }

    public void writeToPacket(DataOutputStream dos)
    {
		try{
			dos.writeFloat(posXstart);
			dos.writeFloat(posYstart);
			dos.writeFloat(posXend);
			dos.writeFloat(posYend);
			dos.writeInt(side);
			dos.writeInt(color);
			dos.writeInt(size);
			dos.writeInt(lineID);
			dos.writeInt(connectID);
			dos.writeBoolean(connect);
			dos.writeBoolean(reverse);

		}catch (IOException e){
			e.printStackTrace();
		}
    }


    public void readToPacket(ByteArrayDataInput data)
    {
    	posXstart = data.readFloat();
    	posYstart = data.readFloat();
    	posXend = data.readFloat();
    	posYend = data.readFloat();
    	side = data.readInt();
    	color = data.readInt();
    	size = data.readInt();
    	lineID = data.readInt();
    	connectID = data.readInt();
    	connect = data.readBoolean();
    	reverse = data.readBoolean();
    }

    public void setEndPosX(float posXend)
    {
       	if(posXstart == posXend)
       	{
           	if(posXstart == lineWidth)
           	{
           		posXstart = 0.0F;
           		posXend = 0.0F;
        	}
           	else if(posXstart == (cubeSize - lineWidth) )
           	{
           		posXstart = cubeSize;
           		posXend = cubeSize;
        	}
    	}

        this.posXend = posXend;
    }

    public void setEndPosY(float posYend)
    {
       	if(posYstart == posYend)
       	{
           	if(posYstart == lineWidth)
           	{
           		posYstart = 0.0F;
           		posYend = 0.0F;
        	}
           	else if(posXstart == (cubeSize - lineWidth) )
           	{
           		posYstart = cubeSize;
           		posYend = cubeSize;
        	}
    	}

        this.posYend = posYend;
    }


    public float getAngle()
    {
    	double atan2 = Math.atan2(posYend - posYstart,posXend - posXstart);
    	return (float)(atan2 * 180.0D / Math.PI);
    }

    public double getAngleLength()
    {
        if(isSide())
        {
        	return getAngleXLength();
        }
        return getAngleYLength();
    }

    private double getAngleYLength()
    {
    	double exRadians = Math.toRadians(getAngle());
        double len = getLengthY();
        return Math.abs(len / Math.sin(exRadians));
    }

    private double getAngleXLength()
    {
    	double exRadians = Math.toRadians(getAngle());
        double len = getLengthX();
        return Math.abs(len / Math.cos(exRadians));
    }

    public float getMinX()
    {
    	return posXstart;
    }

    public float getMinY()
    {
    	return posYstart;
    }

    public float getMaxX()
    {
    	return posXend;
    }

    public float getMaxY()
    {
    	return posYend;
    }


    public float getLengthX()
    {
    	return Math.abs(posXend - posXstart);
    }

    public float getLengthY()
    {
    	return Math.abs(posYend - posYstart);
    }

    public int getColor()
    {
    	return color;
    }

    public int getID()
    {
    	return lineID;
    }

    public boolean isConnect()
    {
    	return connect;
    }

    public boolean isReverse()
    {
    	return reverse;
    }

    public int getConnectID()
    {
    	return connectID;
    }


    public float getLineSize()
    {
    	return BlockGraffitiHelper.getLineWidth() * size;
    }

    public boolean isSide()
    {
    	float angle = getAngle();
    	return ( Math.abs(angle) > 135D || Math.abs(angle) < 45);
    }
}