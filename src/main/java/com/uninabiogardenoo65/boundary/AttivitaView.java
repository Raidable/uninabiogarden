package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Progetto;
import com.uninabiogardenoo65.entity.Attivita;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AttivitaView extends JFrame {

    private final JFrame parentView;
    private final Controller controller;
    private final Progetto progetto;
    
    private JTable attivitaTable;
    private DefaultTableModel attivitaTableModel;
    
    private JButton creaAttivitaButton;
    private JButton eliminaAttivitaButton;
    private JButton modificaAttivitaButton;
    private JButton indietroButton;

    public AttivitaView(Controller controller, Progetto progetto, JFrame parentView) {
        this.controller = controller;
        this.progetto = progetto;
        this.parentView = parentView;
        
        setTitle("Gestione Attività - " + progetto.getNome());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        
        initializeComponents();
        layoutComponents();
        addListeners();
        
        loadAttivitaData();
        
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }

    public void initializeComponents() {
        String[] attivitaColumns = {"ID", "Nome Attività", "Data Inizio", "Data Fine", "Stato", "Priorità", "Costo", "Coltura", "Coltivatore"};
        attivitaTableModel = new DefaultTableModel(attivitaColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        attivitaTable = new JTable(attivitaTableModel);
        
        attivitaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
    
        creaAttivitaButton = new JButton("Crea Nuova Attività");
        eliminaAttivitaButton = new JButton("Elimina Attività");
        modificaAttivitaButton = new JButton("Modifica Attività");
        indietroButton = new JButton("Indietro");
        
        creaAttivitaButton.setBackground(new Color(60, 179, 113));
        creaAttivitaButton.setForeground(Color.WHITE);
        eliminaAttivitaButton.setBackground(new Color(247, 100, 79));
        eliminaAttivitaButton.setForeground(Color.WHITE);
        modificaAttivitaButton.setBackground(new Color(79, 134, 247));
        modificaAttivitaButton.setForeground(Color.WHITE);
        indietroButton.setBackground(new Color(169, 169, 169));
        indietroButton.setForeground(Color.WHITE);
        
        attivitaTable.setRowHeight(25);
        attivitaTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        hideColumn(attivitaTable, 0);
    }

    public void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Gestione Attività - Progetto: " + progetto.getNome(), SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        JLabel tableLabel = new JLabel("Attività del Progetto", SwingConstants.LEFT);
        tableLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        tablePanel.add(tableLabel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(attivitaTable), BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(indietroButton);
        buttonPanel.add(eliminaAttivitaButton);
        buttonPanel.add(modificaAttivitaButton);
        buttonPanel.add(creaAttivitaButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    public void addListeners() {
        creaAttivitaButton.addActionListener(e -> handleCreaAttivita());
        eliminaAttivitaButton.addActionListener(e -> handleEliminaAttivita());
        modificaAttivitaButton.addActionListener(e -> handleModificaAttivita());
        indietroButton.addActionListener(e -> handleIndietro());
        
        attivitaTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = attivitaTable.getSelectedRow() != -1;
                eliminaAttivitaButton.setEnabled(hasSelection);
                modificaAttivitaButton.setEnabled(hasSelection);
            }
        });
        
        eliminaAttivitaButton.setEnabled(false);
        modificaAttivitaButton.setEnabled(false);
    }

    public void handleIndietro(){
        this.dispose(); 
        if (this.parentView != null) {
            parentView.setVisible(true);
        }

    }

    public void loadAttivitaData() {
        attivitaTableModel.setRowCount(0);
        try {
            List<Attivita> attivita = controller.getAttivitaByProgetto(progetto);
            if (attivita != null) {

                for (Attivita a : attivita) {
                    String dataFineTestoCasella;
                    if (a.getDataFine() == null) {
                        dataFineTestoCasella = "-";
                    } else {
                        dataFineTestoCasella = a.getDataFine().toString();
                    }



                    attivitaTableModel.addRow(new Object[]{
                        a.getIdAttivita(),
                        a.getNome(),
                        a.getDataInizio(),
                        dataFineTestoCasella,
                        a.getStato(),
                        a.getPriorita(),
                        a.getCosto(),
                        a.getColtura(),
                        a.getColtivatore() != null ? a.getColtivatore().getNome() + " " + a.getColtivatore().getCognome() : "N/A"
                    });
                }
            } else {
                System.out.println("Nessuna attività trovata per il progetto: " + progetto.getNome());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Errore nel caricamento delle attività: " + e.getMessage(), 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleCreaAttivita() {
        CreaAttivitaView creaAttivitaView = new CreaAttivitaView(this.controller, this.progetto, this);

        creaAttivitaView.setVisible(true);

        loadAttivitaData();

    }

    public void handleEliminaAttivita() {
        int selectedRow = attivitaTable.getSelectedRow();
        if (selectedRow != -1) {
            String idAttivita = (String) attivitaTableModel.getValueAt(selectedRow, 0);
            String nomeAttivita = (String) attivitaTableModel.getValueAt(selectedRow, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare l'attività '" + nomeAttivita + "'?\n" +
                "Questa operazione non può essere annullata.",
                "Conferma Eliminazione",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = controller.deleteAttivita(idAttivita);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(this,
                            "Attività eliminata con successo!",
                            "Operazione Completata",
                            JOptionPane.INFORMATION_MESSAGE);
                        loadAttivitaData();
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Errore durante l'eliminazione dell'attività.",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this,
                        "Errore database durante l'eliminazione: " + e.getMessage(),
                        "Errore DB",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void handleModificaAttivita() {
        int selectedRow = attivitaTable.getSelectedRow();
        if (selectedRow != -1) {
            String idAttivita = (String) attivitaTableModel.getValueAt(selectedRow, 0);
            try {
                Attivita attivita = controller.getAttivitaById(idAttivita);
                if (attivita != null) {
                    ModificaAttivitaView modificaView = new ModificaAttivitaView(this, this.controller, attivita);
                    
                    modificaView.setVisible(true);
                    loadAttivitaData();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Errore nel recupero dell'attività: " + e.getMessage(),
                    "Errore DB",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void hideColumn(JTable table, int index) {
        table.getColumnModel().getColumn(index).setMaxWidth(0);
        table.getColumnModel().getColumn(index).setMinWidth(0);
        table.getColumnModel().getColumn(index).setPreferredWidth(0);
    }
}