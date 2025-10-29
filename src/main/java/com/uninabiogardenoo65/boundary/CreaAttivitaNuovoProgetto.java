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

public class CreaAttivitaNuovoProgetto extends JFrame {

    private final Controller controller;
    private final Progetto progettoCorrente; 
    private final AssegnaAttivitaInizialiView parentView;
    private final DashboardProprietarioView dashboardProprietarioView;

    private static final String[] TIPI_ATTIVITA = {"Raccolta", "Irrigazione", "Seminazione"};
    private JComboBox<String> nomeComboBox;
    private JTextField dataInizioField; 
    private JTextField costoField;
    private JComboBox<Priorita> prioritaComboBox;
    private JComboBox<Coltura> colturaComboBox;
    private JComboBox<Coltivatore> coltivatoreComboBox;
    
    private JButton saveButton;
    private JButton cancelButton;

    public CreaAttivitaNuovoProgetto(Controller controller, Progetto progetto, AssegnaAttivitaInizialiView parentView, DashboardProprietarioView dashboardProprietarioView) {
        this.controller = controller;
        this.progettoCorrente = progetto;
        this.dashboardProprietarioView = dashboardProprietarioView; 
        this.parentView = parentView;
        
        setTitle("Crea Nuova Attività per Progetto: " + progetto.getNome());
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
        
        prioritaComboBox = new JComboBox<>(Priorita.values());
        
        colturaComboBox = new JComboBox<>();
        coltivatoreComboBox = new JComboBox<>();
        
        saveButton = new JButton("Salva Attività");
        cancelButton = new JButton("Annulla");
        
        saveButton.setBackground(new Color(60, 179, 113));
        saveButton.setForeground(Color.WHITE);
    }
    
    private void loadFKData() {
        colturaComboBox.removeAllItems();
        coltivatoreComboBox.removeAllItems();

        try {
            List<Coltivatore> coltivatori = controller.getColtivatoriByProgetto(progettoCorrente.getIdProgetto()); 
            for (Coltivatore c : coltivatori) {
                coltivatoreComboBox.addItem(c);
            }
            
            List<Coltura> colture = controller.getColtureByLottoId(progettoCorrente.getLotto().getIdLotto()); 
            for (Coltura c : colture) {
                colturaComboBox.addItem(c);
            }
            
            if (colturaComboBox.getItemCount() == 0 || coltivatoreComboBox.getItemCount() == 0) {
                 JOptionPane.showMessageDialog(this, "Non ci sono colture o coltivatori assegnabili a questo progetto.", "Dati Mancanti", JOptionPane.WARNING_MESSAGE);
                 saveButton.setEnabled(false);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore caricamento dati: " + e.getMessage(), "Errore DB", JOptionPane.ERROR_MESSAGE);
            saveButton.setEnabled(false);
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
                
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Tipo Attività:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0; formPanel.add(nomeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Data Inizio (AAAA-MM-GG):"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0; formPanel.add(dataInizioField, gbc);

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Costo (€):"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0; formPanel.add(costoField, gbc);

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Priorità:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0; formPanel.add(prioritaComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; formPanel.add(new JLabel("Coltura Target:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0; formPanel.add(colturaComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Coltivatore Assegnato:"), gbc);
        gbc.gridx = 1; gbc.gridy = row++; gbc.weightx = 1.0; formPanel.add(coltivatoreComboBox, gbc);

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

            
            JOptionPane.showMessageDialog(this, "Attività salvata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            parentView.setVisible(true); 
            this.dispose();
            dashboardProprietarioView.loadProgettiData();

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