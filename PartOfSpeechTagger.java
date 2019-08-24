import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import gr.aueb.cs.nlp.postagger.SmallSetFunctions;

public class PartOfSpeechTagger {
	
	private HashMap<String,String> tags = new HashMap<String,String>();//e.g. (word1,noun)
	
	private String filename;
	
	public PartOfSpeechTagger(String filename){
		this.filename = filename;
	}
	
	public ArrayList<String> FindCandidates(ArrayList<String> document){
		ArrayList<String> candidates = new ArrayList<String>();
		this.getWordTags(document);
		candidates.addAll(this.getSingles(document));
		candidates.addAll(this.Remove_Duplicates(this.getPhrases(document)));
	    this.Write(candidates);
		return candidates;
	}
	
	private String ReturnCategory(String word){
		try{
			return SmallSetFunctions.smallSetClassifyString(word).get(0).getCategory();
		}catch(Exception NotInCat){return "";}	
	}
	
	private void getWordTags(ArrayList<String> document) {
		this.LoadIfExists_ExistingWordTags();
		for(String line:document) {
			for(String word:line.split(" ")) {
				if(!this.tags.containsKey(word)) {
					this.tags.put(word,this.ReturnCategory(word));
				}
			}
		}
	}
	
	private ArrayList<String> getSingles(ArrayList<String> document){
		Ngram one_grams = new Ngram(1,document);
		ArrayList<String> res = new ArrayList<String>();
		for(String candidate: one_grams.getForWholeDoc()){
			if(!res.contains(candidate)&&(this.isNoun(candidate)==true||this.isAdjective(candidate)==true)){
				res.add(candidate);
			}
		}
		return res;
	}
	
	private void LoadIfExists_ExistingWordTags(){
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Public\\Documents\\Legal Texts\\Word Tags\\"+this.filename));
			String line;
			while( !(line=br.readLine()).contains("*****") ) {
				String[] tok = line.split(" ");
				if(tok.length>1 && tok!=null){
					this.tags.put(tok[0],tok[1]);
				}
			}br.close();
		}catch(Exception TagsNotFound) {}
	}
		
	private boolean isNoun(String word) {
		return this.tags.get(word).equals("noun")==true?true:false;
	}
	
	private boolean isAdjective(String word) {
		return this.tags.get(word).equals("adjective")==true?true:false;
	}
	
	private ArrayList<String> getPhrases(ArrayList<String> document){
		ArrayList<String> candidates = new ArrayList<String>();
		for(int i=2;i<=4;i++) {
			Ngram ngram = new Ngram(i,document);
			for(String phrase:ngram.getForWholeDoc()) {
				phrase = this.PhraseRegex(phrase);
				if(phrase!=null && (!candidates.contains(phrase))) {
					candidates.add(phrase);
				}
			}
		}
		return candidates;
	}
	
	private String PhraseRegex(String phrase) {
		String[] words = phrase.split(" ");
		if(words.length<1) {
			return null;
		} else {
			if(this.isNoun(words[0])) {
				return this.NounPrepNoun(words);
			}else if(this.isAdjective(words[0])){
				return this.EpithetikosProsdiorismos(words);
			}else {
				return null;
			}
		}
	}
	
	
	private String NounPrepNoun(String[] phrase) {
		String n="";
		if(phrase.length>2) {
			if(this.isNoun(phrase[1]))
				return this.NounSequence(phrase);
			if(this.tags.get(phrase[1]).equals("noun")) {
				return null;
			}
			if((this.tags.get(phrase[1]).equals("preposition")||this.tags.get(phrase[1]).equals("article"))&&this.isNoun(phrase[2])) {
				 n = n + phrase[0] + " " + phrase[1] + " " + phrase[2];
				 if(phrase.length==4 && this.isNoun(phrase[3])) {
					 n+= " "+phrase[3];
				 }
				return n;
			}else {
				return null;
			}
		}else {
			return null;
		}
	}
	
	private String NounSequence(String[] phrase) {//[noun]*
		String parse = "" + phrase[0]+" ";
		int ncount=1;
		for(int i=1;i<phrase.length;i++) {
			if(this.isNoun(phrase[i])==false) {
				break;
			}else if(this.isNoun(phrase[i])&&ncount<=3){
				parse += phrase[i]+" ";ncount++;
			}
		}
		return parse.split(" ").length>1?parse.trim():null;
	}
	
	private String EpithetikosProsdiorismos(String[] phrase) {//one adjective with two nouns at most
		final int max_nouns=3;
		String parse = "" + phrase[0]+" ";
		int num_of_nouns=0;
		for(int i=1;i<phrase.length;i++) {
			if(num_of_nouns==0 && this.isNoun(phrase[i])==false) {
				return null;
			}else if(num_of_nouns>0 && num_of_nouns<max_nouns && this.isAdjective(phrase[i])) {
				num_of_nouns++;
				parse = parse + phrase[i] + " ";
			}else if(this.isNoun(phrase[i])==false && num_of_nouns>0) {
				break;
			}
		}
		return parse.trim();
	}
	
		
	private void Write(ArrayList<String> candidates) {
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> cat = new ArrayList<String>();
		for(Map.Entry<String,String> e: this.tags.entrySet()){
			words.add(e.getKey());
			cat.add(e.getValue());
		}
		new GenericOutput(words,cat,candidates,this.filename);
	}
	
	private ArrayList<String> Remove_Duplicates(ArrayList<String> phrases){
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> res = new ArrayList<String>();
		for(String phrase:phrases) {
			if(!list.contains(this.RemoveSameWords(phrase))) {
				list.add(this.RemoveSameWords(phrase));
			}
		}
		for(String phrase:list) {
			boolean add=true;
			for(String p:res) {
				if(this.HaveSameWords(phrase, p)==true) {
					add=false;
					break;
				}
			}
			if(add==true) {
				res.add(phrase);
			}
		}
		res = this.RemoveSubsets(res);
		return res;
	}
	
	private String RemoveSameWords(String phrase) {
		ArrayList<String> used = new ArrayList<String>();
		String new_phrase="";
		for(String word:phrase.split(" ")) {
			if(!used.contains(word)) {
				new_phrase=new_phrase+word+" ";
				used.add(word);
			}
		}
		return new_phrase;
	}
	
	private boolean HaveSameWords(String phrase1,String phrase2) {
		int count=0;
		for(String word:phrase1.split(" ")){
			for(String w:phrase2.split(" ")) {
				if(w.equals(word)) {
					count+=1;
				}
			}
		}
		return count==phrase1.split(" ").length?true:false;
	}
	
	private ArrayList<String> RemoveSubsets(ArrayList<String> phrases){
		ArrayList<String> indices = new ArrayList<String>();
		for(int len=4;len>=2;len--) {
			ArrayList<String> current = new ArrayList<String>();
			for(String p:phrases) {
				if(p.split(" ").length==len) {
					current.add(p);
				}
			}
			for(String p:phrases) {
				if(p.split(" ").length == len-1) {
					if(this.IsSubset(p, current))
						indices.add(p);
				}
			}
		}
		for(String s:indices) {
			phrases.remove(s);
		}
		return phrases;
	}
	
	private boolean IsSubset(String p,ArrayList<String> current) {
		for(String w:current) {
			int count=0;
			for(String ww:w.split(" ")) {
				for(String x:p.split(" ")) {
					if(ww.equals(x))
						count++;
				}
			}
			if(count==p.split(" ").length)
				return true;
		}
		return false;
	}
}
