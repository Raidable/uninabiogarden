package com.uninabiogarden;

public class Lotto {

    private String nome; // Nome del lotto
    private double superficie; // In metri quadrati
    private String coordinate; // Formato: "latitudine,longitudine"
    private String stato; // Stato del lotto (es. "libero", "occupato", "in coltivazione")
    private String tipoTerreno; // Tipo di terreno (es. "sabbioso", "argilloso", "limoso")

    public Lotto(String nome, double superficie, String coordinate, String stato, String tipoTerreno) {
        this.nome = nome;
        this.superficie = superficie; 
        this.coordinate = coordinate;
        this.stato = stato;
        this.tipoTerreno = tipoTerreno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getTipoTerreno() {
        return tipoTerreno;
    }

    public void setTipoTerreno(String tipoTerreno) {
        this.tipoTerreno = tipoTerreno;
    }


}
