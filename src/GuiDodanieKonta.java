import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

//Dostępne dla stanowisk: 9

public class GuiDodanieKonta extends JDialog {
    private static int OstatniNumer;
    private JPanel contentPane;
    private JButton buttonDodajKonto;
    private JButton buttonAnuluj;
    private JTextField textFieldLogin;
    private JTextField textFieldHasło;


    public GuiDodanieKonta() {
        SprawdzenieOstatniegoNumeru();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonDodajKonto);

        buttonDodajKonto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDodajKonto();
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

    private void SprawdzenieOstatniegoNumeru() {
        try {
            Statement stmt = SQLConnection.connection.createStatement();
            ResultSet rs = stmt.executeQuery("select max(id_pracownik) from pracownik");
            if (rs.next()) {
                OstatniNumer = rs.getInt(1);
                System.out.println("Ostatni: " + OstatniNumer);
            }
        }catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    private void onDodajKonto() {
        String login = textFieldLogin.getText();
        String haslo = textFieldHasło.getText();

        try {
            String query = "INSERT INTO konto (login, haslo, id_pracownik) VALUES (?, ?, ?)";
            PreparedStatement ps = SQLConnection.connection.prepareStatement(query);
            ps.setString(1, login);
            ps.setString(2, haslo);
            ps.setInt(3, OstatniNumer);
            ps.executeUpdate();
            dispose();
            onAnuluj();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    private void onAnuluj() {
        dispose();
        GuiListaPracownikow guiListaPracownikow = new GuiListaPracownikow();
        guiListaPracownikow.pack();
        guiListaPracownikow.setVisible(true);
    }
}
