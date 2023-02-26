import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class GuiKończenieAwarii extends JDialog {
    private JPanel contentPane;
    private JButton buttonZakończ;
    private JButton buttonZamknij;
    private JTextArea textAreaOpis;

    public GuiKończenieAwarii() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonZakończ);

        buttonZakończ.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonZamknij.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    private void KończenieAwarii() {
        String opis = textAreaOpis.getText();
        try {
            String query = "UPDATE awaria " +
                    "SET id_pracownik = ?, data_zakonczenia = ?, podjete_dzialania = ?, czy_zakonczona = 1 " +
                    "WHERE id_awaria = ?";
            PreparedStatement ps = SQLConnection.connection.prepareStatement(query);
            ps.setInt(1, GuiLogowanie.pracownikId);
            Timestamp data = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(2, data);
            ps.setString(3, opis);
            ps.setInt(4, GuiAwarieWToku.NumerWybranejAwariiWToku);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    private void onOK() {
        KończenieAwarii();
        dispose();
        GuiAwarieZakończone guiAwarieZakończone = new GuiAwarieZakończone();
        guiAwarieZakończone.pack();
        guiAwarieZakończone.setVisible(true);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
        GuiStronaGlowna guiStronaGlowna = new GuiStronaGlowna();
        guiStronaGlowna.pack();
        guiStronaGlowna.setVisible(true);
    }
}
