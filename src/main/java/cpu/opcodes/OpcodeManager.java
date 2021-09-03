package cpu.opcodes;

import cpu.BitUtils;
import cpu.Registers;
import memory.IOBuffer;
import memory.Memory;

import static display.Display.SCREEN_HEIGHT;
import static display.Display.SCREEN_WIDTH;

public class OpcodeManager {
    public static void clearDisplay(Registers r, Memory mem) {
        IOBuffer vram = mem.getScreen();
        for (int i = 0; i < vram.size(); i++) {
            vram.setByte(i, 0);
        }
    }

    public static void jump(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int address = BitUtils.getAddress(opcode);
        r.setProgramCounter(address - 2);
    }

    public static void loadByteToRegister(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int index = BitUtils.getX(opcode);
        int value = BitUtils.getKK(opcode);
        r.setRegister(index, value);
    }

    public static void addByteToRegister(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int index = BitUtils.getX(opcode);
        int value = (BitUtils.getKK(opcode) + r.getRegister(index)) & 0xFF;
        r.setRegister(index, value);
    }

    public static void setIndexRegister(Registers r, Memory mem) {
        int opcode = getOpcode(r, mem);
        int address = BitUtils.getAddress(opcode);
        r.setIndexRegister(address);
    }

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
                int screenPixel = screen.getByte((yCoord + row) * SCREEN_WIDTH + (xCoord + col));

                if (spritePixel != 0) {
                    if (screenPixel != 0) {
                        r.setRegister(0xF, 1);
                    }
                    screen.setByte((yCoord + row) * SCREEN_WIDTH + (xCoord + col), screenPixel ^ 1);
                }
            }
        }


        for (int i = 0; i < SCREEN_HEIGHT; i++) {
            for (int j = 0; j < SCREEN_WIDTH; j++) {
                int screenAddress = j + i * SCREEN_WIDTH;
                if (screen.getByte(screenAddress) == 1) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

    }

    private static int getOpcode(Registers r, Memory mem) {
        int high = mem.getRam().getByte(r.getProgramCounter());
        int low = mem.getRam().getByte(r.getProgramCounter() + 1);

        int opcode = ((high << 8) | low) & 0xFFFF;
        return opcode;
    }
}
