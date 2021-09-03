package memory;

import static cpu.BitUtils.checkByte;

public class AddressSpace {
    protected int[] memory;

    public AddressSpace(int size) {
        this.memory = new int[size];
    }

    public int getByte(int address) {
        checkAddress(address);
        return memory[address];
    }

    public void setByte(int address, int value) {
        checkAddress(address);
        checkByte(value);
        memory[address] = value;
    }

    public int size() {
        return memory.length;
    }

    protected void checkAddress(int address) {
        if (address < 0 || address > memory.length) {
            throw new IllegalArgumentException("Cannot read memory at " + address);
        }
    }
}
