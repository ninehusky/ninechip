package memory;

import cpu.BitUtils;

import java.util.Stack;

public class ChipStack {
    private static final int MAX_STACK_SIZE = 16;

    private Stack<Integer> stack;

    public ChipStack() {
        this.stack = new Stack<>();
    }

    public void push(int address) {
        BitUtils.checkWord(address);
        if (stack.size() == MAX_STACK_SIZE) {
            throw new IllegalStateException("Cannot push address - stack is full!");
        }
    }

    public int pop() {
        // TODO: perhaps check of what happens when popping from empty stack?
        // unsure how some ROMs handle that
        return stack.pop();
    }
}
