
import java.util.concurrent.Semaphore;

/**
 * Exclusion mutuelle sur un anneau a jeton
 * 
 * Programme principal d'un site
 * 
 * @author wassim
 *
 */
public class ProgrammeSite {

	public static boolean veutentrer = false;
	
	
	/**
	 * @param args[0] adresse IP du dispositif
	 * @param args[1] numero de port du dispositif
	 * @param args[2] numero de port d'ecoute du serveur local
	 * @param args[3] adresse IP du successeur
	 * @param args[4] numero de port du successeur
	 * @param args[5] booleen indiquant si le jeton doit etre cree
	 */
	public static void main(String[] args) {
		Semaphore debutsc = new Semaphore(0);
		Semaphore finsc = new Semaphore(0);
		
		String dispositif = args[0];
		int portdispositif = Integer.parseInt(args[1]);
		int portecoute = Integer.parseInt(args[2]);
		String successeur = args[3];
		int portsuccesseur = Integer.parseInt(args[4]);
		boolean initjeton = Boolean.parseBoolean(args[5]);
		
		Traitement calculsite = new Traitement(debutsc,finsc,dispositif,portdispositif);
		GestionJeton gestionsite = new GestionJeton(debutsc,finsc,portecoute,successeur,portsuccesseur,initjeton);
		
		calculsite.setName("site"+portecoute+"calcul");
		gestionsite.setName("site"+portecoute+"gestion");
		
		gestionsite.start();
		calculsite.start();
	}

}
