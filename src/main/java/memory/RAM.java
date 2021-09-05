package memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static cpu.BitUtils.checkWord;
import static memory.FontSet.FONT_SET;

public class RAM extends AddressSpace {
    public static final int START_ADDRESS = 0x200;
    public static final int FONT_START_ADDRESS = 0x50;
    public static final int FONT_SPRITE_SIZE = 5;

    private static final int RAM_SIZE = 4096;

    public RAM() {
        super(RAM_SIZE);
        loadFontSet();
    }

    public void loadROM(String fileName) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File ROMFile = new File(classLoader.getResource(fileName).getFile());
            InputStream inputStream = new FileInputStream(ROMFile);

            byte[] gameData = new byte[(int)ROMFile.length()];

            inputStream.read(gameData);

            for (int i = 0; i < gameData.length; i++) {
                memory[START_ADDRESS + i] = (gameData[i] & 0xFF);
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1); // lol
        }
    }

    @Override
    protected void checkAddress(int address) {
        checkWord(address);
        if (address < 0 || address >= memory.length) {
            throw new IllegalArgumentException("Cannot access memory at address " + address);
        }
    }

    private void loadFontSet() {
        for (int i = 0; i < FONT_SET.length; i++) {
            memory[FONT_START_ADDRESS + i] = FONT_SET[i];
        }
    }
}
