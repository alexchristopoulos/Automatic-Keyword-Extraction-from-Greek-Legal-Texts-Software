import java.util.ArrayList;

public class Trie
{
	
	private Node root;
	private int word_count=0;
	private ArrayList<String> allWords;
	
	public Trie(){
		allWords = new ArrayList<String>();
		root = new Node();
	}
	
	public void Insert(String word){
		if(word=="")
			return;
		char[] chars = word.toCharArray();
		int iter=0;
		if(chars.length>1){
			if(root.edges.containsKey(chars[iter])){
				Node temp = new Node();
				temp.edges.put(chars[iter], RecurseInsert(root.edges.get(chars[iter]),++iter,chars,word) );
				root.update(temp);
			}else{
				root.edges.put(chars[iter],this.RecurseInsert(root.edges.get(chars[iter]),++iter, chars, word));
			}
		}else{//of size 1
			boolean tmp = root.AddNewWord(word);
			if(tmp==true) {allWords.add(word);}++word_count;
		}
	}
	
	private Node RecurseInsert(Node current,int iter,char[] chars,String word){
		if(current==null){
			current = new Node();
			if(iter<chars.length-1){
				Node temp = new Node();
				temp.edges.put(chars[iter], RecurseInsert(current.edges.get(chars[iter]),++iter,chars,word) );
				current.update(temp);
				return current;
			}else{
				boolean tmp = current.AddNewWord(word);
				if(tmp==true) {allWords.add(word);}++word_count;
				return current;
			}
		}else{
			if(iter<chars.length-1){
				Node temp = new Node();
				temp.edges.put(chars[iter], RecurseInsert(current.edges.get(chars[iter]),++iter,chars,word) );
				current.update(temp);
				return current;
			}else{
				boolean tmp = current.AddNewWord(word);
				if(tmp==true) {allWords.add(word);}++word_count;
				return current;
			}
		}
	}
	
	public double Get(String word){//returns frequency of the word inside the Tree/Trie
		double response=0;
		char[] chars = word.toCharArray();
		Node current = root;
		for(int i=0;i<chars.length;i++){
			if(i==chars.length-1){
				if(current.words.contains(word)){
					response = current.word_freq.get(word);
				}
			}else{
				if(!current.edges.containsKey(chars[i])){
					return 0;
				}
			}
			current = current.edges.get(chars[i]);
		}
		return (double)response;
	}
	
	public ArrayList<String> GetAllExistingWords(){
		return this.allWords;
	}
	
	public double GetWordCount(){
		return (double)this.word_count;
	}
}