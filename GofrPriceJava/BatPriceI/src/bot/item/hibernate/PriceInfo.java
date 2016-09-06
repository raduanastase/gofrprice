package bot.item.hibernate;

import java.util.Date;

public class PriceInfo {

	private int id;

	private ItemInfo itemInfo;

	private Float price;

	private Date date;

	public PriceInfo() {}
	public PriceInfo(Float price, Date date) {
		this.price = price;
		this.date = date;
	}

	/*---GET/SET---*/
	public int getId() {
		return id;
	}
	public void setId( int id ) {
		this.id = id;
	}

	public ItemInfo getItemInfo() {return itemInfo;}
	public void setItemInfo(ItemInfo v) { itemInfo = v; }

	public Float getPrice() {return price;}
	public void setPrice(Float v) {price = v;}

	public Date getDate() {return date;}
	public void setDate(Date v) {date = v;}

	/*public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!this.getClass().equals(obj.getClass())) return false;

		PriceInfo obj2 = (PriceInfo) obj;
		if ((this.id == obj2.getId()) && (this.date.equals(obj2.getDate()))) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int tmp = 0;
		tmp = (id + price).hashCode();
		return tmp;
	}*/

}
