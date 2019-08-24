import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.io.*;
public class program {
	//                                                  >>>>>>> Download from diavgeia API <<<<<<<
	static String mainh="https://diavgeia.gov.gr/luminapi/opendata/search/advanced?q=";
	static String query="decisionType:\"ΓΝΩΜΟΔΟΤΗΣΗ\"ANDorganizationUid:\"50024\"ANDstatus:\"Αναρτημένη\"";//gnwmodotiseis apo NSK
	static String page="&page=1&size=50";
	static final int pnum=35;
	
	public static String getHTML(String urlToRead) throws Exception {
	      StringBuilder result = new StringBuilder();
	      URL url = new URL(urlToRead);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         result.append(line);
	      }
	      rd.close();
	      System.out.println("RESPONSE OK");
	      return result.toString();
	   }
	   
	   
	public static void main(String[] args) {
		System.out.println("***Diavgeia API Downloader***");
		for(int i=1;i<=pnum;i++){
			try {
				String res = getHTML(mainh+query+page);
				String[] tok = res.split(",");
				page = "&page="+Integer.toString(i-1)+"&size=50"; 
				System.out.println("At WebPage: "+ mainh+query+page);
				for(String s:tok){
					if(s.startsWith("\"documentUrl\"")==true){
						String[] p = s.split("\"");
						if(p.length>3){//etc if indeed contains download url
							String durl = p[3];
							String fname = getOriginalName(durl);
							fname = fname.replaceAll("[\\\\/:*?\"<>|]", "");
							int success=0;
							while(success==0){
								try{
									downloadUsingNIO(durl, "C:/Users/Public/Documents/Crawler/"+fname+".pdf");
									success=1;
								}catch(Exception o){
									success=0;
								}
							}
						}
					}
				}
			}catch(Exception e){
				System.out.println("Page " + i+">> "+e.toString());
			}
		}
	}
	private static String getOriginalName(String url){
		String res="";
		int i=0;
		char[] tmp = url.toCharArray();
		for(char c:tmp){
			if(i==4){
				res=res+c;
			}
			if(c=='/'){
				i+=1;
			}
		}
		return res;
	}
	
	private static void downloadUsingNIO(String urlStr, String file) throws IOException {
	        URL url = new URL(urlStr);
	        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
	        FileOutputStream fos = new FileOutputStream(file);
	        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	        fos.close();
	        rbc.close();
	    }
}
