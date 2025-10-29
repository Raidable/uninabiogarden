package com.uninabiogardenoo65.entity;

import java.util.ArrayList;

import com.uninabiogardenoo65.controller.Controller;

public class Lotto {
	
	private String idLotto;
	private String nome;
	private float superficie;
	private Progetto progetto;
	private ArrayList<Coltura> colture;
	private Proprietario proprietario;
	
	
	public Lotto(String nome, float superficie, Progetto progetto, Proprietario proprietario){
		this.idLotto = Controller.generaIdUnivoco("lotto");
		this.nome = nome;
		this.superficie = superficie;
		this.colture = new ArrayList<>();
		this.progetto = progetto;
		this.proprietario = proprietario;
	}
	
	
	public Lotto(String idLotto, String nome, float superficie, Progetto progetto,  Proprietario proprietario){
		this.idLotto = idLotto;
		this.nome = nome;
		this.superficie = superficie;
		this.colture = new ArrayList<>();
		this.proprietario = proprietario;
		this.progetto = progetto;
	}
	
	public Lotto(){};


	
	public String getIdLotto() {
		return idLotto;
	}
	
	public void setIdLotto(String idLotto) {
		this.idLotto = idLotto;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public float getSuperficie() {
		return superficie;
	}
	
	public void setSuperficie(float superficie) {
		this.superficie = superficie;
	}
	
	public Progetto getProgetto() {
		return progetto;
	}
	
	public void setProgetto(Progetto progetto) {
		this.progetto = progetto;
	}
	
	public ArrayList<Coltura> getColture() {
		return colture;
	}
	


	public Proprietario getProprietario() {
		return proprietario;
	}


	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}
	
	
	@Override
    public String toString() {
        return this.nome;
    }
	
}
