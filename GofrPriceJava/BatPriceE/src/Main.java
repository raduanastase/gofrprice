import java.io.IOException;

import other.CreatorModule;
import other.Item;
import other.XmlCommunicator;
import sites.Emag;


public class Main {

	private XmlCommunicator xmlComm;
	
	private Emag _emag;
	
	private String[] productsName = {"Telefon LG Nexus 5", "telefon nexus 4"};//this list will be retrieved from a database
	private CreatorModule discoverer;
	private Item[] items;
	
	
	public Main() throws IOException {
		
		xmlComm = new XmlCommunicator();
		
		discoverer = new CreatorModule(productsName);
		
//		_emag = new Emag();
		
//		findProducts();
	}
	
//	private void findProducts() throws IOException {
//		_emag.findProduct(productsName[productIndex]);
//		
//		
//		//after a delay, go to the next item
//		productIndex++;
//	}
	
	public static void main(String[] args) throws IOException {
		Main mainP = new Main();
	}
	
}
