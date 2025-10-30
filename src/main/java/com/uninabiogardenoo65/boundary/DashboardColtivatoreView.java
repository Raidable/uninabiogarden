package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Coltivatore;
import com.uninabiogardenoo65.entity.Progetto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DashboardColtivatoreView extends JFrame {

    private final Controller controller;
    private final Coltivatore coltivatoreCorrente; 
    
    private JTable progettiAssegnatiTable;
    private DefaultTableModel progettiTableModel;
    private JButton vediAttivitaAssegnateButton;

    public DashboardColtivatoreView(Controller controller, Coltivatore coltivatore) {
        this.controller = controller;
        this.coltivatoreCorrente = coltivatore;
        
        setTitle("Dashboard Coltivatore - Benvenuto, " + coltivatore.getNome());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initializeComponents();
        layoutComponents();
        addListeners();
        
        loadProgettiAssegnati(); // Carica i dati all'avvio
    }

    public void initializeComponents() {
        String[] columnNames = {"ID Progetto", "Nome Progetto", "Stagione", "Data Inizio", "Stato", "Proprietario"};
        progettiTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        progettiAssegnatiTable = new JTable(progettiTableModel);
        
        vediAttivitaAssegnateButton = new JButton("Visualizza Mie Attività Assegnate");
    }

    public void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("UninaBioGarden: Progetti", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        mainPanel.add(new JScrollPane(progettiAssegnatiTable), BorderLayout.CENTER);
        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.add(vediAttivitaAssegnateButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        this.add(mainPanel);
    }
    
    public void addListeners() {
        vediAttivitaAssegnateButton.addActionListener(e -> {
            handleVediAttivitaAssegnate();
        });
    }
    
    private void loadProgettiAssegnati() {
        progettiTableModel.setRowCount(0);
        try {
            List<Progetto> progetti = controller.getProgettiByColtivatoreCodFisc(coltivatoreCorrente.getCod_fisc());
            
            if (progetti != null && !progetti.isEmpty()) {
                for (Progetto p : progetti) {
                    String statoProgetto;
                        if (p.getDataFine() == null) {
                            statoProgetto = "In Corso";
                        } else {
                            statoProgetto = "Finito";
                        }

                    progettiTableModel.addRow(new Object[]{
                        p.getIdProgetto(),
                        p.getNome(), 
                        p.getStagione(), 
                        p.getDataInizio(),
                        statoProgetto,
                        p.getProprietario() != null ? p.getProprietario().getNome() : "N/D"
                    });
                }
            } else {
                 System.out.println("Nessun progetto assegnato a questo coltivatore.");
            }
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(this, "Errore DB: Impossibile caricare i progetti.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
        }

    public void handleVediAttivitaAssegnate() {
        int selectedRow = progettiAssegnatiTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un progetto per visualizzare le attività assegnate.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idProgetto = (String) progettiTableModel.getValueAt(selectedRow, 0);

        try {
            Progetto progettoSelezionato = controller.getProgettoById(idProgetto);
            System.out.println(progettoSelezionato);
            if (progettoSelezionato != null) {
                AttivitaColtivatoreView attivitaView = new AttivitaColtivatoreView(this, controller, coltivatoreCorrente, progettoSelezionato);
                
                this.setVisible(false);
                attivitaView.setVisible(true);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore DB: Impossibile recuperare il progetto selezionato.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

}