
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Creation d'un anneau virtuel
 * 
 * Programme principal d'un site
 * 
 * @author Nicolas
 *
 */
public class ProgrammeSite {

	/**
	 * @param args[0] numero du site
	 * @param args[1] numero de port d'ecoute du serveur local
	 * @param args[2] adresse IP du successeur
	 * @param args[3] numero de port du successeur
	 * @param args[4] booleen indiquant si le site doit envoyer un message de test
	 */
	public static void main(String[] args) {

		int idSite = Integer.parseInt(args[0]);
		int portEcoute = Integer.parseInt(args[1]);
		String ipSuccesseur = args[2];
		int portSuccesseur = Integer.parseInt(args[3]);
		boolean init = Boolean.parseBoolean(args[4]);
		
		
		ThreadServeurEcoute servlocal;		
		BufferedReader in; // pour recevoir des messages du predecesseur

		Socket theSocket;
		PrintStream out; // pour envoyer des messages au successeur

		String message = null;
		int idInit = -1;
		StringTokenizer st = null;
		
				
		//creation et lancement du thread d'attente de connexion 
		// du site predecesseur (pour ne pas rester bloque ici sur le accept())

		servlocal = new ThreadServeurEcoute(portEcoute);
		servlocal.start();



		try {
			//attendre (4 secondes) que tous les autres sites (leurs serveurs tcp)
			// soient bien lances 
			servlocal.sleep(4000);
			//connexion avec le successeur
			theSocket = new Socket(ipSuccesseur, portSuccesseur);
			System.out.println("Connexion a "+ipSuccesseur+" "+portSuccesseur);
			//recuperation du fluix de sortie et creation du printwriter "out"

			out = new PrintStream(theSocket.getOutputStream());
						
			//attendre (4 secondes) que tous les autres connexions soient bien
			// effectuees (anneau construit)
			//TODO
			servlocal.sleep(4000);

            //affectation du bufferedreader "in" avec celui cree dans le threadserveurecoute

			 in = servlocal.in;

			if(init==true) {
			    //envopi du premier message
				//TODO
				init=false;
				out.println("Bonjour  !! :"+idSite);
			}


			while(true) {
				System.out.println("Site "+idSite+" attend un message");
				//recevoir un message
				
				message = in.readLine();
				System.out.println("Site "+idSite+" a recu le message : "+message);

				st = new StringTokenizer(message,":");
				st.nextToken(); // correspond a "test"
				idInit = Integer.parseInt(st.nextToken());
				if(idInit != idSite) {
					//envoyer le message
					Thread.sleep(1000); // attendre 1 seconde pour ne pas aller trop vite
					System.out.println("Site "+idSite+" envoie le message a "+ipSuccesseur+" "+portSuccesseur);
					//TODO
					out.println("test:"+idSite);
				}
				else {
				    //le message a fait le tour de l'anneau, donc fini
					System.out.println("OK. Fin.");
				}
			}
		} catch (InterruptedException e) {
			System.err.println("Erreur\n"+"Site "+idSite+"\n"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Erreur\n"+"Site "+idSite+"\n"+e.getMessage());
			e.printStackTrace();
		}

	}

}


