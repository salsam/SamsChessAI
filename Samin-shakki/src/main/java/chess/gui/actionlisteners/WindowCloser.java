package chess.gui.actionlisteners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author sami
 */
public class WindowCloser implements MouseListener {

    private Map<String, JFrame> frames;

    public WindowCloser(Map<String, JFrame> frames) {
        this.frames = frames;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        frames.values().stream().forEach(frame -> {
            frame.dispose();
        });
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
