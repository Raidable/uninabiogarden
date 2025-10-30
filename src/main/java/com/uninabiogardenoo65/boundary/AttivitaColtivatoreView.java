package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Attivita;
import com.uninabiogardenoo65.entity.Coltivatore;
import com.uninabiogardenoo65.entity.Progetto;
import com.uninabiogardenoo65.enums.Stato; 
import com.uninabiogardenoo65.enums.Priorita; 

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AttivitaColtivatoreView extends JFrame {

    private final JFrame parentFrame;
    private final Controller controller;
    private final Coltivatore coltivatoreCorrente; 
    
    private JTable attivitaTable;
    private DefaultTableModel attivitaTableModel;
    private JButton indietroButton;
    private JButton aggiornaStatoButton;
    private final Progetto progettoCorrente;
    

    public AttivitaColtivatoreView(JFrame parent, Controller controller, Coltivatore coltivatore, Progetto progetto) {

        this.parentFrame = parent;
        this.controller = controller;
        this.coltivatoreCorrente = coltivatore;
        this.progettoCorrente = progetto; 

        setTitle("Attività Assegnate - " + coltivatore.getNome());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        
        initializeComponents();
        layoutComponents();
        addListeners();
        
        loadAttivitaData();
        hideColumn(attivitaTable, 0);

    }

    private void initializeComponents() {
        String[] attivitaColumns = {"ID Attività", "Nome Attività", "Progetto", "Coltura", "Stato", "Priorità", "Data Inizio", "Costo"};

        attivitaTableModel = new DefaultTableModel(attivitaColumns, 0) {
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Stato.class; 
                return super.getColumnClass(columnIndex);
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; 
            }
            
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                super.setValueAt(aValue, row, column); 

                if (column == 4) { 
                    String idAttivita = (String) getValueAt(row, 0); 
                    Stato nuovoStato = (Stato) aValue;
                    
                    try {
                        controller.updateStato(idAttivita, nuovoStato);
                        JOptionPane.showMessageDialog(null, "Stato attività aggiornato a " + nuovoStato.toString(), "Successo", JOptionPane.INFORMATION_MESSAGE);
                        
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Errore DB: Impossibile aggiornare lo stato.", "Errore", JOptionPane.ERROR_MESSAGE);
                        loadAttivitaData();
                    }
                }
            }
        };


        
        attivitaTable = new JTable(attivitaTableModel);
        attivitaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        attivitaTable.setRowHeight(25);
        
        JComboBox<Stato> statoEditor = new JComboBox<>(Stato.values());
        attivitaTable.setDefaultEditor(Stato.class, new DefaultCellEditor(statoEditor));
        
        indietroButton = new JButton("Indietro alla Dashboard");
        
        indietroButton.setBackground(new Color(169, 169, 169));
        indietroButton.setForeground(Color.WHITE);
    }

    public void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Le Mie Attività Assegnate", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        mainPanel.add(new JScrollPane(attivitaTable), BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.add(indietroButton);
        mainPanel.add(footer, BorderLayout.SOUTH);
        
        this.add(mainPanel);
        
        hideColumn(attivitaTable, 0); 
    }

    public void addListeners() {
        indietroButton.addActionListener(e -> {
            this.dispose(); 
            if (parentFrame != null) {
                parentFrame.setVisible(true); 
            }
        });
        
    }

    
    public void loadAttivitaData() {
        attivitaTableModel.setRowCount(0);
        try {
            List<Attivita> attivitaList = controller.getAttivitaColtivatoreByCodFisc(coltivatoreCorrente.getCod_fisc());
            
            if (attivitaList != null) {
                for (Attivita a : attivitaList) {
                    
                    String nomeColtura = a.getColtura() != null ? a.getColtura().getNome() : "N/D";

                    attivitaTableModel.addRow(new Object[]{
                        a.getIdAttivita(),
                        a.getNome(),
                        a.getIdProgetto(),
                        nomeColtura,
                        a.getStato(),
                        a.getPriorita(), 
                        a.getDataInizio(),
                        a.getCosto()
                    });
                }
            }
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(this, "Errore DB: Impossibile caricare le attività assegnate.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hideColumn(JTable table, int index) {
        table.getColumnModel().getColumn(index).setMaxWidth(0);
        table.getColumnModel().getColumn(index).setMinWidth(0);
        table.getColumnModel().getColumn(index).setPreferredWidth(0);
    }
}