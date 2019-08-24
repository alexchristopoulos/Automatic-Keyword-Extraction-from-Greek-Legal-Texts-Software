import java.io.*;
import java.util.*;
public class Reader {
	
	private HashMap<String,ArrayList<String>> documents = new HashMap<String,ArrayList<String>>();
	
	public HashMap<String,ArrayList<String>> readDocuments(String destination){
		try {
			File parent = new File(destination);
			File[] files = parent.listFiles();
			for(File f:files){
				documents.put(f.getName(),this.ReadFile(f));
			}
		}catch(Exception e){System.out.println(e.toString());}
		System.out.println("Step 1 complete");
		return documents;
	}
	
	private ArrayList<String> ReadFile(File f){
		ArrayList<String> lines = new ArrayList<String>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line="";
			while((line=br.readLine())!=null){
				lines.add(line);
			}
			br.close();
		}catch(Exception e){System.out.println(e.toString());}
		return lines;
	}
}
