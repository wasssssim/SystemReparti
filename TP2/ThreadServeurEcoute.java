
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Creation d'un anneau virtuel
 * 
 * Thread attendant la connexion d'un site (le predecesseur)
 * 
 * @author Nicolas
 *
 */
public class ThreadServeurEcoute extends Thread {

	ServerSocket listenSocket;
	int portecoute;
	Socket clientSocket;
	public BufferedReader in;

	/**
	 * @param port Numero de port d'ecoute
	 */
	public ThreadServeurEcoute(int port) {
		this.portecoute = port;
	}
	
	/**
	 * Activite du thread (etablir la connexion avec le predecesseur)
	 */
	public void run() {
		try {
			//creation de la socket serveur
			listenSocket = new ServerSocket(portecoute);
			
			//attente de connexion d'un client
			
			clientSocket = listenSocket.accept();
			
			System.out.println("Connexion de :" + clientSocket.getInetAddress());
			
			//recuperation du flux d'entree, creation d'un bufferedreader et affectation du bufferedreader "in"
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		} catch (IOException e) {
			System.err.println("Erreur\n"+e.getMessage());
			e.printStackTrace();
		}
	}

}


