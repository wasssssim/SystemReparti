
import java.util.Vector;
import java.net.Socket;
import java.lang.NumberFormatException;
import java.lang.InterruptedException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Systemes repartis
 * TP 
 * Tchat P2P
 * 
 * Programme d'un noeud tchat
 * 
 * @author Nicolas
 * @version 1.0
 */
public class NoeudTchat {

	public static void main (String[] args) {
		if(args.length < 2) {
			System.out.println("Usage: java NoeudTchat portEcoute nomUtilisateur [IP1:port1,...]");
			System.exit(1);
		}

		try {
			int portEcoute = Integer.parseInt(args[0]);
			String nomUtilisateur = args[1];
			String listeInfosNoeuds = null;
			if(args.length == 3) listeInfosNoeuds = args[2];
			
			Socket sock = null;
			PrintWriter ecriture = null;
			Vector<PrintWriter> listeEcriture = null;
			String infosNoeuds[] = null;
			String infoUnNoeud[] = null;
			String adresseNoeud = null;
			int portNoeud = -1;
			String SEPARATEUR = ",";
			String SEPARATEUR2 = ":";
			
			//creation de la liste de printwriter
			listeEcriture = new Vector<PrintWriter>();
			
			//creation et demarrage du thread permettant la connexion d'autres noeuds
			Thread threadConnexion = new Thread(new ThreadConnexionTchat(portEcoute, listeEcriture));
			threadConnexion.start();
			
			//connexion aux noeuds (pour creer la liste de "printwriter" a indiquer au thread envoyer messages)				
			if(listeInfosNoeuds != null) {
				infosNoeuds = listeInfosNoeuds.split(SEPARATEUR);
				
				for (int i = 0; i < infosNoeuds.length; i++) {
					// infosNoeuds[i] correspond a "IP:port"
					infoUnNoeud = infosNoeuds[i].split(SEPARATEUR2);
					adresseNoeud = infoUnNoeud[0];
					portNoeud = Integer.parseInt(infoUnNoeud[1]);
					
					//connexion au noeud ayant l'adresse IP "adresseNoeud" et le numero de port "portNoeud"
					//TODO
					sock = new Socket(adresseNoeud, portNoeud);


					//recuperation des flux d'entree et de sortie de la socket creee precedemment pour la connexion
					//TODO
					InputStreamReader lecture = new InputStreamReader(sock.getInputStream());
					BufferedReader lectureMessage = new BufferedReader(lecture);
					ecriture = new PrintWriter(sock.getOutputStream(), true);
					
					//mise a jour de la liste de printwriter
					//TODO
						listeEcriture.add(ecriture);

								
					//creation et demarrage d'un thread reception (pour recuperer les messages venant du noeud)
					//TODO
					Thread threadReception=new Thread(new ThreadRecevoirMessage(lectureMessage,sock,ecriture,listeEcriture));
					threadReception.start();
					
					System.out.println("Connecte a " + adresseNoeud + ":" + portNoeud);
				}
			}
			
			// creation et demarrage du thread envoyer messages
			//TODO
			Thread threadEnvoie=new Thread(new ThreadEnvoyerMessage(nomUtilisateur,listeEcriture));
			threadEnvoie.start();

			
			//attendre la fin d'execution du thread envoyer messages
			//TODO
			threadEnvoie.join();


			System.exit(0);
		}
		catch (NumberFormatException nfe) {
			System.err.println("L'argument doit etre un entier");
			nfe.printStackTrace();
			System.exit(1);
	    }
		catch (InterruptedException ie) {
			System.err.println("Erreur");
			ie.printStackTrace();
			System.exit(1);
	    }
		catch (IOException ex) {
			System.err.println("Erreur lors de la connexion");
			ex.printStackTrace();
			System.exit(1);
		}
	}

}
