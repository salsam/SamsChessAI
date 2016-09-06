package chess.domain.datastructures;

import java.util.Map;

/**
 * This class is used for saving search results of AI for search corresponding
 * to transposition key with result of transposition entry.
 *
 * @author sami
 */
public class TranspositionTable {

    private Map<TranspositionKey, TranspositionEntry> table;

    public TranspositionTable() {
        table = new MyHashMap();
    }

    public boolean containsRelevantKey(TranspositionKey key, int height) {
        if (!table.containsKey(key)) {
            return false;
        }
        return table.get(key).getHeight() >= height;
    }

    public TranspositionEntry get(TranspositionKey key) {
        return table.get(key);
    }

    /**
     * Puts the given key and entry associated with it to HashMap. For each key
     * only one result with highest depth is saved to save memory and provide
     * better evaluation. There is no memory cap for this table so in the long
     * run this table will take up exponential amount of memory space. If I had
     * time, I would've added my own implementation for heap and used it keep
     * keys sorted by search depth. Thus I could remove results from shallower
     * searches first. Saved-field in keys would've been used to make all keys
     * deletable after a search was completed and if entry is used it would be
     * set unremovable for current search.
     *
     * @param key key representing chessboard situation at time of search.
     * @param entry entry representing the result of said search.
     */
    public void put(TranspositionKey key, TranspositionEntry entry) {
        if (!table.containsKey(key)) {
            table.put(key, entry);
        } else if (table.get(key).getHeight() < entry.getHeight()) {
            table.put(key, entry);
        }
    }

    /**
     * Makes all pairs saved HashMap eligible for being removed if memory cap is
     * reached.
     */
    public void makePairsUnsaved() {
        table.keySet().stream().forEach((key) -> {
            key.setSaved(false);
        });
    }

}
