package fr.caranouga.technoverse.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SandingMachineScreen extends AbstractMachineScreen<SandingMachineMenu> {

    public SandingMachineScreen(SandingMachineMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
}
