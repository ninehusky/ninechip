package memory;

/**
 * The memory manager for the emulator.
 */
public class Memory {
    private static final int KEYBOARD_SIZE = 16;
    private static final int SCREEN_BUFFER_SIZE = 64 * 32;

    private final RAM ram;
    private final IOBuffer keyboard;
    private final IOBuffer screen;

    private final ChipStack stack;

    public Memory() {
        ram = new RAM();
        keyboard = new IOBuffer(KEYBOARD_SIZE);
        screen = new IOBuffer(SCREEN_BUFFER_SIZE);
        stack = new ChipStack();
    }

    public RAM getRam() {
        return ram;
    }

    public IOBuffer getKeyboard() {
        return keyboard;
    }

    public IOBuffer getScreen() {
        return screen;
    }

    public ChipStack getStack() {
        return stack;
    }
}
