package com.uninabiogardenoo65.boundary;

import javax.swing.*;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Coltivatore;
import com.uninabiogardenoo65.entity.Proprietario;
import com.uninabiogardenoo65.entity.Utente;


import java.awt.*;
import java.net.URL; 

public class LoginView extends JFrame {
    
    private final Controller controller; 

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginView(Controller controller) {
        this.controller = controller;
        
        setTitle("UninaBioGarden - Accesso Proprietario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        initializeComponents();
        layoutComponents();
        addListeners();
        
        pack(); // Adatta la dimensione al contenuto
        setLocationRelativeTo(null); // Centra la finestra sullo schermo
    }
    
    public void initializeComponents() {
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Accedi");
        registerButton = new JButton("Registrati");
    }

    public void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
       try {
            URL imgURL = getClass().getResource("/images/logo_unina.png"); 
            
            ImageIcon logoIcon = (imgURL != null) ? new ImageIcon(imgURL) : new ImageIcon();
            
            JLabel headerLabel = new JLabel("UninaBioGarden", logoIcon, SwingConstants.CENTER);
            headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
            headerLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
            headerLabel.setVerticalTextPosition(SwingConstants.CENTER);
            
            mainPanel.add(headerLabel, BorderLayout.NORTH);
            
        } catch (Exception e) {
             JLabel headerLabel = new JLabel("UninaBioGarden - Accesso", SwingConstants.CENTER);
             headerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
             headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
             mainPanel.add(headerLabel, BorderLayout.NORTH);
        }
        
      JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Padding tra i componenti
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0; // Larghezza fissa
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0; // Larghezza espandibile
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0; 
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0; 
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        loginButton.setPreferredSize(new Dimension(100, 30));
        registerButton.setPreferredSize(new Dimension(100, 30));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        this.add(mainPanel);
    }

    public void addListeners() {
        
        loginButton.addActionListener(e -> handleLogin());
        
        registerButton.addActionListener(e -> {
        	RegisterView registerView = new RegisterView(this.controller);
            registerView.setVisible(true);
            this.dispose(); 
        });
    }

    public void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci Username e Password.", "Errore Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Utente utente = controller.autenticaUtente(email, password);
            
            if (utente != null) {
                if (utente instanceof Proprietario) {
                    
                    Proprietario proprietario = (Proprietario) utente;
                    
                    this.dispose();
                    DashboardProprietarioView dashboard = new DashboardProprietarioView(controller, proprietario);
                        dashboard.setVisible(true);
                        
                    
                } else if (utente instanceof Coltivatore) {
                    
                    Coltivatore coltivatore = (Coltivatore) utente;
                    
                    JOptionPane.showMessageDialog(this, 
                        "Accesso come COLTIVATORE riuscito! Benvenuto, " + coltivatore.getNome(), 
                        "Login OK", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    this.dispose();
                        
                    // APRI LA DASHBOARD COLTIVATORE
                    DashboardColtivatoreView dashboard = new DashboardColtivatoreView(controller, coltivatore);
                    dashboard.setVisible(true);                    
                } 

                this.dispose();

                
            } else {
                JOptionPane.showMessageDialog(this, "Credenziali non valide. Riprova.", "Errore di Login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore di sistema: " + ex.getMessage(), "Errore Fatale", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}