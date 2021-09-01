import static org.junit.jupiter.api.Assertions.*;

class HelloTest {

    @org.junit.jupiter.api.Test
    void return2() {
        assertEquals(new Hello().return2(), 2);
    }
}