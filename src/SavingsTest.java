import static org.junit.jupiter.api.Assertions.*;

class SavingsTest {

    @org.junit.jupiter.api.Test
    void getCustNo() {

        Savings c = new Savings("123", "Julie", "12345", "2", "Savings-Deluxe");
        assertEquals("123", c.getCustNo());


    }

    @org.junit.jupiter.api.Test
    void getCustName() {
        Savings c = new Savings("234", "Nikhil", "45667", "2", "Savings-Regular");
        assertEquals("Nikhil", c.getCustName());

    }
}