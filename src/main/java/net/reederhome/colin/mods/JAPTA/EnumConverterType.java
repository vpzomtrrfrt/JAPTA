package net.reederhome.colin.mods.JAPTA;

import net.minecraft.util.IStringSerializable;

public enum EnumConverterType implements IStringSerializable {
    ABSORB("Absorb"),
    DEPLOY("Deploy");

    private String name;

    public String getName() {
        return name;
    }

    EnumConverterType(String name) {
        this.name = name;
    }

    public EnumConverterType getOpposite() {
        switch(this) {
            case ABSORB:
                return DEPLOY;
            default:
                return ABSORB;
        }
    }
}
