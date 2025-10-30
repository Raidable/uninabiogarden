package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Proprietario;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;

public class RegisterView extends JFrame {

    private final Controller controller;

    private JTextField codFiscField, emailField, nomeField, cognomeField, telefonoField;
    private JPasswordField passwordField;
    private JTextField dataNField;
    private JButton submitButton, backButton;

    public RegisterView(Controller controller) {
        this.controller = controller;

        setTitle("UninaBioGarden - Registrazione Nuovo Proprietario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initializeComponents();
        layoutComponents();
        addListeners();
        
        pack();
        setLocationRelativeTo(null);
    }

    public void initializeComponents() {
        codFiscField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        nomeField = new JTextField(20);
        cognomeField = new JTextField(20);
        dataNField = new JTextField("AAAA-MM-GG", 20);
        telefonoField = new JTextField(20);
        
        submitButton = new JButton("Registra e Accedi");
        backButton = new JButton("Indietro (Login)");
    }

    public void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("Registrazione Proprietario", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        addRow(formPanel, new JLabel("Codice Fiscale:"), codFiscField, gbc, row++);
        addRow(formPanel, new JLabel("Email:"), emailField, gbc, row++);
        addRow(formPanel, new JLabel("Password:"), passwordField, gbc, row++);
        addRow(formPanel, new JLabel("Nome:"), nomeField, gbc, row++);
        addRow(formPanel, new JLabel("Cognome:"), cognomeField, gbc, row++);
        addRow(formPanel, new JLabel("Data di Nascita (AAAA-MM-GG):"), dataNField, gbc, row++);
        addRow(formPanel, new JLabel("Telefono:"), telefonoField, gbc, row++);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }
    
    public void addRow(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    public void addListeners() {
        submitButton.addActionListener(e -> handleRegistration());

        backButton.addActionListener(e -> {
            LoginView loginView = new LoginView(this.controller);
            loginView.setVisible(true);
            this.dispose();
        });
    }

    public void handleRegistration() {
        String codFisc = codFiscField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String telefono = telefonoField.getText().trim();
        String dataNText = dataNField.getText().trim();
        
        if (codFisc.isEmpty() || email.isEmpty() || password.isEmpty() || dataNText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori.", "Errore Input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        LocalDate dataN = null;
        try {
            dataN = LocalDate.parse(dataNText);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato data non valido. Usa AAAA-MM-GG.", "Errore Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Proprietario nuovoProprietario = controller.registraProprietario(
                codFisc, email, password, nome, cognome, dataN, telefono);
            
            
            if (nuovoProprietario != null) {
                JOptionPane.showMessageDialog(this, "Registrazione completata! Accesso effettuato.", "Successo", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Errore: Codice Fiscale o Email già registrati.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore di sistema durante la registrazione.", "Errore Fatale", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}