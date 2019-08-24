import java.util.ArrayList;
import java.util.HashMap;
public class Node
{
	ArrayList<String> words;
	HashMap<Character,Node> edges;
	HashMap<String,Integer> word_freq;
	
	public Node(){
		words = new ArrayList<String>();
		edges = new HashMap<Character,Node>();
		word_freq = new HashMap<String,Integer>();
	}
	
	public boolean AddNewWord(String word){
		if(this.word_freq.containsKey(word)){//existing
			int curr_freq = word_freq.get(word);
			word_freq.replace(word, ++curr_freq);return false;
		}else{//new entry
			words.add(word);
			word_freq.put(word,1);return true;
		}
	}
	
	public void update(Node new_node){
		this.word_freq.putAll(new_node.word_freq);
		this.edges.putAll(new_node.edges);
		this.words.addAll(new_node.words);
	}
	
}