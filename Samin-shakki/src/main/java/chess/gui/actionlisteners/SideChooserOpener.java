package chess.gui.actionlisteners;

import chess.gui.MainFrame;
import chess.gui.SideChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author sami
 */
public class SideChooserOpener implements ActionListener {

    private MainFrame mf;

    public SideChooserOpener(MainFrame mf) {
        this.mf = mf;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        mf.setVisible(false);
        SideChooser sc = new SideChooser(mf);
        sc.setVisible(true);
    }
}
