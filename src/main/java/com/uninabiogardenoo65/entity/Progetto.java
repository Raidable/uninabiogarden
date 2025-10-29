package com.uninabiogardenoo65.entity;

import java.time.LocalDate;
import java.util.ArrayList;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.enums.Stagione;

public class Progetto {

	private String idProgetto;
	private String nome;
	private LocalDate dataInizio;
	private LocalDate dataFine;
	private double budget;
	private Stagione stagione;
	private ArrayList<Coltivatore> coltivatori;
	private Proprietario proprietario;
	private Lotto lotto;
	
	
	public Progetto(String idProgetto, String nome, LocalDate dataInizio,
			double budget, Stagione stagione, Lotto lotto, Proprietario proprietario){
		
		this.idProgetto = idProgetto;
		this.nome = nome;
		this.dataInizio = dataInizio;
		this.dataFine = null;
		this.budget = budget;
		this.stagione = stagione;
		this.proprietario = proprietario;
		this.lotto = lotto;
		this.coltivatori = new ArrayList<>();
	
	}
	
	
	public Progetto(String nome, LocalDate dataInizio,
			double budget, Stagione stagione, Lotto lotto, Proprietario proprietario){
		
		this.idProgetto = Controller.generaIdUnivoco("Progetto");;
		this.nome = nome;
		this.dataInizio = dataInizio;
		this.dataFine = null;
		this.budget = budget;
		this.stagione = stagione;
		this.proprietario = proprietario;
		this.lotto = lotto;
		this.coltivatori = new ArrayList<>();
	
	}
	

	public Progetto(String nome, LocalDate dataInizio,
			double budget, Stagione stagione, Lotto lotto){
		
		this.idProgetto = Controller.generaIdUnivoco("Progetto");;
		this.nome = nome;
		this.dataInizio = dataInizio;
		this.dataFine = null;
		this.budget = budget;
		this.stagione = stagione;
		this.proprietario = null;
		this.lotto = lotto;
		this.coltivatori = new ArrayList<>();
	
	}

	
	
	public Progetto(String idProgetto, String nome, LocalDate dataInizio, LocalDate datafine,
			double budget, Stagione stagione, Lotto lotto, Proprietario proprietario){

		this.idProgetto = idProgetto;
		this.nome = nome;
		this.dataInizio = dataInizio;
		this.dataFine = datafine;
		this.budget = budget;
		this.stagione = stagione;
		this.proprietario = proprietario;
		this.lotto = lotto;
		this.coltivatori = new ArrayList<>();
	}
	

	public String getIdProgetto() {
		return idProgetto;
	}
	
	
	
	public void setIdProgetto(String idProgetto) {
		this.idProgetto = idProgetto;
	}
	
	public boolean isChiuso() {
        return this.dataFine != null;
    }
	
	
	public String getNome() {
		return nome;
	}
	
	
	public void setNome(String nome) {
		this.nome = nome;
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



	public double getBudget() {
		return budget;
	}



	public void setBudget(double budget) {
		this.budget = budget;
	}



	public Stagione getStagione() {
		return stagione;
	}



	public void setStagione(Stagione stagione) {
		this.stagione = stagione;
	}



	public ArrayList<Coltivatore> getColtivatori() {
		return coltivatori;
	}



	public void setColtivatori(ArrayList<Coltivatore> coltivatori) {
		this.coltivatori = coltivatori;
	}



	public Proprietario getProprietario() {
		return proprietario;
	}



	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}



	public Lotto getLotto() {
		return lotto;
	}



	public void setLotto(Lotto lotto) {
		this.lotto = lotto;
	}
	
	
	
	
	
}
