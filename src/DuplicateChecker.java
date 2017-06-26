import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DuplicateChecker {

	public static boolean dupStringChk(String newValue, String[] data){
		
		boolean isDuplicate = false;
		
		for(int x = 0; x < data.length; x++){
			/*if(newValue instanceof String){
				if(((String)newValue).equalsIgnoreCase(((String)data[x]))){
					isDuplicate = true;
					break;
				}
			}*/
			if(newValue.equalsIgnoreCase(data[x])){
				isDuplicate = true;
				break;
			}
		}
		return isDuplicate;
	}
	
	public static boolean dupElementChk(String newGame, ArrayList<String> gameNames){		
		for(String hostedGames : gameNames){
			if(newGame.equalsIgnoreCase(hostedGames)){
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean dupGameCheck(String oldGame, Elements gameNames){	
		boolean isDuplicate = false;
		
		for(Element hostedGames : gameNames){
			if(oldGame.equalsIgnoreCase(hostedGames.tagName())){
				isDuplicate = true;
				break;
			}
		}
		return isDuplicate;
	}
}
