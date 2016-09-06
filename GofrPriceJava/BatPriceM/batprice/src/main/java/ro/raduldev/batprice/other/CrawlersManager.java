package ro.raduldev.batprice.other;

import ro.raduldev.batprice.crawlers.EmagCrawler;
import ro.raduldev.batprice.crawlers.EvomagCrawler;
import ro.raduldev.batprice.crawlers.crawler.Crawler;

import java.util.ArrayList;

/**
 * Created by Radu on 01-Dec-14.
 */
public class CrawlersManager {

    private ArrayList<Crawler> _crawlers = new ArrayList<Crawler>();

    public CrawlersManager() {
        _crawlers.add(new EmagCrawler());
        _crawlers.add(new EvomagCrawler());
        //(Crawler) _crawlers.get(1).findItem();
    }

    public ArrayList<Crawler> crawlers() {
        return _crawlers;
    }
}
