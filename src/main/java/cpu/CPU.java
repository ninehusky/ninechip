package cpu;

import cpu.opcodes.Command;
import cpu.opcodes.OpcodeManager;
import memory.Memory;

import java.util.HashMap;
import java.util.Map;

import static cpu.BitUtils.checkWord;

public class CPU {
    private final Map<Integer, Command> commandMap;

    private final Registers registers;
    private final Memory memory;

    public CPU(Memory memory) {
        this.memory = memory;
        this.registers = new Registers();

        Map<Integer, Command> map = new HashMap<>();
        addCommand(map, "CLS", 0x0000, (r,m) -> OpcodeManager.clearDisplay(r, m));
        addCommand(map, "JP", 0x1000, (r,m) -> OpcodeManager.jump(r, m));
        addCommand(map, "LD Vx", 0x6000, (r,m) -> OpcodeManager.loadByteToRegister(r, m));
        addCommand(map, "ADD Vx", 0x7000, (r,m) -> OpcodeManager.addByteToRegister(r, m));
        addCommand(map, "LD I, addr", 0xA000, (r,m) -> OpcodeManager.setIndexRegister(r, m));
        addCommand(map, "DRW", 0xD000, (r, m) -> OpcodeManager.drawDisplay(r, m));
        this.commandMap = map;
    }

    public void execute() {
        // fetch
        int high = memory.getRam().getByte(registers.getProgramCounter());
        int low = memory.getRam().getByte(registers.getProgramCounter() + 1);

        int opcode = ((high << 8) | low);

        System.out.println("program counter: " + Integer.toHexString(registers.getProgramCounter()));
        System.out.println("opcode: " + Integer.toHexString(opcode));
        // execute
        int opcodeRoot = opcode & 0xF000;
        Command currentCommand = commandMap.get(opcodeRoot);
        assert(currentCommand != null);
        currentCommand.operation.execute(registers, memory);
        // increment pc
        registers.setProgramCounter(registers.getProgramCounter() + 2);
    }

    private void addCommand(Map<Integer, Command> map, String label, int opcodeRoot, Command.Operation operation) {
        checkWord(opcodeRoot);
        if (map.containsKey(opcodeRoot)) {
            throw new IllegalArgumentException("Already has command - " + map.get(opcodeRoot));
        }
        map.put(opcodeRoot, new Command(label, operation));
    }

}
