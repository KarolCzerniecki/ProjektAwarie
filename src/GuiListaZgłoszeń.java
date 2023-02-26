import javax.swing.*;
import java.awt.event.*;

//Dostępne dla stanowisk: 1, 3, 4, 5, 9

import java.sql.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class GuiListaZgłoszeń extends JDialog {
    private JPanel contentPane;
    private JButton buttonRozpatrz;
    private JButton buttonZamknij;
    private JScrollPane scrollPane;
    private JTable tabelaZgłoszenia;
    public static int NumerWybranegoZgłoszenia;

    private void ListaZgłoszeń(){

        try {
            String query = "select id_zgloszenie, p.imie, p.nazwisko, s.nazwa as stanowisko, zgloszenie.id_maszyna, " +
                    "m.nazwa as nazwa_maszyny, w.nazwa as wydział, data_zgloszenia, opis_awarii from zgloszenie " +
                    "inner join maszyna m on m.id_maszyna = zgloszenie.id_maszyna " +
                    "inner join wydzial w on w.id_wydzial = m.id_wydzial " +
                    "inner join zglaszajacy z on z.id_zglaszajacy = zgloszenie.id_zglaszajacy " +
                    "inner join pracownik p on p.id_pracownik = z.id_pracownik " +
                    "inner join stanowisko s on s.id_stanowisko = p.id_stanowisko " +
                    "where czy_rozpatrzone = 0";
            Statement stmt = SQLConnection.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel();
            tabelaZgłoszenia.setModel(model);
            model.addColumn("Numer zgłoszenia");
            model.addColumn("Zgłaszający");
            model.addColumn("Stanowisko");
            model.addColumn("Numer maszyny");
            model.addColumn("Nazwa maszyny");
            model.addColumn("Wydział");
            model.addColumn("Data zgłoszenia");
            model.addColumn("Opis");

            while (rs.next()) {
                Object[] row = new Object[9];
                row[0] = rs.getInt(1);
                row[1] = (rs.getString(2)+" "+rs.getString(3));
                row[2] = rs.getString(4);
                row[3] = rs.getInt(5);
                row[4] = rs.getString(6);
                row[5] = rs.getString(7);
                row[7] = rs.getTimestamp(8);
                row[8] = rs.getString(9);
                model.addRow(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }

    public GuiListaZgłoszeń() {
        ListaZgłoszeń();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonRozpatrz);
        tabelaZgłoszenia.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {
                    return;
                }

                int row = tabelaZgłoszenia.getSelectedRow();

                if (row >= 0) {
                    NumerWybranegoZgłoszenia = (int) tabelaZgłoszenia.getValueAt(row, 0);
                    System.out.println(NumerWybranegoZgłoszenia);
                }
            }
        });
        buttonRozpatrz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRozpatrz();
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
    private void RozpatrzenieTrue() {
        try {
            String sql = "UPDATE zgloszenie SET czy_rozpatrzone = 1 WHERE id_zgloszenie = ?";
            PreparedStatement statement = SQLConnection.connection.prepareStatement(sql);
            statement.setInt(1, GuiListaZgłoszeń.NumerWybranegoZgłoszenia); // set the id of the row you want to update
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Row updated successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void TworzenieRaportu() {
        try {
            String query = "INSERT INTO awaria (id_zgloszenie, data_rozpoczecia, id_pracownik) VALUES (?, ?, ?)";
            PreparedStatement ps = SQLConnection.connection.prepareStatement(query);
            ps.setInt(1, GuiListaZgłoszeń.NumerWybranegoZgłoszenia);
            Timestamp data = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(2, data);
            ps.setInt(3, GuiLogowanie.pracownikId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void onRozpatrz() {
        if(GuiLogowanie.pracownikStanowiskoId == 1 ||
        GuiLogowanie.pracownikStanowiskoId == 3 ||
        GuiLogowanie.pracownikStanowiskoId == 7 ||
        GuiLogowanie.pracownikStanowiskoId == 8 ||
        GuiLogowanie.pracownikStanowiskoId == 9) {
            RozpatrzenieTrue();
            TworzenieRaportu();
            dispose();
            GuiZgłoszenieRozpatrzone guiZgłoszenieRozpatrzone = new GuiZgłoszenieRozpatrzone();
            guiZgłoszenieRozpatrzone.pack();
            guiZgłoszenieRozpatrzone.setVisible(true);
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
