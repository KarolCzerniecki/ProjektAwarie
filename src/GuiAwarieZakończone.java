import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GuiAwarieZakończone extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JTable tabelaAwarieZakończone;

    private void ListaAwariiZakończonych() {
        try {
            String query = "select id_awaria, data_rozpoczecia, data_zakonczenia, imie, nazwisko, s.nazwa as stanowisko, z.id_maszyna as numer_maszyny, " +
                    "m.nazwa as nazwa_maszyny, w.nazwa as wydzial, podjete_dzialania from awaria " +
                    "inner join pracownik p on p.id_pracownik = awaria.id_pracownik " +
                    "inner join stanowisko s on s.id_stanowisko = p.id_stanowisko " +
                    "inner join zgloszenie z on z.id_zgloszenie = awaria.id_zgloszenie " +
                    "inner join maszyna m on m.id_maszyna = z.id_maszyna " +
                    "inner join wydzial w on w.id_wydzial = m.id_wydzial " +
                    "where czy_zakonczona=1";
            Statement stmt = SQLConnection.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel();
            tabelaAwarieZakończone.setModel(model);
            model.addColumn("Numer awarii");
            model.addColumn("Data rozpoczęcia");
            model.addColumn("Data zakończenia");
            model.addColumn("Kto naprawił");
            model.addColumn("Stanowisko");
            model.addColumn("Numer maszyny");
            model.addColumn("Nazwa maszyny");
            model.addColumn("Wydział");
            model.addColumn("Podjęte działania");

            while (rs.next()) {
                Object[] row = new Object[9];
                row[0] = rs.getInt(1);
                row[1] = rs.getTimestamp(2);
                row[2] = rs.getTimestamp(3);
                row[3] = (rs.getString(4)+" "+rs.getString(5));
                row[4] = rs.getString(6);
                row[5] = rs.getInt(7);
                row[6] = rs.getString(8);
                row[7] = rs.getString(9);
                row[8] = rs.getString(10);
                model.addRow(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }

    public GuiAwarieZakończone() {
        ListaAwariiZakończonych();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);

        buttonCancel.addActionListener(new ActionListener() {
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

    private void onCancel() {
        dispose();
        GuiStronaGlowna guiStronaGlowna = new GuiStronaGlowna();
        guiStronaGlowna.pack();
        guiStronaGlowna.setVisible(true);
    }
}
