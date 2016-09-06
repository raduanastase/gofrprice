package ro.raduldev.batprice.other;

public class ItemInfo {

    String url;
    Float price;
    String name;
    String description;
    String photoURL;

	public ItemInfo(String url, Float price, String name, String description, String photoURL) {
        this.url = url;
        this.price = price;
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
	}

    public String getUrl() {return url;}
    public Float getPrice() {return price;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public String getPhotoURL() {return photoURL;}

}
