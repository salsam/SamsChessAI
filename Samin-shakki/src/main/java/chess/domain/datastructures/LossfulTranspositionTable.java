package chess.domain.datastructures;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This class is used for saving search results of AI for search corresponding
 * to transposition key with result of transposition entry.
 *
 * Table is lossful because only highest depth entry saved for each position and
 * it will be used for queries of any depth..
 *
 * @author sami
 */
public class LossfulTranspositionTable implements TranspositionTable {

    private Map<TranspositionKey, TranspositionEntry> table;
    private PriorityQueue<TranspositionKey> pq;

    public LossfulTranspositionTable() {
        table = new HashMap(100000);
        pq = new PriorityQueue(new Comparator<TranspositionKey>() {
            public int compare(TranspositionKey k1, TranspositionKey k2) {
                return table.get(k2).getHeight() - table.get(k1).getHeight();
            }
        });
    }

    public boolean containsKey(TranspositionKey key, int height) {
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
        if (table.size() == 100000) {
            TranspositionKey removed = pq.poll();
            while (get(removed).isSaved()) {
                removed = pq.poll();
            }
            table.remove(removed);

        }
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
        table.entrySet().stream().forEach((Map.Entry<TranspositionKey, TranspositionEntry> entry) -> {
            entry.getValue().setSaved(false);
        });
        pq.clear();
        pq.addAll(table.keySet());
    }

    /**
     * Removes given key from priority queue that is responsible for ordering
     * removed keys.
     *
     * @param key TranspositionKey that is removed from to-be-removed queue.
     */
    public void makeSaved(TranspositionKey key) {
        get(key).setSaved(true);
    }

    public void clear() {
        table.clear();
        pq.clear();
    }

}
