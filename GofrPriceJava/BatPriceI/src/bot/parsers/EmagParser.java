package bot.parsers;

import bot.parsers.parser.Parser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import bot.item.ItemData;

public class EmagParser extends Parser {

    private static final int CRAWLER_ID = 1;
	private static final String BASE_URL = "http://www.emag.ro";
	private static final String SEARCH_URL = "http://www.emag.ro/search/";
	
	public EmagParser() {
		super();
		//_product = product;
		//System.out.println("constructed URL " + constructURL());
		
	}

    public ItemData findItem(JSONObject jsonObj) throws Exception {

        String itemName = (String) jsonObj.get("name");
        String[] itemNameSplit = itemName.split(" ");
        //String itemNameLowerCase = itemName.toLowerCase();
        String mandatory = ((String) jsonObj.get("mandatory")).toLowerCase();
        JSONArray forbidden = (JSONArray) jsonObj.get("forbidden");
        String tempSearchURL = constructURL(itemName);
        Document doc = super.crawl(tempSearchURL);
        //System.out.println(tempSearchURL);
        //System.out.println(doc);
        if (doc != null) {
            int i;
            int j;
            int k;

            Elements allProducts = doc.select(".product-holder-grid");

            String prodUrl = "";
            Float prodLowestPrice = Float.MAX_VALUE;
            String prodDetails = "";

            Element currentProduct;
            Element linkWithProp;
            Boolean isForbidden;
            Boolean inStock;
            String tempTitle;
            String tempTitleLowerCase;
            Float tempPrice;
            Boolean nameIsValid;
            int cheapestProductIdx = -1;
            for (i = 0; i < allProducts.size(); i++) {
                currentProduct = allProducts.get(i);
                linkWithProp = currentProduct.select(".poza-produs a").get(0);
                tempTitle = linkWithProp.attr("title");
                tempTitleLowerCase = tempTitle.toLowerCase();
                isForbidden = false;
                nameIsValid = true;

                //verify if it's resigilat
                if(!currentProduct.select("h2").text().toLowerCase().contains("resigilat")) {
                    //verify if every part of the name that came from json it's found in name
                    //this is a little bit tricky because the title could miss a word
                    for(k = 0; k < itemNameSplit.length; k++) {
                        nameIsValid = tempTitleLowerCase.contains(itemNameSplit[k].toLowerCase());
                        if(!nameIsValid) break;
                    }
                    //System.out.println("nameIsValid "+nameIsValid);
                    if (tempTitleLowerCase.contains(mandatory) && nameIsValid) {//get all the items that have the mandatory string together included in description
                        //verify if it has one of the forbidden strings
                        if(forbidden != null && forbidden.size() > 0) {
                            for (j = 0; j < forbidden.size(); j++) {
                                if (tempTitleLowerCase.contains(((String) forbidden.get(j)).toLowerCase()))
                                    isForbidden = true;
                            }
                        }
                        //System.out.println("isForbidden "+isForbidden+" "+tempTitle);
                        if(!isForbidden) {
                            //sort the ones that are in stock
                            inStock = !currentProduct.select("span.stare-disp-listing").text().matches("Stoc epuizat");
                            if(inStock) {
                                //System.out.println("inStock "+tempTitle);
                                //validProducts.add(allProducts.get(i));
                                //find the price of the product and keep the cheapest one
                                tempPrice = Float.parseFloat(currentProduct.select("span.price-over span.money-int").text().replace(".","") +"." +currentProduct.select("span.price-over sup.money-decimal").text());
                                //System.out.println("tempPrice "+tempTitle +" "+tempPrice);
                                if(tempPrice < prodLowestPrice) {
                                    cheapestProductIdx = i;
                                    prodLowestPrice = tempPrice;
                                    prodUrl = BASE_URL + linkWithProp.attr("href");
                                    prodDetails = tempTitle;
                                    //System.out.println("if "+tempTitle+lowestPrice);
                                }
                            }
                        }
                    }
                }
            }
            //System.out.println("Cheapest "+cheapestProductIdx + " "+ prodTitle+ " " + lowestPrice +" "+url);

            if (cheapestProductIdx > -1) {
                Element product = allProducts.get(cheapestProductIdx);

                //get product image
                String prodImgURL = product.select(".poza-produs a img").get(0).attr("src");
                return new ItemData(CRAWLER_ID, prodUrl, prodLowestPrice, itemName, prodDetails, prodImgURL);

                //System.out.println(product);
            } else {
                throw new Exception("[WARNING] The product " + itemName + " could not be found.", new Throwable(Parser.ITEM_NOT_FOUND_EX));
            }
        } else {
            throw new Exception("[WARNING] The document is null.\n Parser:"+this.getClass() + " ItemInfo: "+ itemName, new Throwable(Parser.DOCUMENT_NULL_EX));
        }
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
