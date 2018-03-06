package rs.ac.bg.fon.chatbot;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import rs.ac.bg.fon.chatbot.db.domain.Message;
import rs.ac.bg.fon.chatbot.db.domain.MessageType;
import rs.ac.bg.fon.chatbot.db.domain.Professor;

import java.io.IOException;
import java.util.Map;

public class ParsingUtil {


    public static Message parseEventFromJson(String json) throws IOException {
        JsonObject object = Json.parse(json).asObject();
        if (json.contains("messaging")) {
            JsonValue messagesArray = object.get("entry").asArray().get(0).asObject().get("messaging");
            if (messagesArray != null && messagesArray.asArray().size() >= 1) {
                JsonObject msg = messagesArray.asArray().get(0).asObject();
                return new Message(msg.get("sender").asObject().get("id").asString(), msg.get("message").asObject().get("text").asString(), MessageType.TEXT);
            }
        }
        return null;
    }

    public static String parseListToJson(Iterable<?> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    public static String parseDomainObjectToJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T parseJsonToDomainObject(String json, Class<T> tClass) {
        Gson gson = new Gson();
        return gson.fromJson(json, tClass);
    }

    public static String getJsonField(String json, String paramName) {
        JsonObject object = Json.parse(json).asObject();
        return object.get(paramName).asString();
    }

    public static String setJsonField(String json, Map<String, String> userInfo) {
        com.eclipsesource.json.JsonArray array = Json.parse(json).asArray();
        JsonObject object = new JsonObject();
        object.add("appointments", array);
        for (String key : userInfo.keySet()) {
            object.add(key, userInfo.get(key));
        }
        return object.toString();
    }


}
