import java.util.*;
public class ExtractKeywords {
	
	ArrayList<Trie> documents_Tries = new ArrayList<Trie>();
	ArrayList<ArrayList<String>> candidates = new ArrayList<ArrayList<String>>();//candidates foreach doc
	ArrayList<ArrayList<String>> keywords = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> documents = new ArrayList<ArrayList<String>>();//content foreach doc
	ArrayList<String> filenames = new ArrayList<String>();//name foreach doc
	private Collection corpus = new Collection();
	
	public ExtractKeywords(ArrayList<ArrayList<String>> docs,ArrayList<ArrayList<String>> cnd,ArrayList<String> filenames,ArrayList<Trie> tries){
		documents=docs;
		candidates=cnd;
		this.filenames=filenames;
		this.documents_Tries = tries;
	}
	
	public void Extract(){
		for(int i=0;i<documents.size();i++){
			this.ExtractForDocument(filenames.get(i),documents.get(i),candidates.get(i),documents_Tries.get(i));//and trie
			System.out.print(".");
		}
		System.out.print("complete");
	}
	
	private void ExtractForDocument(String currFileName,ArrayList<String> content,ArrayList<String> cnds,Trie currentTrie){
		Candidate[] ranking = new Candidate[cnds.size()];
		int iter=0;
		HashMap<String,double[]> single_words = new HashMap<String,double[]>();
		for(String candidate:cnds){
			if(candidate.split(" ").length==1){ //keyword
				if(single_words.containsKey(candidate)==false){
					double[] attributes = new double[3];
					attributes[0] = this.getTf(candidate,currentTrie);
					attributes[1] = this.getIdf(candidate);
					attributes[2] = this.getInfo(candidate,currentTrie);
					single_words.put(candidate, attributes);
					ranking[iter] = new Candidate(candidate,attributes[0]*attributes[1],attributes[2]);
					iter+=1;
				}
			}
		}
		for(String candidate:cnds){ //keyphrase
			String[] words=candidate.split(" ");
			if(words.length>1){
				ArrayList<Double> avg1 = new ArrayList<Double>();
				ArrayList<Double> avg2 = new ArrayList<Double>();
				for(String w:words){
					if(single_words.containsKey(w)) {
						double[] temp = single_words.get(w);
						avg1.add(temp[0]*temp[1]);
						avg2.add(temp[2]);
						
					}
				}
				ranking[iter] = new Candidate(candidate,this.getPhraseTfIdf(avg1),this.getPhraseInfo(avg2));
				iter+=1;
			}
		}
		new Output(ranking,currFileName);
	}
	
	private double getTf(String term,Trie trie){
		double tf=0;
		tf = Math.log10(trie.Get(term)+1);
		return tf/Math.log10(2);//log2(tf+1)
	}
	
	private double getIdf(String term){
		double idf = Math.log10(this.corpus.getCorpusSize()/this.corpus.InCorpusFrequency(term));
		return  idf/Math.log10(2);//log2(N/id.t)
	}
	
	private double getInfo(String term,Trie t){
		double Prob = ( t.Get(term)+1)/t.GetWordCount();
		return -(Math.log10(Prob)/Math.log10(2));
	}
	
	private double getPhraseTfIdf(ArrayList<Double> arr){
		double toReturn = 0;
		for(double d: arr){
			toReturn+=d;
		}
		return toReturn/arr.size();
	}
	
	private double getPhraseInfo(ArrayList<Double> arr){
		double toReturn = 0;
		for(double d: arr){
			toReturn+=d;
		}
		return toReturn/(double)arr.size();
	}
}