package fr.caranouga.technoverse.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.caranouga.technoverse.Technoverse;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractMachineScreen<M extends AbstractMachineMenu<?, ?, ?>> extends AbstractContainerScreen<M> {
    protected final ResourceLocation GUI_TEXTURE;
    private final ResourceLocation ARROW_TEXTURE = ResourceLocation.fromNamespaceAndPath(Technoverse.MODID, "textures/gui/arrow_progress.png");
    private final int textureWidth;
    private final int textureHeight;

    protected static int ARROW_WIDTH = 22;
    protected static int ARROW_HEIGHT = 16;

    public AbstractMachineScreen(M pMenu, Inventory pPlayerInventory, String id, Component component) {
        this(pMenu, pPlayerInventory, id, component, 256, 256);
    }

    public AbstractMachineScreen(M pMenu, Inventory pPlayerInventory, String id, Component component, int textureWidth, int textureHeight) {
        super(pMenu, pPlayerInventory, component);

        this.GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(Technoverse.MODID, "textures/gui/" + id + "/" + id + "_gui.png");
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        Tuple<Integer, Integer> xy = getXY();
        int x = xy.getA();
        int y = xy.getB();

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, textureWidth, textureHeight);
    }

    protected void renderProgressArrow(GuiGraphics guiGraphics, int toDrawX, int toDrawY, int progress){
        Tuple<Integer, Integer> xy = getXY();
        int x = xy.getA();
        int y = xy.getB();

        renderProgressArrow(guiGraphics, x, y, toDrawX, toDrawY, progress);
    }

    protected void renderProgressArrow(GuiGraphics guiGraphics, int toDrawX, int toDrawY, int x, int y, int progress){
        guiGraphics.blit(ARROW_TEXTURE, x + toDrawX, y + toDrawY, 0, 0, progress, ARROW_HEIGHT, ARROW_WIDTH, ARROW_HEIGHT);
    }

    protected Tuple<Integer, Integer> getXY(){
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        return new Tuple<>(x, y);
    }
}
