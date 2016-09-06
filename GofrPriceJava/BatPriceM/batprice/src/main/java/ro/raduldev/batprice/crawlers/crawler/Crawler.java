package ro.raduldev.batprice.crawlers.crawler;


import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ro.raduldev.batprice.other.ItemInfo;

import java.io.IOException;
import java.util.ArrayList;

public class Crawler {

    public static final String DOCUMENT_NULL_EX = "document_null_ex";
    public static final String ITEM_NOT_FOUND_EX = "item_not_found";

    private String _url;
    private String _finalName;

    public Crawler() {

    }

    public Document crawl(String url) {
        Document doc = null;
        try {
            //i don't use Jsoup to get the content because it fails often and I can have control over my WebFile Class
            WebFile file   = new WebFile(url);
            Object content = file.getContent();
            if ( file.getMIMEType().equals( "text/html" ) && content instanceof String )
            {
                doc = Jsoup.parse((String)content);
                //String html = (String)content;
                //System.out.println(html);
            }
        } catch (IOException e) {

        }

        return doc;
    }

    public ItemInfo findItem(JSONObject jsonObj)  throws Exception {
        return null;
    }
}
