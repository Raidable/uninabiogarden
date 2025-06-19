package com.uninabiogarden;

public class Coltura {

    private String tipoColtura;
    private String varieta;
    private int tempoMaturazione; // In giorni
    private double temperaturaMin;
    private double temperaturaMax;
    private double phIdeale;
    

    public Coltura(String tipoColtura, String varieta, int tempoMaturazione, double temperaturaMin, double temperaturaMax, double phIdeale) {
        this.tipoColtura = tipoColtura;
        this.varieta = varieta;
        this.tempoMaturazione = tempoMaturazione;
        this.temperaturaMin = temperaturaMin;
        this.temperaturaMax = temperaturaMax;
        this.phIdeale = phIdeale;
    }

    public int getTempoMaturazione() {
        return tempoMaturazione;
    }

    public void setTempoMaturazione(int tempoMaturazione){
        this.tempoMaturazione = tempoMaturazione;
    }

    public String getTipoColtura() {
        return tipoColtura;
    }

    public void setTipoColtura(String tipoColtura) {
        this.tipoColtura = tipoColtura;
    }

    public String getVarieta() {
        return varieta;
    }

    public void setVarieta(String varieta) {
        this.varieta = varieta;
    }

    public double getTemperaturaMin() {
        return temperaturaMin;
    }

    public void setTemperaturaMin(double temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }

    public double getTemperaturaMax() {
        return temperaturaMax;
    }

    public void setTemperaturaMax(double temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }

    public double getPhIdeale() {
        return phIdeale;
    }

    public void setPhIdeale(double phIdeale) {
        this.phIdeale = phIdeale;
    }



}
