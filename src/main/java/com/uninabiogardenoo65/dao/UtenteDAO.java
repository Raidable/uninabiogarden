package com.uninabiogardenoo65.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.controller.DBManager;
import com.uninabiogardenoo65.entity.Coltivatore;
import com.uninabiogardenoo65.entity.Proprietario;
import com.uninabiogardenoo65.entity.Utente;

public class UtenteDAO {

	private Controller controller;

    public UtenteDAO(Controller controller){
        this.controller = controller;
    }


            
    public Utente findUtenteEConRuoloByCredentials(String email, String password) throws SQLException {
            
            final String SQL_FIND_UTENTE = "SELECT cod_fisc, nome, cognome, dataN, telefono, email, password FROM Utente WHERE email = ? AND password = ?";
            final String SQL_FIND_PROPRIETARIO = "SELECT pIva FROM Proprietario WHERE cod_fisc = ?";
            final String SQL_FIND_COLTIVATORE = "SELECT cod_fisc FROM Coltivatore WHERE cod_fisc = ?";
            
            
            String codFisc = null;
            String nome = null;
            String cognome = null;
            String telefono = null;
            LocalDate dataNascita = null;
            
            try (Connection conn = DBManager.getConnection(); 
                PreparedStatement stmtUtente = conn.prepareStatement(SQL_FIND_UTENTE)) {
                
                stmtUtente.setString(1, email); 
                stmtUtente.setString(2, password); 
                
                try (ResultSet rsUtente = stmtUtente.executeQuery()) {
                    if (rsUtente.next()) {
                        codFisc = rsUtente.getString("cod_fisc");
                        nome = rsUtente.getString("nome");
                        cognome = rsUtente.getString("cognome");
                        telefono = rsUtente.getString("telefono");
                        dataNascita = rsUtente.getObject("dataN", LocalDate.class);
                    } else {
                        return null;
                    }
                }
                
                try (PreparedStatement stmtProprietario = conn.prepareStatement(SQL_FIND_PROPRIETARIO)) {
                    stmtProprietario.setString(1, codFisc);
                    
                    try (ResultSet rsProprietario = stmtProprietario.executeQuery()) {
                        if (rsProprietario.next()) {
                            String pIva = rsProprietario.getString("pIva");
                            
                            return new Proprietario(
                                codFisc, 
                                email,
                                password,
                                nome, 
                                cognome,
                                dataNascita,
                                telefono,
                                pIva
                            );
                        }
                    }
                }

                try (PreparedStatement stmtColtivatore = conn.prepareStatement(SQL_FIND_COLTIVATORE)) {
                    stmtColtivatore.setString(1, codFisc);
                    
                    try (ResultSet rsColtivatore = stmtColtivatore.executeQuery()) {
                        if (rsColtivatore.next()) {
                            
                            return new Coltivatore(
                                codFisc, 
                                email,
                                password,
                                nome, 
                                cognome,
                                dataNascita,
                                telefono
                            );
                        }
                    }
                }

                return null; 
                
            } 
        }



    public Utente getUtenteByCredentials(String email, String password) throws SQLException {
        
        System.out.println("UtenteDAO: getUtenteByCredentials called with email=" + email);
        Utente utente = null;
        
        String sql = "SELECT cod_fisc, nome, cognome, dataN, telefono, email " +
                        "FROM Utente WHERE email = ? AND password = ?";
        
        
        try (Connection conn = DBManager.getConnection(); 
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                
            stmt.setString(1, email); 
            stmt.setString(2, password); 
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    
                    LocalDate dataNascita = rs.getObject("dataN", LocalDate.class);
                    
                    utente = new Utente(
                        rs.getString("cod_fisc"),
                        rs.getString("email"),
                        password,
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        dataNascita,
                        rs.getString("telefono")
                    ) {};
                    
                }
            }
        } 
        
        return utente;
    }

}



