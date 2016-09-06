/**
 * 
 */
package ro.raduldev.batprice.other;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ro.raduldev.batprice.crawlers.crawler.Crawler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Radu
 *
 */
public class CreatorModule {
	
	//for now i chose evomag, but it can be any site
    private int crawlerIdx = 1;//by default i go with evomag (for now)

	private int productIndex = 0;
	private JSONArray itemsToCreate;
    private ArrayList<Crawler> crawlers;
	private ArrayList<ItemInfo> items = new ArrayList<ItemInfo>();
	private Timer timer = new Timer();
	
	public CreatorModule(JSONArray iToCreate, ArrayList<Crawler> crawlersArr) {
		itemsToCreate = iToCreate;

        crawlers = crawlersArr;
		//System.out.println(itemsToCreate);
		findItem((JSONObject) itemsToCreate.get(productIndex));
	}
	
	
	private void findItem(JSONObject jsonObj) {
        ItemInfo tempItemInfo;
        try {
            tempItemInfo = crawlers.get(crawlerIdx).findItem(jsonObj);
            System.out.println(tempItemInfo.getName() +" "+tempItemInfo.getPrice());
        } catch (Exception e) {
            if(e.getCause() != null) {
                if(e.getCause().getMessage() == Crawler.DOCUMENT_NULL_EX) {
                    //try again
                    productIndex--;
                } else if(e.getCause().getMessage() == Crawler.ITEM_NOT_FOUND_EX) {

                }
                System.out.println(e.getMessage());
            }
        }


        //todo create the Item based on item info

        //after a delay, go to the next item
        productIndex++;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //System.out.println(productIndex+ " "+ (JSONObject)itemsToCreate.get(productIndex));
                if(productIndex < itemsToCreate.size()) findItem((JSONObject)itemsToCreate.get(productIndex));
                else System.out.println("[LOG] Items finished building");
            }
        }, 3*1000);
	}
}
