package org.valkyrienskies.addon.control.nodenetwork;

import net.minecraft.item.Item;
import org.valkyrienskies.addon.control.ValkyrienSkiesControl;

public enum EnumWireType {
    RELAY("relay_wire"),
    VANISHING("vanishing_wire");

    private final String name;

    EnumWireType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public Item toItem() {
        if (this == RELAY) return ValkyrienSkiesControl.INSTANCE.relayWire;
        if (this == VANISHING) return ValkyrienSkiesControl.INSTANCE.vanishingWire;
        return null;
    }
}
