package Database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBconnectionTest {

    DBconnection db = new DBconnection();

    @Test
    void accountCreate() {
        assertEquals(true, db.accountCreate("test", "test"));
    }
}