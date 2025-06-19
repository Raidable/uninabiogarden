package com.uninabiogarden;
import java.time.LocalDate;

public class Richiesta {

    private String messaggioCandidatura;
    private LocalDate dataRichiesta;
    private String stato; // In attesa, Accettata, Rifiutata
    private String motivoRifiuto = null;
    private ProgettoStagionale progettoStagionale;

    public Richiesta(String messaggio, ProgettoStagionale progettoStagionale) {
        this.messaggioCandidatura = messaggio;
        this.dataRichiesta = LocalDate.now();
        this.progettoStagionale = progettoStagionale;
        this.stato = "In attesa"; // Stato iniziale
    }

    public ProgettoStagionale getProgettoStagionale() {
        return progettoStagionale;
    }

    public String getMessaggioCandidatura() {
        return messaggioCandidatura;
    }

    public LocalDate getDataRichiesta() {
        return dataRichiesta;
    }

    public void setDataRichiesta(LocalDate dataRichiesta) {
        this.dataRichiesta = dataRichiesta;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getMotivoRifiuto() {
        return motivoRifiuto;
    }

    public void setMotivoRifiuto(String motivoRifiuto) {
        this.motivoRifiuto = motivoRifiuto;
    }







}
