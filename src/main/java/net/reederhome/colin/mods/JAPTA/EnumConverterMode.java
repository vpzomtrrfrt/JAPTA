package net.reederhome.colin.mods.JAPTA;

import net.minecraft.util.IStringSerializable;

public enum EnumConverterMode implements IStringSerializable {
    ABSORB("absorb"),
    DEPLOY("deploy");

    private String name;

    public String getName() {
        return name;
    }

    EnumConverterMode(String name) {
        this.name = name;
    }

    public EnumConverterMode getOpposite() {
        switch (this) {
            case ABSORB:
                return DEPLOY;
            default:
                return ABSORB;
        }
    }
}
