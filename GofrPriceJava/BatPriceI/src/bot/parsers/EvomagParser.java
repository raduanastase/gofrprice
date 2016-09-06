package bot.parsers;

import bot.parsers.parser.Parser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import bot.item.ItemData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Radu on 01-Dec-14.
 *
 */
public class EvomagParser extends Parser {

    private static final int CRAWLER_ID = 0;
    private static final String BASE_URL = "http://www.emag.ro";
    private static final String SEARCH_URL = "http://www.evomag.ro/Produse/Filtru/Cautare:";

    public EvomagParser() {
        super();
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

            Elements allProducts = doc.select(".prod_list_det.tabel-view");
            //Elements allProducts = doc.select(".prod_list_detleft_poza.prod_list_detleft_poza_tabel.relative a");
            //Elements validProducts = new Elements();
            //System.out.println("allProducts\n"+ allProducts);

            //TO DO find the best match for my product - find the item that has all the items in the name and then find the one that has the terms as close together as possible
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
                linkWithProp = currentProduct.select(".prod_list_detleft_poza.prod_list_detleft_poza_tabel.relative a").get(0);
                tempTitle = linkWithProp.attr("title");
                tempTitleLowerCase = tempTitle.toLowerCase();
                isForbidden = false;
                nameIsValid = true;

                //todo verify if it's resigilat

                //verify if every part of the name that came from json it's found in name
                //this is a little bit tricky because the title could miss a word
                for(k = 0; k < itemNameSplit.length; k++) {
                    nameIsValid = tempTitleLowerCase.contains(itemNameSplit[k].toLowerCase());
                    if(!nameIsValid) break;
                }

                if (tempTitleLowerCase.contains(mandatory) && nameIsValid) {//get all the items that have the mandatory string together included in description
                    //verify if it has one of the forbidden strings
                    if(forbidden != null && forbidden.size() > 0) {
                       for (j = 0; j < forbidden.size(); j++) {
                           if (tempTitleLowerCase.contains(((String) forbidden.get(j)).toLowerCase()))
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
                                if(tempPrice < prodLowestPrice) {
                                    cheapestProductIdx = i;
                                    prodLowestPrice = tempPrice;
                                    prodUrl = BASE_URL + linkWithProp.attr("href");
                                    prodDetails = tempTitle;
                                }
                            }
                        }
                    }
                }
            }
            //System.out.println(cheapestProductIdx + " " + lowestPrice +" "+url);

            if (cheapestProductIdx > -1) {
                Element product = allProducts.get(cheapestProductIdx);

                //get product image
                Elements tempImgs = product.select("a").get(0).select("img");
                String productImgURL = BASE_URL;
                for (i = 0; i < tempImgs.size(); i++) {
                    if (tempImgs.get(i).attr("alt").length() == prodDetails.length()) {
                        productImgURL += tempImgs.get(i).attr("src");
                        break;
                    }
                }
                return new ItemData(CRAWLER_ID, prodUrl, prodLowestPrice, itemName, prodDetails, productImgURL);

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
        int i;
        for(i = 0; i < productComponents.length + 1; i++) {
            if(i == 0) tempURL += productComponents[i];
            else if(i > 0 && i < productComponents.length) tempURL += "%20" + productComponents[i];
            else tempURL += '/';
        }
        return tempURL;
    }
}
