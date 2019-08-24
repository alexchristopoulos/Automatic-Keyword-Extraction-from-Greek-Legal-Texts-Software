import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GenericOutput {
	
	public GenericOutput(String filename,ArrayList<String> content){//for step 2 results
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Public\\Documents\\Legal Texts\\Processed Text\\"+filename));
			for(String line:content){
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		}catch(Exception WriteFileException) {System.out.println("Failed to Write Step2 results: "+WriteFileException);}
	}
	
	public GenericOutput(ArrayList<String> words, ArrayList<String> categories, ArrayList<String> candidates,String filename){//for step 3 results
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Public\\Documents\\Legal Texts\\Word Tags\\"+filename));
			for(int i=0;i<words.size();i++){
				bw.write(words.get(i) + " " + categories.get(i));
				if(i<words.size()-1){
					bw.newLine();
				}else {
					bw.newLine();
					bw.write("*****");
					bw.newLine();
				}
			}
			for(String c:candidates){
				bw.write(c);
				bw.newLine();
			}
			bw.close();
		}catch(Exception WriteFileException) {System.out.println("Failed to Write Step3 results: "+WriteFileException);}	
	}
}
