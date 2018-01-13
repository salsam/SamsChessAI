package chess.domain.datastructures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 *
 * Transposition table that is lossless and keeps track of most common game
 * situations.
 *
 * Entries are removed in the order that they were set as unsaved.
 *
 * @author sami
 */
public class SimpleTranspositionTable implements TranspositionTable {

    private Map<TranspositionKey, TranspositionEntry> table;
    private Set<TranspositionKey> removableKeys;
    private Stack<TranspositionKey> removingOrder;
    private int sizeLimit;

    public SimpleTranspositionTable() {
        this.table = new HashMap<>(100000);
        this.sizeLimit = 100000;
        this.removableKeys = new HashSet<>();
        this.removingOrder = new Stack<>();
    }

    public SimpleTranspositionTable(int size) {
        this.table = new HashMap<>(size);
        this.sizeLimit = size;
        this.removableKeys = new HashSet<>();
        this.removingOrder = new Stack<>();
    }

    public Map<TranspositionKey, TranspositionEntry> getTable() {
        return table;
    }

    public Set<TranspositionKey> getRemovableKeys() {
        return removableKeys;
    }

    public Stack<TranspositionKey> getRemovingOrder() {
        return removingOrder;
    }

    public void setSizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public int getSizeLimit() {
        return sizeLimit;
    }

    public TranspositionEntry get(TranspositionKey key) {
        return table.get(key);
    }

    public boolean containsKey(TranspositionKey key) {
        return table.containsKey(key);
    }

    public void put(TranspositionKey key, TranspositionEntry value) {
        if (table.size() == sizeLimit) {
            if (!removableKeys.isEmpty()) {
                TranspositionKey removed = null;
                while (!removingOrder.isEmpty() && (removed == null || !removableKeys.contains(removed) || get(removed).isSaved())) {
                    removed = removingOrder.pop();
                }
                if (!removableKeys.contains(removed)) {
                    return;
                }
                removableKeys.remove(removed);
                table.remove(removed);
            } else {
                return;
            }
        }
        table.put(key, value);
    }

    public void makeSaved(TranspositionKey key) {
        if (containsKey(key)) {
            get(key).setSaved(true);
        }
    }

    public void makeUnSaved(TranspositionKey key) {
        if (containsKey(key)) {
            get(key).setSaved(false);
        }
    }

    public void remove(TranspositionKey key) {
        table.remove(key);
        removableKeys.remove(key);
    }

    public void makePairsUnsaved() {
        table.entrySet().stream().forEach((entry) -> {
            entry.getValue().setSaved(false);
            removableKeys.add(entry.getKey());
            removingOrder.push(entry.getKey());
        });
    }

    public void clear() {
        table.clear();
        removableKeys.clear();
        removingOrder.clear();
    }
}
