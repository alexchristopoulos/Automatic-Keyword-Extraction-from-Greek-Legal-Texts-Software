import java.util.ArrayList;
import java.io.*;
public class Collection {
	
	private ArrayList<Trie> Documents= new ArrayList<Trie>();
	private final String Collection_Destination="C:\\Users\\Public\\Documents\\Legal Texts\\Collection";
	
	public Collection(){
		this.LoadCollection();
	}
	
	public double InCorpusFrequency(String term) {
		double count=0;
		for(Trie file:this.Documents) {
			count = file.Get(term)>0?count+1:count;
		}
		return count==0?1:count;
	}
	
	private void LoadCollection() {
		try {
			File parent = new File(this.Collection_Destination);
			File[] files = parent.listFiles();
			for(File file:files){
				Documents.add(this.CreateDocument(this.ProcessLines(this.ReadFile(file))));
			}
		}catch(Exception e){System.out.println(e.toString());}
	}
	
	private ArrayList<String> ReadFile(File file){
		ArrayList<String> lines = new ArrayList<String>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line="";
			while((line=br.readLine())!=null){
				lines.add(line);
			}
			br.close();
		}catch(Exception e){System.out.println(e.toString());}
		return lines;
	}
	
	private ArrayList<String> ProcessLines(ArrayList<String> lines){
		ArrayList<String> newlines = new ArrayList<String>();
		for (int i = 0; i < lines.size(); i++) {
			String s = lines.get(i);
			s=s.replaceAll("\\[","");//remove left square bracket
			s=s.replaceAll("\\]","");//remove right square bracket
		    s=s.replaceAll("[-+`΄.'»‘%.“¨$–#@!”…|_?~;«/><^:)(’*}{,]", " ");
		    s=s.replaceAll("\\s{2,}", " ").trim().toLowerCase();
		    if(s.length()>1) {
			    newlines.add(s);
		    }
		    newlines.add(s);
		}
		return newlines;
	}
	
	private Trie CreateDocument(ArrayList<String> lines) {
		Trie trie = new Trie();
		for(String line:lines) {
			for(String word:line.split(" ")) {
				if(word.length()>1) {
					trie.Insert(word);
				}
			}
		}
		return trie;
	}
	
	public double getCorpusSize() {
		return (double)this.Documents.size();
	}
}
