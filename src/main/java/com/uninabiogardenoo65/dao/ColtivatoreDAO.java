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
import com.uninabiogardenoo65.entity.Coltivatore;

public class ColtivatoreDAO {
    
    private Controller controller;

    public ColtivatoreDAO(Controller controller){
        this.controller = controller;
    }

    public List<Coltivatore> getAll() throws SQLException {
            List<Coltivatore> coltivatori = new ArrayList<>();
            
            final String SQL = "SELECT u.cod_fisc, u.nome, u.cognome, u.email, u.telefono, u.dataN " +
                            "FROM Utente u " +
                            "JOIN Coltivatore c ON u.cod_fisc = c.cod_fisc " +
                            "ORDER BY u.cognome, u.nome";
                            
            try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL);
                ResultSet rs = stmt.executeQuery()) {
                
                while (rs.next()) {

                    Coltivatore coltivatore = new Coltivatore(
                        rs.getString("cod_fisc"),
                        rs.getString("email"),
                        null,
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getObject("dataN", LocalDate.class),
                        rs.getString("telefono")
                    );

                    coltivatori.add(coltivatore);
                }
            } catch (SQLException e) {
                System.err.println("Errore DAO: Impossibile recuperare la lista di tutti i coltivatori.");
                throw e;
            }
            return coltivatori;
        }
        
    public List<Coltivatore> getColtivatoriByProgetto(String idProgetto) throws SQLException {
        List<Coltivatore> coltivatori = new ArrayList<>();
        String sql = "SELECT u.cod_fisc, u.email, u.password, u.nome, u.cognome, u.dataN, u.telefono " +
                    "FROM Utente u " +
                    "JOIN Coltivatore c ON u.cod_fisc = c.cod_fisc " +
                    "JOIN ColtivatoreProgetto cp ON c.cod_fisc = cp.cod_fisc_coltivatore " +
                    "WHERE cp.idProgetto = ?";
        
        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idProgetto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    java.sql.Date sqlDate = rs.getDate("dataN");
                    LocalDate localDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                    Coltivatore coltivatore = new Coltivatore(
                        rs.getString("cod_fisc"), rs.getString("email"), rs.getString("password"),
                        rs.getString("nome"), rs.getString("cognome"), localDate, rs.getString("telefono")
                    );
        
                    
                    coltivatori.add(coltivatore);
                }
            }
        }
        return coltivatori;
    
    }



}
