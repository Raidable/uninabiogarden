package com.uninabiogardenoo65.entity;

import java.time.LocalDate;
import java.util.ArrayList;

import com.uninabiogardenoo65.controller.Controller;

public class Coltivatore extends Utente{
	
	private ArrayList<Attivita> attivita;
	private ArrayList<Progetto> progetti;
	
	
	public Coltivatore(String cod_fisc, String email, String password, String nome, String cognome, LocalDate dataN, String telefono) {
		super(cod_fisc, email, password, nome, cognome, dataN, telefono);
		
	}

	public Coltivatore(String email, String password, String nome, String cognome, LocalDate dataN, String telefono) {
		super(Controller.generaIdUnivoco("coltivatore"), email, password, nome, cognome, dataN, telefono);
		
	}

	public Coltivatore() {
		super();
		this.attivita = new ArrayList<>();
		this.progetti = new ArrayList<>();
	}
	
	public ArrayList<Progetto> getProgetti() {
		return progetti;
	}

	
	public void addProgetto(Progetto progetto) {
		
		if (!this.progetti.contains(progetto)) {
			this.progetti.add(progetto);
			//progetto.aggiungiColtivatore(this);
		}
	}
	
	
    public void rimuoviProgetto(Progetto progetto) {
        if (!this.progetti.contains(progetto)) {
            this.progetti.remove(progetto);
        }
    }

	
	public void setProgetti(ArrayList<Progetto> progetti) {
		this.progetti = progetti;
	}


	public ArrayList<Attivita> getAttivita() {
		return attivita;
	}


	public void aggiungiAttivita(Attivita attivita) {
		if (!this.attivita.contains(attivita)) {
			this.attivita.add(attivita);
		}
	}
	
	public void rimuoviAttivita(Attivita attivita) {
		if (!this.attivita.contains(attivita)) {
			this.attivita.remove(attivita);
		}
	}
	
	@Override
	public String toString() {
		// Restituisce Nome e Cognome per la visualizzazione nella ComboBox
		return this.getNome() + " " + this.getCognome();
	}

}
