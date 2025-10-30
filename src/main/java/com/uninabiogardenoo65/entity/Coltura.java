package com.uninabiogardenoo65.entity;

import java.time.LocalDate;
import java.util.ArrayList;

public class Coltura {
	
	
	private String idColtura;
	private String nome;
	private int tempoDiMaturazione;
	private LocalDate dataSeminazione;
	private ArrayList<Raccolto> raccolti;
	private double quantitaSeminataM2;
	private String idLotto;
	private LocalDate dataPrevistaRaccolta;
	
	
	public Coltura(String idColtura, String nome, int tempoDiMaturazione, LocalDate dataSeminazione, String idLotto){
		this.idColtura = idColtura;
		this.nome = nome;
		this.tempoDiMaturazione = tempoDiMaturazione;
		this.dataSeminazione = dataSeminazione;
		this.raccolti = new ArrayList<>();
		this.idLotto = idLotto;
		this.dataPrevistaRaccolta = dataSeminazione.plusDays(tempoDiMaturazione);
	}
	

	public Coltura(String idColtura, String nome, int tempoDiMaturazione, LocalDate dataSeminazione, ArrayList<Raccolto> raccolti, String idLotto, double quantitaSeminataM2){
		
		this.idColtura = idColtura;
		this.nome = nome;
		this.tempoDiMaturazione = tempoDiMaturazione;
		this.dataSeminazione = dataSeminazione;
		this.raccolti = raccolti;
		this.idLotto = idLotto;
		this.dataPrevistaRaccolta = dataSeminazione.plusDays(tempoDiMaturazione);
	}

	public Coltura(){
		this.raccolti = new ArrayList<>();
	}
	
	public String getIdColtura() {
		return idColtura;
	}

	public void setIdColtura(String idColtura) {
		this.idColtura = idColtura;
	}


	public String getIdLotto() {
		return idLotto;
	}


	public void setIdLotto(String idLotto) {
		this.idLotto = idLotto ;
	}


	public int getTempoDiMaturazione() {
		return tempoDiMaturazione;
	}

	public void setTempoDiMaturazione(int tempoDiMaturazione) {
		this.tempoDiMaturazione = tempoDiMaturazione;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataSeminazione() {
		return dataSeminazione;
	}

	public void setDataSeminazione(LocalDate dataSeminazione) {
		this.dataSeminazione = dataSeminazione;
	}

	public ArrayList<Raccolto> getRaccolti() {
		return raccolti;
	}
	
	

	public LocalDate getDataPrevistaRaccolta() {
		return dataPrevistaRaccolta;
	}

    public double getQuantitaSeminataM2() {
        return this.quantitaSeminataM2;
    }

    public void setQuantitaSeminataM2(double quantitaSeminataM2) {
        this.quantitaSeminataM2 = quantitaSeminataM2;
    }


	
	@Override
	public String toString() {
		return this.nome;
	}


}
