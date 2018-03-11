

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

/**
* Example program to list links from a URL.
*/
public class GetGameName{
	
	private StringChecker checkString;
	
	private GameList gameList;
	private GameTrayIcon trayIcon;
	private boolean retrieveData = false;
	private boolean trCheck = false;
	
	protected static String gameName;
	protected static ArrayList<String> incomingGames;
	protected static ArrayList<String> gameNames;
	protected static ArrayList<String> botNames;
	protected static ArrayList<String> ownerNames;
	protected static ArrayList<String> numOfPlayers;
	protected static ArrayList<String> queuePosition;
	protected static ArrayList<Integer> numberOfPlayers;

	private Elements getElements;
	
	public GetGameName(Object o){}
	
	public GetGameName(){
		incomingGames = new ArrayList<String>();
		gameNames = new ArrayList<String>();
		ownerNames = new ArrayList<String>();
		numOfPlayers = new ArrayList<String>();
		botNames = new ArrayList<String>();
		queuePosition = new ArrayList<String>();
		
		gameList = new GameList(this);
		gameList.updateGameList();
		
		checkString = new StringChecker();
	}
	
	public void getData(){
		try {
			 /*Connection.Response response = Jsoup.connect("http://makemehost.com/lobby-and-queue.php")
			                         .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
			                         .timeout(10000)
			                         .execute();

			 int statusCode = response.statusCode();
			 if(statusCode == 200) {
			     Document doc = Jsoup.connect("http://makemehost.com/lobby-and-queue.php").get();//connection.get();
			     Elements element = doc.select("loc");   
			     for (Element urls : element) {
			         System.out.println(urls.text());
			     }
			 }
			 else
			     System.out.println("received error code : " + statusCode);*/
			     
			//http://makemehost.com/index.php
			Document mmhDoc = Jsoup.connect("http://makemehost.com/games.php").timeout(10000).get();
			//Document mmhDoc = Jsoup.connect("https://encrypted.google.com/").get();
	    	getElements = mmhDoc.select("*");
			removeOldGames();
			displayNewGame();
		} catch (IOException e){
			//JOptionPane.showMessageDialog(null,e);
			//GameList.mainFrame.setVisible(true);
			System.out.println(e+" (Time: "+new Date().getHours()+":"+new Date().getMinutes()+")");
		}
	}
	
	@SuppressWarnings("unchecked")
	private void removeOldGames() throws IOException{
				
    	ArrayList<String> removeGame = new ArrayList<String>();
    	boolean removeString = true;
    	int index = 0;
    	for(String gameName : gameNames){
    		removeString = true;
    		for(Element getElement : getElements){
        		if(getElement.tagName().equalsIgnoreCase("td")){
        			if(getElement.toString().contains("MakeMeHost") || getElement.toString().contains("ENT")){
        				index = 0;
        			}
    	    		if(getElement.ownText().length() > 0 && checkString.checkForString((getElement.ownText()), gameCheck)){
			    		if(gameName.equalsIgnoreCase(getElement.text())){
			    			removeString = false;
			    		}
    	    		}
        		}
    		}
    		if(removeString){
    			removeGame.add(gameName);
    		}
    			
    	}
    	for(String getGameName : removeGame){
    		gameNames.remove(getGameName);
			GameList.gameListModel.removeElement(getGameName);
    	}
    	
    	removeGame = new ArrayList<String>();
    	int queueIndex = 0;
    	for(String gameName : incomingGames){
    		removeString = true;
    		for(Element getElement : getElements){
        		if(getElement.tagName().equalsIgnoreCase("td")){
    	    		if(getElement.ownText().length() > 0 && checkString.checkForString((getElement.ownText()), gameCheck)){
		    			if(DuplicateChecker.dupElementChk(getElement.text(),incomingGames)){
		    					
		    					
	    					//String getParent = getElement.parent().toString();
	    					//String queuePosition = getParent.substring(10,getParent.indexOf("</td>"));
				    		if(gameName.equalsIgnoreCase(getElement.text())){
				    			removeString = false;
					    		GameList.queueListModel.remove(queueIndex);
					    		GameList.queueListModel.add(queueIndex, queuePosition);
				    		}
		    			}
    	    		}
        		}
    		}
    		if(removeString){
    			removeGame.add(gameName);
    		}
			queueIndex++;    			
    	}
    	for(String getGameName : removeGame){
			GameList.queueListModel.removeElementAt(incomingGames.indexOf(getGameName));
			queuePosition.remove(incomingGames.indexOf(getGameName));
    		incomingGames.remove(getGameName);
			GameList.incomingListModel.removeElement(getGameName);
    	}
	}
	
	public String getTableData(String tableData){
		return tableData.trim().substring(5, tableData.trim().length()-1);
	}
	
	private void displayNewGame() throws IOException{
		String announceGames = "";
		String announceIncomingGames = "";
		//Elements getElements = doc.select("div[class=refreshMe2]");
		int index = 0;
		
		for(Element getElement : getElements){
			if(getElement.tagName().equals("td")){
				if(getElement.parent().parent().parent().parent().className().equals("refreshMe2")){
		    		if(checkString.checkForString((getElement.ownText()), gameCheck)){
		    			if(!DuplicateChecker.dupElementChk(getElement.text(),gameNames)
		    					&& ((getElement.siblingIndex() == 3 && getElement.parent().childNodes().size() == 6)
		    					|| (getElement.siblingIndex() == 1 && getElement.parent().childNodes().size() == 3))){
			    			announceGames += "\n"+getElement.text();
				    		gameNames.add(getElement.text());
				    		//System.out.println("Adding: "+ getElement.text());
							GameList.gameListModel.addElement(getElement.text());
		    			}
		    		}
				}
				else if(getElement.parent().parent().parent().parent().className().equals("refreshMe3")){
					if(getElement.ownText().length() > 0 && checkString.checkForString((getElement.ownText()), gameCheck)){
		    			if(!DuplicateChecker.dupElementChk(getElement.text(),incomingGames)){
		    				if(!getElement.lastElementSibling().equals(getElement)){
		    					if(!getElement.toString().contains("Game Name")){
		    						//numberOfPlayers
		    						System.out.println(getElement);
			    					String getParent = getElement.parent().toString();
			    					queuePosition.add(getParent.substring(10,getParent.indexOf("</td>")));
			    					String queuePosition = getParent.substring(10,getParent.indexOf("</td>"));
				    				announceIncomingGames += "\n"+getElement.text();
					    			incomingGames.add(getElement.text());
									GameList.incomingListModel.addElement(getElement.text());
									GameList.queueListModel.addElement(queuePosition);
		    					}
		    				}
		    			}
		    		}
				}
			}    		
		}
		if(!announceGames.equals("") || !announceIncomingGames.equals("")){
			trayIcon.displayMessage(announceGames, announceIncomingGames);
		}
	}
	
	
    /*private void displayNewGame() throws IOException{
    	String announceGames = "";
    	String announceIncomingGames = "";
    	//Elements getElements = doc.select("div[class=refreshMe2]");
    	int index = 0;
    	
    	for(Element getElement : getElements){
    		if(getElement.tagName().equals("td")){
    			if(getElement.parent().parent().parent().parent().className().equals("refreshMe2")){
        			if(getElement.toString().contains("MakeMeHost")){
                		System.out.println(getElement);
        				index = 0;
        			}
        			else
        				index++;
		    		if(getElement.ownText().length() > 0 && checkString.checkForString((getElement.ownText()), gameCheck) && index == 2){
		    			if(!DuplicateChecker.dupElementChk(getElement.text(),gameNames)){
			    			announceGames += "\n"+getElement.text();
				    		gameNames.add(getElement.text());
							GameList.gameListModel.addElement(getElement.text());
		    			}
		    		}
    			}
    			else if(getElement.parent().parent().parent().parent().className().equals("refreshMe3")){
    				if(getElement.ownText().length() > 0 && checkString.checkForString((getElement.ownText()), gameCheck)){
		    			if(!DuplicateChecker.dupElementChk(getElement.text(),incomingGames)){
		    				if(!getElement.lastElementSibling().equals(getElement)){
		    					if(!getElement.toString().contains("Game Name")){
			    					String getParent = getElement.parent().toString();
			    					queuePosition.add(getParent.substring(10,getParent.indexOf("</td>")));
			    					String queuePosition = getParent.substring(10,getParent.indexOf("</td>"));
				    				announceIncomingGames += "\n"+getElement.text();
					    			incomingGames.add(getElement.text());
									GameList.incomingListModel.addElement(getElement.text());
									GameList.queueListModel.addElement(queuePosition);
		    					}
		    				}
		    			}
		    		}
    			}
    		}    		
    	}
    	if(!announceGames.equals("") || !announceIncomingGames.equals("")){
    		trayIcon.displayMessage(announceGames, announceIncomingGames);
    	}
    }*/
    
    public void removeOldGame() throws IOException{
    	Document doc = Jsoup.connect("http://makemehost.com/lobby-and-queue.php").get();
    	Elements getElements = doc.select("*");
    	for(String gameName : gameNames){
    		if(!DuplicateChecker.dupGameCheck(gameName, getElements)){
	    		gameNames.remove(gameName);
    		}
    	}
    }
	
	public void runLink1(String url) throws IOException{
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        //Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        Elements links = doc.select("[Map]");

        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
           // print(" * a: <%s> (%s)", link.attr("abs:href"), trim(link.text(), 35));
        	System.out.println(link.text());
        }
	}
    
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
    
    public void setTrayIcon(GameTrayIcon trayIcon){
    	this.trayIcon = trayIcon;
    }
}
