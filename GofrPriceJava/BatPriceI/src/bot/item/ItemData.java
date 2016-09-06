package bot.item;

/**
 * This class works as a temporal buffer for Item and ItemInfo
 */

public class ItemData {

    private int crawlerID;
    private String url;
    private Float price;
    private String name;
    private String details;
    private String photoURL;

	public ItemData(int crawlerID, String url, Float price, String name, String details, String photoURL) {
        this.crawlerID = crawlerID;
        this.url = url;
        this.price = price;
        this.name = name;
        this.details = details;
        this.photoURL = photoURL;
	}

    public int getCrawlerID() {return crawlerID;}
    public String getUrl() {return url;}
    public Float getPrice() {return price;}
    public String getName() {return name;}
    public String getDetails() {return details;}
    public String getPhotoURL() {return photoURL;}

}
