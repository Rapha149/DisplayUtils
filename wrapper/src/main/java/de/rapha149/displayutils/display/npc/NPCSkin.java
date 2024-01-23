package de.rapha149.displayutils.display.npc;

import java.util.Arrays;
import java.util.List;

/**
 * A class holding information about an NPC's skin.
 */
public class NPCSkin {

    private final String texture;
    private final String signature;
    private final List<NPCSkinPart> enabledParts;

    /**
     * Constructs a new NPCSkin.
     * @param texture The texture of the skin.
     * @param signature The signature of the skin.
     * @param enabledParts The parts of the skin that are enabled. See {@link NPCSkinPart#getDefaultParts()} for more information.
     */
    public NPCSkin(String texture, String signature, List<NPCSkinPart> enabledParts) {
        this.texture = texture;
        this.signature = signature;
        this.enabledParts = enabledParts;
    }

    /**
     * Returns the texture of the NPC's skin.
     * @return The texture of the NPC's skin.
     */
    public String getTexture() {
        return texture;
    }

    /**
     * Returns the signature of the NPC's skin.
     * @return The signature of the NPC's skin.
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Returns the list of enabled parts of the NPC's skin.
     * @return The list of enabled parts of the NPC's skin.
     */
    public List<NPCSkinPart> getEnabledParts() {
        return enabledParts;
    }

    /**
     * An enum for the different parts of an NPC's skin.
     */
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

        /**
         * Returns the bit of the NPCSkinPart. This is used in the metadata packet.
         * @return The bit of the NPCSkinPart.
         */
        public int getBit() {
            return bit;
        }

        /**
         * Returns the default parts of an NPC's skin. These are all parts except the cape.
         * @return The default parts of an NPC's skin.
         */
        public static List<NPCSkinPart> getDefaultParts() {
            return defaultParts;
        }
    }
}
