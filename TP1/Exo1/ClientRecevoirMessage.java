
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Systemes repartis
 * TP 
 * Tchat
 * 
 * Classe correspondant au thread de reception de messages
 * au niveau du client
 * 
 * @author Nicolas
 * @version 1.0
 */
public class ClientRecevoirMessage implements Runnable {

	private BufferedReader lectureMessage;

	public ClientRecevoirMessage(BufferedReader lecture) {
		this.lectureMessage = lecture;
	}

	public void run () {
		String message;
		boolean fini = false;
		try {
			while(!fini) {
				message = lectureMessage.readLine();
				if(message == null || (message!=null && message.endsWith("FIN$"))) {
					fini = true;
				}
				//System.out.println("... reception de : " + message);
				System.out.println(message);
			}
		}
		catch (IOException e) {
			System.out.println("IOException: " + e);
			e.printStackTrace();
		}
	} 

}
