package com.uninabiogardenoo65.entity;

public class RaccoltoDati {
    
    private String nomeLotto;
    private String nomeColtura;
    private long totaleRaccolte;
    private double quantitaMedia;
    private double quantitaMinima;
    private double quantitaMassima;
    private double quantitaTotale; 

    public RaccoltoDati(String nomeLotto, String nomeColtura, long totalRaccolte, 
                             double quantitaMedia, double quantitaMinima, 
                             double quantitaMassima, double quantitaTotale) {
        this.nomeLotto = nomeLotto;
        this.nomeColtura = nomeColtura;
        this.totaleRaccolte = totalRaccolte;
        this.quantitaMedia = quantitaMedia;
        this.quantitaMinima = quantitaMinima;
        this.quantitaMassima = quantitaMassima;
        this.quantitaTotale = quantitaTotale;
    }

    public String getNomeLotto() { 
        return nomeLotto; 
    }

    public String getNomeColtura() { 
        return nomeColtura; 
    }

    public long getTotaleRaccolte() { 
        return totaleRaccolte; 
    }

    public double getQuantitaMedia() { 
        return quantitaMedia; 
    }

    public double getQuantitaMinima() {
        return quantitaMinima; 
        }

    public double getQuantitaMassima() { 
        return quantitaMassima; 
    }

    public double getQuantitaTotale() { return quantitaTotale; }


}