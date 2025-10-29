package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Attivita;
import com.uninabiogardenoo65.entity.Coltivatore;
import com.uninabiogardenoo65.entity.Coltura;
import com.uninabiogardenoo65.entity.Progetto;
import com.uninabiogardenoo65.enums.Priorita;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CreaAttivitaView extends JFrame {

    private final Controller controller;
    private final Progetto progettoCorrente; 
    
    private static final String[] TIPI_ATTIVITA = {"Raccolta", "Irrigazione", "Seminazione"};
    private JComboBox<String> nomeComboBox;
    private JTextField dataInizioField; 
    private JTextField costoField;
    private JList<Coltivatore> coltivatoreList;
    private DefaultListModel<Coltivatore> coltivatoreListModel;
    private JComboBox<Priorita> prioritaComboBox;
    private JComboBox<Coltura> colturaComboBox;
    private JComboBox<Coltivatore> coltivatoreComboBox;
    private final AttivitaView parentView;

    private JButton saveButton;
    private JButton cancelButton;

    public CreaAttivitaView(Controller controller, Progetto progetto, AttivitaView parent) {
        this.controller = controller;
        this.progettoCorrente = progetto;
        this.parentView = parent;
        
        setTitle("Crea Nuova Attività per " + progetto.getNome());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        initializeComponents();
        loadFKData();
        layoutComponents();
        addListeners();
        
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        nomeComboBox = new JComboBox<>(TIPI_ATTIVITA);        
        dataInizioField = new JTextField(LocalDate.now().toString(), 25); 
        costoField = new JTextField("0.00", 25);
        coltivatoreListModel = new DefaultListModel<>();
        coltivatoreList = new JList<>(coltivatoreListModel);
        coltivatoreList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        prioritaComboBox = new JComboBox<>(Priorita.values());
        
        colturaComboBox = new JComboBox<>();
        coltivatoreComboBox = new JComboBox<>();
        
        saveButton = new JButton("Salva Attività");
        cancelButton = new JButton("Annulla");
    }
    
    private void loadFKData() {
        try {
            
            List<Coltivatore> coltivatori = controller.getColtivatoriByProgetto(progettoCorrente.getIdProgetto()); 
            for (Coltivatore c : coltivatori) {
                coltivatoreComboBox.addItem(c);
            }
            
            List<Coltura> colture = controller.getColtureByLottoId(progettoCorrente.getLotto().getIdLotto()); 
            for (Coltura c : colture) {
                colturaComboBox.addItem(c);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento dati: " + e.getMessage(), "Errore DB", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Nuova Attività per Lotto: " + progettoCorrente.getLotto().getNome());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Tipo Attività:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; formPanel.add(nomeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Data Inizio (AAAA-MM-GG):"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; formPanel.add(dataInizioField, gbc);

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Costo (€):"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; formPanel.add(costoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Priorità:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; formPanel.add(prioritaComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Coltura Target:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; formPanel.add(colturaComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Coltivatore Assegnato:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; formPanel.add(coltivatoreComboBox, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        this.add(mainPanel);
    }

    private void addListeners() {
        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> handleSaveAttivita());
    }

    private void handleSaveAttivita() {
        try {
            String nome = (String) nomeComboBox.getSelectedItem();            
            LocalDate dataInizio = LocalDate.parse(dataInizioField.getText().trim());
            double costo = Double.parseDouble(costoField.getText().trim());

            Priorita priorita = (Priorita) prioritaComboBox.getSelectedItem();
            
            Coltura colturaSelezionata = (Coltura) colturaComboBox.getSelectedItem();
            Coltivatore coltivatoreSelezionato = (Coltivatore) coltivatoreComboBox.getSelectedItem();
            
            LocalDate dataOdierna = LocalDate.now();
            
            if (dataInizio.isBefore(dataOdierna)) {
                JOptionPane.showMessageDialog(this, 
                    "La Data Inizio non può essere precedente alla data odierna (" + dataOdierna.toString() + ").", 
                    "Violazione Temporale", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (colturaSelezionata == null || coltivatoreSelezionato == null) {
                 JOptionPane.showMessageDialog(this, "Seleziona Coltura e Coltivatore.", "Errore", JOptionPane.WARNING_MESSAGE);
                 return;
            }
            

            Attivita nuovaAttivita = new Attivita(
                nome,
                dataInizio,
                null, 
                priorita,
                costo,
                coltivatoreSelezionato,
                colturaSelezionata,
                progettoCorrente.getIdProgetto()
            );

            controller.createAttivita(nuovaAttivita);

            if (parentView != null) {
                parentView.loadAttivitaData();
            }
            
            JOptionPane.showMessageDialog(this, "Attività salvata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();


        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato data non valido. Usa AAAA-MM-GG.", "Errore Input", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Costo non valido. Inserisci un numero.", "Errore Input", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(this, "Errore DB: Impossibile salvare l'attività. " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore sconosciuto: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}