import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

public class GuiStronaGlowna extends JDialog {
    private JPanel contentPane;
    private JButton buttonWyloguj;
    private JButton buttonZamknij;
    private JButton zgłośAwarięButton;
    private JButton listaMaszynButton;
    private JButton listaPracownikówButton;
    private JButton awarieWTokuButton;
    private JLabel ktoZalogowany;
    private JLabel stanowiskoZalogowany;
    private JButton zgłoszeniaButton;
    private JButton oProgramieButton;
    private JButton awarieZakończoneButton;


    public GuiStronaGlowna() {
        ktoZalogowany.setText(GuiLogowanie.pracownikImieNazwisko);
        stanowiskoZalogowany.setText(GuiLogowanie.pracownikStanowisko);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonWyloguj);

        oProgramieButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onInfo();
            }
        });
        listaPracownikówButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPracownicy();
            }
        });
        listaMaszynButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onMaszyny();
            }
        });
        zgłośAwarięButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {onDodajZgłoszenie();
            }
        });
        zgłoszeniaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {onZgłoszenia();
            }
        });
        awarieWTokuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAwarieWToku();
            }
        });
        awarieZakończoneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAwarieZakończone();
            }
        });

        buttonWyloguj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onWyloguj();
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

    private void onAwarieZakończone() {
        if(GuiLogowanie.pracownikStanowiskoId == 1 ||
                GuiLogowanie.pracownikStanowiskoId == 2 ||
                GuiLogowanie.pracownikStanowiskoId == 3 ||
                GuiLogowanie.pracownikStanowiskoId == 7 ||
                GuiLogowanie.pracownikStanowiskoId == 8 ||
                GuiLogowanie.pracownikStanowiskoId == 9)
        {
            dispose();
            GuiAwarieZakończone guiAwarieZakończone = new GuiAwarieZakończone();
            guiAwarieZakończone.pack();
            guiAwarieZakończone.setVisible(true);
        }
        else{
            GuiBrakUprawnien guiBrakUprawnien = new GuiBrakUprawnien();
            guiBrakUprawnien.pack();
            guiBrakUprawnien.setVisible(true);
        }
    }

    private void onAwarieWToku() {
        //Dostępne dla stanowisk:
        if(GuiLogowanie.pracownikStanowiskoId == 1 ||
        GuiLogowanie.pracownikStanowiskoId == 2 ||
        GuiLogowanie.pracownikStanowiskoId == 3 ||
        GuiLogowanie.pracownikStanowiskoId == 7 ||
        GuiLogowanie.pracownikStanowiskoId == 8 ||
        GuiLogowanie.pracownikStanowiskoId == 9)
        {
            dispose();
            GuiAwarieWToku guiAwarieWToku = new GuiAwarieWToku();
            guiAwarieWToku.pack();
            guiAwarieWToku.setVisible(true);
        }
        else{
            GuiBrakUprawnien guiBrakUprawnien = new GuiBrakUprawnien();
            guiBrakUprawnien.pack();
            guiBrakUprawnien.setVisible(true);
        }
    }
    private void onZgłoszenia() {
        //Dostępne dla stanowisk:
        if(GuiLogowanie.pracownikStanowiskoId == 1 ||
                GuiLogowanie.pracownikStanowiskoId == 2 ||
                GuiLogowanie.pracownikStanowiskoId == 3 ||
                GuiLogowanie.pracownikStanowiskoId == 4 ||
                GuiLogowanie.pracownikStanowiskoId == 5 ||
                GuiLogowanie.pracownikStanowiskoId == 7 ||
                GuiLogowanie.pracownikStanowiskoId == 8 ||
                GuiLogowanie.pracownikStanowiskoId == 9)
        {
            dispose();
            GuiListaZgłoszeń guiListaZgłoszeń = new GuiListaZgłoszeń();
            guiListaZgłoszeń.pack();
            guiListaZgłoszeń.setVisible(true);
        }
        else{
            GuiBrakUprawnien guiBrakUprawnien = new GuiBrakUprawnien();
            guiBrakUprawnien.pack();
            guiBrakUprawnien.setVisible(true);
        }
    }

    private void onDodajZgłoszenie() {
        dispose();
        GuiDodajZgloszenie guiDodajZgloszenie = new GuiDodajZgloszenie();
        guiDodajZgloszenie.pack();
        guiDodajZgloszenie.setVisible(true);
    }

    private void onMaszyny() {
        dispose();
        GuiListaMaszyn guiListaMaszyn = new GuiListaMaszyn();
        guiListaMaszyn.pack();
        guiListaMaszyn.setVisible(true);
    }

    private void onPracownicy() {
        //Dostępne dla stanowisk:
        if(GuiLogowanie.pracownikStanowiskoId == 1 ||
        GuiLogowanie.pracownikStanowiskoId == 2 ||
        GuiLogowanie.pracownikStanowiskoId == 3 ||
        GuiLogowanie.pracownikStanowiskoId == 4 ||
        GuiLogowanie.pracownikStanowiskoId == 5)
        {
            dispose();
            GuiListaPracownikow guiListaPracownikow = new GuiListaPracownikow();
            guiListaPracownikow.pack();
            guiListaPracownikow.setVisible(true);
        }
        else{
            GuiBrakUprawnien guiBrakUprawnien = new GuiBrakUprawnien();
            guiBrakUprawnien.pack();
            guiBrakUprawnien.setVisible(true);
        }
    }

    private void onWyloguj() {
        dispose();
        GuiLogowanie guiLogowanie = new GuiLogowanie();
        guiLogowanie.pack();
        guiLogowanie.setVisible(true);
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


