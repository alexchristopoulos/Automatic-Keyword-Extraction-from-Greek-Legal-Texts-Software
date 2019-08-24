import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class GarbageRemover {
	
	private ArrayList<String> stopwords;
	private ArrayList<String> codes;
	private ArrayList<Trie> Document_Tries = new ArrayList<Trie>();
	
	public ArrayList<Trie> getTries(){
		return this.Document_Tries;
	}
	
	private void CreateTries(ArrayList<ArrayList<String>> files){
		for(ArrayList<String> document: files) {
			Trie t = new Trie();
			for(String line:document){
				for(String word:line.split(" ")){
					if(word.length()>2 && word!=null){
						t.Insert(word);
					}
				}
			}
			this.Document_Tries.add(t);			
		}
	}
	
	public GarbageRemover(){
		stopwords = this.LoadList("stopwords.in");
		codes = this.LoadList("codes.in");
	}
	
	public ArrayList<ArrayList<String>> RemoveGarbage(ArrayList<ArrayList<String>> files){
		files = this.RemoveCodes(files);
		files = this.RemoveStopwords(files);
		this.CreateTries(files);
		System.out.println("Step 2 complete");
		return files;
	}
	
	private ArrayList<ArrayList<String>> RemoveCodes(ArrayList<ArrayList<String>> files){
		ArrayList<String>[] files_arr = this.FilesList2FilesArray(files);
		for(int i=0;i<files_arr.length;i++){
			files_arr[i] = this.RemoveCodesForFile(files_arr[i]);
		}
		return this.FilesArray2FilesList(files_arr);
	}
	
	private ArrayList<ArrayList<String>> RemoveStopwords(ArrayList<ArrayList<String>> files){
		ArrayList<String>[] files_arr = this.FilesList2FilesArray(files);
		for(int i=0;i<files_arr.length;i++){
			files_arr[i] = this.ProcessContext(files_arr[i]);
		}
		return this.FilesArray2FilesList(files_arr);
	}
	
	private ArrayList<String> RemoveCodesForFile(ArrayList<String> file){
		ArrayList<String> tmp = new ArrayList<String>();
		for(String line:file){
			for(String s:this.codes){
				if(line.startsWith(s)){
					tmp.add(line);
					break;
				}
				try{
					Integer.parseInt(line); tmp.add(line);
					break;
				}catch(Exception NotPageNumber) {continue;}
			}
		}
		for(String delete:tmp){
			file.remove(delete);
		}
		return file;
	}
	
	private ArrayList<String>[] FilesList2FilesArray(ArrayList<ArrayList<String>> files){
		ArrayList<String>[] filesarr = new ArrayList[files.size()];
		int i=0;
		for(ArrayList<String> file:files){
			filesarr[i] = file;
			i+=1;
		}
		return filesarr;
	}
	
	private ArrayList<ArrayList<String>> FilesArray2FilesList(ArrayList<String>[] files_arr){
		ArrayList<ArrayList<String>> files = new ArrayList<ArrayList<String>>();
		for(ArrayList<String> a:files_arr){
			files.add(a);
		}
		return files;
	}
	
	private boolean inStopList(String word){
		return this.stopwords.contains(word.toLowerCase().trim())?true:false;
	}
	
	private boolean hasNumbers(String x){//Check if word has numbers
		char[] arr = x.toCharArray();
		for(char c:arr){
			try {
				Integer.parseInt(Character.toString(c));
				return true;// is number or pagenumber
			}catch(Exception NotNumber) {
				continue;
			}
		}
		return false;
	}
	
	private String RemoveSymbols(String s){//remove symbols from String
		s=s.replaceAll("\\[","");//remove left square bracket
		s=s.replaceAll("\\]","");//remove right square bracket
		return s.replaceAll("[-+`΄.'»‘%.“¨$–#@!”…|_?~;«/><^:)(’*}{,]", " ");
	}
	
	private ArrayList<String> ProcessContext(ArrayList<String> context){
		ArrayList<String> res = new ArrayList<String>();
		for(String line:context){
			if(line!="\n" && line!=" " && line!="" &&line.length()>1){
				String newline="";
				for(String word:line.split(" ")){//remove from stoplist
					newline+=this.hasNumbers(word)==true?"":this.RemoveSymbols(word)+" ";
				}
				newline=this.RemoveFromStopList(newline.trim());
				res.add(newline.toLowerCase().replaceAll("\\s{2,}", " ").trim());
			}
		}
		return res;
	}
	
	private String RemoveFromStopList(String line){
		String res="";
		for(String word:line.split(" ")){
			res+=this.inStopList(word)?"":(word.length()>1?word+" ":" ");
		}
		return res.trim().toLowerCase();
	}
	
	private ArrayList<String> LoadList(String dest){
		ArrayList<String> list = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(dest));
			String line;
			while((line=br.readLine())!=null){
				list.add(line);
			}
			br.close();
		}catch(Exception e) {}
		return list;
	}
	
}
