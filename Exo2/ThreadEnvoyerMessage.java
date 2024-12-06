
import java.util.Vector;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Systemes repartis
 * TP 
 * Tchat P2P
 * 
 * Classe correspondant au thread d'envoi de messages
 * 
 * @author Nicolas
 * @version 1.0
 */
public class ThreadEnvoyerMessage implements Runnable {

	private String nomUtilisateur;
	//private PrintWriter ecritureMessage;
	private Vector<PrintWriter> lesNoeuds;
	private BufferedReader saisieClavier; 

	public ThreadEnvoyerMessage(String nom, Vector<PrintWriter> lesNoeuds) {
		this.nomUtilisateur = nom;
		this.lesNoeuds = lesNoeuds;
		this.saisieClavier = new BufferedReader(new InputStreamReader(System.in)); 
	}
	
	public void run () {
		boolean fini = false;
		String message = null;
		
		System.out.println("Bienvenue dans le tchat");
		
		try {
			while(!fini) {
				message = saisieClavier.readLine(); 
				
				if(message!=null && message.endsWith("FIN$")) {
					fini = true;
				}
				
				message = nomUtilisateur + " > " + message;

				//parcours de la liste lesNoeuds pour envoyer le message a tous
				for(PrintWriter p : lesNoeuds) {
					p.println(message); 
					p.flush();
				}
			}
			System.out.println("Fin du thread envoyer");
		}
		catch (IOException e) {
			System.out.println("IOException: " + e);
			e.printStackTrace();
		}
	}
}
