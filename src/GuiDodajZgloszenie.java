import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Timestamp;

//Dostępne dla stanowisk: 2, 9

public class GuiDodajZgloszenie extends JDialog {
    private static int NumerWydziału;
    private static int NumerMaszyny;
    private static int OstatniNumerZgłoszenia;
    private JPanel contentPane;
    private JButton buttonDodajZgloszenie;
    private JButton buttonAnuluj;
    private JComboBox comboBoxMaszyna;
    private JTextArea textAreaOpis;
    private JComboBox comboBoxWydział;

    private void listaWydziałów() {
        try {
            // clear the current values in the comboBoxMaszyna
            comboBoxMaszyna.removeAllItems();

            // populate the JComboBox with data from a table
            PreparedStatement statement = SQLConnection.connection.prepareStatement("SELECT nazwa FROM wydzial");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comboBoxWydział.addItem(resultSet.getString("nazwa"));
            }

            // add a listener to the JComboBox to handle selection changes
            comboBoxWydział.addActionListener(e -> {
                // get the selected item from the JComboBox
                String selectedWydział = (String) comboBoxWydział.getSelectedItem();
                try {
                    // retrieve the row from the database that corresponds to the selected item
                    PreparedStatement statement2 = SQLConnection.connection.prepareStatement("SELECT * FROM wydzial WHERE nazwa = ?");
                    statement2.setString(1, selectedWydział);
                    ResultSet resultSet2 = statement2.executeQuery();
                    if (resultSet2.next()) {
                        // display the row data in the console
                        NumerWydziału = resultSet2.getInt("id_wydzial");
                        System.out.println(NumerWydziału + " " + resultSet2.getString("nazwa"));

                        // clear the current values in the comboBoxMaszyna
                        comboBoxMaszyna.removeAllItems();

                        // reload the comboBoxMaszyna with values based on the selected NumerWydziału
                        listaMaszyn();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void listaMaszyn() {
        try {
            // populate the JComboBox with data from a table
            PreparedStatement statement = SQLConnection.connection.prepareStatement("SELECT nazwa FROM maszyna where id_wydzial = ?");
            statement.setInt(1, NumerWydziału);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comboBoxMaszyna.addItem(resultSet.getString("nazwa"));
            }

            // add a listener to the JComboBox to handle selection changes
            comboBoxMaszyna.addActionListener(e -> {
                // get the selected item from the JComboBox
                String selectedMaszyna = (String) comboBoxMaszyna.getSelectedItem();
                System.out.println(selectedMaszyna);
                try {
                    // retrieve the row from the database that corresponds to the selected item
                    PreparedStatement statement2 = SQLConnection.connection.prepareStatement("SELECT * FROM maszyna WHERE nazwa = ?");
                    statement2.setString(1, selectedMaszyna);
                    ResultSet resultSet2 = statement2.executeQuery();
                    if (resultSet2.next()) {
                        // display the row data in the console
                        NumerMaszyny = resultSet2.getInt("id_maszyna");
                        System.out.println(NumerMaszyny + " " + resultSet2.getString("nazwa"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public GuiDodajZgloszenie() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonDodajZgloszenie);
        listaWydziałów();

        buttonDodajZgloszenie.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDodajZgloszenie();
            }
        });

        buttonAnuluj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAnuluj();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onAnuluj();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAnuluj();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onDodajZgloszenie() {
        if (NumerMaszyny == 0) {
            JOptionPane.showMessageDialog(this, "Proszę wybrać maszynę.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String query1 = "INSERT INTO zglaszajacy (id_pracownik) VALUES (?)";
            PreparedStatement ps1 = SQLConnection.connection.prepareStatement(query1);
            ps1.setInt(1, GuiLogowanie.pracownikId);
            ps1.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        try {
            Statement stmt = SQLConnection.connection.createStatement();
            ResultSet rs = stmt.executeQuery("select max(id_zglaszajacy) from zglaszajacy");
            if (rs.next()) {
                OstatniNumerZgłoszenia = rs.getInt(1);
            }
        }catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        try {
            String query2 = "INSERT INTO zgloszenie (id_zglaszajacy, id_maszyna, data_zgloszenia, opis_awarii) VALUES (?, ?, ?, ?)";
            PreparedStatement ps2 = SQLConnection.connection.prepareStatement(query2);
            ps2.setInt(1, OstatniNumerZgłoszenia);
            ps2.setInt(2, NumerMaszyny);
            Timestamp data = new Timestamp(System.currentTimeMillis());
            ps2.setTimestamp(3, data);
            ps2.setString(4, textAreaOpis.getText());
            ps2.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        dispose();
        GuiZgłoszonoAwarię guiZgłoszonoAwarię = new GuiZgłoszonoAwarię();
        guiZgłoszonoAwarię.pack();
        guiZgłoszonoAwarię.setVisible(true);

    }

    private void onAnuluj() {
        dispose();
        GuiStronaGlowna guiStronaGlowna = new GuiStronaGlowna();
        guiStronaGlowna.pack();
        guiStronaGlowna.setVisible(true);
    }
}
