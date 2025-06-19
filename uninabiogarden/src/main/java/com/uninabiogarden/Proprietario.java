package com.uninabiogarden;

import java.time.LocalDate;
import java.util.ArrayList;

public class Proprietario extends Utente{

    private ArrayList<ProgettoStagionale> progettiStagionali = new ArrayList<>();




    public Proprietario(String nome, String cognome, String indirizzo, String citta, String email, String password, String telefono) {
        super(nome, cognome, indirizzo, citta, email, password, telefono);
    }

    public ArrayList<ProgettoStagionale> getProgettiStagionali() {
        return progettiStagionali;
    }

    public ProgettoStagionale creaProgettoStagionale(String nome, String descrizione, LocalDate dataInizio, LocalDate dataFine) {
        
        ProgettoStagionale nuovoProgetto = new ProgettoStagionale(nome, descrizione, dataInizio, dataFine);
        this.progettiStagionali.add(nuovoProgetto); // Aggiunge il nuovo progetto alla lista dei progetti stagionali del proprietario
        nuovoProgetto.setProprietario(this); // Imposta il proprietario del progetto

        return nuovoProgetto;
    }

}
