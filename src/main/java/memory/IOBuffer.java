package memory;

/**
 * IOBuffer represents the buffers used by the CHIP-8 to store data relating to input/output.
 *
 * This is done in two places - IOBuffer serves as "VRAM" to be drawn to the display, and it also keeps track of
 * keys that are pressed.
 *
 */
public class IOBuffer extends AddressSpace {
    public IOBuffer(int size) {
        super(size);
    }

    @Override
    public void setByte(int address, int value) {
        if (value != 1 && value != 0) {
            System.out.println("Warning: IOMemory values must only be either 1 or 0!");
        }
        super.setByte(address, value);
    }
}
