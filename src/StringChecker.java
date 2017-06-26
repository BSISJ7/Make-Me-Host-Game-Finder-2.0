import java.util.ArrayList;
import java.util.regex.Pattern;


public class StringChecker {


	public boolean checkForString(String checkString, String... findStrings){
		for(String findString : findStrings){
			//System.out.println("checking: "+checkString);
			if(checkString.toLowerCase().contains(findString.toLowerCase())){
				return true;
			}
		}
		return false;
	}

	public boolean checkForString(String checkString, ArrayList<String> gameList) {
		for(String findString : gameList){
			if(Pattern.compile(Pattern.quote(findString), Pattern.CASE_INSENSITIVE).matcher(checkString).find()){
				return true;
			}
		}
		return false;
		
	}
}
