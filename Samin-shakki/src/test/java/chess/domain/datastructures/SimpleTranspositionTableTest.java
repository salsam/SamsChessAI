package chess.domain.datastructures;

import chess.domain.board.Player;
import java.util.Iterator;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class SimpleTranspositionTableTest {

    private SimpleTranspositionTable table;

    public SimpleTranspositionTableTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        table = new SimpleTranspositionTable();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void newTableIsEmpty() {
        assertTrue(table.getTable().isEmpty());
    }

    @Test
    public void tableDoesNotContainKeysThatHaveNotBeenInserted() {
        assertFalse(table.containsKey(new TranspositionKey(Player.WHITE, 0)));
    }

    @Test
    public void putAddsKeyValuePair() {
        TranspositionKey key = new TranspositionKey(Player.WHITE, 0);
        TranspositionEntry value = new TranspositionEntry(0, 0, Type.ALPHA);
        table.put(key, value);
        assertTrue(table.getTable().containsKey(key));
        assertTrue(table.getTable().containsValue(value));
    }

    @Test
    public void containsKeyReturnsTrueIfKeyInTable() {
        TranspositionKey key = new TranspositionKey(Player.WHITE, 0);
        TranspositionEntry value = new TranspositionEntry(0, 0, Type.ALPHA);
        table.put(key, value);
        assertTrue(table.containsKey(key));
    }

    @Test
    public void getReturnsEntryAssociatedWithKey() {
        TranspositionKey key = new TranspositionKey(Player.WHITE, 0);
        TranspositionEntry value = new TranspositionEntry(0, 0, Type.ALPHA);
        table.put(key, value);
        assertEquals(table.get(key), value);
    }

    @Test
    public void transPositionTableCannotPassDefaultSizeLimit() {
        TranspositionKey key;
        TranspositionEntry value = new TranspositionEntry(0, 0, Type.ALPHA);
        for (long i = 0; i < 101101; i++) {
            key = new TranspositionKey(Player.WHITE, i);
            table.put(key, value);
        }
        assertEquals(table.getTable().size(), 100000);
    }

    @Test
    public void transPositionTableCannotPassSizeLimit() {
        table = new SimpleTranspositionTable(100);
        TranspositionKey key;
        TranspositionEntry value = new TranspositionEntry(0, 0, Type.ALPHA);
        for (long i = 0; i < 101101; i++) {
            key = new TranspositionKey(Player.WHITE, i);
            table.put(key, value);
        }
        assertEquals(table.getTable().size(), 100);
    }

    @Test
    public void makeSavedMakesAssociatedEntrySaved() {
        TranspositionKey key = new TranspositionKey(Player.WHITE, 0);
        TranspositionEntry value = new TranspositionEntry(0, 0, Type.ALPHA);
        table.put(key, value);
        table.makeSaved(key);
        assertTrue(table.get(key).isSaved());
    }

    @Test
    public void makeUnSavedMakesAssociatedEntryUnSaved() {
        TranspositionKey key = new TranspositionKey(Player.WHITE, 0);
        TranspositionEntry value = new TranspositionEntry(0, 0, Type.ALPHA);
        table.put(key, value);
        table.makeSaved(key);
        table.makeUnSaved(key);
        assertFalse(table.get(key).isSaved());
    }

    @Test
    public void makeAllPairUnsavedMakesAllEntriesUnsaved() {
        table = new SimpleTranspositionTable(100);
        TranspositionKey key;
        TranspositionEntry value = new TranspositionEntry(0, 0, Type.ALPHA);
        for (long i = 0; i < 100; i++) {
            key = new TranspositionKey(Player.WHITE, i);
            table.put(key, value);
        }
        table.makePairsUnsaved();
        for (Iterator<Map.Entry<TranspositionKey, TranspositionEntry>> it = table.getTable().entrySet().iterator(); it.hasNext();) {
            assertFalse(it.next().getValue().isSaved());
        }
    }

    @Test
    public void makePairsUnsavedAddsAllKeysToRemovable() {
        table = new SimpleTranspositionTable(100);
        TranspositionKey key;
        TranspositionEntry value = new TranspositionEntry(0, 0, Type.ALPHA);
        for (long i = 0; i < 100; i++) {
            key = new TranspositionKey(Player.WHITE, i);
            table.put(key, value);
        }
        table.makePairsUnsaved();
        for (TranspositionKey keyz : table.getTable().keySet()) {
            assertTrue(table.getRemovableKeys().contains(keyz));
        }
    }

    @Test
    public void makePairsUnsavedAddsAllKeysToRemovingOrder() {
        table = new SimpleTranspositionTable(100);
        TranspositionKey key;
        TranspositionEntry value = new TranspositionEntry(0, 0, Type.ALPHA);
        for (long i = 0; i < 100; i++) {
            key = new TranspositionKey(Player.WHITE, i);
            table.put(key, value);
        }
        table.makePairsUnsaved();
        for (TranspositionKey keyz : table.getTable().keySet()) {
            assertTrue(table.getRemovableKeys().contains(keyz));
        }
    }

    @Test
    public void removeRemovesElementsFromBothRemovablesAndTable() {
        table = new SimpleTranspositionTable(100);
        TranspositionKey key;
        TranspositionEntry value = new TranspositionEntry(0, 0, Type.ALPHA);
        for (long i = 0; i < 100; i++) {
            key = new TranspositionKey(Player.WHITE, i);
            table.put(key, value);
        }
        table.makePairsUnsaved();
        while (!table.getRemovingOrder().isEmpty()) {
            table.remove(table.getRemovingOrder().pop());
        }

        assertTrue(table.getTable().isEmpty());
    }
}
