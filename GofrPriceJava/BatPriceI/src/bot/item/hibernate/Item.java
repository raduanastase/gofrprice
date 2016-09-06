package bot.item.hibernate;

import java.util.Set;

/**
 * Created by Radu on 22-Dec-14.
 *
 */

public class Item {

	private int id;

	private String name;

	private String details;

	private String photoURL;

	private Set itemInfoArr;


    public Item() {}
    public Item(String name, String details, String photoURL) {
		this.name = name;
		this.details = details;
		this.photoURL = photoURL;
	}

	/*---GET/SET---*/
	public int getId() {return id;}
	public void setId( int id ) {this.id = id;}

	public String getName() { return name;}
	public void setName(String v) { name = v;}

	public String getDetails() { return details;}
	public void setDetails(String v) { details = v;}

	public String getPhotoURL() {return photoURL;}
	public void setPhotoURL(String v) {photoURL = v;}

	public Set getItemInfoArr() { return itemInfoArr; }
	public void setItemInfoArr(Set v) {itemInfoArr = v;}

	/*---PUBLIC---*/
   /* public void addItemInfo(ItemInfo v) {
		System.out.println("addItemInfo  START "+itemInfoArr +" "+ v);
		itemInfoArr.add(v);
		System.out.println("addItemInfo  END"+itemInfoArr);
	}*/

}
