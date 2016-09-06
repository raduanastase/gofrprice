package bot.item.hibernate;

import java.util.Set;

public class ItemInfo {

	private int id;
	private Item item;
	private int crawlerID;
	private String url;
	private Set priceInfoArr;

	public ItemInfo() {
	}

	public ItemInfo(int crawlerID, String url) {
		this.crawlerID = crawlerID;
		this.url = url;
	}

	/*---GET/SET---*/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item v) {
		item = v;
	}

	public int getCrawlerID() {
		return crawlerID;
	}

	public void setCrawlerID(int v) {
		crawlerID = v;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String v) {
		url = v;
	}

	public Set getPriceInfoArr() {
		return priceInfoArr;
	}

	public void setPriceInfoArr(Set v) {
		priceInfoArr = v;
	}

	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!this.getClass().equals(obj.getClass())) return false;

		ItemInfo obj2 = (ItemInfo) obj;
		if ((this.id == obj2.getId()) && (this.url.equals(obj2.getUrl()))) {//Wouldn't the id always be different?
			return true;
		}
		return false;
	}

	public int hashCode() {
		int tmp = 0;
		tmp = (id + url).hashCode();
		return tmp;
	}

}
