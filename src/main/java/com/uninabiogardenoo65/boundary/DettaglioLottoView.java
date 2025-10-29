package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.Coltura;
import com.uninabiogardenoo65.entity.Lotto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DettaglioLottoView extends JFrame {
    
    private final JFrame parentView; 
    private final Controller controller;
    private final Lotto lottoCorrente;

    private JTable coltureTable;
    private JButton indietroButton;

    public DettaglioLottoView(JFrame parent, Controller controller, Lotto lotto) {
        this.parentView = parent;
        this.controller = controller;
        this.lottoCorrente = lotto;
        
        setTitle("Dettagli Lotto: " + lotto.getNome() + " (" + lotto.getSuperficie() + " m²)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initializeComponents();
        layoutComponents();
        addListeners();
        
        loadColtureData();
        
        setSize(800, 500); 
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        String[] coltureColumns = {"Nome Coltura", "Data Seminazione", "Data Prevista Raccolta", "Quantità/m²"};
        coltureTable = new JTable(new DefaultTableModel(coltureColumns, 0));
        
        indietroButton = new JButton("Indietro alla Dashboard");
        
    }
    
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel title = new JLabel("Colture Attuali nel Lotto: " + lottoCorrente.getNome(), SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(title, BorderLayout.NORTH);
        
        mainPanel.add(new JScrollPane(coltureTable), BorderLayout.CENTER);
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.add(indietroButton);
        mainPanel.add(footer, BorderLayout.SOUTH);
        
        this.add(mainPanel);
    }

    public void loadColtureData() {
        DefaultTableModel model = (DefaultTableModel) coltureTable.getModel();
        model.setRowCount(0); 
        
        String idLotto = lottoCorrente.getIdLotto(); 

        try {
            List<Coltura> colture = controller.getColtureByLottoId(idLotto); 
            
            if (colture != null && !colture.isEmpty()) {
                for (Coltura c : colture) {
                    
                    System.out.println("Caricando coltura: " + c.getNome() + ", Data Semina: " + c.getDataSeminazione() + ", Quantità/m²: " + c.getQuantitaSeminataM2());
                    model.addRow(new Object[]{
                        c.getNome(), 
                        c.getDataSeminazione(), 
                        c.getDataPrevistaRaccolta(),
                        c.getQuantitaSeminataM2()
                    });
                }
            } else {
                System.out.println("Nessuna coltura trovata per il lotto: " + lottoCorrente.getNome());
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Errore DB: Impossibile caricare i dati delle colture: " + e.getMessage(), 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void addListeners() {
        indietroButton.addActionListener(e -> {
            this.dispose(); 
            if (parentView != null) {
                parentView.setVisible(true);
            }
        });
    }
}