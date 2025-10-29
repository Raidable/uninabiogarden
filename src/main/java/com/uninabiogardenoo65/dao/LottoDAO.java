package com.uninabiogardenoo65.dao;



import com.uninabiogardenoo65.dao.LottoDAO;
import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.controller.DBManager;
import com.uninabiogardenoo65.entity.Lotto;
import com.uninabiogardenoo65.entity.Proprietario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class LottoDAO {

    
    private Controller controller;

    public LottoDAO(Controller controller){
        this.controller = controller;
    }


    
    public List<Lotto> getLottiOfProprietarioByCodFisc(String codFiscProprietario) throws SQLException {
            List<Lotto> lotti = new ArrayList<>();
            
            final String SQL = "SELECT idLotto, nome, superficie, cod_fisc_proprietario FROM Lotto WHERE cod_fisc_proprietario = ?";
            
            try (Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL)) {
                
                stmt.setString(1, codFiscProprietario);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        
                        String idLotto = rs.getString("idLotto");
                        String nome = rs.getString("nome");
                        float superficie = rs.getFloat("superficie");
                        
                        Proprietario proprietarioPlaceholder = new Proprietario();
                        proprietarioPlaceholder.setCod_fisc(codFiscProprietario);
                        
                        Lotto lotto = new Lotto(
                            idLotto, 
                            nome, 
                            superficie, 
                            null, 
                            null
                        );
                        lotto.setProprietario(proprietarioPlaceholder);
                        
                        lotti.add(lotto);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Errore DAO: Impossibile recuperare i lotti per il proprietario " + codFiscProprietario);
                throw e; 
            }
            
            return lotti;
        }


    public Lotto getLottoById(String idLotto) throws SQLException {
        
        Lotto lotto = null;
        
        final String SQL = "SELECT idLotto, nome, superficie, cod_fisc_proprietario " +
                           "FROM Lotto WHERE idLotto = ?";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
             
            stmt.setString(1, idLotto); 
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    
                    String nomeLotto = rs.getString("nome");
                    float superficie = rs.getFloat("superficie");
                    
                    lotto = new Lotto(
                        idLotto, 
                        nomeLotto, 
                        superficie, 
                        null,
                        null
                    );
                    
                    
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore DAO: Impossibile recuperare il lotto con ID " + idLotto);
            throw e; 
        }
        
        return lotto;
    }


}
