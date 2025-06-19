import java.util.Date;

public class Richiesta {

    private Date dataRichiesta;
    private String messaggio;
    private String stato; // In attesa, accettata, rifiutata

    public Richiesta(Date dataRichiesta, String messaggio) {
        this.dataRichiesta = dataRichiesta;
        this.messaggio = messaggio;
        this.stato = "In attesa"; // Stato iniziale
    }

    public Date getDataRichiesta() {
        return dataRichiesta;
    }

    public String getMessaggio(){
        return this.messaggio;
    }

    public void setStato(String stato){
        this.stato = stato;
    }


}
