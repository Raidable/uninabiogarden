package com.uninabiogardenoo65.entity;

import java.time.LocalDate;

import com.uninabiogardenoo65.controller.Controller;

public abstract class Utente {

	private String cod_fisc;
	private String email;
	private String password;
	private String nome;
	private String cognome;
	private LocalDate dataN;
	private String telefono;
	
	
	public Utente(String cod_fisc, String email, String password, String nome, String cognome, LocalDate dataN, String telefono){
		this.cod_fisc = cod_fisc;
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.dataN = dataN;
		this.telefono = telefono;
	}

	
	public Utente(String email, String password, String nome, String cognome, LocalDate dataN, String telefono){
		this.cod_fisc = Controller.generaIdUnivoco();
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.dataN = dataN;
		this.telefono = telefono;
	}
	

	public Utente(){};

	public String getCod_fisc() {
		return cod_fisc;
	}


	public void setCod_fisc(String cod_fisc) {
		this.cod_fisc = cod_fisc;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public String getCognome() {
		return cognome;
	}



	public void setCognome(String cognome) {
		this.cognome = cognome;
	}



	public LocalDate getDataN() {
		return dataN;
	}



	public void setDataN(LocalDate dataN) {
		this.dataN = dataN;
	}



	public String getTelefono() {
		return telefono;
	}



	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	
	
}
