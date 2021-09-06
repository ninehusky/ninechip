import cpu.CPU;
import gui.Display;
import memory.Memory;

import java.io.File;

public class Main {
    public static double TIMER_REFRESH_FREQUENCY = 17;
    public static void main(String[] args) {
        Memory memory = new Memory();
        if (args.length != 1) {
            System.err.println("Usage: java -jar ninechip.jar <filepath>");
            System.exit(-1);
        }

        File ROMFile = new File(args[0]);
        if (!ROMFile.exists()) {
            System.err.println("The given file does not exist!");
            System.exit(-1);
        }

        memory.getRam().loadROM(ROMFile);
        CPU cpu = new CPU(memory);
        Display display = new Display(memory);

        long oldTime = System.currentTimeMillis();
        long currentTime;

        while (true) {
            currentTime = System.currentTimeMillis();
            for (int i = 0; i < 10; i++) {
                cpu.execute();
            }
            display.render();
            // TODO: CONVERSION
            if (currentTime - oldTime > TIMER_REFRESH_FREQUENCY) {
                oldTime = currentTime;
                cpu.registers.decrementTimers();
            }
            try {
                Thread.sleep((long)TIMER_REFRESH_FREQUENCY);
            } catch(Exception e) {}
        }
    }
}
