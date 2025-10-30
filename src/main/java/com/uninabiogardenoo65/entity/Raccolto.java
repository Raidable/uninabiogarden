package com.uninabiogardenoo65.entity;

import java.time.LocalDate;

import com.uninabiogardenoo65.controller.Controller;
import com.uninabiogardenoo65.enums.Qualita;


public class Raccolto {
	private String idRaccolto;
	private double quantitaEffettiva;
	private Qualita qualita;
	private LocalDate dataRaccolto;
	private String idColtura;
	
	
	public Raccolto(String idRaccolto, float quantitaEffettiva, Qualita qualita, LocalDate dataRaccolto, String idColtura){
		this.idRaccolto = idRaccolto;
		this.quantitaEffettiva = quantitaEffettiva;
		this.qualita = qualita;
		this.dataRaccolto = dataRaccolto;
		this.idColtura = idColtura;
	}
	
	
	public Raccolto(double quantitaEffettiva, Qualita qualita, LocalDate dataRaccolto, String idColtura){
		this.idRaccolto = Controller.generaIdUnivoco();
		this.quantitaEffettiva = quantitaEffettiva;
		this.qualita = qualita;
		this.dataRaccolto = dataRaccolto;
		this.idColtura = idColtura;
	}
	
	public Raccolto(double quantitaEffettiva, Qualita qualita){
		this.idRaccolto = Controller.generaIdUnivoco();
		this.quantitaEffettiva = quantitaEffettiva;
		this.qualita = qualita;
		this.dataRaccolto = LocalDate.now();
		this.idColtura = null;
	}
	

	public String getIdRaccolto() {
		return idRaccolto;
	}


	public void setIdRaccolto(String idRaccolto) {
		this.idRaccolto = idRaccolto;
	}


	public double getQuantitaEffettiva() {
		return this.quantitaEffettiva;
	}


	public void setQuantitaEffettiva(double quantitaEffettiva) {
		this.quantitaEffettiva = quantitaEffettiva;
	}



	public Qualita getQualita() {
		return qualita;
	}



	public void setQualita(Qualita qualita) {
		this.qualita = qualita;
	}




	public LocalDate getDataRaccolto() {
		return dataRaccolto;
	}


	public void setDataRaccolto(LocalDate dataRaccolto) {
		this.dataRaccolto = dataRaccolto;
	}



	public String getIDColtura() {
		return this.idColtura;
	}


	public void setColtura(String idColtura) {
		this.idColtura = idColtura;
	}
	
	
	
	
}
