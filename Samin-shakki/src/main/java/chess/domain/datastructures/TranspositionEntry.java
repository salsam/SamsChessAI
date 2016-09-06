package chess.domain.datastructures;

/**
 * This class is used as entry that is saved for each key in transposition
 * table.
 *
 * @author sami
 */
public class TranspositionEntry {

    private int height;
    private int value;
    private Type type;

    public TranspositionEntry(int height, int value, Type type) {
        this.height = height;
        this.value = value;
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
