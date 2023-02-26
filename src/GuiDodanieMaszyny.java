import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

//Dostępne dla stanowisk: 2, 9

public class GuiDodanieMaszyny extends JDialog {
    private static int NumerWydziału;
    private JPanel contentPane;
    private JButton buttonDodajMaszynę;
    private JButton buttonAnuluj;
    private JTextField textFieldNrSeryjny;
    private JTextField textFieldNazwa;
    private JComboBox comboBoxWydział;

    private void listaWydziałów() {
        try {
            // populate the JComboBox with data from a table
            PreparedStatement statement = SQLConnection.connection.prepareStatement("SELECT nazwa FROM wydzial");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comboBoxWydział.addItem(resultSet.getString("nazwa"));
            }

            // add a listener to the JComboBox to handle selection changes
            comboBoxWydział.addActionListener(e -> {
                // get the selected item from the JComboBox
                String selected = (String) comboBoxWydział.getSelectedItem();
                try {
                    // retrieve the row from the database that corresponds to the selected item
                    PreparedStatement statement2 = SQLConnection.connection.prepareStatement("SELECT * FROM wydzial WHERE nazwa = ?");
                    statement2.setString(1, selected);
                    ResultSet resultSet2 = statement2.executeQuery();
                    if (resultSet2.next()) {
                        // display the row data in the console
                        NumerWydziału = resultSet2.getInt("id_wydzial");
                        System.out.println(NumerWydziału + " " + resultSet2.getString("nazwa"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public GuiDodanieMaszyny() {
        listaWydziałów();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonDodajMaszynę);

        buttonDodajMaszynę.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDodajMaszynę();
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

    private void onDodajMaszynę() {
        String nazwa = textFieldNazwa.getText();
        String nr_seryjny = textFieldNrSeryjny.getText();

        try {
            String query = "INSERT INTO maszyna (nazwa, nr_seryjny, id_wydzial) VALUES (?, ?, ?)";
            PreparedStatement ps = SQLConnection.connection.prepareStatement(query);
            ps.setString(1, nazwa);
            ps.setString(2, nr_seryjny);
            ps.setInt(3, NumerWydziału);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        onAnuluj();

    }

    private void onAnuluj() {
        // add your code here if necessary
        dispose();
        GuiListaMaszyn guiListaMaszyn = new GuiListaMaszyn();
        guiListaMaszyn.pack();
        guiListaMaszyn.setVisible(true);
    }
}
