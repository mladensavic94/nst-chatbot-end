package rs.ac.bg.fon.chatbot;

import com.owlike.genson.Genson;

import java.util.Map;

public class ParsingUtil {


    public static String getFieldByName(String json, String fieldName) throws Exception {
        Map<String, String> test = new Genson().deserialize(json, Map.class);
        if(test == null){
            throw new Exception();
        }
        return test.getOrDefault(fieldName, json);
    }
}
