import java.util.*;
public class MAIN {
	
	private static ArrayList<ArrayList<String>> documents = new ArrayList<ArrayList<String>>();
	private static ArrayList<String> filenames = new ArrayList<String>();
	private static ArrayList<ArrayList<String>> candidates = new ArrayList<ArrayList<String>>();	
	private static ArrayList<Trie> trie_for_file = new ArrayList<Trie>();//Trie foreach document
	private static String FilesPath = "C:\\Users\\Public\\Documents\\Legal Texts\\Documents";
	
	public static void main(String[] args) {//Start of program
		System.out.println("Started on: "+new Date().toString());
		loadDocuments(FilesPath);// step 1 Read Files
		RemoveGarbage();// step 2 Process Files
		CandidateGenerate();//step 3 Define Candidates
		Extract_Keywords();//step 4 extract keywords
		System.out.println("End: "+new Date().toString());
	}
	
	private static void RemoveGarbage(){
		GarbageRemover remover = new GarbageRemover();
		documents = remover.RemoveGarbage(documents);
		for(int i=0;i<documents.size();i++)
		{new GenericOutput(filenames.get(i),documents.get(i));}
		trie_for_file = remover.getTries();
	}
	
	private static void loadDocuments(String path){
		Reader reader = new Reader();
		HashMap<String,ArrayList<String>> temp = reader.readDocuments(path); 
		for(Map.Entry<String,ArrayList<String>> e: temp.entrySet()){
			documents.add(e.getValue());
			filenames.add(e.getKey());
		}
	}
	
	private static void CandidateGenerate(){
		System.out.print("Step 3.");int index=0;
		for(ArrayList<String> doc: documents){
			candidates.add(SmartCandidateGenerator(doc,index));
			System.out.print(".");
			index++;
		}
		System.out.println("complete");
	}
	
	private static ArrayList<String> SmartCandidateGenerator(ArrayList<String> currentdoc,int index){
		PartOfSpeechTagger pos = new PartOfSpeechTagger(filenames.get(index));
		return pos.FindCandidates(currentdoc);
	}
	
	private static void Extract_Keywords(){
		System.out.print("Step 4.");
		ExtractKeywords ew = new ExtractKeywords(documents,candidates,filenames,trie_for_file);
		ew.Extract();
		System.out.println("\n___________________________");
	}
}
