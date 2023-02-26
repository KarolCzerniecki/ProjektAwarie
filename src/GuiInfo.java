import javax.swing.*;
import java.awt.event.*;

public class GuiInfo extends JDialog {
    private JPanel contentPane;
    private JButton buttonZamknij;

    public GuiInfo() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonZamknij);

        buttonZamknij.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onZamknij();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onZamknij();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onZamknij();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onZamknij() {
        // add your code here if necessary
        dispose();
    }
}
