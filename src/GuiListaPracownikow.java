import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

//Dostępne dla stanowisk: 1, 2, 3, 4, 9

public class GuiListaPracownikow extends JDialog {
    private JPanel contentPane;
    private JButton buttonDodaj;
    private JButton buttonZamknij;
    private JScrollPane scrollPane;
    private JTable tabelaPracownicy;

    private void ListaPracownikow(){

            try {
                String query = "select id_pracownik, imie, nazwisko, nazwa from pracownik " +
                        "inner join stanowisko s on s.id_stanowisko = pracownik.id_stanowisko";
                Statement stmt = SQLConnection.connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                DefaultTableModel model = new DefaultTableModel();
                tabelaPracownicy.setModel(model);
                model.addColumn("Numer");
                model.addColumn("Imię");
                model.addColumn("Nazwisko");
                model.addColumn("Stanowisko");

                while (rs.next()) {
                    Object[] row = new Object[4];
                    row[0] = rs.getInt("id_pracownik");
                    row[1] = rs.getString("imie");
                    row[2] = rs.getString("nazwisko");
                    row[3] = rs.getString("nazwa");
                    model.addRow(row);
                }

                rs.close();
                stmt.close();
            } catch (SQLException e) {
                System.out.println("Błąd: " + e.getMessage());
            }
        }

    public GuiListaPracownikow() {
        ListaPracownikow();
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
           GuiLogowanie.pracownikStanowiskoId == 2) {
            dispose();
            GuiDodaniePracownika guiDodaniePracownika = new GuiDodaniePracownika();
            guiDodaniePracownika.pack();
            guiDodaniePracownika.setVisible(true);
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
