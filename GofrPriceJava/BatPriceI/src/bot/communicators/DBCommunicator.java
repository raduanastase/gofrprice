package bot.communicators;

import org.hibernate.Session;
import bot.item.hibernate.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radu on 22-Dec-14.
 *
 */
public class DBCommunicator {

    private Map<String, Item> itemsDict = new HashMap<String, Item>();// I should discard this object because i don't need it, or do I?.... man this is some crappy programming


    public DBCommunicator() {
        itemsDict = new HashMap<String, Item>();
    }

	public void writeDataToDB() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		for(Map.Entry<String, Item> entry : itemsDict.entrySet()) {
			System.out.println(entry.getKey() + "/" + entry.getValue().getItemInfoArr());

			if(verifyExistence(session, entry.getValue())) {
				System.out.println("Item already exists");
			} else {
				session.saveOrUpdate(entry.getValue());
			}

		}

		session.getTransaction().commit();
		HibernateUtil.shutdown();
	}

    public Map<String, Item> getItemsDict() { return itemsDict; }
    public void setItemsDict(Map<String, Item> v) {
        itemsDict = v;
    }

	public Boolean verifyExistence(Session session, Item item) {//this doesn't work
		if((Item)session.get(Item.class, item.getId()) != null) return true;
		return false;
	}

}
