package com.uninabiogardenoo65.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.controller.DBManager;
import com.uninabiogardenoo65.entity.Raccolto;
import com.uninabiogardenoo65.entity.RaccoltoDati;

public class RaccoltoDAO {


    private Controller controller;

    public RaccoltoDAO(Controller controller){
        this.controller = controller;
    }

    public List<RaccoltoDati> getHarvestStatisticsForLotto(String idLotto) throws SQLException {
        List<RaccoltoDati> reportList = new ArrayList<>();
        
        final String SQL = "SELECT " +
                        "l.nome AS nome_lotto, " +
                        "c.nome AS nome_coltura, " +
                        "COUNT(r.idRaccolto) AS total_raccolte, " +
                        "SUM(r.quantitaEffettiva) AS quantita_totale, " +
                        "AVG(r.quantitaEffettiva) AS quantita_media, " +
                        "MIN(r.quantitaEffettiva) AS quantita_minima, " +
                        "MAX(r.quantitaEffettiva) AS quantita_massima " +
                        "FROM Raccolto r " +
                        "JOIN Coltura c ON r.idColtura = c.idColtura " + // Modificato per usare idColtura in Raccolto
                        "JOIN Lotto l ON c.idLotto = l.idLotto " +
                        "WHERE l.idLotto = ? " + // <-- FILTRO SUL LOTTO SPECIFICO
                        "GROUP BY l.nome, c.nome " +
                        "ORDER BY c.nome";

        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL)) {
            
            stmt.setString(1, idLotto); // Imposta il parametro del filtro
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Mappatura identica a getHarvestStatistics
                    RaccoltoDati data = new RaccoltoDati(
                        rs.getString("nome_lotto"),
                        rs.getString("nome_coltura"),
                        rs.getLong("total_raccolte"),
                        rs.getDouble("quantita_media"),
                        rs.getDouble("quantita_minima"),
                        rs.getDouble("quantita_massima"),
                        rs.getDouble("quantita_totale")
                    );
                    reportList.add(data);
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return reportList;
    }

    public void save(Raccolto raccolto) throws SQLException {
        
        final String SQL = "INSERT INTO Raccolto (idRaccolto, idColtura, quantitaEffettiva, dataRaccolto, qualita) " +
                           "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
             
            
            stmt.setString(1, raccolto.getIdRaccolto());
            stmt.setString(2, raccolto.getIDColtura()); 
            stmt.setDouble(3, raccolto.getQuantitaEffettiva());
            stmt.setObject(4, raccolto.getDataRaccolto());
            stmt.setString(5, raccolto.getQualita().toString());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("L'inserimento del record di raccolto nel DB è fallito.");
            }
            
        } catch (SQLException e) {
            System.err.println("Errore DAO: Impossibile salvare il record di raccolto.");
            throw e; 
        }
    }

}
