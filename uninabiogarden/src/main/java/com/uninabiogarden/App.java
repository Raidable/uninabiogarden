package com.uninabiogarden;

/**
 * Hello world!
 *
 */

import java.time.LocalDate;

public class App 
{
    public static void main( String[] args )
    {

        Utente utente1 = new Utente("Mario", "Rossi", "Via Roma 1", "Roma", "mario.rossi@example.com", "password123", "1234567890");
        Coltivatore coltivatore1 = new Coltivatore("Luca", "Bianchi", "Via Milano 2", "Milano", "luca.bianchi@example.com", "password456", "0987654321");
        Proprietario proprietario1 = new Proprietario("Giulia", "Verdi", "Via Napoli 3", "Napoli", "giulia.verdi@example.com", "password789", "1122334455");

        ProgettoStagionale prog1 = proprietario1.creaProgettoStagionale("Progetto 1", "Descrizione Progetto 1", LocalDate.now(), LocalDate.now().plusMonths(6));
        ProgettoStagionale prog2 = proprietario1.creaProgettoStagionale("Progetto 2", "Descrizione Progetto 2", LocalDate.now(), LocalDate.now().plusMonths(3));   
        ProgettoStagionale prog3 = proprietario1.creaProgettoStagionale("Progetto 3", "Descrizione Progetto 3", LocalDate.now(), LocalDate.now().plusMonths(1));

        /* 
        for (ProgettoStagionale progetto : proprietario1.getProgettiStagionali()) {
            System.out.println("Progetto creato: " + progetto.getDescrizione());
        }   
        */

        coltivatore1.effettuaRichiesta("Voglio candidarmi", prog1);

    }
}
