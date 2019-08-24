	import java.io.*;
	import org.apache.pdfbox.pdmodel.PDDocument;
	import org.apache.pdfbox.text.PDFTextStripper;
	import org.apache.pdfbox.text.PDFTextStripperByArea;
	public class Program {
		//                                           >>>>>>>>>>>>  PDF2TEXT  <<<<<<<<<<<<<<<
		public static void main(String[] args) {
			 File dir = new File("C:\\Users\\Public\\Documents\\Crawler");
			 File[] directoryListing = dir.listFiles();
			 if (directoryListing != null) {
				 for (File child : directoryListing) {
					 try {
						 String[] lines = LoadPDFText(child.getAbsolutePath());
						 if(isScan(lines,child.getName())==false){//ean den einai scanarismeno pdf
							 WriteFile("C:\\Users\\Public\\Documents\\Pdf2Text\\"+getName(child.getName())+".txt",lines);
			    	     }
			    	 }catch(Exception l) {}
			     }
			 }
		}
		
		private static void WriteFile(String destination,String[] lines) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(destination));
				for(String line:lines) {
					bw.write(line);
					bw.newLine();
				}
				bw.close();
			}catch(Exception e) {
				System.out.println("Write File->"+ e.toString());
			}
		}

		public static String[] LoadPDFText(String filename)
		{
			try {
				String[] lines;
			    File RF = new File(filename);
			    PDDocument document = PDDocument.load(RF);
	            document.getClass();
	            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
	            stripper.setSortByPosition(true);
	            PDFTextStripper tStripper = new PDFTextStripper();
	            String pdfFileInText = tStripper.getText(document);
	            lines = pdfFileInText.split("\\r?\\n");
	            document.close();
	            return lines;
			}catch(Exception e) { System.out.println("Could not parse PDF: "+e.toString()); }
			return null;
			
		}
		
		private static String getName(String Filename) {
			char[] arr = Filename.toCharArray();
			String name="";
			for(char c:arr) {
				if(c!='.') {
					name+=c;
				}else {
					break;
				}
			}
			return name;
 		}
		
		private static boolean isScan(String[] lines,String fname){
			int lines_count=0;
			
			for(String s:lines){
				
				if(s.startsWith("ΑΔΑ:") || s.startsWith("ΑΔΑ :")) {
				}else{
					lines_count++;
				}
			}
			if(lines_count>0){
				return false;//einai pdf me keimeno
			}else{
				return true;
			}
		}
	}

	
	