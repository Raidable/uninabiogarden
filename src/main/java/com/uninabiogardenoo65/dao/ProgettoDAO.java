package com.uninabiogardenoo65.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.uninabiogardenoo65.controller.DBManager;
import com.uninabiogardenoo65.entity.Coltivatore;
import com.uninabiogardenoo65.entity.Coltura;
import com.uninabiogardenoo65.entity.Lotto;
import com.uninabiogardenoo65.entity.Progetto;
import com.uninabiogardenoo65.entity.Proprietario;
import com.uninabiogardenoo65.enums.Stagione;
import com.uninabiogardenoo65.controller.Controller;


public class ProgettoDAO {

    private Controller controller;

    public ProgettoDAO(Controller controller){
        this.controller = controller;
    }


    public boolean removeColtivatoreFromProgetto(String idColtivatore, String idProgetto) throws SQLException {
        String sql = "DELETE FROM ColtivatoreProgetto WHERE idProgetto = ? AND cod_fisc_coltivatore = ?";
        
        System.out.println("Rimuovendo coltivatore " + idColtivatore + " dal progetto " + idProgetto);
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idProgetto);
            stmt.setString(2, idColtivatore);
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected + " righe eliminate.");
            return rowsAffected > 0;
            
        } catch (SQLException error) {
            System.err.println("Errore durante la rimozione del coltivatore dal progetto: " + error.getMessage());
            throw error;
        }
    }

    public boolean addColtivatoreToProgetto(String idColtivatore, String idProgetto) throws SQLException {
        String sql = "INSERT INTO ColtivatoreProgetto (idProgetto, cod_fisc_coltivatore) VALUES (?, ?)";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idProgetto);
            stmt.setString(2, idColtivatore);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiunta del coltivatore al progetto: " + e.getMessage());
            throw e;
        }
    }
    


    public Progetto getProgettoById(String idProgetto) throws SQLException {
            
            final String SQL = "SELECT p.idProgetto, p.nome AS nomeProgetto, p.dataInizio, p.dataFine, p.budget, p.stagione, p.idLotto, p.cod_fisc_proprietario, " +
                            "l.nome AS nomeLotto, l.superficie " + 
                            "FROM Progetto p " +
                            "JOIN Lotto l ON p.idLotto = l.idLotto " +
                            "WHERE p.idProgetto = ?";
                            
            Progetto progetto = null;
            
            try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL)) {
                
                stmt.setString(1, idProgetto);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        
                        LocalDate dataInizio = rs.getObject("dataInizio", LocalDate.class);
                        LocalDate dataFine = rs.getObject("dataFine", LocalDate.class); 
                        Stagione stagione = Stagione.valueOf(rs.getString("stagione"));
                        
                        String idLotto = rs.getString("idLotto"); 
                        String nomeLotto = rs.getString("nomeLotto");
                        float superficieLotto = rs.getFloat("superficie");
                        
                        Proprietario proprietario = new Proprietario();
                        proprietario.setCod_fisc(rs.getString("cod_fisc_proprietario"));
                        

                        Lotto lotto = new Lotto(
                            idLotto, 
                            nomeLotto, 
                            superficieLotto, 
                            null, 
                            proprietario
                        ); 
                        

                        progetto = new Progetto(
                            idProgetto, 
                            rs.getString("nomeProgetto"), 
                            dataInizio, 
                            dataFine, 
                            rs.getDouble("budget"), 
                            stagione, 
                            lotto, 
                            proprietario
                        );
                    }
                }
            } catch (SQLException e) {
                System.err.println("Errore DB durante la ricerca del progetto: " + idProgetto);
                throw new SQLException("Errore di accesso al DB durante la ricerca del progetto.", e);
            } 
            
            return progetto;
        }
        
    public List<Progetto> getProgettiOfProprietarioByCodFisc(String codFiscProprietario) throws SQLException {
            
            List<Progetto> progetti = new ArrayList<>();
            
            final String SQL = "SELECT p.idProgetto, p.nome AS nomeProgetto, p.dataInizio, p.dataFine, p.budget, p.stagione, p.idLotto, p.cod_fisc_proprietario, " +
                            "l.nome AS nomeLotto, l.superficie " + 
                            "FROM Progetto p " +
                            "JOIN Lotto l ON p.idLotto = l.idLotto " +
                            "WHERE p.cod_fisc_proprietario = ?";
            
            try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL)) {
                
                stmt.setString(1, codFiscProprietario);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        
                        String idProgetto = rs.getString("idProgetto");
                        LocalDate dataInizio = rs.getObject("dataInizio", LocalDate.class);
                        LocalDate dataFine = rs.getObject("dataFine", LocalDate.class); 
                        Stagione stagione = Stagione.valueOf(rs.getString("stagione"));
                        
                        String idLotto = rs.getString("idLotto"); 
                        String nomeLotto = rs.getString("nomeLotto");
                        float superficieLotto = rs.getFloat("superficie");
                        
                        
                        Lotto lotto = new Lotto(
                            idLotto, 
                            nomeLotto, 
                            superficieLotto, 
                            null,
                            null
                        ); 
                        
                        Proprietario proprietarioPlaceholder = new Proprietario();
                        proprietarioPlaceholder.setCod_fisc(rs.getString("cod_fisc_proprietario"));
                        
                        Progetto progetto = new Progetto(
                            idProgetto,
                            rs.getString("nomeProgetto"),
                            dataInizio, 
                            dataFine, 
                            rs.getDouble("budget"), 
                            stagione, 
                            lotto,
                            proprietarioPlaceholder
                        );
                        
                        progetti.add(progetto);
                    }
                }
            } catch (SQLException e) {
                throw new SQLException("Errore di accesso al DB durante la ricerca dei progetti.", e);
            } catch (IllegalArgumentException e) {
                throw new SQLException("Errore di mappatura dei dati (ENUM Stagione non valido).", e);
            }
            
            return progetti;
        }

    public void addColture(String idProgetto, List<Coltura> colture) throws SQLException {
        
        final String SQL_UPDATE = "UPDATE Coltura SET idProgetto = ? WHERE idColtura = ?";
        
        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
                
            for (Coltura c : colture) {
                stmt.setString(1, idProgetto);
                stmt.setString(2, c.getIdColtura());
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            
        } catch (SQLException e) {
            throw e;
        }
}


    public List<Progetto> getProgettiByLottoId(String idLotto) throws SQLException {
        List<Progetto> progetti = new ArrayList<>();
            
            final String SQL = "SELECT p.idProgetto, p.nome AS nomeProgetto, p.dataInizio, p.dataFine, p.budget, p.stagione, p.idLotto, p.cod_fisc_proprietario, " +
                            "l.nome AS nomeLotto, l.superficie, l.cod_fisc_proprietario AS l_cod_fisc_proprietario " + 
                            "FROM Progetto p JOIN Lotto l ON p.idLotto = l.idLotto WHERE p.idLotto = ?";
                            
            try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL)) {
                
                stmt.setString(1, idLotto);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        
                        
                        Proprietario proprietario = new Proprietario();
                        proprietario.setCod_fisc(rs.getString("cod_fisc_proprietario"));
                        
                        Lotto lotto = new Lotto(
                            rs.getString("idLotto"),
                            rs.getString("nomeLotto"),
                            rs.getFloat("superficie"),
                            null,
                            proprietario
                        );
                        
                                                
                        Progetto progetto = new Progetto(
                            rs.getString("idProgetto"),
                            rs.getString("nomeProgetto"),
                            rs.getObject("dataInizio", LocalDate.class),
                            rs.getObject("dataFine", LocalDate.class),
                            rs.getDouble("budget"),        
                            Stagione.valueOf(rs.getString("stagione")),    
                            lotto,                                         
                            proprietario                                    
                        );
                        
                        progetti.add(progetto);
                    }
                }
            } catch (SQLException e) {
                throw e;
            }
            return progetti;
        }

    public void creaProgetto(Progetto progetto) throws SQLException {
        
        final String SQL = "INSERT INTO Progetto (idProgetto, nome, dataInizio, dataFine, budget, stagione, idLotto, cod_fisc_proprietario) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL)) {
            
            stmt.setString(1, progetto.getIdProgetto());
            stmt.setString(2, progetto.getNome());
            stmt.setObject(3, progetto.getDataInizio()); 
            
            if (progetto.getDataFine() != null) {
                stmt.setObject(4, progetto.getDataFine());
            } else {
                stmt.setNull(4, java.sql.Types.DATE); 
            }
            
            stmt.setDouble(5, progetto.getBudget());
            stmt.setString(6, progetto.getStagione().toString()); 
            
            stmt.setString(7, progetto.getLotto().getIdLotto());
            stmt.setString(8, progetto.getProprietario().getCod_fisc()); 
            
            if (stmt.executeUpdate() == 0) {
                throw new SQLException("Inserimento progetto fallito: nessuna riga affetta.");
            }
            
        } catch (SQLException e) {
            System.err.println("Errore DAO: Impossibile salvare il nuovo progetto.");
            throw e; 
        }
    }


    public boolean chiudiProgetto(String idProgetto, LocalDate dataFine) throws SQLException {
        
        final String SQL = "UPDATE Progetto SET dataFine = ? WHERE idProgetto = ? AND dataFine IS NULL";
        
        Date dataFineSQL = Date.valueOf(dataFine);
        int righeAffette = 0;

        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL)) {
            
            stmt.setDate(1, dataFineSQL);
            stmt.setString(2, idProgetto);

            righeAffette = stmt.executeUpdate();
            
        } catch (SQLException error) {
            System.err.println("Errore DAO: Impossibile chiudere il progetto con ID " + idProgetto + ". " + error.getMessage());
            throw error; 
        }
        
        return righeAffette == 1;
    }


        public void addColtivatori(String idProgetto, List<Coltivatore> coltivatori) throws SQLException {
            
            final String SQL_PONTE = "INSERT INTO ColtivatoreProgetto (idProgetto, cod_fisc_coltivatore) VALUES (?, ?)";
            
            try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_PONTE)) {
                    
                for (Coltivatore c : coltivatori) {
                    stmt.setString(1, idProgetto);
                    stmt.setString(2, c.getCod_fisc());
                    stmt.addBatch();
                }
                
                stmt.executeBatch();
                
            } catch (SQLException e) {
                System.err.println("Errore DAO Progetto: Impossibile assegnare i coltivatori al progetto.");
                throw e;
            }
        }


        public void assegnaColtureAProgetto(String idProgetto, List<Coltura> colture) throws SQLException {
            
            final String SQL_PONTE = "INSERT INTO ProgettoColtura (idProgetto, idColtura) VALUES (?, ?)";
            
            try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_PONTE)) {
                    
                for (Coltura c : colture) {
                    stmt.setString(1, idProgetto);
                    stmt.setString(2, c.getIdColtura());
                    stmt.addBatch();
                }
                
                int[] results = stmt.executeBatch(); 
                
            } catch (SQLException e) {
                System.err.println("Errore DAO Progetto: Impossibile assegnare le colture al progetto.");
                throw e;
            }
        }

}


    
            