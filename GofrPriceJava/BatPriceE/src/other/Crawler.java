package other;


import java.io.IOException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawler {
	
	

	private String _url;
	private String _finalName;
	
	public Crawler() {
		
	}
	
	public Document crawl(String url) throws IOException {
		
		//File input = new File("/test/test.html");
		
		
		//Document doc = Jsoup.parse(input, "UTF-8");
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			
//			Elements products = doc.select(".product-container");
//			Elements productTitles = products.select(".product-title");
//			
//			//_finalName= findTheGoodName
//	        System.out.print(productTitles);
	        
		} catch (HttpStatusException ex) {
			System.out.println(ex);
			if(ex.getStatusCode() == 511) {
				System.out.println(url+" has blocked me!");
			}
		}
		return doc;
//		for(int i = 0; i < 100; i++) {
//			doc = Jsoup.connect(url).get();
//			System.out.println(i);
//		}
		//Document doc = Jsoup.connect(url).get();
		
		/*BufferedWriter writer = null;
		try {
		    writer = new BufferedWriter(new FileWriter("emag.html"));
		    writer.write(doc.html());

		} catch (IOException e) {
			
		}
		finally	{
		    try {
		        if ( writer != null)
		        writer.close( );
		    } catch ( IOException e) {
		    
		    }
		}*/
		//Element text = doc.select(".product-container").first();
		//Element text = doc.getElementById("products-holder");
		
        
        
	}
}
