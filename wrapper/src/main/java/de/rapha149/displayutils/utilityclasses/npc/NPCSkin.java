package de.rapha149.displayutils.utilityclasses.npc;

import java.util.Arrays;
import java.util.List;

public class NPCSkin {

    private final String texture;
    private final String signature;
    private final List<NPCSkinPart> enabledParts;

    public NPCSkin(String texture, String signature, List<NPCSkinPart> enabledParts) {
        this.texture = texture;
        this.signature = signature;
        this.enabledParts = enabledParts;
    }

    public String getTexture() {
        return texture;
    }

    public String getSignature() {
        return signature;
    }

    public List<NPCSkinPart> getEnabledParts() {
        return enabledParts;
    }

    public enum NPCSkinPart {

        CAPE(0x01),
        JACKET(0x02),
        LEFT_SLEEVE(0x04),
        RIGHT_SLEEVE(0x08),
        LEFT_PANTS_LEG(0x10),
        RIGHT_PANTS_LEG(0x20),
        HAT(0x40);

        private static final List<NPCSkinPart> defaultParts = Arrays.asList(JACKET, LEFT_SLEEVE, RIGHT_SLEEVE, LEFT_PANTS_LEG, RIGHT_PANTS_LEG, HAT);

        private final int bit;

        NPCSkinPart(int bit) {
            this.bit = bit;
        }

        public int getBit() {
            return bit;
        }

        public static List<NPCSkinPart> getDefaultParts() {
            return defaultParts;
        }
    }
}
