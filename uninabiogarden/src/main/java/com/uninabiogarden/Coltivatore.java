package com.uninabiogarden;

import java.util.ArrayList;

public class Coltivatore extends Utente {

    private ArrayList<Richiesta> richiesteEffettuate = new ArrayList<>();


    // Costruttore della classe Coltivatore che estende la classe Utente
    public Coltivatore(String nome, String cognome, String indirizzo, String citta, String email, String password, String telefono) {
        super(nome, cognome, indirizzo, citta, email, password, telefono);
    }


    // Metodo per aggiungere una richiesta alla lista delle richieste effettuate
    public void effettuaRichiesta(String messaggioRichiesta, ProgettoStagionale progettoStagionale) {

        
        if (progettoStagionale.getAccettaRichiesta() == false) {
            System.out.println("Il progetto non accetta più richieste.");
            return;
        }

        Richiesta richiesta = new Richiesta(messaggioRichiesta, progettoStagionale); // Crea una nuova richiesta con il messaggio e il progetto stagionale associato
        richiesteEffettuate.add(richiesta); // Aggiunge la richiesta alla lista delle richieste effettuate dal coltivatore
        progettoStagionale.aggiungiRichiesta(richiesta); // Aggiunge la richiesta al progetto stagionale
        
        System.out.println("Richiesta effettuata con successo per il progetto: " + progettoStagionale.getNomeProgetto());
    
    }

    public ArrayList<Richiesta> getRichieste() {
        return richiesteEffettuate; // Restituisce la lista delle richieste effettuate dal coltivatore
    }   
    

}
