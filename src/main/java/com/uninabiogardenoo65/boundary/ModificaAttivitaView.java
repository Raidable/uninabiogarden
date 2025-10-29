package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Attivita;
import com.uninabiogardenoo65.entity.Coltivatore;
import com.uninabiogardenoo65.entity.Coltura;
import com.uninabiogardenoo65.enums.Priorita;
import com.uninabiogardenoo65.enums.Qualita;
import com.uninabiogardenoo65.enums.Stato;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ModificaAttivitaView extends JDialog {

    private final Controller controller;
    private final Attivita attivitaOriginale; 
    private final AttivitaView parentView;

    private JTextField dataInizioField; 
    private JTextField dataFineField; 
    private JTextField costoField;
    private JComboBox<Stato> statoComboBox; 
    private JComboBox<Priorita> prioritaComboBox;
    private JComboBox<Coltura> colturaComboBox;
    private JComboBox<Coltivatore> coltivatoreComboBox;

    private JButton updateButton;
    private JButton cancelButton;

    public ModificaAttivitaView(AttivitaView parent, Controller controller, Attivita attivita) {
        super(parent, "Modifica Attività: " + attivita.getNome(), true); 
        
        this.controller = controller;
        this.attivitaOriginale = attivita;
        this.parentView = parent;
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        initializeComponents();
        loadFKData();
        layoutComponents();
        prefillForm();
        addListeners();
        
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        dataInizioField = new JTextField(25); 
        dataFineField = new JTextField(25); 
        costoField = new JTextField(25);
        
        statoComboBox = new JComboBox<>(Stato.values());
        prioritaComboBox = new JComboBox<>(Priorita.values());
        
        colturaComboBox = new JComboBox<>();
        coltivatoreComboBox = new JComboBox<>();
        
        updateButton = new JButton("Salva Modifiche");
        cancelButton = new JButton("Annulla");
        
        updateButton.setBackground(new Color(79, 134, 247));
        updateButton.setForeground(Color.WHITE);
    }
    
    private void loadFKData() {
        try {

            String idProgetto = attivitaOriginale.getIdProgetto();
            String idLotto = attivitaOriginale.getColtura().getIdLotto();
            List<Coltivatore> coltivatori = controller.getColtivatoriByProgetto(idProgetto); 
            for (Coltivatore c : coltivatori) {
                coltivatoreComboBox.addItem(c);
            }
            
            List<Coltura> colture = controller.getColtureByLottoId(idLotto); 
            for (Coltura c : colture) {
                colturaComboBox.addItem(c);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errore DB: Caricamento FK fallito.", "Errore DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prefillForm() {
        dataInizioField.setText(attivitaOriginale.getDataInizio().toString());
        costoField.setText(String.valueOf(attivitaOriginale.getCosto()));
        
        if (attivitaOriginale.getDataFine() != null) {
            dataFineField.setText(attivitaOriginale.getDataFine().toString());
        } else {
            dataFineField.setText(""); 
        }
        
        statoComboBox.setSelectedItem(attivitaOriginale.getStato());
        prioritaComboBox.setSelectedItem(attivitaOriginale.getPriorita());
        
        colturaComboBox.setSelectedItem(attivitaOriginale.getColtura());
        coltivatoreComboBox.setSelectedItem(attivitaOriginale.getColtivatore());
        
        dataInizioField.setEditable(false); 
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Modifica Attività: " + attivitaOriginale.getNome(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        row = addFieldRow(formPanel, gbc, row, "Data Inizio (Non Modificabile):", dataInizioField);
        row = addFieldRow(formPanel, gbc, row, "Data Fine (Opzionale):", dataFineField);
        row = addFieldRow(formPanel, gbc, row, "Costo (€):", costoField);
        row = addFieldRow(formPanel, gbc, row, "Stato Attività:", statoComboBox);
        row = addFieldRow(formPanel, gbc, row, "Priorità:", prioritaComboBox);
        row = addFieldRow(formPanel, gbc, row, "Coltura Target:", colturaComboBox);
        row = addFieldRow(formPanel, gbc, row, "Coltivatore Assegnato:", coltivatoreComboBox);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(cancelButton);
        buttonPanel.add(updateButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        this.add(mainPanel);
    }
    
    private int addFieldRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent component) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1.0;
        panel.add(component, gbc);
        return row + 1;
    }

    private void addListeners() {
        cancelButton.addActionListener(e -> dispose());
        updateButton.addActionListener(e -> handleUpdateAttivita());
    }

    private void handleUpdateAttivita() {
        try {
            LocalDate dataInizio = LocalDate.parse(dataInizioField.getText().trim());
            double costo = Double.parseDouble(costoField.getText().trim());
            
            Stato stato = (Stato) statoComboBox.getSelectedItem();
            Priorita priorita = (Priorita) prioritaComboBox.getSelectedItem();
            Coltura colturaSelezionata = (Coltura) colturaComboBox.getSelectedItem();
            Coltivatore coltivatoreSelezionato = (Coltivatore) coltivatoreComboBox.getSelectedItem();
            
            LocalDate dataFine = null; 
            String dataFineTesto = dataFineField.getText().trim();



            if (!dataFineTesto.isEmpty()) {
                dataFine = LocalDate.parse(dataFineTesto); 
                
                if (dataFine.isBefore(dataInizio)) {
                    JOptionPane.showMessageDialog(this, 
                        "La Data di Fine non può essere precedente alla Data di Inizio (" + dataInizio.toString() + ").", 
                        "Violazione Temporale", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            if (stato == Stato.Finito && dataFine == null) {
                JOptionPane.showMessageDialog(this, 
                    "ATTENZIONE: Se vuoi concludere l'attività (FINITO), devi inserire la Data Fine.", 
                    "Errore di Validazione", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            double quantitaEffettiva = 0.0;
            Qualita qualitaRaccolto = null;

            if (attivitaOriginale.getNome().equals("Raccolta") && stato == Stato.Finito) {
                
                String quantitaInput = JOptionPane.showInputDialog(this, 
                    "REGISTRAZIONE RACCOLTO:\nInserisci la Quantità Effettiva (es. 15.5):", 
                    "Quantità Raccolta", JOptionPane.QUESTION_MESSAGE);
                
                if (quantitaInput == null) return; 

                try {
                    quantitaEffettiva = Double.parseDouble(quantitaInput);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Quantità non valida. L'operazione è stata annullata.", "Errore Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            
            qualitaRaccolto = (Qualita) JOptionPane.showInputDialog(this,
                "Seleziona la Qualità del raccolto:",
                "Qualità Raccolto",
                JOptionPane.QUESTION_MESSAGE,
                null,
                Qualita.values(), 
                Qualita.Eccellente); 
                
            if (qualitaRaccolto == null) return;
        }

            attivitaOriginale.setDataInizio(dataInizio); 
            attivitaOriginale.setDataFine(dataFine);
            attivitaOriginale.setCosto(costo);
            attivitaOriginale.setStato(stato);
            attivitaOriginale.setPriorita(priorita);
            attivitaOriginale.setColtura(colturaSelezionata);
            attivitaOriginale.setColtivatore(coltivatoreSelezionato);

            controller.updateAttivitaAndRecordRaccolto(attivitaOriginale, quantitaEffettiva, qualitaRaccolto);

            JOptionPane.showMessageDialog(this, "Attività aggiornata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            
            if (parentView != null) {
                parentView.loadAttivitaData();
            }
            this.dispose(); 

        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato data non valido (Data Fine). Usa AAAA-MM-GG.", "Errore Input", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Costo non valido. Inserisci un numero.", "Errore Input", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(this, "Errore DB durante l'aggiornamento: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore sconosciuto: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

}