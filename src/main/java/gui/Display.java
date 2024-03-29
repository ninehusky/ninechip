package gui;

import controller.Keyboard;
import memory.IOBuffer;
import memory.Memory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Display extends JFrame {
    public static final int SCREEN_WIDTH = 64;
    public static final int SCREEN_HEIGHT = 32;

    private static final int PIXEL_SIZE = 10;

    private IOBuffer screen;
    private BufferedImage backBuffer;

    public Display(Memory memory) {
        setTitle("ninechip");
        setSize(SCREEN_WIDTH * PIXEL_SIZE, SCREEN_HEIGHT * PIXEL_SIZE + 10);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.screen = memory.getScreen();
        addKeyListener(new Keyboard(memory.getKeyboard()));
        this.backBuffer = new BufferedImage(SCREEN_WIDTH * PIXEL_SIZE, SCREEN_HEIGHT * PIXEL_SIZE,
                BufferedImage.TYPE_INT_RGB);
        setBackground(Color.BLACK); // will this get overridden? who knows?

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        fileMenu.add("Load ROM");
        fileMenu.add("Exit");
        menuBar.add(new JMenu("File"));

        setJMenuBar(menuBar);

        setVisible(true);
    }

    public void render() {
        Graphics frontGraphics = getGraphics();

        Graphics backGraphics = backBuffer.getGraphics();

        for (int i = 0; i < screen.size(); i++) {
            int pixel = screen.getByte(i);
            backGraphics.setColor(pixel == 0 ? Color.BLACK : Color.WHITE);

            backGraphics.fillRect((i % SCREEN_WIDTH) * PIXEL_SIZE, (i / SCREEN_WIDTH) * PIXEL_SIZE,
                    PIXEL_SIZE, PIXEL_SIZE);
        }

        frontGraphics.drawImage(backBuffer, getInsets().left, getInsets().top, this);

    }

    private void printDisplay() {
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

    private class DisplayPanel extends JPanel { // implements KeyListener
        public DisplayPanel() {
        }

        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            graphics.drawOval(0, 0, 30, 30);
        }
    }
}
