package cpu;

/**
 * A series of utility functions to declunk bit operations.
 */
public final class BitUtils {

    // 0x0NNN --> returns the NNN part
    public static int getAddress(int opcode) {
        checkWord(opcode);
        return opcode & 0x0FFF;
    }

    // 0x0X00 --> returns the X part
    public static int getX(int opcode) {
        checkWord(opcode);
        return ((opcode >>> 8) & 0xF);
    }

    // 0x00Y0 --> returns the Y part
    public static int getY(int opcode) {
        checkWord(opcode);
        return ((opcode >>> 4) & 0xF);
    }

    // 0x00kk --> returns the kk part
    public static int getKK(int opcode) {
        checkWord(opcode);
        return opcode & 0xFF;
    }

    /**
     * @throws IllegalArgumentException if the value exceeds the maximum value of a byte (0xFF)
     * @param value - the value to check
     */
    public static void checkByte(int value) {
        if (value > 0xFF) {
            throw new IllegalArgumentException("Invalid byte value: " + value);
        }
    }

    /**
     * @throws IllegalArgumentException if the value exceeds the maximum value of a word (0xFFFF)
     * @param value - the value to check
     */
    public static void checkWord(int value) {
        if (value > 0xFFFF) {
            throw new IllegalArgumentException("Invalid word value: " + value);
        }
    }
}
