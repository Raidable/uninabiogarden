package com.uninabiogarden;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProgettoStagionale {

    private String nomeProgetto;
    private String descrizione;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private String stato; // In corso, Finito
    private boolean accettaRichieste = true;


    private Proprietario proprietario;
    private ArrayList<Richiesta> richieste = new ArrayList<>();

    public ProgettoStagionale(String nomeProgetto, String descrizione, LocalDate dataInizio, LocalDate dataFine) {
        this.nomeProgetto = nomeProgetto;
        this.descrizione = descrizione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    // Getters e Setters


    public ArrayList<Richiesta> getRichieste() {
        return richieste;
    }

    public void aggiungiRichiesta(Richiesta richiesta) {

        if (this.richieste.contains(richiesta)) {
            System.out.println("La richiesta è già presente nel progetto.");
            return;
        }

        this.richieste.add(richiesta);
    }


    public String getNomeProgetto() {
        return nomeProgetto;
    }

    public void setStato(String stato){
        this.stato = stato;
    }

    public String getStato(){
        return this.stato;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }


    public boolean getAccettaRichiesta() {
        return accettaRichieste;
    }

    public void setAccettaRichiesta(boolean accettaRichieste) {
        this.accettaRichieste = accettaRichieste;
    }

    public void setNomeProgetto(String nomeProgetto) {
        this.nomeProgetto = nomeProgetto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

}
