package chess.domain.datastructures;

/**
 *This object contains information on current game settings.
 * @author sami
 */
public class Settings {

    private boolean autoMove;

    public Settings() {
        autoMove = true;
    }

    public void setAutoMove(boolean autoMove) {
        this.autoMove = autoMove;
    }

    public boolean isAutoMove() {
        return autoMove;
    }

}
