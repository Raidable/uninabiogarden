package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Coltivatore;
import com.uninabiogardenoo65.entity.Coltura;
import com.uninabiogardenoo65.entity.Lotto;
import com.uninabiogardenoo65.entity.Proprietario;
import com.uninabiogardenoo65.entity.Progetto;
import com.uninabiogardenoo65.enums.Stagione;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CreaProgettoView extends JDialog {

    private final Controller controller;
    private final Proprietario proprietarioCorrente;
    private final DashboardProprietarioView parentView;
    
    private JTextField nomeField;
    private JTextField budgetField;
    private JTextField dataInizioField;
    private JComboBox<Stagione> stagioneComboBox;
    private JComboBox<Lotto> lottoComboBox;
    private JList<Coltura> colturaList;
    private DefaultListModel<Coltura> colturaListModel;
    private JList<Coltivatore> coltivatoreList; 
    private DefaultListModel<Coltivatore> coltivatoreListModel;

    private JButton saveButton;
    private JButton cancelButton;

    public CreaProgettoView(DashboardProprietarioView parent, Controller controller, Proprietario proprietario) {
        super(parent, "Crea Nuovo Progetto Stagionale", true); 
        this.controller = controller;
        this.proprietarioCorrente = proprietario;
        this.parentView = parent;
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initializeComponents();
        loadFKData();

        layoutComponents();
        addListeners();
        
        pack();
        setLocationRelativeTo(parent);
    }

    public void initializeComponents() {
        nomeField = new JTextField(20);
        budgetField = new JTextField("0.00", 20);
        dataInizioField = new JTextField(LocalDate.now().toString(), 20); 
        this.coltivatoreListModel = new DefaultListModel<>();
        
        this.coltivatoreList = new JList<>(coltivatoreListModel);
        this.coltivatoreList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        stagioneComboBox = new JComboBox<>(Stagione.values());
        lottoComboBox = new JComboBox<>();
        colturaListModel = new DefaultListModel<>();
        colturaList = new JList<>(colturaListModel);
        colturaList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        saveButton = new JButton("Crea Progetto");
        cancelButton = new JButton("Annulla");
        
        saveButton.setBackground(new Color(60, 179, 113));
        saveButton.setForeground(Color.WHITE);
    }

    public void loadFKData() {
        lottoComboBox.removeAllItems();
        coltivatoreListModel.clear(); 
        
        try {
            List<Lotto> lotti = controller.getLottiByProprietario(proprietarioCorrente);
            boolean lottoAggiunto = false;

            if (lotti != null && !lotti.isEmpty()) {
                for (Lotto l : lotti) {
                    if (l.getProgetto() == null) {
                        lottoComboBox.addItem(l);
                        lottoAggiunto = true;
                    }
                }
            } 
            
            List<Coltivatore> coltivatori = controller.getAllColtivatori(); 
            if (coltivatori != null && !coltivatori.isEmpty()) {
                for (Coltivatore c : coltivatori) {
                    coltivatoreListModel.addElement(c); 
                }
            }
            
            if (lottoComboBox.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "Nessun lotto disponibile.", "Lotti Mancanti", JOptionPane.WARNING_MESSAGE);
                saveButton.setEnabled(false);
            }

            if (lottoAggiunto && lottoComboBox.getSelectedItem() != null) {
                loadColtureBySelectedLotto();
            } else if (!lottoAggiunto) {
                 JOptionPane.showMessageDialog(this, "Nessun lotto disponibile. Impossibile creare il progetto.", "Lotti Mancanti", JOptionPane.WARNING_MESSAGE);
                 saveButton.setEnabled(false);
            }

            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore DB nel caricamento dati FK.", "Errore", JOptionPane.ERROR_MESSAGE);
            saveButton.setEnabled(false);
        }
    }
    public void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Nuovo Progetto - Dettagli Iniziali", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int row = 0;
        
        
        gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0; formPanel.add(new JLabel("Nome Progetto:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0; formPanel.add(nomeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Data Inizio (AAAA-MM-GG):"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0; formPanel.add(dataInizioField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Lotto Assegnato:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0; formPanel.add(lottoComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Stagione:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0; formPanel.add(stagioneComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Budget (€):"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0; formPanel.add(budgetField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.anchor = GridBagConstraints.NORTHWEST; 
        formPanel.add(new JLabel("Coltivatori Assegnati (Ctrl+Click):"), gbc);

        gbc.gridx = 1; gbc.gridy = row++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH; 

        JScrollPane scrollPane = new JScrollPane(coltivatoreList);
        
        scrollPane.setPreferredSize(new Dimension(200, 150));
        
        formPanel.add(scrollPane, gbc); 
        
        gbc.weighty = 0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = row; 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.anchor = GridBagConstraints.NORTHWEST; 
        formPanel.add(new JLabel("Colture da Includere (Seleziona dal Lotto):"), gbc);

        gbc.gridx = 1; gbc.gridy = row++;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH; 
        formPanel.add(new JScrollPane(colturaList), gbc);
        gbc.weighty = 0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;


        
        gbc.weighty = 0; 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        this.add(mainPanel);
    }

    
    public void addListeners() {
        cancelButton.addActionListener(e -> {
            dispose();
            parentView.setVisible(true); 
        });

        saveButton.addActionListener(e -> handleSaveProgetto());
        lottoComboBox.addActionListener(e -> loadColtureBySelectedLotto());
    }

    public void loadColtureBySelectedLotto() {
        colturaListModel.clear();
        Lotto lotto = (Lotto) lottoComboBox.getSelectedItem();
        
        if (lotto == null) return;
        
        try {
            List<Coltura> colture = controller.getColtureByLottoId(lotto.getIdLotto()); 
            
            if (colture != null && !colture.isEmpty()) {
                for (Coltura c : colture) {
                    colturaListModel.addElement(c);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento colture: " + e.getMessage(), "Errore DB", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void handleSaveProgetto() {
        try {
            String nome = nomeField.getText().trim();
            LocalDate dataInizio = LocalDate.parse(dataInizioField.getText().trim());
            double budget = Double.parseDouble(budgetField.getText().trim());
            
            Stagione stagione = (Stagione) stagioneComboBox.getSelectedItem();
            Lotto lottoSelezionato = (Lotto) lottoComboBox.getSelectedItem();
            
            List<Coltivatore> coltivatoriSelezionati = null;
            coltivatoriSelezionati = coltivatoreList.getSelectedValuesList();
            List<Coltura> coltureSelezionate = null;
            coltureSelezionate = colturaList.getSelectedValuesList();
            Progetto nuovoProgetto = null;

            if (nome.isEmpty() || lottoSelezionato == null) {
                JOptionPane.showMessageDialog(this, "Nome e Lotto sono obbligatori.", "Errore", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (coltureSelezionate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Devi selezionare almeno una coltura per il progetto.", "Errore", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (coltivatoriSelezionati.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Devi assegnare almeno un coltivatore al progetto.", "Errore", JOptionPane.WARNING_MESSAGE);
                return; 
            }

            nuovoProgetto = new Progetto(
                Controller.generaIdUnivoco(),
                nome,
                dataInizio,
                null,
                budget,
                stagione,
                lottoSelezionato,
                proprietarioCorrente 
            );

            controller.creaProgetto(nuovoProgetto, coltivatoriSelezionati); 

           
            this.dispose(); 
            
            AssegnaAttivitaInizialiView assegnaView = new AssegnaAttivitaInizialiView(
                parentView,
                controller, 
                nuovoProgetto, 
                coltureSelezionate
            );
            assegnaView.setVisible(true);
            

        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato data non valido. Usa AAAA-MM-GG.", "Errore Input", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Budget non valido. Inserisci un numero.", "Errore Input", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore DB: Impossibile salvare il progetto. " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore sconosciuto: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

}