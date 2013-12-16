package unyuho.graffiti.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import unyuho.common.gui.GuiScrollBar;
import unyuho.common.gui.GuiScrollBarHorizontal;
import unyuho.graffiti.util.ItemGraffitiNBTHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiColorPalette extends GuiContainer
{
	private static final ResourceLocation recource = new ResourceLocation("unyuho","textures/gui/LineGui.png");

	//スクロールバー
	private GuiScrollBar scrollBarRed;
	private GuiScrollBar scrollBarGreen;
	private GuiScrollBar scrollBarBlue;
	private GuiScrollBar scrollBarFontSize;
	private GuiScrollBar currentScrollBar;
	private List<GuiScrollBar> scrollBarList;

	//線の色、太さ
    private int colorRed;
    private int colorGreen;
    private int colorBlue;
    private int fontSize;

    //container
    private ContainerColorPalette container;
    private Icon icon;

	//基本色のスロット
    private List<SlotDefaultColor> slotColorList;

    public GuiColorPalette(InventoryPlayer inventoryplayer, World world)
    {
        super(new ContainerColorPalette(inventoryplayer, world));

        this.ySize = 200;
        this.xSize = 175;

        container = (ContainerColorPalette)inventorySlots;
    }

    @Override
    public void initGui()
    {
    	super.initGui();

    	int minColor = ItemGraffitiNBTHelper.RANGE_MIN_COLOR;
    	int maxColor = ItemGraffitiNBTHelper.RANGE_MAX_COLOR;
    	int minFontSize = ItemGraffitiNBTHelper.VALUE_DEFAULT_LINE_SIZE;
    	int maxFontSize = ItemGraffitiNBTHelper.VALUE_MAX_LINE_SIZE;

    	//スクロールバー設定
    	scrollBarRed = new GuiScrollBarHorizontal(container, 0, this.guiLeft + 46, this.guiTop + 48, 104, minColor , maxColor);
    	scrollBarGreen = new GuiScrollBarHorizontal(container, 1, this.guiLeft + 46, this.guiTop + 65, 104, minColor , maxColor);
    	scrollBarBlue = new GuiScrollBarHorizontal(container, 2, this.guiLeft + 46, this.guiTop + 82, 104, minColor , maxColor);
    	scrollBarFontSize = new GuiScrollBarHorizontal(container, 3, this.guiLeft + 46, this.guiTop + 99, 104, minFontSize , maxFontSize);

    	scrollBarList = new ArrayList<GuiScrollBar>();
    	scrollBarList.add(scrollBarRed);
    	scrollBarList.add(scrollBarGreen);
    	scrollBarList.add(scrollBarBlue);
    	scrollBarList.add(scrollBarFontSize);

    	//初期値
        colorRed = scrollBarRed.getValue();
        colorGreen = scrollBarGreen.getValue();
        colorBlue = scrollBarBlue.getValue();
        fontSize = scrollBarFontSize.getValue();

    	//基本色のスロット保持
    	slotColorList = new ArrayList<SlotDefaultColor>();
        for (int cnt = 0; cnt < this.inventorySlots.inventorySlots.size(); ++cnt)
        {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(cnt);
            if(slot instanceof SlotDefaultColor)
            {
            	slotColorList.add((SlotDefaultColor)slot);
            }
        }

        //色のイメージを描写する時に使うアイコン
        icon = Block.cloth.getIcon(0,0);
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        fontRenderer.drawStringWithShadow("赤", 30, 51, 0xFF0000);
        fontRenderer.drawStringWithShadow("緑", 30, 68, 0x00FF00);
        fontRenderer.drawStringWithShadow("青", 30, 85, 0x0000FF);
        fontRenderer.drawStringWithShadow("太さ", 30, 102, 0x404040);

        fontRenderer.drawString(convertHexString(colorRed), 155, 51, 0x404040);
        fontRenderer.drawString(convertHexString(colorGreen), 155, 68, 0x404040);
        fontRenderer.drawString(convertHexString(colorBlue), 155, 85, 0x404040);
        fontRenderer.drawString(Integer.toString(fontSize), 155, 102, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        TextureManager renderEngine = mc.getTextureManager();

        //メイン画像
        renderEngine.bindTexture(recource);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        //スクロールバー
		for(GuiScrollBar scrollBar : scrollBarList)
		{
			scrollBar.drawScrollBar();
		}

		//基本色
		renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		for(SlotDefaultColor slot : slotColorList)
		{
			drawTexturedModelRectFromIcon(guiLeft+slot.xDisplayPosition, guiTop+slot.yDisplayPosition, slot.getIcon(), 16, 16);
		}

        //線の描写
        drawTexturedModelRectFromIconColor(guiLeft+8, guiTop+48, fontSize/4D, 16);
        drawTexturedModelRectFromIconColor(guiLeft+8, guiTop+64, fontSize/4D, 16);
        drawTexturedModelRectFromIconColor(guiLeft+8, guiTop+80, fontSize/4D, 16);
        drawTexturedModelRectFromIconColor(guiLeft+8, guiTop+96, fontSize/4D, 16);
    }

    /**
     * 押下時のslot取得
     * @param mouseX
     * @param mouseY
     * @return
     */
    private SlotDefaultColor getSlotColorAtPosition(int mouseX, int mouseY)
    {
		for(SlotDefaultColor slot : slotColorList)
		{
			if(isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY))
			{
				return slot;
			}
		}
        return null;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseClick)
    {
        super.mouseClicked(mouseX, mouseY, mouseClick);

        //基本色のクリック判定
        if (mouseClick == 0 || mouseClick == 1)
        {
        	SlotDefaultColor slot = getSlotColorAtPosition(mouseX, mouseY);
        	if(slot != null)
        	{
        		slot.onSlotClicked();
        	}
        }
    }

    @Override
    public void handleMouseInput()
    {
    	//スクロール
        super.handleMouseInput();

    	if(currentScrollBar == null)
    	{
    		for(GuiScrollBar scrollBar : scrollBarList)
    		{
                if(scrollBar.mouseOver())
                {
                	scrollBar.scrollTo();
                	break;
                }
    		}
    	}
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3)
    {
        boolean flag = Mouse.isButtonDown(0);

        if(flag)
        {
        	if(currentScrollBar == null)
        	{
        		for(GuiScrollBar scrollBar : scrollBarList)
        		{
                    if( scrollBar.mouseOver(mouseX, mouseY) )
                    {
                    	currentScrollBar = scrollBar;
                    	break;
                    }
        		}
        	}
        }
        else
        {
        	currentScrollBar = null;
        }

        if(currentScrollBar != null)
        {
        	currentScrollBar.scrollTo(flag, mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, par3);
    }

    @Override
    public void updateScreen()
    {
    	super.updateScreen();

    	boolean enable = container.existItemStack();

		for(GuiScrollBar scrollBar : scrollBarList)
		{
			scrollBar.setEnabled(enable);
		}

    	colorRed = container.getColorRed();
    	colorGreen = container.getColorGreen();
    	colorBlue = container.getColorBlue();
    	fontSize = container.getFontSize();

    	scrollBarRed.setValue(colorRed);
    	scrollBarGreen.setValue(colorGreen);
    	scrollBarBlue.setValue(colorBlue);
    	scrollBarFontSize.setValue(fontSize);
    }

    /**
     * 色乗算対応
     * @param xPosition
     * @param yPosition
     * @param icon
     * @param offsetX
     * @param offsetY
     */
    public void drawTexturedModelRectFromIconColor(int xPosition, int yPosition, double offsetX, int offsetY)
    {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();

        //色乗算
        tessellator.setColorOpaque(colorRed, colorGreen, colorBlue);

        tessellator.addVertexWithUV((double)(xPosition + 0), (double)(yPosition + offsetY), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)(xPosition + offsetX), (double)(yPosition + offsetY), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)(xPosition + offsetX), (double)(yPosition + 0), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMinV());
        tessellator.addVertexWithUV((double)(xPosition + 0), (double)(yPosition + 0), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMinV());

        tessellator.draw();
    }


    /**
     * 大文字16進数に変換
     * @param value
     * @return
     */
	private String convertHexString(int value)
	{
		String s = Integer.toHexString(value);
		return s.toUpperCase();
	}
}
