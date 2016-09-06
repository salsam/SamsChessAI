package chess.domain.datastructures;

import chess.domain.board.Player;
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
public class TranspositionTableTest {

    private TranspositionTable tt;

    public TranspositionTableTest() {
    }

    @Before
    public void setUp() {
        tt = new TranspositionTable();
    }

    @Test
    public void containsRelevantKeyReturnsFalseIfTableDoesNotContainKey() {
        assertFalse(tt.containsRelevantKey(new TranspositionKey(Player.WHITE, 42), 0));
    }

    @Test
    public void containsKeyReturnsTrueIfKeyWasAdded() {
        tt.put(new TranspositionKey(Player.WHITE, 0), new TranspositionEntry(0, 0, Type.ALPHA));
        assertTrue(tt.containsRelevantKey(new TranspositionKey(Player.WHITE, 0), 0));
    }

    @Test
    public void containsKeyReturnsFalseIfTableOnlyContainsKeyWithLowerDepth() {
        tt.put(new TranspositionKey(Player.WHITE, 0), new TranspositionEntry(0, 0, Type.ALPHA));
        assertFalse(tt.containsRelevantKey(new TranspositionKey(Player.WHITE, 0), 1));
    }

    @Test
    public void containsKeyTrueIfTableContainsKeyWithHigherDepth() {
        tt.put(new TranspositionKey(Player.WHITE, 0), new TranspositionEntry(2, 0, Type.ALPHA));
        assertTrue(tt.containsRelevantKey(new TranspositionKey(Player.WHITE, 0), 1));
    }

    @Test
    public void getReturnsEntryassociatedWithGivenKey() {
        TranspositionKey key = new TranspositionKey(Player.WHITE, 0);
        TranspositionEntry entry = new TranspositionEntry(2, 0, Type.ALPHA);
        tt.put(key, entry);
        assertEquals(entry, tt.get(key));
    }

    @Test
    public void addedKeysHaveSavedFalse() {
        TranspositionKey key = new TranspositionKey(Player.WHITE, 0);
        TranspositionEntry entry = new TranspositionEntry(2, 0, Type.ALPHA);
        tt.put(key, entry);
        assertFalse(key.isSaved());
    }

    @Test
    public void whenAddingAShallowerResultForOldKeyNothingHappens() {
        TranspositionKey key = new TranspositionKey(Player.WHITE, 0);
        TranspositionEntry entry = new TranspositionEntry(2, 1, Type.EXACT);
        tt.put(key, entry);
        TranspositionEntry shallow = new TranspositionEntry(0, 0, Type.ALPHA);
        tt.put(key, shallow);
        assertEquals(2, tt.get(key).getHeight());
        assertEquals(1, tt.get(key).getValue());
        assertEquals(Type.EXACT, tt.get(key).getType());
    }

    @Test
    public void addingDeeperResultForOldKeyReplacesOldEntry() {
        TranspositionKey key = new TranspositionKey(Player.WHITE, 0);
        TranspositionEntry entry = new TranspositionEntry(2, 1, Type.EXACT);
        tt.put(key, entry);
        TranspositionEntry deep = new TranspositionEntry(7, 42, Type.ALPHA);
        tt.put(key, deep);
        assertEquals(7, tt.get(key).getHeight());
        assertEquals(42, tt.get(key).getValue());
        assertEquals(Type.ALPHA, tt.get(key).getType());
    }

    @Test
    public void makeAllKeysUnsavedMakesAllKeysUnsaved() {
        TranspositionKey key = new TranspositionKey(Player.WHITE, 0);
        TranspositionEntry entry = new TranspositionEntry(2, 1, Type.EXACT);
        tt.put(key, entry);
        TranspositionKey newKey = new TranspositionKey(Player.BLACK, 7);
        TranspositionEntry deep = new TranspositionEntry(7, 42, Type.ALPHA);
        tt.put(newKey, deep);
        key.setSaved(true);
        newKey.setSaved(true);
        tt.makePairsUnsaved();
        assertFalse(key.isSaved());
        assertFalse(newKey.isSaved());
    }
}
