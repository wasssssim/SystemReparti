
import java.util.Vector;
import java.net.Socket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Systemes repartis
 * TP 
 * Tchat P2P
 * 
 * Classe correspondant au thread de reception de messages
 * 
 * @author Nicolas
 * @version 1.0
 */
public class ThreadRecevoirMessage implements Runnable {

	private BufferedReader lectureMessage;
	private InetAddress adresseNoeud;
	private int portNoeud;
	private Socket laSocket;
	private PrintWriter ecriture;
	private Vector<PrintWriter> listeEcriture;
	

	public ThreadRecevoirMessage(BufferedReader lecture, Socket uneSocket, PrintWriter ecriture, Vector<PrintWriter> listeEcriture) {
		this.lectureMessage = lecture;
		this.laSocket = uneSocket;
		this.adresseNoeud = uneSocket.getInetAddress();
		this.portNoeud = uneSocket.getPort();
		this.ecriture = ecriture;
		this.listeEcriture = listeEcriture;
	}

	public void run () {
		boolean fini = false;
		String message;
		try {
			while(!fini) {
				message = lectureMessage.readLine();
				if(message == null || (message!=null && message.endsWith("FIN$"))) {
					fini = true;
				}
				System.out.println(message);
			}
			System.out.println("Fin du thread recevoir messages venant de " + adresseNoeud + ":" + portNoeud);
			
			//enlever de la liste le printwriter correspondant au noeud parti
			listeEcriture.remove(ecriture);
			
			//fermer la socket
			laSocket.close();
		}
		catch (IOException e) {
			System.out.println("IOException: " + e);
			e.printStackTrace();
		}
	} 

}
