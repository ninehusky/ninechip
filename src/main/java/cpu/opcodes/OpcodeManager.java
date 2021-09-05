package cpu.opcodes;

import cpu.BitUtils;
import cpu.Registers;
import memory.IOBuffer;
import memory.Memory;

import static display.Display.SCREEN_HEIGHT;
import static display.Display.SCREEN_WIDTH;

public class OpcodeManager {
    // 00EE
    public static void clearDisplay(Registers r, Memory mem) {
        IOBuffer vram = mem.getScreen();
        for (int i = 0; i < vram.size(); i++) {
            vram.setByte(i, 0);
        }
    }

    // 1NNN
    public static void jump(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int address = BitUtils.getAddress(opcode);
        // after the program does this.pc += 2, the pc have the value of address.
        // a little gross, i know
        r.setProgramCounter(address - 2);
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

    // ANNN
    public static void setIndexRegister(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int address = BitUtils.getAddress(opcode);
        r.setIndexRegister(address);
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
            for (int col = 0; col < 8; col++) {
                int spritePixel = spriteByte & (0x80 >>> col);
                int address = (yCoord + row) * SCREEN_WIDTH + (xCoord + col);
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

    private static int getOpcode(Registers r, Memory mem) {
        int high = mem.getRam().getByte(r.getProgramCounter());
        int low = mem.getRam().getByte(r.getProgramCounter() + 1);

        int opcode = ((high << 8) | low) & 0xFFFF;
        return opcode;
    }
}
