package rs.ac.bg.fon.chatbot;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rs.ac.bg.fon.chatbot.db.domain.RecursiveJson;

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
                if (dateString != null) {
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

    public static String chageDateFormatUSAToEur(String inputDate) {
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
    public static String chageDateFormatEurToUSA(String inputDate) {
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

    public static String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MMM");
        return dateFormat.format(date);
    }

    public static String formatDateForWit(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM-dd-yyyy");
        return dateFormat.format(date);
    }

    private static class JSONExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldAttributes.getAnnotation(RecursiveJson.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }


}
