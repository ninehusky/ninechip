import cpu.CPU;
import display.Display;
import memory.Memory;

public class Main {
    public static double TIMER_REFRESH_FREQUENCY = 17;
    public static void main(String[] args) {
        Memory memory = new Memory();
        memory.getRam().loadROM("tetris.ch8"); // TODO: read from args
        CPU cpu = new CPU(memory);
        Display display = new Display(memory);

        int opcodes = 0;

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
