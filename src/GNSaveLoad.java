
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GNSaveLoad {

	private final static String SAVE_FILE = "SaveFile.txt";
	
	public GNSaveLoad(){
		
	}
	
	public static void saveList(ArrayList<String> filters){
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(new File(SAVE_FILE));
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for(String getFitler : filters){
				bufferedWriter.write(getFitler);
				bufferedWriter.close();
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static ArrayList<String> loadList(){
		try {
			String line = "";
			ArrayList<String> gameFilters = new ArrayList<String>();
			FileReader fileReader = new FileReader(SAVE_FILE);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null){
				gameFilters.add(line);
			}
			bufferedReader.close();
			
			return gameFilters;
		} catch (FileNotFoundException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
		
		return new ArrayList<String>();
	}
}
