
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Exclusion mutuelle sur un anneau a jeton
 * 
 * Programme du Dispositif recoltant les messages des sites
 * (cela correspond a la ressource partagee)
 * 
 * @author wassim
 *
 */
public class Dispositif {
	
	/**
	 * @param args[0] numero de port
	 * @param args[1] taille max des messages
	 */
	public static void main(String[] args) {
		DatagramSocket ds = null;
		try {
			//construction d'un DatagramSocket c'est à dire un point de communication
			int port = Integer.parseInt(args[0]); // recuperation du numero de port
			ds = new DatagramSocket(port); // creation d'un socket sur le port
			//construction d'un DatagramPacket
			int len = Integer.parseInt(args[1]); // recuperation de la taille max des messages
			byte[] buffer = new byte[len]; // creation d'un buffer pour stocker les messages
			DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length); // creation d'un packet pour recevoir les messages
			while (true) {
				ds.receive(incomingPacket); // reception d'un message 
				String s = new String(incomingPacket.getData()); // recuperation des donnees (chaine de carecteres) du messages
				System.out.println(s); // affichage du message
			}
		} 
		catch (Exception e) { 
			System.err.println("Erreur\n"+e.getMessage());
			e.printStackTrace();
		}
		finally {
			ds.close();
		}
	} /* */

}
