import java.util.*;
public class Ngram {
	private int n;
	private ArrayList<String> context;
	
	public Ngram(int n,ArrayList<String> context){
		this.context = context;
		this.n=n;
	}
	
	public ArrayList<String> getForWholeDoc(){
		String[] Word_Array = this.getWordArray();
		if(this.n==1){
			return this.Array2List(Word_Array);
		}else {
			int right=this.n-1; int left=0;
			ArrayList<String> combinations = new ArrayList<String>();
			while(right<Word_Array.length) {
				String phrase="";
				for(int i=left;i<=right;i++){
					phrase+=Word_Array[i]+" ";
				}
				combinations.add(phrase);
				left+=1;right+=1;
			}
			return combinations;
		}
	}
	
	private String[] getWordArray(){
		ArrayList<String> words = new ArrayList<String>();
		for(String line:this.context){
			for(String word:line.split(" ")){
				words.add(word);
			}
		}
		return words.toArray(new String[words.size()]);
	}
	
	private ArrayList<String> Array2List(String[] arr){
		ArrayList<String> res = new ArrayList<String>();
		for(String s:arr){
			res.add(s);
		}
		return res;
	}
}