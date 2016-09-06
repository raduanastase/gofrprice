package ro.raduldev.batprice.crawlers;

import ro.raduldev.batprice.crawlers.crawler.Crawler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class EmagCrawler extends Crawler {
	
	private static final String BASE_URL = "http://www.emag.ro";
	private static final String SEARCH_URL = "http://www.emag.ro/search/";
	
	public EmagCrawler() {
		super();
		//_product = product;
		//System.out.println("constructed URL " + constructURL());
		
	}
	
	public void findProduct(String productName) {
		Document doc = super.crawl(constructURL(productName));
		if(doc != null) {
			Elements products = doc.select(".product-container");
			
			//TO DO find the best match for my product (for now I get the first one) - maybe find the item that has all the items in the name
			int productIdx = 0;
			Element product = products.get(productIdx);
			
			//Transform the product into an ItemInfo
			String productImgURL = product.select("div.img-container.square").get(0).child(0).attr("src");
			String linkToProductURL = BASE_URL + product.attr("href");
			Number priceOfProduct = Float.valueOf(product.select(".price-over").select(".money-int").html().replace(".", ""));
			
			Object productObj = new Object();
			//productObj.ss = 11;
			
			System.out.println(priceOfProduct);
		} else System.out.println("Document is NULL");
	}
	
	private String constructURL(String product) {
		String tempURL = SEARCH_URL;
		String[] productComponents = product.split(" ");
		for(int i = 0; i < productComponents.length + 1; i++) {
			if(i == 0) tempURL += productComponents[i];
			else if(i > 0 && i < productComponents.length) tempURL += "+" + productComponents[i];
			else tempURL += '/';
		}
		
		return tempURL;
	}

}
