package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Coltura;
import com.uninabiogardenoo65.entity.Progetto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AssegnaAttivitaInizialiView extends JDialog {

    private final DashboardProprietarioView parentView;
    private final Controller controller;
    private final Progetto progettoCorrente;
    private final List<Coltura> coltureSelezionate;
    private JButton indietroButton;


    public AssegnaAttivitaInizialiView(DashboardProprietarioView parent, Controller controller, Progetto progetto, List<Coltura> colture) {
        super(parent, "Assegna Attività Iniziali a " + progetto.getNome(), true); 
        this.parentView = parent;
        this.controller = controller;
        this.progettoCorrente = progetto;
        this.coltureSelezionate = colture;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeComponents();
        layoutComponents();

        pack();
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }
    

    public void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(indietroButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JLabel title = new JLabel("Assegna attività per le seguenti colture:", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS)); 
        
        for (Coltura coltura : coltureSelezionate) {
            listPanel.add(createColturaActionRow(coltura));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }


    public void initializeComponents() {
        indietroButton = new JButton("Chiudi");
        indietroButton.setBackground(new Color(70, 130, 180));
        indietroButton.setForeground(Color.WHITE);
        
        indietroButton.addActionListener(e -> {
            this.dispose(); 
            parentView.setVisible(true); 
        }); 
    }

    public JPanel createColturaActionRow(Coltura coltura) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel nameLabel = new JLabel(coltura.getNome());
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JButton addButton = new JButton("Aggiungi Attività (+)");
        addButton.setBackground(new Color(60, 179, 113));
        addButton.setForeground(Color.WHITE);
        
        addButton.addActionListener(e -> handleAggiungiAttivita(coltura));

        rowPanel.add(nameLabel, BorderLayout.WEST);
        rowPanel.add(addButton, BorderLayout.EAST);
        
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowPanel.getPreferredSize().height));
        
        return rowPanel;
    }


    public void handleAggiungiAttivita(Coltura coltura) {
        this.dispose();
        
        CreaAttivitaNuovoProgetto creaAttivitaView = new CreaAttivitaNuovoProgetto(
            this.controller, 
            this.progettoCorrente, 
            this,
            this.parentView
        );
        this.setVisible(false);
        creaAttivitaView.setVisible(true);
    }
}