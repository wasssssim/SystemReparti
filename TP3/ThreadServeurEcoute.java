
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Exclusion mutuelle sur un anneau a jeton
 * 
 * Thread attendant la connexion d'un site (le predecesseur)
 * 
 * @author wassim
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
			listenSocket = new ServerSocket(this.portecoute);
			clientSocket = listenSocket.accept(); //bloquant jusqu'a la connexion du site predecesseur
			System.out.println("Connexion de :" + clientSocket.getInetAddress());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Erreur\n"+e.getMessage());
			e.printStackTrace();
		}
	}

}
