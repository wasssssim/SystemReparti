
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Systemes repartis
 * TP 
 * Tchat IHM
 * 
 * Programme du thread cote serveur de tchat
 * 
 * @author Nicolas
 * @version 1.0
 */
public class ServeurTchatThreadService extends Thread {

	private Socket clientSocket;
	private Vector<PrintWriter> lesClients;

	public ServeurTchatThreadService(Socket clientSocket, Vector<PrintWriter> lesClients) {
		System.out.println("Creation d'un thread pour repondre au client : "+clientSocket.getInetAddress()+" port "+clientSocket.getPort());
		this.clientSocket = clientSocket;		
		this.lesClients = lesClients;
	}

	public void run() { 
		try {
			//creation du flux qui permet de "lire sur la socket" (autrement dit recuperer les messages recus)
			BufferedReader lectureMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
			
			//mise a jour de la liste des printwriters lesClients
			lesClients.add(pw);


			boolean fini = false;
			String message = null;
		
			while(!fini) {
				// reception d'un message venant du client
				message=lectureMessage.readLine(); 
				System.out.println("message recu = " + message);
				
				if(message!=null && message.endsWith("FIN$")) {
					fini = true;
				}
				else {
					// envoi d'un message a tous les clients de la liste
					for(PrintWriter p : lesClients) {
						p.println(message); 
						p.flush();
					}
				}
			}

			// envoi "FIN$" au client (pour que son thread recevoir puisse terminer)
			pw.println(message); 
			pw.flush();

			//mise a jour de la liste de printwriters lesClients
			lesClients.remove(pw);

			// fermeture de la socket utilisee pour dialoguer avec le client
			clientSocket.close();

			System.out.println("Deconnexion de : " + clientSocket.getInetAddress() + " : port " + clientSocket.getPort());
			
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}		
		finally {
			try {
				if(this.clientSocket!=null) this.clientSocket.close();
			} catch (IOException e) {
				System.err.println("Erreur : "+e);
				e.printStackTrace();
			}
		}
	}

}

