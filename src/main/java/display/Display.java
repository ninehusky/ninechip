package display;

import memory.IOBuffer;

public class Display {
    public static final int SCREEN_WIDTH = 64;
    public static final int SCREEN_HEIGHT = 32;

    private IOBuffer screen;

    public Display(IOBuffer screen) {
        this.screen = screen;
    }

    public void printDisplay() {
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
}
