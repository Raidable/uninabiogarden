package com.uninabiogardenoo65.boundary;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.entity.RaccoltoDati;
import com.uninabiogardenoo65.entity.Lotto;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.text.DecimalFormat; // Per formattare i numeri nella tabella

public class ReportView extends JDialog {

    private final Controller controller;
    private final Lotto lottoDiRiferimento;

    // Componenti GUI
    private JTable reportTable;
    private DefaultTableModel reportTableModel;

    public ReportView(JFrame parent, Controller controller, Lotto lotto) {
        super(parent, "Report per: " + lotto.getNome(), true);
        this.controller = controller;
        this.lottoDiRiferimento = lotto;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        try {
            // ACQUISIZIONE DATI AGGREGATI
            List<RaccoltoDati> reportData = controller.getReportDataByIdLotto(lotto.getIdLotto());
            
            // CREAZIONE DEL GRAFICO JFreeChart
            JFreeChart chart = createChart(reportData, lotto.getNome());
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 400)); // Dimensione grafico
            
            // CREAZIONE DELLA TABELLA DETTAGLIATA
            initializeTable(); // Inizializza la tabella e il modello
            populateTable(reportData); // Popola la tabella con i dati
            JScrollPane tableScrollPane = new JScrollPane(reportTable);
            tableScrollPane.setPreferredSize(new Dimension(800, 150)); // Dimensione tabella
            
            // LAYOUT (Grafico sopra, Tabella sotto)
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            mainPanel.add(chartPanel, BorderLayout.CENTER);
            mainPanel.add(tableScrollPane, BorderLayout.SOUTH); // Tabella in basso
            
            add(mainPanel);
            
            pack(); 
            setLocationRelativeTo(parent);

        } catch (SQLException e) {
             JOptionPane.showMessageDialog(this, "Errore DB: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
             dispose();
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Errore Creazione Report: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
             dispose();
        }
    }

    public void initializeTable() {
        String[] columnNames = {"Coltura", "Tot. Raccolte", "Q.tà Media", "Q.tà Min", "Q.tà Max", "Q.tà Totale"};
        reportTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(reportTableModel);
        reportTable.setFillsViewportHeight(true); // Estetica
    }
    
    // Popola la tabella con i dati dal Controller
    public void populateTable(List<RaccoltoDati> data) {
        if (data == null) return;
        
        // Formattatore per i numeri decimali (es. 15.5 invece di 15.500001)
        DecimalFormat df = new DecimalFormat("#.##"); 

        for (RaccoltoDati item : data) {
            reportTableModel.addRow(new Object[]{
                item.getNomeColtura(),
                item.getTotaleRaccolte(), // Conteggio
                df.format(item.getQuantitaMedia()), // Media formattata
                df.format(item.getQuantitaMinima()), // Min formattato
                df.format(item.getQuantitaMassima()),// Max formattato
                df.format(item.getQuantitaTotale()) // Totale formattato
            });
        }
    }

    // Crea il grafico JFreeChart (come prima)
    private JFreeChart createChart(List<RaccoltoDati> data, String nomeLotto) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (data != null && !data.isEmpty()) {
            for (RaccoltoDati item : data) {
                String categoria = item.getNomeColtura();
                dataset.addValue(item.getQuantitaMedia(), "Media", categoria);
                dataset.addValue(item.getQuantitaMinima(), "Minima", categoria);
                dataset.addValue(item.getQuantitaMassima(), "Massima", categoria);
            }
        } else {
            dataset.addValue(0, "Nessun Dato", "Nessuna Coltura");
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Report Quantità Raccolta (Media, Min, Max) - Lotto: " + nomeLotto,
            "Coltura", "Quantità", dataset, PlotOrientation.VERTICAL, true, true, false);
        
        chart.setBackgroundPaint(Color.white);
        return chart;
    }
}