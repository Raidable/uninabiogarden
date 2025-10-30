package com.uninabiogardenoo65.controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.SwingUtilities;

import com.uninabiogardenoo65.boundary.LoginView;
import com.uninabiogardenoo65.dao.AttivitaDAO;
import com.uninabiogardenoo65.dao.ColtivatoreDAO;
import com.uninabiogardenoo65.dao.ColturaDAO;
import com.uninabiogardenoo65.dao.LottoDAO;
import com.uninabiogardenoo65.dao.ProgettoDAO;
import com.uninabiogardenoo65.dao.ProprietarioDAO;
import com.uninabiogardenoo65.dao.RaccoltoDAO;
import com.uninabiogardenoo65.dao.UtenteDAO;
import com.uninabiogardenoo65.entity.Attivita;
import com.uninabiogardenoo65.entity.Coltivatore;
import com.uninabiogardenoo65.entity.Coltura;
import com.uninabiogardenoo65.entity.Lotto;
import com.uninabiogardenoo65.entity.Progetto;
import com.uninabiogardenoo65.entity.Proprietario;
import com.uninabiogardenoo65.entity.Raccolto;
import com.uninabiogardenoo65.entity.RaccoltoDati;
import com.uninabiogardenoo65.entity.Utente;
import com.uninabiogardenoo65.enums.Qualita;
import com.uninabiogardenoo65.enums.Stato;

public class Controller {
	
	private ColtivatoreDAO coltivatoreDAO;
	private ProprietarioDAO propDAO;
	private AttivitaDAO attivitaDAO;
	private LottoDAO lottoDAO;
	private RaccoltoDAO raccoltoDAO;
	private ColturaDAO colturaDAO;
	private ProgettoDAO progettoDAO;
	private UtenteDAO utenteDAO;
	
	
	public Controller(){
		DBManager.testConnection();
        
        coltivatoreDAO = new ColtivatoreDAO(this);
        propDAO = new ProprietarioDAO(this);
        attivitaDAO = new AttivitaDAO(this);
        lottoDAO = new LottoDAO(this);
        raccoltoDAO = new RaccoltoDAO(this);
        colturaDAO = new ColturaDAO(this);
        progettoDAO = new ProgettoDAO(this);
        utenteDAO = new UtenteDAO(this);
		
	}
	

    public static String generaIdUnivoco() {
        return UUID.randomUUID().toString();
    }
    
    
    public static String generaIdUnivoco(String prefisso) {
        return prefisso + "_" + UUID.randomUUID().toString();
    }
    


    public boolean chiudiProgetto(String idProgetto) throws SQLException {
        
        if (idProgetto == null || idProgetto.trim().isEmpty()) {
            return false;
        }
        
        LocalDate dataFineOggi = LocalDate.now();
        
        try {
            boolean progettoAggiornato = progettoDAO.chiudiProgetto(idProgetto, dataFineOggi);
            
            if (progettoAggiornato) {
                boolean attivitaSospese = attivitaDAO.sospendiAttivitaAttive(idProgetto, dataFineOggi);
                
                return progettoAggiornato && attivitaSospese;
            }
            
            return false;
            
        } catch (SQLException e) {
            throw new SQLException("Errore transazionale: Chiusura progetto fallita. " + e.getMessage(), e);
        }
    }
        

    public void creaProgetto(Progetto progetto, List<Coltivatore> coltivatori) throws SQLException {
        
        if (progetto == null || progetto.getProprietario() == null || progetto.getLotto() == null) {
            throw new IllegalArgumentException("Impossibile creare il progetto: mancano dati essenziali (Proprietario o Lotto).");
        }
        
        progettoDAO.creaProgetto(progetto);
        
        if (coltivatori != null && !coltivatori.isEmpty()) {
            
            progettoDAO.addColtivatori(progetto.getIdProgetto(), coltivatori); 
        }
        
    }

    public List<Progetto> getProgettiByLotto(String idLotto) throws SQLException {
        
        if (idLotto == null || idLotto.trim().isEmpty()) {
            throw new IllegalArgumentException("ID Lotto non valido per il filtro.");
        }
        
        return progettoDAO.getProgettiByLottoId(idLotto); 
    }

    public List<Coltivatore> getColtivatoriDisponibili(String idProgetto) throws SQLException {
        List<Coltivatore> coltivatori = new ArrayList<>();
        String sql = "SELECT u.cod_fisc, u.email, u.password, u.nome, u.cognome, u.dataN, u.telefono " +
                    "FROM Utente u " +
                    "JOIN Coltivatore c ON u.cod_fisc = c.cod_fisc " +
                    "WHERE c.cod_fisc NOT IN (" +
                    "    SELECT cp.cod_fisc_coltivatore " +
                    "    FROM ColtivatoreProgetto cp " +
                    "    WHERE cp.idProgetto = ?" +
                    ") " +
                    "ORDER BY u.cognome, u.nome";
        
        try (Connection conn = DBManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idProgetto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date sqlDate = rs.getDate("dataN");
                    LocalDate localDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                    
                    Coltivatore coltivatore = new Coltivatore(
                        rs.getString("cod_fisc"), 
                        rs.getString("email"), 
                        rs.getString("password"),
                        rs.getString("nome"), 
                        rs.getString("cognome"), 
                        localDate, 
                        rs.getString("telefono")
                    );
                    
                    coltivatori.add(coltivatore);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei coltivatori disponibili: " + e.getMessage());
            throw e;
        }
        return coltivatori;
    }

    public List<Attivita> getAttivitaByProgetto(Progetto progetto) throws SQLException {
        return attivitaDAO.getAttivitaByProgetto(progetto.getIdProgetto());
    }


    public boolean deleteAttivita(String idAttivita) throws SQLException {
        return attivitaDAO.deleteAttivita(idAttivita);
    }

    public Attivita getAttivitaById(String idAttivita) throws SQLException {
        return attivitaDAO.getAttivitaById(idAttivita);
    }


    public boolean removeColtivatoreFromProgetto(String idColtivatore, String idProgetto) throws SQLException {
        return progettoDAO.removeColtivatoreFromProgetto(idColtivatore, idProgetto);
    }

    public boolean addColtivatoreToProgetto(String idColtivatore, String idProgetto) throws SQLException {
        return progettoDAO.addColtivatoreToProgetto(idColtivatore, idProgetto);
    }


    public void updateStatoAttivita(String idAttivita, Stato nuovoStato) throws SQLException {
            
            if (idAttivita == null || idAttivita.trim().isEmpty() || nuovoStato == null) {
                throw new IllegalArgumentException("ID attività o nuovo stato non validi per l'aggiornamento.");
            }
            
            attivitaDAO.updateStato(idAttivita, nuovoStato);
        }

    public List<Progetto> getProgettiByColtivatoreCodFisc(String codFiscColtivatore) throws SQLException {
        if (codFiscColtivatore == null || codFiscColtivatore.trim().isEmpty()) {
            throw new IllegalArgumentException("Codice Fiscale Coltivatore mancante.");
        }

        return progettoDAO.getProgettiByColtivatoreCodFisc(codFiscColtivatore);
    }

    public List<Attivita> getAttivitaColtivatoreByCodFisc(String codFiscColtivatore) throws SQLException {
        if (codFiscColtivatore == null || codFiscColtivatore.trim().isEmpty()) {
            throw new IllegalArgumentException("Codice Fiscale Coltivatore mancante.");
        }

        return attivitaDAO.getAttivitaByColtivatoreCodFisc(codFiscColtivatore);
    }

    public Utente autenticaUtente(String email, String password) throws SQLException {
            
        Utente utente = utenteDAO.findUtenteEConRuoloByCredentials(email, password);

        return utente;
    }
    
    

	public Proprietario registraProprietario(String codFisc, String email, String password, String nome, String cognome,
			LocalDate dataN, String telefono) {
		
		return null;
	}


	public List<Progetto> getProgettiByProprietario(Proprietario proprietario) throws SQLException {
        
        if (proprietario == null || proprietario.getCod_fisc() == null) {
            throw new IllegalArgumentException("Il proprietario non è valido o non è autenticato.");
        }
        
        String codFiscProprietario = proprietario.getCod_fisc();
        
        return progettoDAO.getProgettiOfProprietarioByCodFisc(codFiscProprietario);
    }


    public Progetto getProgettoById(String idProgetto) throws SQLException {
        
        if (idProgetto == null || idProgetto.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID del progetto non può essere vuoto.");
        }
        
        return progettoDAO.getProgettoById(idProgetto);
    }


    public Lotto getLottoById(String idLotto) throws SQLException {
            if (idLotto == null || idLotto.trim().isEmpty()) {
                throw new IllegalArgumentException("L'ID del lotto non può essere vuoto.");
            }
            
            return lottoDAO.getLottoById(idLotto);
        }



    public void updateStato(String idAttivita, Stato nuovoStato) throws SQLException {
        
        // 1. Validazione di Controllo
        if (idAttivita == null || idAttivita.trim().isEmpty() || nuovoStato == null) {
            throw new IllegalArgumentException("ID attività o nuovo stato non validi per l'aggiornamento.");
        }
        
        // 2. DELEGAZIONE AL DAO (chiamata al metodo che esegue l'UPDATE)
        attivitaDAO.updateStato(idAttivita, nuovoStato);
    }

    public List<Lotto> getLottiByProprietario(Proprietario proprietario) throws SQLException {
        
        if (proprietario == null || proprietario.getCod_fisc() == null || proprietario.getCod_fisc().trim().isEmpty()) {
            throw new IllegalArgumentException("Il proprietario non è valido. Codice Fiscale mancante.");
        }
        
        String codFiscProprietario = proprietario.getCod_fisc();
        
        return lottoDAO.getLottiOfProprietarioByCodFisc(codFiscProprietario);
    }

    public List<Coltivatore> getColtivatoriByProgetto(String idProgetto) throws SQLException {
            if (idProgetto == null || idProgetto.trim().isEmpty()) {
                throw new IllegalArgumentException("L'ID del progetto non è valido.");
            }
            return coltivatoreDAO.getColtivatoriByProgetto(idProgetto);
        }

    public void createAttivita(Attivita attivita) throws SQLException {
            
           attivitaDAO.createAttivita(attivita);

        }
        

    public void updateAttivita(Attivita attivita) throws SQLException {
        
        if (attivita == null || attivita.getIdAttivita() == null || attivita.getIdAttivita().trim().isEmpty()) {
            throw new IllegalArgumentException("Impossibile aggiornare. L'ID dell'Attività è mancante o non valido.");
        }
        
        
        attivitaDAO.updateAttivita(attivita);
    }


    public List<Coltura> getColtureByLottoId(String idLotto) throws SQLException {
            
            if (idLotto == null || idLotto.trim().isEmpty()) {
                throw new IllegalArgumentException("L'ID Lotto non può essere vuoto per la ricerca delle colture.");
            }
            

            return colturaDAO.getColtureByLottoId(idLotto);
        }

    public List<Coltivatore> getAllColtivatori() throws SQLException {
        return coltivatoreDAO.getAll(); 
    }


    public void updateAttivitaAndRecordRaccolto(Attivita attivita, double quantitaEffettiva, Qualita qualitaRaccolto) throws SQLException {
        
        attivitaDAO.updateAttivita(attivita); 
        
        
        if (attivita.getNome().equals("Raccolta") && attivita.getStato() == Stato.Finito) {
            
            if (quantitaEffettiva <= 0.0 || qualitaRaccolto == null || attivita.getDataFine() == null) {
                throw new IllegalArgumentException("Dati Raccolto incompleti o non validi per la registrazione finale.");
            }
            
            Raccolto nuovoRaccolto = new Raccolto(
                quantitaEffettiva,
                qualitaRaccolto,
                attivita.getDataFine(),
                attivita.getColtura().getIdColtura()
            );
            
            raccoltoDAO.save(nuovoRaccolto);
        }
        
    }

    public List<RaccoltoDati> getReportDataByIdLotto(String idLotto) throws SQLException {
        if (idLotto == null || idLotto.trim().isEmpty()) {
            throw new IllegalArgumentException("ID Lotto non valido per il report.");
        }
        return raccoltoDAO.getHarvestStatisticsForLotto(idLotto); 
    }

	public static void main(String[] args) {
			
        final Controller controller = new Controller();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    LoginView loginView = new LoginView(controller);
                    
                    loginView.setVisible(true);
                    
                } catch (Exception e) {
                    System.err.println("Errore fatale durante l'avvio della Login View.");
                    e.printStackTrace();
                }
            }
        });
        
		
	       
	}

    
    
}
