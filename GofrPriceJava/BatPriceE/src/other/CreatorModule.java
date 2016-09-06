/**
 * 
 */
package other;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.eaio.stringsearch.BNDMCI;

/**
 * @author Radu
 *
 */
public class CreatorModule extends Crawler {
	
	//for now i chose evomag, but it can be any site
	private static final String BASE_URL = "http://www.evomag.ro";
	private static final String SEARCH_URL = "http://www.evomag.ro/Produse/Filtru/Cautare:";
	
	private int productIndex = 0;
	private String[] productsName;
	private ArrayList<Item> items = new ArrayList<Item>();
	private Timer timer = new Timer();
	
	public CreatorModule(String[] pNames) throws IOException {
		productsName = pNames;
		
		findItem(productsName[productIndex]);
	}
	
	
	private void findItem(String prodName) throws IOException {
		String tempSearchURL = constructURL(prodName);
		System.out.println(tempSearchURL);
		Document doc = super.crawl(tempSearchURL);
		if(doc != null) {
			int i;
			int j;
			
			Elements products = doc.select(".prod_list_detleft_poza.prod_list_detleft_poza_tabel.relative a");
			
			//TO DO find the best match for my product - find the item that has all the items in the name and then find the one that has the terms as close togheter as possible
			int productIdx = 0;
			String tempTitle;
			String[] nameArr = prodName.split(" ");
			for(i = 0; i < products.size(); i++) {
				tempTitle = products.get(i).attr("title");
				//find the best matching title
				//BNDMCI search = new BNDMCI();
				//System.out.println(search.searchString(prodName, tempTitle));
				
//				for(j = 0; j < nameArr.length; j++) {
//					tempTitle.indexOf(nameArr[j]);
//				}
			}
			Element product = products.get(productIdx);
			
			//Transform the product into an Item
			String productName = prodName;
			String productDescription = product.select("a").get(0).attr("title");
			
			Elements tempImgs = product.select("a").get(0).select("img");
			String productImgURL = BASE_URL;
			for(i = 0; i < tempImgs.size(); i++) {
				if(tempImgs.get(i).attr("alt").length() == productDescription.length()) {
					productImgURL += tempImgs.get(i).attr("src");
					break;
				}
			}
			
			Item tempItem = new Item(productName, productDescription, productImgURL);
			items.add(tempItem);
			
			System.out.println(product);
			
			//after a delay, go to the next item
			productIndex++;
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						if(productIndex < productsName.length) findItem(productsName[productIndex]);
						else System.out.println("Items finished building");
					} catch(IOException e) {
						
					}
				}
			}, 7*1000);
			
			
		} else System.out.println("Document is NULL");
	}
	
	private String constructURL(String product) {
		String tempURL = SEARCH_URL;
		String[] productComponents = product.split(" ");
		for(int i = 0; i < productComponents.length + 1; i++) {
			if(i == 0) tempURL += productComponents[i];
			else if(i > 0 && i < productComponents.length) tempURL += "%20" + productComponents[i];
			else tempURL += '/';
		}
		
		return tempURL;
	}

}
