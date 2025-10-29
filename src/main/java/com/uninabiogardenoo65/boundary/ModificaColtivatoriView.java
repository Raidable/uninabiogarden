package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Progetto;
import com.uninabiogardenoo65.entity.Proprietario;
import com.uninabiogardenoo65.entity.Coltivatore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ModificaColtivatoriView extends JFrame {

    private final Controller controller;
    private final Progetto progetto;
    private final Proprietario proprietario;

    private JTable coltivatoriAssegnatiTable;
    private DefaultTableModel coltivatoriAssegnatiModel;
    private JTable coltivatoriDisponibiliTable;
    private DefaultTableModel coltivatoriDisponibiliModel;
    
    private JButton rimuoviButton;
    private JButton aggiungiButton;
    private JButton indietroButton;

    public ModificaColtivatoriView(Controller controller, Progetto progetto, Proprietario proprietario) {
        this.controller = controller;
        this.progetto = progetto;
        this.proprietario = proprietario;
        
        setTitle("Gestione Coltivatori - " + progetto.getNome());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
        
        initializeComponents();
        layoutComponents();
        addListeners();
        
        loadColtivatoriData();
        
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        String[] assegnatiColumns = {"ID", "Nome", "Cognome", "Email"};
        coltivatoriAssegnatiModel = new DefaultTableModel(assegnatiColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        coltivatoriAssegnatiTable = new JTable(coltivatoriAssegnatiModel);
        
        String[] disponibiliColumns = {"ID", "Nome", "Cognome", "Email"};
        coltivatoriDisponibiliModel = new DefaultTableModel(disponibiliColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        coltivatoriDisponibiliTable = new JTable(coltivatoriDisponibiliModel);
        
        rimuoviButton = new JButton("Rimuovi Selezionato");
        aggiungiButton = new JButton("Aggiungi Selezionato");
        indietroButton = new JButton("Indietro");
        
        rimuoviButton.setBackground(new Color(247, 100, 79));
        rimuoviButton.setForeground(Color.WHITE);
        aggiungiButton.setBackground(new Color(60, 179, 113));
        aggiungiButton.setForeground(Color.WHITE);
        indietroButton.setBackground(new Color(169, 169, 169));
        indietroButton.setForeground(Color.WHITE);
        
        coltivatoriAssegnatiTable.setRowHeight(25);
        coltivatoriDisponibiliTable.setRowHeight(25);
        coltivatoriAssegnatiTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        coltivatoriDisponibiliTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        hideColumn(coltivatoriAssegnatiTable, 0);
        hideColumn(coltivatoriDisponibiliTable, 0);
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Gestione Coltivatori - Progetto: " + progetto.getNome(), SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        
        JPanel assegnatiPanel = new JPanel(new BorderLayout(10, 10));
        JLabel assegnatiLabel = new JLabel("Coltivatori Assegnati", SwingConstants.LEFT);
        assegnatiLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        assegnatiPanel.add(assegnatiLabel, BorderLayout.NORTH);
        assegnatiPanel.add(new JScrollPane(coltivatoriAssegnatiTable), BorderLayout.CENTER);
        
        JPanel disponibiliPanel = new JPanel(new BorderLayout(10, 10));
        JLabel disponibiliLabel = new JLabel("Coltivatori Disponibili", SwingConstants.LEFT);
        disponibiliLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        disponibiliPanel.add(disponibiliLabel, BorderLayout.NORTH);
        disponibiliPanel.add(new JScrollPane(coltivatoriDisponibiliTable), BorderLayout.CENTER);
        
        centerPanel.add(assegnatiPanel);
        centerPanel.add(disponibiliPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel transferPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        transferPanel.add(aggiungiButton);
        transferPanel.add(rimuoviButton);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.add(indietroButton);
        
        southPanel.add(transferPanel, BorderLayout.NORTH);
        southPanel.add(actionPanel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    private void addListeners() {
        rimuoviButton.addActionListener(e -> handleRimuoviColtivatore());
        aggiungiButton.addActionListener(e -> handleAggiungiColtivatore());
        indietroButton.addActionListener(e -> tornaIndietro());
        
        coltivatoriAssegnatiTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                rimuoviButton.setEnabled(coltivatoriAssegnatiTable.getSelectedRow() != -1);
            }
        });
        
        coltivatoriDisponibiliTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                aggiungiButton.setEnabled(coltivatoriDisponibiliTable.getSelectedRow() != -1);
            }
        });
        
        rimuoviButton.setEnabled(false);
        aggiungiButton.setEnabled(false);
    }


    private void tornaIndietro() {
        DashboardProprietarioView dashboard = new DashboardProprietarioView(controller, proprietario);
            dashboard.setVisible(true);
        
        this.dispose();
    }


    private void loadColtivatoriData() {
        coltivatoriAssegnatiModel.setRowCount(0);
        try {
            List<Coltivatore> assegnati = controller.getColtivatoriByProgetto(progetto.getIdProgetto());
            System.out.println(assegnati);
            if (assegnati != null) {
                for (Coltivatore c : assegnati) {
                    coltivatoriAssegnatiModel.addRow(new Object[]{
                        c.getCod_fisc(),
                        c.getNome(),
                        c.getCognome(),
                        c.getEmail()
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dei coltivatori assegnati", "Errore", JOptionPane.ERROR_MESSAGE);
        }

        coltivatoriDisponibiliModel.setRowCount(0);
        try {
            List<Coltivatore> disponibili = controller.getColtivatoriDisponibili(progetto.getIdProgetto());
            if (disponibili != null) {
                for (Coltivatore c : disponibili) {
                    coltivatoriDisponibiliModel.addRow(new Object[]{
                        c.getCod_fisc(),
                        c.getNome(),
                        c.getCognome(),
                        c.getEmail()
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dei coltivatori disponibili", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRimuoviColtivatore() {
        int selectedRow = coltivatoriAssegnatiTable.getSelectedRow();
        if (selectedRow != -1) {
            String idColtivatore = (String) coltivatoriAssegnatiModel.getValueAt(selectedRow, 0);
            String nome = (String) coltivatoriAssegnatiModel.getValueAt(selectedRow, 1);
            String cognome = (String) coltivatoriAssegnatiModel.getValueAt(selectedRow, 2);

            System.out.println("Rimuovi coltivatore ID: " + idColtivatore);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Rimuovere " + nome + " " + cognome + " dal progetto?",
                "Conferma Rimozione",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (controller.removeColtivatoreFromProgetto(idColtivatore, progetto.getIdProgetto())){
                    
                    Object[] rowData = new Object[4];
                    for (int i = 0; i < 4; i++) {
                        rowData[i] = coltivatoriAssegnatiModel.getValueAt(selectedRow, i);
                    }
                    coltivatoriAssegnatiModel.removeRow(selectedRow);
                    coltivatoriDisponibiliModel.addRow(rowData);
                    }
                } catch (SQLException error) {
                    JOptionPane.showMessageDialog(this, "Errore durante la rimozione del coltivatore: " + error.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void handleAggiungiColtivatore() {
        int selectedRow = coltivatoriDisponibiliTable.getSelectedRow();
        if (selectedRow != -1) {
            String idColtivatore = (String) coltivatoriDisponibiliModel.getValueAt(selectedRow, 0);
            String nome = (String) coltivatoriDisponibiliModel.getValueAt(selectedRow, 1);
            String cognome = (String) coltivatoriDisponibiliModel.getValueAt(selectedRow, 2);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Aggiungere " + nome + " " + cognome + " al progetto?",
                "Conferma Aggiunta",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (controller.addColtivatoreToProgetto(idColtivatore, progetto.getIdProgetto())){
                        Object[] rowData = new Object[4];
                        for (int i = 0; i < 4; i++) {
                            rowData[i] = coltivatoriDisponibiliModel.getValueAt(selectedRow, i);
                        }
                        coltivatoriDisponibiliModel.removeRow(selectedRow);
                        coltivatoriAssegnatiModel.addRow(rowData);
                    }
                } catch (SQLException error) {
                    JOptionPane.showMessageDialog(this, "Errore durante l'aggiunta del coltivatore: " + error.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void hideColumn(JTable table, int index) {
        table.getColumnModel().getColumn(index).setMaxWidth(0);
        table.getColumnModel().getColumn(index).setMinWidth(0);
        table.getColumnModel().getColumn(index).setPreferredWidth(0);
    }
}