import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuiLogowanie extends JDialog {
    public static String pracownikImieNazwisko;
    public static String pracownikStanowisko;
    public static int pracownikStanowiskoId;
    public static int pracownikId;
    private JPanel contentPane;
    private JButton buttonZaloguj;
    private JButton buttonZamknij;
    private JTextField podanyLogin;
    private JPasswordField podaneHasło;
    private JButton oProgramieButton;

    public GuiLogowanie() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonZaloguj);

        buttonZaloguj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onZaloguj();
            }
        });
        oProgramieButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onInfo();
            }
        });

        buttonZamknij.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onZamknij();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    onZamknij();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onZamknij();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void onZaloguj() {
        //logowanie
        String login = podanyLogin.getText();
        String haslo = podaneHasło.getText();

        try{
            PreparedStatement statement = SQLConnection.connection.prepareStatement(
                    "select * from konto inner join pracownik p on p.id_pracownik = konto.id_pracownik\n" +
                            "inner join stanowisko s on s.id_stanowisko = p.id_stanowisko where login = ?");
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                String rsLogin = rs.getString("login");
                String rsHaslo = rs.getString("haslo");
                int rsPracownikId = rs.getInt("id_pracownik");
                if(rsLogin.equals(login) && rsHaslo.equals(haslo)){
                    //gdy zalogowano pomyślnie
                    //pokaż imię i nazwisko zalogowanego
                    String rsImie = rs.getString("imie");
                    String rsNazwisko = rs.getString("nazwisko");
                    int rsStanowkiskoId = rs.getInt("id_stanowisko");
                    String rsStanowisko = rs.getString("nazwa");
                    pracownikId =(rsPracownikId);
                    pracownikImieNazwisko = (rsImie+" "+rsNazwisko);
                    pracownikStanowisko =(rsStanowisko);
                    pracownikStanowiskoId =(rsStanowkiskoId);
                    System.out.println("Zalogowany: "+ pracownikImieNazwisko);
                    rs.close();
                    statement.close();
                    dispose();
                    //zamknij logowanie, otwórz stronę główną
                    GuiStronaGlowna guiStronaGlowna = new GuiStronaGlowna();
                    guiStronaGlowna.pack();
                    guiStronaGlowna.setVisible(true);
                } else {
                    System.out.println("Login lub haslo sa nieprawidlowe");
                    GuiNiepoprawneDane guiNiepoprawneDane = new GuiNiepoprawneDane();
                    guiNiepoprawneDane.pack();
                    guiNiepoprawneDane.setVisible(true);

                }
            } else {
                System.out.println("Uzytkownik nie istnieje");
                GuiNiepoprawneDane guiNiepoprawneDane = new GuiNiepoprawneDane();
                guiNiepoprawneDane.pack();
                guiNiepoprawneDane.setVisible(true);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void onZamknij() throws SQLException {
        SQLConnection.connection.close();
        dispose();
    }
    private void onInfo() {
        GuiInfo guiInfo = new GuiInfo();
        guiInfo.pack();
        guiInfo.setVisible(true);
    }
}
