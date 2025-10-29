package com.uninabiogardenoo65.entity;

import java.time.LocalDate;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.enums.Priorita;
import com.uninabiogardenoo65.enums.Stato;

public class Attivita {

	private String idAttivita;
	private String nome;
	private LocalDate dataInizio;
	private LocalDate dataFine;
	private Stato stato;
	private Priorita priorita;
	private double costo;
	private Coltivatore coltivatore;
	private Coltura coltura;
	private String idProgetto;
	
	public Attivita(String idAttivita, String nome, LocalDate dataInizio, LocalDate dataFine,
			Stato stato, Priorita priorita, double costo, Coltivatore coltivatore, Coltura coltura, String idProgetto){
		
		this.idAttivita = idAttivita;
		this.nome = nome;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.stato = stato;
		this.priorita = priorita;
		this.costo = costo;
		this.coltivatore = coltivatore;
		this.coltura = coltura;
		this.idProgetto = idProgetto;	
	}
	
	
	Attivita(String idAttivita, String nome, LocalDate dataInizio, LocalDate dataFine,
			double costo, Coltivatore coltivatore, Coltura coltura, String idProgetto){
		
		this.idAttivita = idAttivita;
		this.nome = nome;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.stato = Stato.InCorso;
		this.priorita = Priorita.Media;
		this.costo = costo;
		this.coltivatore = coltivatore;
		this.coltura = coltura;
		this.idProgetto = idProgetto;
		
	}
	
	
	public Attivita(String nome, LocalDate dataInizio, LocalDate dataFine,
			Priorita priorita, double costo, Coltivatore coltivatore, Coltura coltura, String idProgetto){
		
		this.idAttivita = Controller.generaIdUnivoco();
		this.nome = nome;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.stato = Stato.InCorso;
		this.priorita = priorita;
		this.costo = costo;
		this.coltivatore = coltivatore;
		this.coltura = coltura;
		this.idProgetto = idProgetto;
				
	}
	


    public String getIdAttivita() {
		return idAttivita;
	}
	

	public void setIdAttivita(String idAttivita) {
		this.idAttivita = idAttivita;
	}
	
	public String getIdProgetto() {
		return this.idProgetto;
	}

	public void setIdProgetto(String idProgetto){
		this.idProgetto = idProgetto;
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
	
	
	public Stato getStato() {
		return stato;
	}
	
	
	public void setStato(Stato stato) {
		this.stato = stato;
	}
	
	
	public Priorita getPriorita() {
		return priorita;
	}
	
	
	public void setPriorita(Priorita priorita) {
		this.priorita = priorita;
	}
	
	
	public double getCosto() {
		return costo;
	}
	
	
	public void setCosto(double costo) {
		this.costo = costo;
	}
	
	
	public Coltivatore getColtivatore() {
		return coltivatore;
	}
	
	
	public void setColtivatore(Coltivatore coltivatore) {
		this.coltivatore = coltivatore;
	}
	
	
	public Coltura getColtura() {
		return this.coltura;
	}
	
	public void setColtura(Coltura coltura) {
		this.coltura = coltura;
	}
	
	
	
	
}
