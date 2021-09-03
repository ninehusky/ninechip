package cpu.opcodes;

import cpu.Registers;
import memory.Memory;

public class Command {
    private String label;
    public final Operation operation;

    public Command(String label, Operation operation) {
        this.label = label;
        this.operation = operation;
    }

    public interface Operation {
        void execute(Registers r, Memory mem);
    }
}
