
import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.PrintStream;
//import java.net.ServerSocket;
import java.net.Socket;
//import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * Exclusion mutuelle sur un anneau a jeton
 * 
 * Thread gerant le jeton sur un site
 * 
 * @author wassim
 *
 */
public class GestionJeton extends Thread {

	Semaphore debutsc;
	Semaphore finsc;

	ThreadServeurEcoute servlocal;
	
	int portecoute;
	Socket clientSocket;
	BufferedReader in; // pour recevoir des messages du predecesseur

	String successeur;
	int portsuccesseur;
	Socket theSocket;
	PrintStream out; // pour envoyer des messages au successeur

	boolean initjeton;

	/**
	 * Constructeur
	 * @param debutsc
	 * @param finsc
	 * @param veutentrer Booleen
	 */
	public GestionJeton(Semaphore debutsc, Semaphore finsc, int port, String successeur, int portsuccesseur, boolean initjeton) {
		this.debutsc = debutsc;  // debutsc et finsc sont des semaphores pour la gestion de l'exclusion mutuelle
		this.finsc = finsc;
		this.portecoute = port;
		this.successeur = successeur;
		this.portsuccesseur = portsuccesseur;
		this.initjeton = initjeton; // pour savoir si le site cree le jeton ou non
		
		//creation et lancement du thread d'attente de connexion du site predecesseur (pour ne pas rester bloque ici sur le accept())
		servlocal = new ThreadServeurEcoute(this.portecoute); //,this.listenSocket,this.clientSocket,this.in);
		servlocal.start();
	}

	/**
	 * Activite du thread (gestion du jeton par le site)
	 */
	public void run() { 
		try {
			//attendre que tous les autres sites (serveurs tcp) soient bien lances
			Thread.sleep(4000); // 4 secondes

			//connexion avec le successeur
			theSocket = new Socket(successeur, portsuccesseur);
			out = new PrintStream(theSocket.getOutputStream());

			//attendre que tous les autres connexions soient bien effectuees (anneau construit)
			//autre solution : ajout mecanisme de synchronisation entre le thread gestionJeton et le threadServeurEcoute
			Thread.sleep(4000); // 4 secondes

			this.in = servlocal.in; 

			String theLine = null;

			if(this.initjeton==true) {
				System.out.println(this.getName()+this.portecoute+" : cree et envoi le jeton");
				
				//envoi du message "jeton"
				out.println("jeton");
				out.flush();

				
				this.initjeton=false;
			}

			while(true) {
				System.out.println(this.getName()+this.portecoute+" : attend le jeton");

				//recevoir_jeton
				do{
					theLine=in.readLine();

				}
				 while(theLine.equals("jeton")==false);
				System.out.println(this.getName()+this.portecoute+" : a recu le jeton : "+theLine);

				//si veutentrer est vrai alors
    			// V(debutsc)
	    		// P(finsc)
	    		//TODO
				if(ProgrammeSite.veutentrer == true) {
					debutsc.release();
					finsc.acquire();
				}

				//envoyer le jeton
				Thread.sleep(1000); // 1 seconde
				System.out.println(this.getName()+this.portecoute+" : envoi le jeton a "+this.successeur+" "+this.portsuccesseur);
				out.println("jeton");
			}
		} catch (InterruptedException e) {
			System.err.println("Erreur\n"+this.getName()+"\n"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Erreur\n"+this.getName()+"\n"+e.getMessage());
			e.printStackTrace();
		}
	}

}
