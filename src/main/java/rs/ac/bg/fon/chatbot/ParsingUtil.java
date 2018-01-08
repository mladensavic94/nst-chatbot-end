package rs.ac.bg.fon.chatbot;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import rs.ac.bg.fon.chatbot.db.domain.Message;
import rs.ac.bg.fon.chatbot.db.domain.MessageType;

import java.io.IOException;
import java.util.HashMap;

public class ParsingUtil {


   public static Message parseEventFromJson(String json) throws IOException {
       JsonObject object = Json.parse(json).asObject();
       if(json.contains("messaging")){
           JsonValue messagesArray =  object.get("entry").asArray().get(0).asObject().get("messaging");
           if(messagesArray != null && messagesArray.asArray().size() >= 1){
               JsonObject msg =  messagesArray.asArray().get(0).asObject();
               return new Message(msg.get("sender").asObject().get("id").asString(),msg.get("message").asObject().get("text").asString(), MessageType.TEXT);
           }
       }
       return null;
   }


}
