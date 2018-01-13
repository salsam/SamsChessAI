package chess.domain.datastructures;

/**
 *
 * @author sami
 */
public interface TranspositionTable {
    public abstract TranspositionEntry get (TranspositionKey key);
    public abstract void put(TranspositionKey key, TranspositionEntry value);
    public abstract void clear();
}
