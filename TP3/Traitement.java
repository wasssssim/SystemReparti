
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Exclusion mutuelle sur un anneau a jeton
 * 
 * Thread effectuant des calculs dont la section critique sur un site
 * 
 * @author wassim
 *
 */
public class Traitement extends Thread {

	Semaphore debutsc;
	Semaphore finsc;

	int compteur;
	String dispositif;
	int port;

	/**
	 * Constructeur
	 * @param debutsc Semaphore
	 * @param finsc Semaphore
	 * @param veutentrer Booleen
	 */
	public Traitement(Semaphore debutsc, Semaphore finsc, String dispositif, int port) {
		this.debutsc = debutsc;
		this.finsc = finsc;
		this.compteur = 0;
		this.dispositif = dispositif;
		this.port = port;
	}

	/**
	 * Activite du thread (calcul effectue par le site)
	 */
	public void run() { 
		try {
			DatagramSocket theSocket = null;
			InetAddress receiver = InetAddress.getByName(this.dispositif);
			theSocket = new DatagramSocket();
			long time;
			
			Random r = new Random();
			int alea = 0;
			
			while(compteur < 3) {
				/* du code quelconque */
				alea = 1000 + r.nextInt(9000);
				Thread.sleep(1000); // entre 1 et 10 secondes
				
				System.out.println(this.getName()+" veut entrer en sc");

				//TODO
				ProgrammeSite.veutentrer = true;
				this.debutsc.acquire();
				
				System.out.println(this.getName()+" entre en sc");

				/* code de la section critique */
				//envoyer un message (le nom du thread et la valeur de son compteur) en UDP au dispositif
				this.compteur = this.compteur + 1;

				time = System.currentTimeMillis();
				
				String theLine = time+" "+this.getName()+" : "+this.compteur;
				byte[] data = theLine.getBytes();
				DatagramPacket theOutput = new DatagramPacket(data, data.length, receiver, this.port);
				System.out.println(this.getName()+" envoi "+theLine);
				theSocket.send(theOutput);
				
				System.out.println(this.getName()+" sort de sc");
				/* fin du code de la section critique  */
				ProgrammeSite.veutentrer = false;
				this.finsc.release();
				
				/* du code quelconque */
				alea = 1000 + r.nextInt(9000);
				Thread.sleep(alea); // entre 1 et 10 secondes
			}
			theSocket.close();
			System.out.println(this.getName()+" FIN");
		} catch(InterruptedException e) { 
			System.err.println("Erreur\n"+this.getName()+"\n"+e.getMessage());
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.err.println("Erreur\n"+this.getName()+"\n"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Erreur\n"+this.getName()+"\n"+e.getMessage());
			e.printStackTrace();
		}
	}

}
