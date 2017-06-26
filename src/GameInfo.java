
public class GameInfo {

	String botName;
	String server;
	String running;
	String currentGame;
	String owner;
	String inGame;
	
	public GameInfo(){
		botName = "";
		running = "";
		server = "";
		currentGame = "";
		owner = "";
		inGame = "";
	}
	
	public void setBotName(String name){
		botName = name;
	}
	
	public void setServer(String server){
		this.server = server;
	}
	
	public void setRunning(String running){
		this.running = running;
	}
	
	public void setCurrentGame(String currentGame){
		this.currentGame = currentGame;
	}
	
	public void setOwner(String owner){
		this.owner = owner;
	}
	
	public void setInGame(String inGame){
		this.inGame = inGame;
	}

	public String getBotName(){
		return botName;
	}

	public String getServer(){
		return server;
	}

	public String getRunning(){
		return running;
	}

	public String getCurrentGame(){
		return currentGame;
	}

	public String getOwner(){
		return owner;
	}

	public String getInGame(){
		return inGame;
	}
}
