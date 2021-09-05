import cpu.CPU;
import display.Display;
import memory.Memory;

public class Main {
    public static void main(String[] args) {
        Memory memory = new Memory();
        memory.getRam().loadROM("ibm.ch8"); // TODO: read from args
        CPU cpu = new CPU(memory);
        Display display = new Display(memory.getScreen());

        int opcodes = 0;

        while (true) {
            cpu.execute();
            display.render();
            if (opcodes++ >= 10) {
                try {
                    Thread.sleep(1000);
                } catch(Exception e) { };
            }
        }
    }
}
