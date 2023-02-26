import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

//Dostępne dla stanowisk: 1, 9

public class GuiDodaniePracownika extends JDialog {
    private static int NumerStanowiska;
    private JPanel contentPane;
    private JButton buttonDodajPracownika;
    private JButton buttonAnuluj;
    private JTextField textFieldNazwisko;
    private JTextField textFieldImie;
    private JComboBox comboBoxStanowisko;

    private void listaStanowisk() {
        try {
            // populate the JComboBox with data from a table
            PreparedStatement statement = SQLConnection.connection.prepareStatement("SELECT nazwa FROM stanowisko");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comboBoxStanowisko.addItem(resultSet.getString("nazwa"));
            }

            // add a listener to the JComboBox to handle selection changes
            comboBoxStanowisko.addActionListener(e -> {
                // get the selected item from the JComboBox
                String selected = (String) comboBoxStanowisko.getSelectedItem();
                try {
                    // retrieve the row from the database that corresponds to the selected item
                    PreparedStatement statement2 = SQLConnection.connection.prepareStatement("SELECT * FROM stanowisko WHERE nazwa = ?");
                    statement2.setString(1, selected);
                    ResultSet resultSet2 = statement2.executeQuery();
                    if (resultSet2.next()) {
                        // display the row data in the console
                        NumerStanowiska = resultSet2.getInt("id_stanowisko");
                        System.out.println(NumerStanowiska + " " + resultSet2.getString("nazwa"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public GuiDodaniePracownika() {
        listaStanowisk();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonDodajPracownika);

        buttonDodajPracownika.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDodajPracownika();
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

    private void onDodajPracownika() {
        String imie = textFieldImie.getText();
        String nazwisko = textFieldNazwisko.getText();

        try {
            String query = "INSERT INTO pracownik (imie, nazwisko, id_stanowisko) VALUES (?, ?, ?)";
            PreparedStatement ps = SQLConnection.connection.prepareStatement(query);
            ps.setString(1, imie);
            ps.setString(2, nazwisko);
            ps.setInt(3, NumerStanowiska);
            ps.executeUpdate();
        } catch (SQLException ex) {
                System.out.println("Error: " + ex.getMessage());
        }
        dispose();
        GuiDodanieKonta guiDodanieKonta = new GuiDodanieKonta();
        guiDodanieKonta.pack();
        guiDodanieKonta.setVisible(true);

    }

    private void onAnuluj() {
        // add your code here if necessary
        dispose();
        GuiListaPracownikow guiListaPracownikow = new GuiListaPracownikow();
        guiListaPracownikow.pack();
        guiListaPracownikow.setVisible(true);
    }
}
