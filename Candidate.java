public class Candidate {
	
	private String value;
	private double rank;
	private double tfidf,len;
	private double I;
	
	public Candidate(String x,double tfidf,double I){
		this.value = x;
		this.tfidf=tfidf;
		this.I=I;
		this.len = this.getLen(x)>=11?3.45943161864:Math.log10(this.getLen(x))/Math.log10(2);
		this.rank=this.CalculateRank();
	}
	
	private double getLenPhrase(String x){
		double sum=0,n=0;
		for(String w:x.split(" ")) {
			sum+=w.length();n++;
		}
		return sum/n;
	}
	
	private double getLen(String x){
		if(x.split(" ").length>1)
			return this.getLenPhrase(x);
		else
			return this.getLenWord(x);
	}
	
	
	
	private double getLenWord(String x){
		return x.length();
	}
	
	public double getRank(){
		return this.rank;
	}
	
	public String getValue(){
		return this.value;
	}
	
	private double CalculateRank(){
		double toReturn = this.tfidf*this.I*this.len;
		return toReturn;
	}
	
	public double getInfo(){
		return this.I;
	}
	
	public double getLen(){
		return this.len;
	}
	
	public double getTFIDF(){
		return this.tfidf;
	}
}
