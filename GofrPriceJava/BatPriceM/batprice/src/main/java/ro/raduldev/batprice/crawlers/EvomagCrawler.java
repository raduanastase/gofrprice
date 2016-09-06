package ro.raduldev.batprice.crawlers;

import ro.raduldev.batprice.crawlers.crawler.Crawler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ro.raduldev.batprice.other.ItemInfo;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Radu on 01-Dec-14.
 */
public class EvomagCrawler extends Crawler {

    private static final String BASE_URL = "http://www.emag.ro";
    private static final String SEARCH_URL = "http://www.evomag.ro/Produse/Filtru/Cautare:";

    public EvomagCrawler() {

    }

    public ItemInfo findItem(JSONObject jsonObj) throws Exception {

        ItemInfo itemInfo;
        String itemName = (String) jsonObj.get("name");
        String itemNameLowerCase = itemName.toLowerCase();
        String mandatory = ((String) jsonObj.get("mandatory")).toLowerCase();
        JSONArray forbidden = (JSONArray) jsonObj.get("forbidden");
        String tempSearchURL = constructURL(itemName);
        Document doc = super.crawl(tempSearchURL);
        //System.out.println(tempSearchURL);
        //System.out.println(doc);
        if (doc != null) {
            int i;
            int j;

            Elements allProducts = doc.select(".prod_list_det.tabel-view");
            //Elements allProducts = doc.select(".prod_list_detleft_poza.prod_list_detleft_poza_tabel.relative a");
            //Elements validProducts = new Elements();
            //System.out.println("allProducts\n"+ allProducts);

            //TO DO find the best match for my product - find the item that has all the items in the name and then find the one that has the terms as close together as possible
            Element currentProduct;
            Element linkWithProp;
            String url = "";
            Boolean isForbidden;
            Boolean inStock;
            String tempTitle;
            String tempTitleLowerCase;
            Float tempPrice;
            int cheapestProductIdx = -1;
            Float lowestPrice = new Float(Float.MAX_VALUE);
            for (i = 0; i < allProducts.size(); i++) {
                currentProduct = allProducts.get(i);
                linkWithProp = currentProduct.select(".prod_list_detleft_poza.prod_list_detleft_poza_tabel.relative a").get(0);
                tempTitle = linkWithProp.attr("title");
                tempTitleLowerCase = tempTitle.toLowerCase();
                isForbidden = false;
                if (tempTitleLowerCase.indexOf(mandatory) > -1) {//get all the items that have the mandatory string together included in description
                    //verify if it has one of the forbidden strings
                    if(forbidden != null && forbidden.size() > 0) {
                       for (j = 0; j < forbidden.size(); j++) {
                           if (tempTitleLowerCase.indexOf(((String) forbidden.get(j)).toLowerCase()) > -1)
                               isForbidden = true;
                       }
                    }
                    if(!isForbidden) {
                        //System.out.println(StringUtils.getLevenshteinDistance(itemNameLowerCase, tempTitleLowerCase) + " -->" + i);
                        //sort the ones that are in stock
                        inStock = currentProduct.select(".stoc_produs").text().matches("(In stoc magazin)|(Intreaba stoc)|(In stoc furnizor)");
                        if(inStock) {
                            //System.out.println("inStock");
                            //validProducts.add(allProducts.get(i));
                            //find the price of the product and keep the cheapest one
                            Pattern pattern = Pattern.compile("clear:both\\D*([\\d\\D]*)(Lei)");
                            Matcher matcher = pattern.matcher(currentProduct.select(".price_block_list").html());
                            if(matcher.find()) {
                                tempPrice = Float.parseFloat(matcher.group(1).replace(".", "").replace(",", "."));
                                if(tempPrice < lowestPrice) {
                                    cheapestProductIdx = i;
                                    lowestPrice = tempPrice;
                                    url = BASE_URL + linkWithProp.attr("href");
                                }
                            }
                        }
                    }
                }
            }
            //System.out.println(cheapestProductIdx + " " + lowestPrice +" "+url);

            if (cheapestProductIdx > -1) {
                Element product = allProducts.get(cheapestProductIdx);

                //Get product info
                String productName = itemName;
                String productDescription = product.select("a").get(0).attr("title");

                //get product image
                Elements tempImgs = product.select("a").get(0).select("img");
                String productImgURL = BASE_URL;
                for (i = 0; i < tempImgs.size(); i++) {
                    if (tempImgs.get(i).attr("alt").length() == productDescription.length()) {
                        productImgURL += tempImgs.get(i).attr("src");
                        break;
                    }
                }
                return new ItemInfo(url, lowestPrice, productName, productDescription, productImgURL);

                //System.out.println(product);
            } else {
                throw new Exception("[WARNING] The product " + itemName + " could not be found.", new Throwable(Crawler.ITEM_NOT_FOUND_EX));
            }
        } else {
            throw new Exception("[WARNING] The document is null.\n Crawler:"+this.getClass() + " ItemInfo: "+ itemName, new Throwable(Crawler.DOCUMENT_NULL_EX));
        }
    }

    private String constructURL(String product) {
        String tempURL = SEARCH_URL;
        String[] productComponents = product.split(" ");
        int i;
        for(i = 0; i < productComponents.length + 1; i++) {
            if(i == 0) tempURL += productComponents[i];
            else if(i > 0 && i < productComponents.length) tempURL += "%20" + productComponents[i];
            else tempURL += '/';
        }
        return tempURL;
    }
}
