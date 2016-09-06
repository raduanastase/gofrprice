/**
 * 
 */
package bot.item;

import java.util.*;

import bot.item.hibernate.Item;
import bot.item.hibernate.ItemInfo;
import bot.item.hibernate.PriceInfo;
import bot.parsers.parser.Parser;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author Radu
 *
 */
public class CreatorModule extends Observable {
	public static String IDLE = "idle";
	public static String WORKING = "working";
	public static String COMPLETE = "complete";
	private String state = IDLE;//maybe idle is the same as complete and thus I should use only two states

	//for now i chose evomag, but it can be any site
    private int crawlerIdx = 0;//by default I go with evomag(0) (for now)

	private int productIndex = 0;
	private JSONArray itemsToCreate;
    private ArrayList<Parser> parsers;
    private Map<String, Item> itemsDict;
	private Timer timer = new Timer();
	private Date today = new Date();
	
	public CreatorModule(JSONArray itemsToCreate, ArrayList<Parser> parsers, Map<String, Item> itemsDict) {
		this.itemsToCreate = itemsToCreate;

        this.parsers = parsers;

        this.itemsDict = itemsDict;
		//System.out.println(itemsToCreate);

		/*if(item is in db) updateItem(item);
		else findItem((JSONObject) this.itemsToCreate.get(productIndex));*/

        //for every object from the json, it should go through each parser and after that it should go to the next object
		findItem((JSONObject) this.itemsToCreate.get(productIndex));
	}
	
	private Map<String, HashSet> itemsInfoDict = new HashMap<String, HashSet>();
	private Map<ItemInfo, HashSet> priceInfoDict = new HashMap<ItemInfo, HashSet>();

	private void findItem(JSONObject jsonObj) {
		//TODO MAJOR - combine the Creator and Search Module into one to add the new items and update the old ones
		state = WORKING;

        ItemData tempItemData;
		ItemInfo tempItemInfo;
		PriceInfo tempPriceInfo;
		Item tempItem;
		String tempItemName;
        try {
            //here I should add the object if it's not in the dictionary or I should add the price, url to the corresponding object
            tempItemData = parsers.get(crawlerIdx).findItem(jsonObj);
			tempItemInfo = new ItemInfo(tempItemData.getCrawlerID(), tempItemData.getUrl());
			tempPriceInfo = new PriceInfo(tempItemData.getPrice(), today);
			tempItemName = tempItemData.getName();
            System.out.println(tempItemName +" "+ tempItemData.getPrice());

			//if an Item does not exist in the dictionary, create it
            if(itemsDict.get(tempItemName) == null) {
				tempItem = new Item(tempItemName, tempItemData.getDetails(), tempItemData.getPhotoURL());
                itemsDict.put(tempItemName, tempItem);
            }


			//put the ItemInfo in the dictionary
			//I got some problems here because the raw data type Set has the "add" method working only
			//for derived data types
			tempItem = itemsDict.get(tempItemName);
			if(itemsInfoDict.get(tempItemName) == null) {
				itemsInfoDict.put(tempItemName, new HashSet());
			}
			itemsInfoDict.get(tempItemName).add(tempItemInfo);
			//setting ItemInfo array for each product
			tempItem.setItemInfoArr(itemsInfoDict.get(tempItemName));



			//TODO search if it were better to have a single map with two fields and if it's possible
			//put the PriceInfo in the dictionary
			if(priceInfoDict.get(tempItemInfo) == null) {
				priceInfoDict.put(tempItemInfo, new HashSet());
			}
			priceInfoDict.get(tempItemInfo).add(tempPriceInfo);
			//setting PriceInfo array for each ItemInfo
			tempItemInfo.setPriceInfoArr(priceInfoDict.get(tempItemInfo));

        } catch (Exception e) {
            if(e.getCause() != null) {
                if(e.getCause().getMessage() == Parser.DOCUMENT_NULL_EX) {
                    //try again
                    crawlerIdx--;
                } else if(e.getCause().getMessage() == Parser.ITEM_NOT_FOUND_EX) {
                    //try the next parser
                }
                System.out.println(e.getMessage());
            }
        }


        //todo create the ItemInfo based on item info//what does that mean??
		try {
			goToNextCrawlerOrItemAfterDelay(1000);
		} catch (ConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (MySQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		}
	}

	private void goToNextCrawlerOrItemAfterDelay(int delay) throws ConstraintViolationException, MySQLIntegrityConstraintViolationException{
		//i do the delay to trick the website into thinking I'm a user
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				//System.out.println(productIndex+ " "+ (JSONObject)itemsToCreate.get(productIndex));
				crawlerIdx++;
				if(crawlerIdx < parsers.size()) findItem((JSONObject)itemsToCreate.get(productIndex));
				else {
					System.out.println("[LOG] Go to next item");
					crawlerIdx = 0;
					productIndex++;
					if(productIndex < itemsToCreate.size()) findItem((JSONObject)itemsToCreate.get(productIndex));
					else {
						System.out.println("[LOG] Items finished building");
						//trigger a complete event
						state = COMPLETE;
						setChanged();
						notifyObservers();
					}
				}
			}
		}, delay);
	}

	public String getState() {return state;}
}
