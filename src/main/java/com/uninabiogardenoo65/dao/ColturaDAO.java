package com.uninabiogardenoo65.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.controller.DBManager;
import com.uninabiogardenoo65.entity.Coltura;
import com.uninabiogardenoo65.entity.Lotto;

public class ColturaDAO {


    private Controller controller;

    public ColturaDAO(Controller controller){
        this.controller = controller;
    }


    public List<Coltura> getColtureByLottoId(String idLotto) throws SQLException {
        List<Coltura> colturaList = new ArrayList<>();
        
        final String SQL = "SELECT idColtura, nome, tempoDiMaturazione, dataSeminazione, idLotto, quantitaseminatam2 " +
                           "FROM Coltura " +
                           "WHERE idLotto = ?";
                           
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
             
            stmt.setString(1, idLotto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Mapping coltura dal ResultSet per idLotto: " + "ao" + rs.getDouble("quantitaseminatam2") + "bell");
                    LocalDate dataSeminazione = rs.getObject("dataSeminazione", LocalDate.class);
                    
                    Coltura coltura = new Coltura(
                        rs.getString("idColtura"),
                        rs.getString("nome"),
                        rs.getInt("tempoDiMaturazione"),
                        dataSeminazione,
                        null,
                        idLotto,
                        rs.getDouble("quantitaseminatam2")
                    );
                    
                    colturaList.add(coltura);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore DAO: Impossibile trovare le colture per il lotto " + idLotto);
            throw e;
        }
        return colturaList;
    }


}
