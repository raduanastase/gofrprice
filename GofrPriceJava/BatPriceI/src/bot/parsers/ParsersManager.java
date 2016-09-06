package bot.parsers;

import bot.parsers.parser.Parser;

import java.util.ArrayList;

/**
 * Created by Radu on 01-Dec-14.
 */
public class ParsersManager {

    private ArrayList<Parser> _parsers = new ArrayList<Parser>();

    public ParsersManager() {
        _parsers.add(new EvomagParser());
        _parsers.add(new EmagParser());
        //(Parser) _parsers.get(1).findItem();
    }

    public ArrayList<Parser> crawlers() {
        return _parsers;
    }
}
