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
import com.uninabiogardenoo65.entity.Attivita;
import com.uninabiogardenoo65.entity.Coltivatore;
import com.uninabiogardenoo65.entity.Coltura;
import com.uninabiogardenoo65.enums.Priorita;
import com.uninabiogardenoo65.enums.Stato;

public class AttivitaDAO {

    private Controller controller;

    public AttivitaDAO(Controller controller){
        this.controller = controller;
    }

    public void updateAttivita(Attivita attivita) throws SQLException {
        final String SQL = "UPDATE Attivita SET " +
                        "nome = ?, " +
                        "dataInizio = ?, " +
                        "dataFine = ?, " +
                        "costo = ?, " +
                        "stato = ?, " +
                        "priorita = ?, " +
                        "cod_fisc_coltivatore = ?, " + 
                        "idColtura = ? " +             
                        "WHERE idAttivita = ?";      
                        
        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL)) {
            
            int i = 1;
            stmt.setString(i++, attivita.getNome());
            stmt.setObject(i++, attivita.getDataInizio());
            
            if (attivita.getDataFine() != null) {
                stmt.setObject(i++, attivita.getDataFine());
            } else {
                stmt.setNull(i++, java.sql.Types.DATE); 
            }
            
            stmt.setDouble(i++, attivita.getCosto());
            stmt.setString(i++, attivita.getStato().toString()); 
            stmt.setString(i++, attivita.getPriorita().toString());
            
            stmt.setString(i++, attivita.getColtivatore().getCod_fisc()); 
            stmt.setString(i++, attivita.getColtura().getIdColtura());
            
            stmt.setString(i, attivita.getIdAttivita());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("Aggiornamento fallito: Nessuna attività trovata con ID " + attivita.getIdAttivita());
            }
            
        } catch (SQLException e) {
            System.err.println("Errore DAO: Impossibile aggiornare l'attività con ID " + attivita.getIdAttivita());
            throw e; 
        }
    }


    public Attivita getAttivitaById(String idAttivita) throws SQLException {
        String sql = "SELECT a.idAttivita, a.nome, a.dataInizio, a.dataFine, a.stato, " +
                    "a.priorita, a.costo, a.cod_fisc_coltivatore, a.idColtura, a.idProgetto," +
                    "c.nome as nome_coltura, " +
                    "u.cod_fisc, u.nome as nome_coltivatore, u.cognome as cognome_coltivatore, " +
                    "c.tempodimaturazione as tempoDiMaturazione, " +
                    "c.dataseminazione as dataSeminazione, " +
                    "c.idlotto as idLotto, " +
                    "c.quantitaseminatam2 as quantitaSeminataM2 " +
                    "FROM Attivita a " +
                    "LEFT JOIN Coltura c ON a.idColtura = c.idColtura " +
                    "LEFT JOIN Coltivatore col ON a.cod_fisc_coltivatore = col.cod_fisc " +
                    "LEFT JOIN Utente u ON col.cod_fisc = u.cod_fisc " +
                    "WHERE a.idAttivita = ?";
        
        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idAttivita);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    Stato stato = Stato.valueOf(rs.getString("stato"));
                    Priorita priorita = Priorita.valueOf(rs.getString("priorita"));
                    

                    Coltivatore coltivatoreAssegnato = new Coltivatore(
                        rs.getString("cod_fisc"),
                        null,
                        rs.getString("nome_coltivatore"),
                        rs.getString("cognome_coltivatore"),
                        null,
                        null
                    );


                    Coltura coltura = new Coltura(
                        rs.getString("idColtura"),
                        rs.getString("nome_coltura"),
                        rs.getInt("tempoDiMaturazione"),
                        rs.getObject("dataSeminazione", LocalDate.class),
                        null,
                        rs.getString("idLotto"),
                        rs.getDouble("quantitaSeminataM2")
                    );
                    

                    return new Attivita(
                        rs.getString("idAttivita"), 
                        rs.getString("nome"),
                        rs.getDate("dataInizio").toLocalDate(),
                        rs.getDate("dataFine") != null ? rs.getDate("dataFine").toLocalDate() : null,
                        stato,
                        priorita,
                        rs.getDouble("costo"),
                        coltivatoreAssegnato,
                        coltura,
                        rs.getString("idProgetto")
                    );
                }
            }
        } catch (SQLException error) {
            System.err.println("Errore durante il recupero dell'attività con ID: " + idAttivita);
            System.err.println("Errore: " + error.getMessage());
            throw error;
        }
        
        return null;
    }

    public boolean sospendiAttivitaAttive(String idProgetto, LocalDate dataFine) throws SQLException {
        
        final String SQL = "UPDATE Attivita SET stato = 'NonConcluso', dataFine = ? " +
                        "WHERE idProgetto = ? AND stato != 'Finito'";
        
        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL)) {
            
            stmt.setObject(1, dataFine);
            
            stmt.setString(2, idProgetto);
            
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore DAO: Impossibile sospendere le attività per il progetto " + idProgetto);
            throw e; 
        }
    }


    public void updateStato(String idAttivita, Stato nuovoStato) throws SQLException {
        final String SQL = "UPDATE Attivita SET stato = ? WHERE idAttivita = ?";
        
        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL)) {
            
            stmt.setString(1, nuovoStato.toString());
            
            stmt.setString(2, idAttivita);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("Aggiornamento stato fallito: Attività con ID " + idAttivita + " non trovata.");
            }
            
        } catch (SQLException e) {
            System.err.println("Errore DAO: Impossibile aggiornare lo stato dell'attività.");
            throw e; 
        }
    }


        
    public List<Attivita> getAttivitaByProgetto(String idProgetto) throws SQLException {
            List<Attivita> attivitaList = new ArrayList<>();
            
            String sql = "SELECT a.idAttivita, a.nome, a.dataInizio, a.dataFine, a.stato, " +
                        "a.priorita, a.costo, a.idProgetto, a.cod_fisc_coltivatore, a.idColtura, " +
                        
                        "c.nome AS nome_coltura, c.tempoDiMaturazione, c.dataSeminazione, c.idLotto AS idLotto_coltura, " +
                        
                        "u.cod_fisc AS cod_fisc_coltivatore_utente, u.nome AS nome_coltivatore, u.cognome AS cognome_coltivatore, " +
                        "u.dataN AS dataN_coltivatore, u.email AS email_coltivatore, u.telefono AS telefono_coltivatore " +
                        
                        "FROM Attivita a " +
                        "LEFT JOIN Coltura c ON a.idColtura = c.idColtura " +
                        "LEFT JOIN Coltivatore col ON a.cod_fisc_coltivatore = col.cod_fisc " +
                        "LEFT JOIN Utente u ON col.cod_fisc = u.cod_fisc " +
                        
                        "WHERE a.idProgetto = ? " +
                        "ORDER BY " +
                        "(a.stato = 'Finito') ASC, " + 
                        "a.dataInizio DESC, " +
                        "a.priorita DESC";
                    
            try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, idProgetto);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        
                        
                        Coltivatore coltivatore = null;
                        if (rs.getString("cod_fisc_coltivatore_utente") != null) {
                            coltivatore = new Coltivatore(
                                rs.getString("cod_fisc_coltivatore_utente"),
                                rs.getString("email_coltivatore"),
                                null,
                                rs.getString("nome_coltivatore"),
                                rs.getString("cognome_coltivatore"),
                                rs.getObject("dataN_coltivatore", LocalDate.class),
                                rs.getString("telefono_coltivatore")
                            );
                        }
                        
                        Coltura coltura = null;

                        if (rs.getString("idColtura") != null) {
                            coltura = new Coltura(
                                rs.getString("idColtura"),
                                rs.getString("nome_coltura"),
                                rs.getInt("tempoDiMaturazione"),
                                rs.getObject("dataSeminazione", LocalDate.class),
                                rs.getString("idLotto_coltura")
                            );
                        }

                        Attivita attivita = new Attivita(
                            rs.getString("idAttivita"),
                            rs.getString("nome"),
                            rs.getObject("dataInizio", LocalDate.class),
                            rs.getObject("dataFine", LocalDate.class),
                            Stato.valueOf(rs.getString("stato")), 
                            Priorita.valueOf(rs.getString("priorita")), 
                            rs.getDouble("costo"),
                            coltivatore,
                            coltura,
                            rs.getString("idProgetto") 

                        );
                        
                        attivitaList.add(attivita);
                    }
                }
            } catch (SQLException error) {
                System.err.println("Errore DAO: Impossibile recuperare attività per il progetto " + idProgetto);
                throw error;
            } catch (IllegalArgumentException e) {
                throw new SQLException("Errore di mappatura ENUM o dati non validi: " + e.getMessage(), e);
            }
            
            return attivitaList;
        }
    



    public void createAttivita(Attivita attivita) throws SQLException {
        
        final String SQL = "INSERT INTO Attivita (idAttivita, nome, dataInizio, dataFine, costo, stato, priorita, cod_fisc_coltivatore, idColtura, idProgetto) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
             
            
            stmt.setString(1, attivita.getIdAttivita());
            stmt.setString(2, attivita.getNome());
            stmt.setObject(3, attivita.getDataInizio()); 
            stmt.setObject(4, attivita.getDataFine());
            stmt.setDouble(5, attivita.getCosto());
            stmt.setString(6, attivita.getStato().toString()); 
            stmt.setString(7, attivita.getPriorita().toString()); 
            stmt.setString(8, attivita.getColtivatore().getCod_fisc()); 
            stmt.setString(9, attivita.getColtura().getIdColtura());
            stmt.setString(10, attivita.getIdProgetto());

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("L'inserimento dell'attività nel DB è fallito: nessuna riga modificata.");
            }
            
        } catch (SQLException error) {
            System.err.println("Errore DAO: Impossibile salvare la nuova attività nel database.");
            System.out.println(error);
            throw error; 
        }
    }


    public boolean deleteAttivita(String idAttivita) throws SQLException {
        if (!attivitaExists(idAttivita)) {
            System.err.println("Attività con ID " + idAttivita + " non trovata");
            return false;
        }
        
        String sql = "DELETE FROM Attivita WHERE idAttivita = ?";
        
        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idAttivita);
            
            int rowsAffected = stmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                System.out.println("Attività con ID " + idAttivita + " eliminata con successo");
            } else {
                System.err.println("Nessuna attività eliminata con ID: " + idAttivita);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Errore SQL durante l'eliminazione dell'attività con ID: " + idAttivita);
            System.err.println("Errore: " + e.getMessage());
            throw e;
        }
    }

    private boolean attivitaExists(String idAttivita) throws SQLException {
        String sql = "SELECT 1 FROM Attivita WHERE idAttivita = ?";
        
        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idAttivita);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }



}
