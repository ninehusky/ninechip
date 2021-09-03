package cpu;

import memory.RAM;

import static cpu.BitUtils.checkByte;
import static cpu.BitUtils.checkWord;

/**
 * Registers are used by the CPU to temporarily store values. The CHIP-8 System has 16 8-bit registers; each
 * register will contain a value of at most 0x0FF.
 *
 * In addition to these registers, a CHIP-8 also has two 8-bit "timer" registers - one is the delay timer,
 * and the other is a sound timer.
 *
 * Finally, there are two 16-bit pseudo-registers inaccessible to programs written in CHIP-8; the stack pointer
 * and the program counter.
 */
public class Registers {
    private static final int NUM_REGISTERS = 16;
    private int[] registers;

    private int indexRegister;
    private int delayTimer;
    private int soundTimer;

    private int stackPointer;
    private int programCounter;


    /**
     * Constructs a new Registers.
     */
    public Registers() {
        registers = new int[NUM_REGISTERS];
        programCounter = RAM.START_ADDRESS;

    }

    /**
     * Returns the value at the given index, e.g., getRegister(9) returns the value at V[9].
     * @param index the index of the register to get
     * @return V[index]
     */
    public int getRegister(int index) {
        checkIndex(index);
        return registers[index];
    }

    /**
     * Sets the register at the given index to the given value, i.e., performs V[index] = value.
     * @param index the index of the register to set
     * @param value the value to set the register to
     */
    public void setRegister(int index, int value) {
        checkIndex(index);
        registers[index] = value;
    }

    /**
     * Throws and IllegalArgumentException if the index is referring to an unreachable index
     * @param index the index of the register
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= NUM_REGISTERS) {
            throw new IllegalArgumentException("no");
        }
    }

    public int getIndexRegister() {
        return this.indexRegister;
    }

    public void setIndexRegister(int value) {
        checkWord(value);
        this.indexRegister = value;
    }

    // im not documenting the rest like this lol
    public int getDelayTimer() {
        return delayTimer;
    }

    public void setDelayTimer(int delayTimer) {
        checkByte(delayTimer);
        this.delayTimer = delayTimer;
    }

    public int getSoundTimer() {
        return soundTimer;
    }

    public void setSoundTimer(int soundTimer) {
        checkByte(soundTimer);
        this.soundTimer = soundTimer;
    }

    public int getStackPointer() {
        return stackPointer;
    }

    public void setStackPointer(int stackPointer) {
        checkWord(stackPointer);
        this.stackPointer = stackPointer;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int programCounter) {
        checkWord(programCounter);
        this.programCounter = programCounter;
    }
}
