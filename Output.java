import java.io.*;
import java.util.ArrayList;
public class Output {
	
	private double threshold_for_phrases=0;
	private double threshold_for_words=0;
	//private final double a=3.08;//coefficient for single words
	//private final double b=2.93;//coefficient for phrases
	private final double a=1.22;
	private final double b=0.8;
	
	public Output(Candidate[] keywords,String filename){
		keywords = this.avoidNull(keywords);
		this.WriteLog(keywords, filename);
		int total=keywords.length;int assigned=0;
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Public\\Documents\\Legal Texts\\Keywords\\"+filename));
			this.initThresholds(keywords);
			this.IncrementThresholds(keywords);
			this.IncrementThresholds(keywords);
			quickSort(keywords,0,keywords.length-1);
			for(Candidate c:keywords){
				if(c!=null){
					if(c.getValue().split(" ").length==1){//keyword
						if(c.getRank()>this.threshold_for_words && this.isCode(c.getValue())==false){
							//bw.write(c.getValue() + "     ( Rank:" + c.getRank()+", Tf-Idf:"+c.getTFIDF()+", Info:"+c.getInfo()+" ,len:"+c.getLen()+")");
							bw.write(c.getValue());
							bw.newLine();
							assigned++;
						}
					}else{//keyphrase
						if(c.getRank()>this.threshold_for_phrases && this.isCode(c.getValue())==false){
							//bw.write(c.getValue() + "     ( Rank:" + c.getRank()+", Tf-Idf:"+c.getTFIDF()+", Info:"+c.getInfo()+" ,len:"+c.getLen()+")");
							bw.write(c.getValue());
							bw.newLine();
							assigned++;
						}
					}
				}
			}
			bw.write("_____\nNumber of Keywords: "+assigned + ", Number of Candidates:"+total + ", Threshold(Keywords)" + this.threshold_for_words + " ,Keyphrases:" + this.threshold_for_phrases);
			bw.close();
		}catch(Exception wo){System.out.println("WARNING! -> WRITING OUTPUT FAILED...\nProblem: " + wo.toString());}
	}
	
	private void initThresholds(Candidate[] keywords){
		setForSingleWords(this.getSingles(keywords));
		setForPhrases(this.getSingles(keywords));
	}
	
	private void IncrementThresholds(Candidate[] arr)
	{
		double mean1=0,mean2=0,n1=0,n2=0;
		for(Candidate c:arr){
			if(c.getValue().split(" ").length>1) {
				if(c.getRank()>this.threshold_for_words) {
					mean1+=c.getRank();n1+=1;
				}
			}else {
				if(c.getRank()>this.threshold_for_words) {
					mean2+=c.getRank();n2+=1;
				}
			}
		}
		this.threshold_for_words=a*mean1/n1;
		this.threshold_for_phrases=b*mean2/n2;
	}
	
	private void setForSingleWords(ArrayList<Candidate> clist){
		double s=0; double k =0;
		for(Candidate c:clist){
			if(c.getRank()>0){
				k++;
				s+=c.getRank();
			}
		}
		s = (s/k);
		this.threshold_for_words = s;
	}
	
	private void setForPhrases(ArrayList<Candidate> clist){
		double s=0; double k =0;
		for(Candidate c:clist){
			k++;
			s+=c.getRank();
		}
		s = (s/k);
		this.threshold_for_phrases = s;
	}
	
	private ArrayList<Candidate> getSingles(Candidate[] arr){
		ArrayList<Candidate> res = new ArrayList<Candidate>();
		for(Candidate c:arr){
			if(c!=null){
				if(c.getValue().split(" ").length==1){
					res.add(c);
				}
			}
		}
		return res;
	}
		
	private boolean isCode(String word){
		int digit_count=0;
		char[] arr=word.toCharArray();
		for(char c:arr){
			try {
				String tmp =""+c;
				Integer.parseInt(tmp);
				digit_count++;
			}catch(Exception e) {}
		}
		return digit_count>0?true:false;
	}
	
	
	private Candidate[] avoidNull(Candidate[] arr){
		Candidate[] res;
		ArrayList<Candidate> tmp = new ArrayList<Candidate>();
		for(Candidate c:arr) {if(c!=null && c.getValue().length()>0) {tmp.add(c);}}
		res = new Candidate[tmp.size()];
		res = tmp.toArray(res);
		return res;
	}
	
	
	private static int partition(Candidate arr[], int left, int right) { //change{
	      int i = left, j = right;
	      Candidate tmp; //change
	      Candidate pivot = arr[(left + right) / 2];  //comparator***
	      while (i <= j) {
	            while (arr[i].getRank() > pivot.getRank()) //comparator***
	                  i++;
	            while (arr[j].getRank() < pivot.getRank())   //comparator***
	                  j--;
	            if (i <= j) {
	                  tmp = arr[i];
	                  arr[i] = arr[j];
	                  arr[j] = tmp;
	                  i++;
	                  j--;
	            }
	      };
	      return i;
	}
	
	private static void quickSort(Candidate arr[], int left, int right) { //change
		if(arr.length==0)return;
	      int index = partition(arr, left, right);
	      if (left < index - 1)
	            quickSort(arr, left, index - 1);
	      if (index < right)
	            quickSort(arr, index, right);
	}
	
	private void WriteLog(Candidate[] arr,String filename){
		ArrayList<Candidate> n1 = new ArrayList<Candidate>();Candidate[] arr1;
		ArrayList<Candidate> n2 = new ArrayList<Candidate>();Candidate[] arr2;
		ArrayList<Candidate> n3 = new ArrayList<Candidate>();Candidate[] arr3;
		ArrayList<Candidate> n4 = new ArrayList<Candidate>();Candidate[] arr4;
		for(Candidate c: arr){
			switch(c.getValue().split(" ").length) {
			case 1:{n1.add(c);break;}
			case 2:{n2.add(c);break;}
			case 3:{n3.add(c);break;}
			case 4:{n4.add(c);break;}
			}
		}
		arr1 = new Candidate[n1.size()];arr1 = n1.toArray(arr1);
		arr2 = new Candidate[n2.size()];arr2 = n2.toArray(arr2);
		arr3 = new Candidate[n3.size()];arr3 = n3.toArray(arr3);
		arr4 = new Candidate[n4.size()];arr4 = n4.toArray(arr4);
		if(arr1.length>1) {quickSort(arr1,0,arr1.length-1);}
		if(arr2.length>1) {quickSort(arr2,0,arr2.length-1);}
		if(arr3.length>1) {quickSort(arr3,0,arr3.length-1);}
		if(arr4.length>1) {quickSort(arr4,0,arr4.length-1);}
		this.WriteLog(arr1, arr2, arr3, arr4,filename);
	}
	
	private void WriteLog(Candidate[] arr1,Candidate[] arr2,Candidate[] arr3,Candidate[] arr4,String filename){
		WriteLog(arr1,"n1",filename);
		WriteLog(arr2,"n2",filename);
		WriteLog(arr3,"n3",filename);
		WriteLog(arr4,"n4",filename);
	}
	
	private void WriteLog(Candidate[] arr,String x,String filename){
		try {
			String dest="C:\\Users\\Public\\Documents\\Legal Texts\\Other\\"+x+"\\"+filename;
			BufferedWriter bw = new BufferedWriter(new FileWriter(dest));
			for(Candidate c:arr){
				bw.write(c.getValue() + "    Final Rank:    " + c.getRank() + "   Info: " + c.getInfo() + "   TFIDF:" + c.getTFIDF() + "     LEN:" + c.getLen());
				bw.newLine();
			}
			bw.close();
		}catch(Exception n){
			System.out.println("WriteLog: "+n.toString());
		}
	}
}