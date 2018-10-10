
public class GameInfo {

	private String botName;
	private String server;
	private String running;
	private String gameName;
	private String owner;
	private String inGame;
	
	public GameInfo(){
		botName = "";
		running = "";
		server = "";
		gameName = "";
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
	
	public void setGameName(String gameName){
		this.gameName = gameName;
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

	public String getGameName(){
		return gameName;
	}

	public String getOwner(){
		return owner;
	}

	public String getInGame(){
		return inGame;
	}
}
