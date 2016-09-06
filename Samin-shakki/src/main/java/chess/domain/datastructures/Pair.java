package chess.domain.datastructures;

import java.util.Objects;

/**
 * This class is used to save pairs formed of two objects acting like Python's
 * tuple.
 *
 * @author sami
 * @param <Type1> type of first saved object.
 * @param <Type2> type of second saved object.
 */
public class Pair<Type1 extends Object, Type2 extends Object> {

    private Type1 first;
    private Type2 second;

    public Pair(Type1 first, Type2 second) {
        this.first = first;
        this.second = second;
    }

    public Type1 getFirst() {
        return first;
    }

    public void setFirst(Type1 first) {
        this.first = first;
    }

    public Type2 getSecond() {
        return second;
    }

    public void setSecond(Type2 second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.first);
        hash = 23 * hash + Objects.hashCode(this.second);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair other = (Pair) obj;
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (this.second != other.second) {
            return false;
        }
        return true;
    }

}
