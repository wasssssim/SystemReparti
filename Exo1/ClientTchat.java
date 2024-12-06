
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Systemes repartis
 * TP 
 * Tchat
 * 
 * Programme du client tchat
 * 
 * @author Nicolas
 * @version 1.0
 */
public class ClientTchat {

	public static void main (String[] args) {
		if(args.length != 3) {
			System.out.println("Usage: java ClientTchat adresseServeur portServeur nomUtilisateur");
			System.exit(1);
		}

		try {
			String adresseServeur = args[0];
			int portServeur = Integer.parseInt(args[1]);
			String nomUtilisateur = args[2];
		
			Socket sock = null;
			BufferedReader lecture = null;
			PrintWriter ecriture = null;

			// Connexion au serveur de tchat
			System.out.println("En cours de connexion au serveur de tchat :  adresse " + adresseServeur + " port " + portServeur);
			sock = new Socket(adresseServeur, portServeur);
			InputStreamReader isr = new InputStreamReader (sock.getInputStream());
			lecture = new BufferedReader(isr);
			ecriture = new PrintWriter(sock.getOutputStream());
			System.out.println("Connexion etablie");

			// creation et demarrage des threads
			Thread threadRecevoir = new Thread(new ClientRecevoirMessage(lecture));
			Thread threadEnvoyer = new Thread(new ClientEnvoyerMessage(nomUtilisateur,ecriture));
			threadRecevoir.start();
			threadEnvoyer.start();
			
			// attente de la fin d'execution des threads
			threadEnvoyer.join(); // attente de la fin d'execution du thread "threadEnvoyer"
			threadRecevoir.join(); // attente de la fin d'execution du thread "threadRecevoir"
			
			// fermeture
			lecture.close();
			ecriture.close();
			sock.close();
		}
		catch (NumberFormatException e) {
			System.err.println("L'argument doit etre un entier");
			e.printStackTrace();
			System.exit(1);
	    }
		catch (IOException ex) {
			System.err.println("Erreur lors de la connexion");
			ex.printStackTrace();
			System.exit(1);
		}
		catch (InterruptedException ex) {
			System.err.println("Erreur lors de la connexion");
			ex.printStackTrace();
			System.exit(1);
		}
	}

}
