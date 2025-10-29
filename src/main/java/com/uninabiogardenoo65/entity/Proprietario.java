package com.uninabiogardenoo65.entity;

import java.time.LocalDate;
import java.util.ArrayList;

import com.uninabiogardenoo65.controller.Controller;

public class Proprietario extends Utente{
	
	private String pIva;
	private ArrayList<Lotto> lottiPosseduti;
	
	public Proprietario(String cod_fisc, String email, String password, String nome, String cognome, LocalDate dataN,
			String telefono, String pIva) {
		
		super(cod_fisc, email, password, nome, cognome, dataN, telefono);
		this.setpIva(pIva);
	}
	
	
	public Proprietario(String email, String password, String nome, String cognome, LocalDate dataN,
			String telefono, String pIva) {
		super(Controller.generaIdUnivoco("proprietario"), email, password, nome, cognome, dataN, telefono);
		this.setpIva(pIva);
	}
	
	
	public Proprietario(){
		super();
	};
	

	public String getpIva() {
		return pIva;
	}

	public void setpIva(String pIva) {
		this.pIva = pIva;
	}

	
	public ArrayList<Lotto> getLottiPosseduti() {
		return lottiPosseduti;
	}

	public void addLotto(Lotto lotto) {
		if (!this.lottiPosseduti.contains(lotto)) {
			this.lottiPosseduti.add(lotto);
			lotto.setProprietario(this);
		}
	}
	
	public void rimuoviLotto(Lotto lotto) {
		if (!this.lottiPosseduti.contains(lotto)) {
			this.lottiPosseduti.remove(lotto);
			lotto.setProprietario(null);
		}
	}
	
	public void setLottiPosseduti(ArrayList<Lotto> lottiPosseduti) {
		this.lottiPosseduti = lottiPosseduti;
	}
	

	
	
	
}
