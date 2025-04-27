package fr.caranouga.technoverse.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;

public class SandingMachineScreen extends AbstractMachineScreen<SandingMachineMenu> {

    public SandingMachineScreen(SandingMachineMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, "sanding_machine", pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);

        renderProgressArrow(pGuiGraphics, 77, 36, menu.getScaledArrowProgress());

        Tuple<Integer, Integer> xy = getXY();
        int x = xy.getA();
        int y = xy.getB();

        pGuiGraphics.blit(GUI_TEXTURE, x + 78, y + 31, 176, 0, 12, 10);
    }
}
