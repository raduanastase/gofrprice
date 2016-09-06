package ro.raduldev.batprice;

import java.io.IOException;

import ro.raduldev.batprice.other.CrawlersManager;
import ro.raduldev.batprice.other.CreatorModule;
import ro.raduldev.batprice.other.ItemInfo;
import ro.raduldev.batprice.other.XmlCommunicator;


public class App {

	private XmlCommunicator xmlComm;
	private CreatorModule creator;
    private CrawlersManager crawlersManager;
	private ItemInfo[] items;
	
	
	public App() throws IOException {
		
		xmlComm = new XmlCommunicator();

        crawlersManager = new CrawlersManager();

		creator = new CreatorModule(xmlComm.getItemsToCreate(), crawlersManager.crawlers());

	}
	
	public static void main(String[] args) throws IOException {
		App mainP = new App();
	}

    //todo make it work
    public static void trace(String value) {
        System.out.println("[LOG]"+value);
    }
}
