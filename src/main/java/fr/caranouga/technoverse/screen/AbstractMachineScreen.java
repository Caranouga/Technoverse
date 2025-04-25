package fr.caranouga.technoverse.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.caranouga.technoverse.Technoverse;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractMachineScreen<M extends AbstractMachineMenu<?, ?, ?>> extends AbstractContainerScreen<M> {
    private final ResourceLocation GUI_TEXTURE;
    private final int textureWidth;
    private final int textureHeight;

    public AbstractMachineScreen(M pMenu, Inventory pPlayerInventory, Component component) {
        this(pMenu, pPlayerInventory, component, 256, 256);
    }

    public AbstractMachineScreen(M pMenu, Inventory pPlayerInventory, Component component, int textureWidth, int textureHeight) {
        // We've put ".." because the format is "container.MODID.xxxxx"
        this(pMenu, pPlayerInventory, component.getString().substring("container..".length() + Technoverse.MODID.length()), component, 256, 256);
    }

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

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, textureWidth, textureHeight);
    }
}
