package controller;

import memory.IOBuffer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    // TODO: we might need to distinguish between IOBuffers
    private IOBuffer keyboard;
    public Keyboard(IOBuffer keyboard) {
        this.keyboard = keyboard;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_1) {
            keyboard.setByte(0x1, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            keyboard.setByte(0x2, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_3) {
            keyboard.setByte(0x3, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_4) {
            keyboard.setByte(0xC, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            keyboard.setByte(0x4, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            keyboard.setByte(0x5, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            keyboard.setByte(0x6, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            keyboard.setByte(0xD, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            keyboard.setByte(0x7, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            keyboard.setByte(0x8, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            keyboard.setByte(0x9, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_F) {
            keyboard.setByte(0xE, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_Z) {
            keyboard.setByte(0xA, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_X) {
            keyboard.setByte(0x0, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            keyboard.setByte(0xB, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_V) {
            keyboard.setByte(0xF, 1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_1) {
            keyboard.setByte(0x1, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            keyboard.setByte(0x2, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_3) {
            keyboard.setByte(0x3, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_4) {
            keyboard.setByte(0xC, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            keyboard.setByte(0x4, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            keyboard.setByte(0x5, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            keyboard.setByte(0x6, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            keyboard.setByte(0xD, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            keyboard.setByte(0x7, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            keyboard.setByte(0x8, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            keyboard.setByte(0x9, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_F) {
            keyboard.setByte(0xE, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_Z) {
            keyboard.setByte(0xA, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_X) {
            keyboard.setByte(0x0, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            keyboard.setByte(0xB, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_V) {
            keyboard.setByte(0xF, 0);
        }

    }
}
