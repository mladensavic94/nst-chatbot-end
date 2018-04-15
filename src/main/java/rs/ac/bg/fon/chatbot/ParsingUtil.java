package rs.ac.bg.fon.chatbot;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ParsingUtil {


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
    public static String getJsonObject(String json, String paramName) {
        JsonObject object = Json.parse(json).asObject();
        return object.get(paramName).asObject().toString();
    }

    public static String getJsonArray(String json, String paramName, int index) {
        JsonObject object = Json.parse(json).asObject();
        return object.get(paramName).asArray().get(index).asObject().toString();
    }

    public static String parseProfessor(String text) {
        String professor = null;
        if (text != null) {
            professor = ParsingUtil.getJsonObject(text, "entities");
            if (professor != null) {
                professor = ParsingUtil.getJsonArray(professor, "contact", 0);
                if (professor != null)
                    professor = ParsingUtil.getJsonField(professor, "value");
            }
        }

        return professor;

    }

    public static Date parseDate(String text) throws ParseException {
        String dateString = null;
        if (text != null) {
            dateString = ParsingUtil.getJsonObject(text, "entities");
            if (dateString != null) {
                dateString = ParsingUtil.getJsonArray(dateString, "datetime", 0);
                if (dateString != null){
                    dateString = ParsingUtil.getJsonField(dateString, "value");
                    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(dateString);
                }
            }
        }
        return null;

    }

    public static String parseIntent(String text) {
        String response = null;
        if (text != null) {
            response = ParsingUtil.getJsonObject(text, "entities");
            if (response != null) {
                response = ParsingUtil.getJsonArray(response, "intent", 0);
                if (response != null)
                    response = ParsingUtil.getJsonField(response, "value");
            }
        }
        return response;

    }



}
