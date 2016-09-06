/**
 * 
 */
package ro.raduldev.batprice.other;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;




/**
 * @author Radu
 *
 */
public class XmlCommunicator {

    private static final String toFindPath = "json/to_create.json";

    private JSONParser parser;
    private FileReader toFindReader;

    public XmlCommunicator() {
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
