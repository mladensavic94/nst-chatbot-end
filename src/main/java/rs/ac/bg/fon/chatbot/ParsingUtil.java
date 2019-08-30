package rs.ac.bg.fon.chatbot;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rs.ac.bg.fon.chatbot.config.JSONExclusionStrategy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParsingUtil {

    public static String parseListToJson(Iterable<?> list) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new JSONExclusionStrategy()).create();
        return gson.toJson(list);
    }

    public static String parseDomainObjectToJson(Object object) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new JSONExclusionStrategy()).create();
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
            professor = getJsonObject(text, "entities");
            if (professor != null) {
                professor = getJsonArray(professor, "contact", 0);
                if (professor != null)
                    professor = getJsonField(professor, "value");
            }
        }

        return professor;

    }

    public static Date parseDate(String text) throws ParseException {
        String dateString = null;
        if (text != null) {
            dateString = getJsonObject(text, "entities");
            if (dateString != null) {
                dateString = getJsonArray(dateString, "datetime", 0);
                if (dateString != null) {
                    dateString = getJsonField(dateString, "value");
                    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(dateString);
                }
            }
        }
        return null;

    }

    public static String parseIntent(String text) {
        String response = null;
        if (text != null) {
            response = getJsonObject(text, "entities");
            if (response != null) {
                response = getJsonArray(response, "intent", 0);
                if (response != null)
                    response = getJsonField(response, "value");
            }
        }
        return response;

    }

    public static String changeDateFormatUSAToEur(String inputDate) {
        try {
            DateFormat string2Date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date input = string2Date.parse(inputDate);
            DateFormat date2String = new SimpleDateFormat("HH:mm dd-MM-yyyy");
            return date2String.format(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputDate;
    }
    public static String changeDateFormatEurToUSA(String inputDate) {
        try {
            DateFormat string2Date = new SimpleDateFormat("HH:mm dd-MM-yyyy");
            Date input = string2Date.parse(inputDate);
            DateFormat date2String = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            return date2String.format(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputDate;
    }

    public static String convertToUserFriendly(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MMM");
        return dateFormat.format(date);
    }

    public static String formatDateForWit(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM-dd-yyyy");
        return dateFormat.format(date);
    }

}
