
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Systemes repartis
 * TP 
 * Tchat
 * 
 * Classe correspondant au thread d'envoi de messages
 * au niveau du client
 *  
 * @author Wassim
 * @version 1.0
 */
public class ClientEnvoyerMessage implements Runnable {

	private String nomClient;
	private PrintWriter ecritureMessage;
	private BufferedReader saisieClavier; 

	public ClientEnvoyerMessage(String nom, PrintWriter ps) {
		this.nomClient = nom;
		this.ecritureMessage = ps;
		this.saisieClavier = new BufferedReader(new InputStreamReader(System.in)); 
	}

	public void run () {
		String message;
		boolean fini = false;
		while(!fini) {
			try {
				//System.out.println("tapez le message a envoyer : ");
				message = saisieClavier.readLine(); 
				message = nomClient + " > " + message;
				//System.out.println("envoi en cours de " +  message + "...");
				ecritureMessage.println(message);
				ecritureMessage.flush();
				if(message!=null && message.endsWith("FIN$"))
				    fini = true;
			}
			catch (IOException e) {
				System.out.println("IOException: " + e);
				e.printStackTrace();
			}
		}
	}
}
