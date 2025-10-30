package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Proprietario;
import com.uninabiogardenoo65.entity.Progetto;
import com.uninabiogardenoo65.entity.Lotto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DashboardProprietarioView extends JFrame {

    private final Controller controller;
    private final Proprietario proprietarioCorrente; 
    
    private JTabbedPane tabbedPane;
    private JComboBox<Lotto> lottoFilterComboBox;
    private final Lotto ALL_LOTS_FILTER = new Lotto(null, "TUTTI I LOTTI", 0.0f, null, null);

    private JTable progettiTable;
    private DefaultTableModel progettiTableModel;
    private JButton creaProgettoButton;
    private JButton reportButton; 

    private JTable lottiTable;
    private DefaultTableModel lottiTableModel;

    public DashboardProprietarioView(Controller controller, Proprietario proprietario) {
        this.controller = controller;
        this.proprietarioCorrente = proprietario;
        
        setTitle("Dashboard - " + proprietario.getNome() + " " + proprietario.getCognome());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        initializeComponents();
        layoutComponents();
        addListeners();
        
        loadProgettiData(); 
        loadLottiData();
        
        setSize(1000, 700); 
        setLocationRelativeTo(null);
    }

    public void initializeComponents() {
        String[] progettiColumnNames = {"ID Progetto", "Nome Progetto", "Stagione", "Data Inizio", "Stato", "Lotto", "Azioni"};
        progettiTableModel = new DefaultTableModel(progettiColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 6 ? JButton.class : Object.class;
            }
        };
        progettiTable = new JTable(progettiTableModel);
        
        String[] lottiColumnNames = {"ID Lotto", "Nome Lotto", "Superficie (m²)"};
        lottiTableModel = new DefaultTableModel(lottiColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lottiTable = new JTable(lottiTableModel);
        
        lottoFilterComboBox = new JComboBox<>();

        creaProgettoButton = new JButton("Crea Nuovo Progetto");
        reportButton = new JButton("Genera Report Raccolto");
        
        
        creaProgettoButton.setBackground(new Color(60, 179, 113)); 
        creaProgettoButton.setForeground(Color.WHITE);
        reportButton.setBackground(new Color(255, 165, 0)); 
        reportButton.setForeground(Color.WHITE);
        
        progettiTable.setRowHeight(25);
        lottiTable.setRowHeight(25);
        progettiTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        lottiTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        
        setupActionColumn();
        
        tabbedPane = new JTabbedPane();
    }

    public void setupActionColumn() {
        progettiTable.getColumnModel().getColumn(6).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JButton button = new JButton("Intervieni");
                return button;
            }
        });
        
        progettiTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JButton button = new JButton("Intervieni");
                button.setBackground(new Color(247, 100, 79));
                button.setForeground(Color.WHITE);
                
                button.addActionListener(e -> {
                    String idProgetto = (String) progettiTableModel.getValueAt(row, 0);
                    handleModificaProgetto(idProgetto, row);
                    
                    fireEditingStopped();
                });
                
                return button;
            }
        });
    }
public void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel welcomeLabel = new JLabel(" Dashboard di " + proprietarioCorrente.getNome() + " | UninaBioGarden", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JPanel centeredTitlePanel = new JPanel(new BorderLayout());
        centeredTitlePanel.setOpaque(false);
        centeredTitlePanel.add(welcomeLabel, BorderLayout.CENTER);
        centeredTitlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, centeredTitlePanel.getPreferredSize().height)); 


        JPanel filterContainerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        filterContainerPanel.add(new JLabel("Filtra Progetti per Lotto: "));
        filterContainerPanel.add(lottoFilterComboBox);
        filterContainerPanel.setOpaque(false);
        filterContainerPanel.setBackground(null);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(null); 
        toolBar.setOpaque(false);
        toolBar.setBorder(null);
        
        toolBar.add(Box.createHorizontalGlue()); 
        toolBar.add(creaProgettoButton);
        toolBar.addSeparator(new Dimension(20, 0)); 
        toolBar.add(reportButton);

        JPanel filterAndActionRow = new JPanel(new BorderLayout());
        filterAndActionRow.setOpaque(false);
        filterAndActionRow.add(filterContainerPanel, BorderLayout.WEST); 
        filterAndActionRow.add(toolBar, BorderLayout.EAST);            
        filterAndActionRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterAndActionRow.getPreferredSize().height));


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS)); 
        topPanel.setBackground(null); 
        topPanel.setOpaque(false); 
        
        topPanel.add(centeredTitlePanel); 
        topPanel.add(filterAndActionRow); 
        
        mainPanel.add(topPanel, BorderLayout.NORTH);

        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabbedPane.addTab("Progetti di Coltivazione", createProgettiPanel());
        tabbedPane.addTab("Lotti di Terreno", createLottiPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        

        this.add(mainPanel);
        
        hideColumn(progettiTable, 0);
        hideColumn(lottiTable, 0);
    }

    public void hideColumn(JTable table, int index) {
        table.getColumnModel().getColumn(index).setMaxWidth(0);
        table.getColumnModel().getColumn(index).setMinWidth(0);
        table.getColumnModel().getColumn(index).setPreferredWidth(0);
    }
    
    
    public JPanel createProgettiPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JScrollPane(progettiTable), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    public JPanel createLottiPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JScrollPane(lottiTable), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    public void addListeners() {
        creaProgettoButton.addActionListener(e -> handleCreaProgetto());
        reportButton.addActionListener(e -> handleGeneraReport());
        lottoFilterComboBox.addActionListener(e -> loadProgettiData());
        
    }
    

    public void handleGeneraReport() {
            int selectedRow = progettiTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleziona un progetto per generare il report del suo lotto.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String idProgetto = (String) progettiTableModel.getValueAt(selectedRow, 0);

            try {
                Progetto progettoSelezionato = controller.getProgettoById(idProgetto);

                if (progettoSelezionato != null && progettoSelezionato.getLotto() != null) {
                    Lotto lottoDelProgetto = progettoSelezionato.getLotto();
                    
                    ReportView reportDialog = new ReportView(this, controller, lottoDelProgetto);
                    reportDialog.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(this, "Impossibile recuperare i dettagli del lotto per questo progetto.", "Errore Dati", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Errore DB nel recupero del progetto: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }

    public void loadLottiData() {
        lottiTableModel.setRowCount(0); 
        lottoFilterComboBox.removeAllItems(); 

        try {
            List<Lotto> lotti = controller.getLottiByProprietario(proprietarioCorrente); 
            
            lottoFilterComboBox.addItem(ALL_LOTS_FILTER); 
            
            if (lotti != null && !lotti.isEmpty()) {
                for (Lotto l : lotti) {
                    lottiTableModel.addRow(new Object[]{ l.getIdLotto(), l.getNome(), l.getSuperficie() });
                    lottoFilterComboBox.addItem(l); 
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore DB: Impossibile caricare i lotti.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void loadProgettiData() {
            progettiTableModel.setRowCount(0); 
            
            Lotto selectedLotto = (Lotto) lottoFilterComboBox.getSelectedItem();

            try {
                List<Progetto> progetti;
                
                final Lotto ALL_LOTS_FILTER = new Lotto(null, "TUTTI I LOTTI", 0.0f, null, null); 
                
                if (selectedLotto == null || selectedLotto.getIdLotto() == null || selectedLotto.equals(ALL_LOTS_FILTER)) {
                    progetti = controller.getProgettiByProprietario(proprietarioCorrente);
                } else {
                    String idLottoDaFiltrare = selectedLotto.getIdLotto();
                    
                    progetti = controller.getProgettiByLotto(idLottoDaFiltrare); 
                }
                
                if (progetti != null && !progetti.isEmpty()) {
                    for (Progetto p : progetti) {
                        progettiTableModel.addRow(new Object[]{
                            p.getIdProgetto(), 
                            p.getNome(), 
                            p.getStagione(), 
                            p.getDataInizio(),
                            p.getDataFine() == null ? "In Corso" : "Finito",
                            p.getLotto() != null ? p.getLotto().getNome() : "N/D",
                            "Modifica"
                        });
                    }
                } else if (selectedLotto != null && !selectedLotto.equals(ALL_LOTS_FILTER)) {
                    JOptionPane.showMessageDialog(this, 
                        "Nessun progetto trovato per il lotto: " + selectedLotto.getNome(), 
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Errore DB: Impossibile caricare i progetti.", "Errore", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        
     

    public void handleModificaProgetto(String idProgetto, int row) {
        try {
            Progetto progettoSelezionato = controller.getProgettoById(idProgetto);
            
            JPopupMenu menuModifica = new JPopupMenu();
            
            JButton modificaAttivitaBtn = new JButton("Modifica Attività");
            JButton modificaColtivatoriBtn = new JButton("Modifica Coltivatori");
            JButton chiudiProgettoBtn = new JButton("Chiudi Progetto");
            
            modificaAttivitaBtn.setBorderPainted(false);
            modificaAttivitaBtn.setFocusPainted(false);
            
            modificaColtivatoriBtn.setBorderPainted(false);
            modificaColtivatoriBtn.setFocusPainted(false);
            
            chiudiProgettoBtn.setBorderPainted(false);
            chiudiProgettoBtn.setFocusPainted(false);
            
            Dimension buttonSize = new Dimension(180, 35);
            modificaAttivitaBtn.setPreferredSize(buttonSize);
            modificaColtivatoriBtn.setPreferredSize(buttonSize);
            chiudiProgettoBtn.setPreferredSize(buttonSize);
            
            modificaAttivitaBtn.addActionListener(e -> {
                menuModifica.setVisible(false);
                handleModificaAttivita(progettoSelezionato);
            });
            
            modificaColtivatoriBtn.addActionListener(e -> {
                menuModifica.setVisible(false);
                handleModificaColtivatori(progettoSelezionato);
            });
            
            chiudiProgettoBtn.addActionListener(e -> {
                menuModifica.setVisible(false);
                handleChiudiProgetto(progettoSelezionato, row);
            });
            
            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
            menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            menuPanel.add(modificaAttivitaBtn);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            menuPanel.add(modificaColtivatoriBtn);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            
            if (!progettoSelezionato.isChiuso()) {
                menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                menuPanel.add(chiudiProgettoBtn);
            }
            
            menuModifica.add(menuPanel);
            
            JTable table = progettiTable;
            Rectangle cellRect = table.getCellRect(row, 6, true);
            menuModifica.show(table, cellRect.x, cellRect.y + cellRect.height);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Errore nel recupero del progetto: " + e.getMessage(), 
                "Errore DB", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleModificaAttivita(Progetto progetto) {
        AttivitaView dashboard = new AttivitaView(controller, progetto, this);
            dashboard.setVisible(true);
        
        this.setVisible(false);
        
    }

    public void handleModificaColtivatori(Progetto progetto) {
       ModificaColtivatoriView dashboard = new ModificaColtivatoriView(controller, progetto, proprietarioCorrente);
            dashboard.setVisible(true);
            
        this.setVisible(false);

    }

    public void handleCreaProgetto() {
        CreaProgettoView creaProgettoView = new CreaProgettoView(this, controller, proprietarioCorrente);
        creaProgettoView.setVisible(true);
        
        
    }


    public void handleChiudiProgetto(Progetto progetto, int row) {
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Sei sicuro di voler chiudere il progetto '" + progetto.getNome() + "'?\n" +
            "Questa operazione imposterà la Data Fine ad oggi.",
            "Conferma Chiusura Progetto",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = controller.chiudiProgetto(progetto.getIdProgetto());
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Progetto chiuso e attività associate sospese con successo!",
                        "Operazione Completata",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    loadProgettiData();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Errore: Il progetto non è stato aggiornato. Potrebbe essere già stato chiuso.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                }
            
            
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Errore inaspettato: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}