
import java.util.Vector;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Systemes repartis
 * TP 
 * Tchat P2P
 * 
 * Classe correspondant au thread d'attente de connexion 
 * d'autres noeuds tchat (serveur tcp)
 * 
 * @author Nicolas
 * @version 1.0
 */
public class ThreadConnexionTchat implements Runnable {

	private ServerSocket maSocketEcoute;
	private int portEcoute;
	private Vector<PrintWriter> listeEcriture;
	
	
	public ThreadConnexionTchat(int portEcoute, Vector<PrintWriter> listeEcriture) {
		this.portEcoute = portEcoute;
		this.listeEcriture = listeEcriture;
		this.maSocketEcoute = null;
	}
	

	public void run() {
		try {
			//creation de la socket serveur
			//TODO
			maSocketEcoute = new ServerSocket(portEcoute);

			
			System.out.println("Serveur pret et ecoute sur le port "+portEcoute);
			
			while(true) {				
				// attente de connexion d'un noeud (bloquant)
				//TODO
				Socket uneSocket = maSocketEcoute.accept();
				
				System.out.println("Connexion de " + uneSocket.getInetAddress() + ":" + uneSocket.getPort());
				
				//recuperation des flux d'entree et de sortie de la socket creee par le accept()
				//TODO
				InputStreamReader lecture = new InputStreamReader(uneSocket.getInputStream());

				BufferedReader lectureMessage = new BufferedReader(new InputStreamReader(uneSocket.getInputStream()));

				PrintWriter ecriture = new PrintWriter(uneSocket.getOutputStream(), true);
				
				//ajout du nouveau printwriter dans la liste
				//TODO
				listeEcriture.add(ecriture);
								
				//creation et demarrage d'un thread reception (pour recuperer les messages venant du noeud connecte)
				//TODO
                Thread threadReception = new Thread(new ThreadRecevoirMessage(lectureMessage, uneSocket, ecriture, listeEcriture));	
				threadReception.start();}
		}
		catch (Exception e) { 
			System.err.println("Erreur : "+e);
			e.printStackTrace();
		}
		finally {
			try {
				if(maSocketEcoute!=null) maSocketEcoute.close();
			} catch (IOException e) {
				System.err.println("Erreur : "+e);
				e.printStackTrace();
			}
		}
	}

}
