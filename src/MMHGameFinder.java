

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
* Example program to list links from a URL.
*/
public class MMHGameFinder{
	
	private StringChecker checkString;
	
	//private GameList gameList;
	private GameTrayIcon trayIcon;
	
	protected static String gameName;
	protected static ArrayList<String> incomingGames;
	protected static ArrayList<String> gameNames;
	protected static ArrayList<String> botNames;
	protected static ArrayList<String> ownerNames;
	protected static ArrayList<String> numOfPlayers;
	protected static ArrayList<String> queuePosition;
	protected static ArrayList<Integer> numberOfPlayers;
	
	private DisplayGameInfo dispInfo;
	
	private ArrayList<GameInfo> gameInfo;

	private Elements getElements;
	
	private int[] options;
	
	public MMHGameFinder(Object o){}
	
	public MMHGameFinder(){
		options = new int[6];
		dispInfo = new DisplayGameInfo("MMH Game Finder");
		checkString = new StringChecker();
		gameInfo = new ArrayList<GameInfo>();
		//this.trayIcon = trayIcon;
		this.trayIcon = new GameTrayIcon();
		
		try {
			setupGameList();
		}catch (IOException e) {}
		updateGameList();
	}

	
	private void setupGameList() throws IOException{
		Document mmhDoc = Jsoup.connect("http://makemehost.com/games.php").timeout(10000).get();
    	getElements = mmhDoc.select("*");
		Elements getElements = mmhDoc.select("div[class=refreshMeMMH]");
		getElements = mmhDoc.select("tr");

		for(Element getElement : getElements){
			//if(getElement.child(0).toString().toLowerCase().contains("bot name") && getElement.parent().parent().parent().className().equals("refreshMe2")){
			if(getElement.child(0).toString().toLowerCase().contains("makemehost-")){
				GameInfo newGame = new GameInfo();
				for(int x = 0; x < getElement.childNodes().size(); x++){
					newGame.setBotName(getElement.child(0).text().toString());
					newGame.setServer(getElement.child(1).text().toString());
					newGame.setRunning(getElement.child(2).text().toString());
					newGame.setCurrentGame(getElement.child(3).text().toString());
					newGame.setInGame(getElement.child(4).text().toString());
				}
					dispInfo.addGame(newGame);
					gameInfo.add(newGame);
			}
		}
		
		dispInfo.setVisible(true);
	}
	
	public void updateGameInfo(){
		Document mmhDoc = null;
		try {
			mmhDoc = Jsoup.connect("http://makemehost.com/games.php").timeout(10000).get();
		} catch (IOException e) {e.printStackTrace();}
    	getElements = mmhDoc.select("*");
		Elements getElements = mmhDoc.select("div[class=refreshMeMMH]");
		getElements = mmhDoc.select("tr");

		gameInfo.clear();
			
		for(Element getElement : getElements){
			//if(getElement.child(0).toString().toLowerCase().contains("bot name") && getElement.parent().parent().parent().className().equals("refreshMe2")){
			if(getElement.child(0).toString().toLowerCase().contains("makemehost-")){
				GameInfo newGame = new GameInfo();
				for(int x = 0; x < getElement.childNodes().size(); x++){
					newGame.setBotName(getElement.child(0).text().toString());
					newGame.setServer(getElement.child(1).text().toString());
					newGame.setRunning(getElement.child(2).text().toString());
					newGame.setCurrentGame(getElement.child(3).text().toString());
					newGame.setInGame(getElement.child(4).text().toString());
				}
					gameInfo.add(newGame);
					dispInfo.updateGames(gameInfo);
			}
		}
	}
    
	public void updateGameList(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				Timer updateListTimer = new Timer(500, new ActionListener(){
					public void actionPerformed(ActionEvent event){
						updateGameInfo();
					}
				});
				updateListTimer.start();
				updateListTimer.setDelay(4000);
			}
		});
	}
   
    public static void main(String[] args) throws IOException {
    	MMHGameFinder run = new MMHGameFinder();
    }
}