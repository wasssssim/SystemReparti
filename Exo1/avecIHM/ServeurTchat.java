
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Systemes repartis
 * TP 
 * Tchat IHM
 * 
 * Programme du serveur de tchat
 * 
 * @author Nicolas
 * @version 1.0
 */
public class ServeurTchat {

	public static void main(String[] args) {

		ServerSocket maSocketEcoute = null;
		int port = 0;

		Vector<PrintWriter> lesClients; // liste des PrintWriter permettant d'envoyer des messages aux clients

		if(args.length != 1) {
			System.out.println("Usage : java ServeurTchat numeroPortEcoute");
			System.exit(1);
		}

		try {
			port = Integer.parseInt(args[0]);
			maSocketEcoute = new ServerSocket(port); // creation d'une socket server sur le port d'ecoute "port"

			lesClients = new Vector<PrintWriter>();

			System.out.println("Serveur pret et ecoute sur le port "+port);

			while(true) {				
				Socket clientSocket = maSocketEcoute.accept(); // attente de connexion d'un client (bloquant tant que pas de client)
				//si un client se connecte, alors accept() retourne une socket pour communiquer avec ce client
				System.out.println("Connexion de : " + clientSocket.getInetAddress() + " : port " + clientSocket.getPort());

				//creation et demarrage d'un thread pour dialoguer avec le client
				ServeurTchatThreadService serviceThread = new ServeurTchatThreadService(clientSocket, lesClients);
				serviceThread.start();
			}
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

