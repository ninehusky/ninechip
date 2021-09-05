package cpu.opcodes;

import cpu.BitUtils;
import cpu.Registers;
import memory.IOBuffer;
import memory.Memory;
import memory.RAM;

import java.util.Random;

import static display.Display.SCREEN_HEIGHT;
import static display.Display.SCREEN_WIDTH;

public class OpcodeManager {
    // 00E0
    public static void clearDisplay(Registers r, Memory mem) {
        IOBuffer vram = mem.getScreen();
        for (int i = 0; i < vram.size(); i++) {
            vram.setByte(i, 0);
        }
    }

    // 00EE
    public static void returnFromSubroutine(Registers r, Memory mem) {
        r.setProgramCounter(mem.getStack().pop() - 2);
        r.setStackPointer(r.getStackPointer() - 1);
    }

    // 1NNN
    public static void jump(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int address = BitUtils.getAddress(opcode);
        // after the program does this.pc += 2, the pc have the value of address.
        // a little gross, i know
        r.setProgramCounter(address - 2);
    }

    // 2NNN
    public static void callSubroutine(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        r.setStackPointer(r.getStackPointer() + 1);
        mem.getStack().push(r.getProgramCounter() + 2);
        r.setProgramCounter(BitUtils.getAddress(opcode) - 2);
    }

    private static boolean checkRegisterByteEquality(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int kk = BitUtils.getKK(opcode);
        return r.getRegister(x) == kk;
    }

    // 3XKK
    public static void skipIfEqualByte(Registers r, Memory mem) {
        if (checkRegisterByteEquality(r, mem)) {
            r.setProgramCounter(r.getProgramCounter() + 2);
        }
    }

    // 4XKK
    public static void skipIfUnequalByte(Registers r, Memory mem) {
        if (!checkRegisterByteEquality(r, mem)) {
            r.setProgramCounter(r.getProgramCounter() + 2);
        }
    }

    // 5XY0
    public static void skipIfRegistersEqual(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int y = BitUtils.getY(opcode);
        if (r.getRegister(x) == r.getRegister(y)) {
            r.setProgramCounter(r.getProgramCounter() + 2);
        }
    }

    // 6XNN
    public static void loadByteToRegister(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int index = BitUtils.getX(opcode);
        int value = BitUtils.getKK(opcode);
        r.setRegister(index, value);
    }

    // 7XNN
    public static void addByteToRegister(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int index = BitUtils.getX(opcode);
        int value = (BitUtils.getKK(opcode) + r.getRegister(index)) & 0xFF;
        r.setRegister(index, value);
    }

    // 8XY0
    public static void loadFromYToX(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int y = BitUtils.getY(opcode);
        r.setRegister(x, r.getRegister(y));
    }

    // 8XY1
    public static void orVxVy(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int y = BitUtils.getY(opcode);
        r.setRegister(x, r.getRegister(x) | r.getRegister(y));
    }

    // 8XY2
    public static void andVxVy(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int y = BitUtils.getY(opcode);
        r.setRegister(x, r.getRegister(x) & r.getRegister(y));
    }

    // 8XY3
    public static void xorVxVy(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int y = BitUtils.getY(opcode);
        r.setRegister(x, r.getRegister(x) ^ r.getRegister(y));
    }

    // 8XY4
    public static void addVxVy(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int y = BitUtils.getY(opcode);
        int result = r.getRegister(x) + r.getRegister(y);
        r.setRegister(0xF, result > 0xFF ? 1 : 0);
        r.setRegister(x, result & 0xFF);
    }

    // 8XY5
    public static void subVxVy(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int y = BitUtils.getY(opcode);
        int result = r.getRegister(x) - r.getRegister(y);
        r.setRegister(0xF, 0);
        if (r.getRegister(x) >= r.getRegister(y)) {
            r.setRegister(0xF, 1);
        }
        r.setRegister(x, result & 0xFF);
    }

    // 8XY6
    public static void shiftRight(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int xValue = r.getRegister(x);
        r.setRegister(0xF, (xValue & 0x1));
        r.setRegister(x, (xValue & 0xFF) >> 1);
    }

    // 8XY7
    public static void subVyVx(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int y = BitUtils.getY(opcode);
        int result = r.getRegister(y) - r.getRegister(x);
        r.setRegister(0xF, 0);
        if (r.getRegister(y) >= r.getRegister(x)) {
            r.setRegister(0xF, 1);
        }
        r.setRegister(x, result & 0xFF);
    }

    // 8XYE
    public static void shiftLeft(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int xValue = r.getRegister(x);
        r.setRegister(0xF, (xValue & 0x80) == 0 ? 0 : 1);
        r.setRegister(x, (xValue << 1) & 0xFF);
    }

    // 9XY0
    public static void skipRegistersUnequal(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int y = BitUtils.getY(opcode);
        if (r.getRegister(x) != r.getRegister(y)) {
            r.setProgramCounter(r.getProgramCounter() + 2);
        }
    }

    // ANNN
    public static void setIndexRegister(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int address = BitUtils.getAddress(opcode);
        r.setIndexRegister(address);
    }

    // BNNN
    public static void jumpWithSum(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int addressSum = (BitUtils.getAddress(opcode) + r.getRegister(0))
                & 0xFFFF;
        r.setProgramCounter(addressSum - 2);
    }

    // CNNN
    public static void generateRandomNumber(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int kk = BitUtils.getKK(opcode);
        int number = new Random().nextInt(256) & kk;
        r.setRegister(x, number);
    }

    // DXNY
    public static void drawDisplay(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int xCoord = r.getRegister(BitUtils.getX(opcode)) % SCREEN_WIDTH;
        int yCoord = r.getRegister(BitUtils.getY(opcode)) % SCREEN_HEIGHT;
        int n = opcode & 0xF;

        IOBuffer screen = mem.getScreen();

        r.setRegister(0xF, 0);

        for (int row = 0; row < n; row++) {
            int spriteByte = mem.getRam().getByte(r.getIndexRegister() + row);
            if (yCoord + row >= SCREEN_HEIGHT) {
                break;
            }
            for (int col = 0; col < 8; col++) {
                int spritePixel = spriteByte & (0x80 >>> col);
                if (xCoord + col >= SCREEN_WIDTH) {
                    break;
                }
                int address = ((yCoord + row) * SCREEN_WIDTH + (xCoord + col));
                int screenPixel = screen.getByte(address);

                if (spritePixel != 0) {
                    if (screenPixel != 0) {
                        r.setRegister(0xF, 1);
                    }
                    screen.setByte(address, screenPixel ^ 1);
                }
            }
        }
    }

    // Ex9E
    public static void skipIfPressed(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        if (mem.getKeyboard().getByte(x) != 0) {
            r.setProgramCounter(r.getProgramCounter() + 2);
        }
    }

    // ExA1
    public static void skipIfNotPressed(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        if (mem.getKeyboard().getByte(x) == 0) {
            r.setProgramCounter(r.getProgramCounter() + 2);
        }
    }

    // Fx07
    public static void setRegisterToDelayTimer(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        r.setRegister(x, r.getDelayTimer());
    }

    // Fx0A
    public static void waitForKeyPress(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int address = -1;
        for (int i = 0; i < mem.getKeyboard().size(); i++) {
            if (mem.getKeyboard().getByte(i) != 0) {
                address = i;
                r.setRegister(x, address);
                break;
            }
        }
        if (address == -1) {
            r.setProgramCounter(r.getProgramCounter() - 2);
        }
    }

    // Fx15
    public static void setDelayTimerToRegister(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        r.setDelayTimer(r.getRegister(x));
    }

    // Fx18
    public static void setSoundTimerToRegister(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        r.setSoundTimer(r.getRegister(x));
    }

    // Fx1E
    public static void addToIndexRegister(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        r.setIndexRegister((r.getRegister(x) + r.getIndexRegister()) & 0xFFFF);
    }

    // Fx29
    public static void setSpriteLocation(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        r.setIndexRegister(RAM.FONT_START_ADDRESS + r.getRegister(x) * RAM.FONT_SPRITE_SIZE);
    }

    // Fx33
    public static void storeDecimalValueOfRegister(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        int xVal = r.getRegister(x);
        int hundreds = (xVal / 100) % 10;
        int tens = (xVal / 10) % 10;
        int ones = xVal % 10;
        mem.getRam().setByte(r.getIndexRegister(), hundreds);
        mem.getRam().setByte(r.getIndexRegister() + 1, tens);
        mem.getRam().setByte(r.getIndexRegister() + 2, ones);
    }

    // Fx55
    public static void storeRegistersInMemory(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        for (int i = 0; i <= x; i++) {
            mem.getRam().setByte(r.getIndexRegister() + i, r.getRegister(i));
        }
    }

    // Fx65
    public static void storeMemoryInRegisters(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int x = BitUtils.getX(opcode);
        for (int i = 0; i <= x; i++) {
            r.setRegister(i, mem.getRam().getByte(r.getIndexRegister() + i));
        }
    }


    private static int getOpcode(Registers r, Memory mem) {
        int high = mem.getRam().getByte(r.getProgramCounter());
        int low = mem.getRam().getByte(r.getProgramCounter() + 1);

        int opcode = ((high << 8) | low) & 0xFFFF;
        return opcode;
    }
}
