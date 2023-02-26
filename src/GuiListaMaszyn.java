import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

//Dostępne dla stanowisk: 1, 2, 3, 4, 9

public class GuiListaMaszyn extends JDialog {
    private JPanel contentPane;
    private JButton buttonDodaj;
    private JButton buttonZamknij;
    private JScrollPane scrollPane;
    private JTable tabelaMaszyny;

    private void ListaMaszyn(){

        try {
            String query = "select id_maszyna, maszyna.nazwa as nazwa, nr_seryjny, w.nazwa as wydzial from maszyna " +
                    "inner join wydzial w on w.id_wydzial = maszyna.id_wydzial";
            Statement stmt = SQLConnection.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel();
            tabelaMaszyny.setModel(model);
            model.addColumn("Numer");
            model.addColumn("Nazwa maszyny");
            model.addColumn("Numer seryjny");
            model.addColumn("Wydział");

            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getInt("id_maszyna");
                row[1] = rs.getString("nazwa");
                row[2] = rs.getString("nr_seryjny");
                row[3] = rs.getString("wydzial");
                model.addRow(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }

    public GuiListaMaszyn() {
        ListaMaszyn();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonDodaj);
        buttonDodaj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDodaj();
            }
        });

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

    private void onDodaj() {
        if(GuiLogowanie.pracownikStanowiskoId == 1 ||
           GuiLogowanie.pracownikStanowiskoId == 2 ||
           GuiLogowanie.pracownikStanowiskoId == 3) {
            dispose();
            GuiDodanieMaszyny guiDodanieMaszyny = new GuiDodanieMaszyny();
            guiDodanieMaszyny.pack();
            guiDodanieMaszyny.setVisible(true);
        }
        else {
            GuiBrakUprawnien guiBrakUprawnien = new GuiBrakUprawnien();
            guiBrakUprawnien.pack();
            guiBrakUprawnien.setVisible(true);
        }
    }

    private void onZamknij() {
        dispose();
        GuiStronaGlowna guiStronaGlowna = new GuiStronaGlowna();
        guiStronaGlowna.pack();
        guiStronaGlowna.setVisible(true);
    }
}
