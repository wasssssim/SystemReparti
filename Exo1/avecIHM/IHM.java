
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Systemes repartis
 * TP 
 * Tchat IHM
 * @author Nicolas
 * @version 1.0
 */
public class IHM implements ActionListener, WindowListener {

	private JTextPane jtextpane;
	private StyledDocument entrants;
	private Style style;

	private JTextField sortants;
	private ArrayList<String> sendMessages = new ArrayList<String>();

	private String nomUtilisateur;
	
	
	/**
	 * Constructeur par defaut
	 */
	public IHM(String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}


	/**
	 * Initialisation des objets graphiques
	 */
   public void go() {	
	JFrame cadre = null;

	//cadre = new JFrame("Tchat");	
	cadre = new JFrame(nomUtilisateur);

	cadre.addWindowListener(this);

	JPanel panneau = new JPanel();

	jtextpane = new JTextPane();
	jtextpane.setEditable(false);
	entrants = jtextpane.getStyledDocument();
	style = jtextpane.addStyle("", null);

	JScrollPane zoneTexte = new JScrollPane(jtextpane);
	zoneTexte.setSize(new Dimension(15, 30));
	zoneTexte.setPreferredSize(new Dimension(15, 30));
	zoneTexte.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	zoneTexte.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	zoneTexte.setPreferredSize(new Dimension(400, 300));
		
	sortants = new JTextField();
	sortants.setPreferredSize(new Dimension(400, 25));
		
	sortants.addActionListener(this);
		
	JButton boutonEnvoi = new JButton("Envoi");

	boutonEnvoi.addActionListener(this);

	panneau.add(zoneTexte);
	panneau.add(sortants);
	panneau.add(boutonEnvoi);

	cadre.getContentPane().add(BorderLayout.CENTER, panneau);
	cadre.setSize(400,310);
	cadre.setVisible(true);

	panneau.setLayout(new BoxLayout(panneau, BoxLayout.Y_AXIS));
	cadre.pack();
   } 


   synchronized public void actionPerformed(ActionEvent ev) {
	sendMessages.add(sortants.getText());			 	
	sortants.setText("");
	sortants.requestFocus();
	this.notifyAll();
   }


   synchronized public String getNextMessageToSend() {
	try{
	    while(sendMessages.isEmpty()) {
			this.wait();
		}	   
	}
	catch(Exception ex) {ex.printStackTrace();}
	String mess = (String)sendMessages.remove(0);
	//System.out.println("IHM -> message a envoyer : " + mess);
	return mess;
   }


   public void writeMessage(String mess) {
	//System.out.println ("IHM -> message a ecrire : " + mess);
	StyleConstants.setForeground(style, Color.BLACK);
	try {
		entrants.insertString(entrants.getLength(), mess+"\n", style);
	} 
	catch (BadLocationException e) {
		e.printStackTrace();
	}
   }


	/**
	 * Action a realiser a la fermeture de la fenetre
	 */
	public void windowClosing(WindowEvent e) {
		synchronized(this) {
		    sendMessages.add("FIN$");
		    sortants.setText("");
		    sortants.requestFocus();
			this.notifyAll();
		}
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {	
	}

	public void windowIconified(WindowEvent e) {	
	}

	public void windowOpened(WindowEvent e) {	
	}

} // fin classe IHM

