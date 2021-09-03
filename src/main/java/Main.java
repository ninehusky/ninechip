import cpu.CPU;
import display.Display;
import memory.Memory;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Memory memory = new Memory();
        memory.getRam().loadROM("ibm.ch8");
        CPU cpu = new CPU(memory);
        Display display = new Display(memory.getScreen());

        Scanner console = new Scanner(System.in); // lol

        while (true) {
            cpu.execute();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(2);
            }
        }
    }
}
