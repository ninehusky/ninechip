package cpu;

import cpu.opcodes.Command;
import cpu.opcodes.OpcodeManager;
import memory.Memory;

import java.util.HashMap;
import java.util.Map;

import static cpu.BitUtils.checkWord;

public class CPU {
    private static final int ZERO_COMMAND_MASK = 0x00FF;
    private static final int EIGHT_COMMAND_MASK = 0xF00F;
    private static final int E_COMMAND_MASK = 0xF0FF;
    private static final int F_COMMAND_MASK = 0xF0FF;

    private final Map<Integer, Command> genericCommandMap;
    private final Map<Integer, Command> zeroCommandMap;
    private final Map<Integer, Command> eightCommandMap;
    private final Map<Integer, Command> eCommandMap;
    private final Map<Integer, Command> fCommandMap;

    private final Registers registers;
    private final Memory memory;

    public CPU(Memory memory) {
        this.memory = memory;
        this.registers = new Registers();

        Map<Integer, Command> genericCommandMap = new HashMap<>();

        Map<Integer, Command> zeroCommandMap = new HashMap<>();
        Map<Integer, Command> eightCommandMap = new HashMap<>();
        Map<Integer, Command> eCommandMap = new HashMap<>();
        Map<Integer, Command> fCommandMap = new HashMap<>();

        addCommand(zeroCommandMap, "CLS", 0x00E0, (r,m) -> OpcodeManager.clearDisplay(r, m));
        addCommand(zeroCommandMap, "RET", 0x00EE, (r,m) -> OpcodeManager.returnFromSubroutine(r, m));

        addCommand(genericCommandMap, "JP", 0x1000, (r,m) -> OpcodeManager.jump(r, m));
        addCommand(genericCommandMap, "CALL", 0x2000, (r,m) -> OpcodeManager.callSubroutine(r, m));
        addCommand(genericCommandMap, "SE byte", 0x3000, (r,m) -> OpcodeManager.skipIfEqualByte(r, m));
        addCommand(genericCommandMap, "SNE byte", 0x4000, (r,m) -> OpcodeManager.skipIfUnequalByte(r, m));
        addCommand(genericCommandMap, "SE registers", 0x5000, (r,m) -> OpcodeManager.skipIfRegistersEqual(r, m));
        addCommand(genericCommandMap, "LD Vx", 0x6000, (r,m) -> OpcodeManager.loadByteToRegister(r, m));
        addCommand(genericCommandMap, "ADD Vx", 0x7000, (r,m) -> OpcodeManager.addByteToRegister(r, m));

        addCommand(eightCommandMap, "LD Vx, Vy", 0x8000, (r,m) -> OpcodeManager.loadFromYToX(r, m));
        addCommand(eightCommandMap, "OR Vx, Vy", 0x8001, (r,m) -> OpcodeManager.orVxVy(r, m));
        addCommand(eightCommandMap, "AND Vx, Vy", 0x8002, (r,m) -> OpcodeManager.andVxVy(r, m));
        addCommand(eightCommandMap, "XOR Vx, Vy", 0x8003, (r,m) -> OpcodeManager.xorVxVy(r, m));
        addCommand(eightCommandMap, "ADD Vx, Vy", 0x8004, (r,m) -> OpcodeManager.addVxVy(r, m));
        addCommand(eightCommandMap, "SUB Vx, Vy", 0x8005, (r,m) -> OpcodeManager.subVxVy(r, m));
        addCommand(eightCommandMap, "SHR Vx, Vy", 0x8006, (r,m) -> OpcodeManager.shiftRight(r, m));
        addCommand(eightCommandMap, "SUBN Vx, Vy", 0x8007, (r,m) -> OpcodeManager.subVyVx(r, m));
        addCommand(eightCommandMap, "SHL Vx, Vy", 0x800E, (r,m) -> OpcodeManager.shiftLeft(r, m));

        addCommand(genericCommandMap, "SNE Vx, Vy", 0x9000, (r,m) -> OpcodeManager.skipRegistersUnequal(r, m));
        addCommand(genericCommandMap, "LD I, addr", 0xA000, (r,m) -> OpcodeManager.setIndexRegister(r, m));
        addCommand(genericCommandMap, "JP V0, addr", 0xB000, (r,m) -> OpcodeManager.jumpWithSum(r, m));
        addCommand(genericCommandMap, "RND Vx, byte", 0xC000, (r,m) -> OpcodeManager.generateRandomNumber(r, m));
        addCommand(genericCommandMap, "DRW", 0xD000, (r, m) -> OpcodeManager.drawDisplay(r, m));

        addCommand(eCommandMap, "SKP Vx", 0xE09E, (r, m) -> OpcodeManager.skipIfPressed(r, m));
        addCommand(eCommandMap, "SKNP Vx", 0xE0A1, (r, m) -> OpcodeManager.skipIfNotPressed(r, m));

        addCommand(fCommandMap, "LD Vx, DT", 0xF007, (r, m) -> OpcodeManager.setRegisterToDelayTimer(r, m));
        addCommand(fCommandMap, "LD Vx, K", 0xF00A, (r, m) -> OpcodeManager.waitForKeyPress(r, m));
        addCommand(fCommandMap, "LD DT, Vx", 0xF015, (r, m) -> OpcodeManager.setDelayTimerToRegister(r, m));
        addCommand(fCommandMap, "LD ST, Vx", 0xF018, (r, m) -> OpcodeManager.setSoundTimerToRegister(r, m));
        addCommand(fCommandMap, "ADD I, Vx", 0xF01E, (r, m) -> OpcodeManager.addToIndexRegister(r, m));
        addCommand(fCommandMap, "LD F, Vx", 0xF029, (r, m) -> OpcodeManager.setSpriteLocation(r, m));
        addCommand(fCommandMap, "LD B, Vx", 0xF033, (r, m) -> OpcodeManager.storeDecimalValueOfRegister(r, m));
        addCommand(fCommandMap, "LD [I], Vx", 0xF055, (r, m) -> OpcodeManager.storeRegistersInMemory(r, m));
        addCommand(fCommandMap, "LD Vx, [I]", 0xF065, (r, m) -> OpcodeManager.storeMemoryInRegisters(r, m));

        this.genericCommandMap = genericCommandMap;

        this.zeroCommandMap = zeroCommandMap;
        this.eightCommandMap = eightCommandMap;
        this.eCommandMap = eCommandMap;
        this.fCommandMap = fCommandMap;
    }

    public void execute() {
        // fetch
        int high = memory.getRam().getByte(registers.getProgramCounter());
        int low = memory.getRam().getByte(registers.getProgramCounter() + 1);

        int opcode = ((high << 8) | low);

        System.out.println("program counter: " + Integer.toHexString(registers.getProgramCounter()));
        System.out.println("opcode: " + Integer.toHexString(opcode));

        // decode
        int opcodeRoot = opcode & 0xF000;

        // TODO: consider putting this in a method. maybe commandMap should have other maps as values?
        Command currentCommand;
        if (opcodeRoot == 0x0000) {
            opcodeRoot = opcode & ZERO_COMMAND_MASK;
            currentCommand = zeroCommandMap.get(opcodeRoot);
        } else if (opcodeRoot == 0x8000) {
            opcodeRoot = opcode & EIGHT_COMMAND_MASK;
            currentCommand = eightCommandMap.get(opcodeRoot);
        } else if (opcodeRoot == 0xE000) {
            opcodeRoot = opcode & E_COMMAND_MASK;
            currentCommand = eCommandMap.get(opcodeRoot);
        } else if (opcodeRoot == 0xF000) {
            opcodeRoot = opcode & F_COMMAND_MASK;
            currentCommand = fCommandMap.get(opcodeRoot);
        } else {
            currentCommand = genericCommandMap.get(opcodeRoot);
        }

        assert(currentCommand != null);

        // execute
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
