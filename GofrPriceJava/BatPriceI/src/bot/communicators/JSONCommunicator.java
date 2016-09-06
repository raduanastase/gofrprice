/**
 * 
 */
package bot.communicators;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * @author Radu
 *
 */
public class JSONCommunicator {

    private static final String toFindPath = "json/to_create.json";

    private JSONParser parser;
    private FileReader toFindReader;

    public JSONCommunicator() {
        parser = new JSONParser();
    }

    public JSONArray getItemsToCreate() {
        JSONArray items = null;
        try {
            toFindReader = new FileReader(toFindPath);
            JSONObject jsonObj = (JSONObject) parser.parse(toFindReader);
            items = (JSONArray) jsonObj.get("items");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
            return items;
    }

}
