import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class GuiAwarieWToku extends JDialog {
    private JPanel contentPane;
    private JTable tabelaAwarieWToku;
    private JButton buttonZakończ;
    private JButton buttonZamknij;
    public static int NumerWybranejAwariiWToku;
    private void ListaAwariiWToku() {
        try {
            String query = "select id_awaria, data_rozpoczecia, imie, nazwisko, s.nazwa as stanowisko, z.id_maszyna as numer_maszyny, " +
                    "m.nazwa as nazwa_maszyny, w.nazwa as wydzial from awaria " +
                    "inner join pracownik p on p.id_pracownik = awaria.id_pracownik " +
                    "inner join stanowisko s on s.id_stanowisko = p.id_stanowisko " +
                    "inner join zgloszenie z on z.id_zgloszenie = awaria.id_zgloszenie " +
                    "inner join maszyna m on m.id_maszyna = z.id_maszyna " +
                    "inner join wydzial w on w.id_wydzial = m.id_wydzial " +
                    "where czy_zakonczona=0";
            Statement stmt = SQLConnection.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel();
            tabelaAwarieWToku.setModel(model);
            model.addColumn("Numer awarii");
            model.addColumn("Data rozpoczęcia");
            model.addColumn("Kto naprawia");
            model.addColumn("Stanowisko");
            model.addColumn("Numer maszyny");
            model.addColumn("Nazwa maszyny");
            model.addColumn("Wydział");

            while (rs.next()) {
                Object[] row = new Object[9];
                row[0] = rs.getInt(1);
                row[1] = rs.getTimestamp(2);
                row[2] = (rs.getString(3)+" "+rs.getString(4));
                row[3] = rs.getString(5);
                row[4] = rs.getInt(6);
                row[5] = rs.getString(7);
                row[6] = rs.getString(8);
                model.addRow(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }

    public GuiAwarieWToku() {
        ListaAwariiWToku();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonZakończ);

        tabelaAwarieWToku.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {
                    return;
                }

                int row = tabelaAwarieWToku.getSelectedRow();

                if (row >= 0) {
                    NumerWybranejAwariiWToku = (int) tabelaAwarieWToku.getValueAt(row, 0);
                    System.out.println(NumerWybranejAwariiWToku);
                }
            }
        });

        buttonZakończ.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onZakończ();
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

    private void onZakończ() {
        dispose();
        GuiKończenieAwarii guiKończenieAwarii = new GuiKończenieAwarii();
        guiKończenieAwarii.pack();
        guiKończenieAwarii.setVisible(true);
    }

    private void onZamknij() {
        dispose();
        GuiStronaGlowna guiStronaGlowna = new GuiStronaGlowna();
        guiStronaGlowna.pack();
        guiStronaGlowna.setVisible(true);
    }
}
