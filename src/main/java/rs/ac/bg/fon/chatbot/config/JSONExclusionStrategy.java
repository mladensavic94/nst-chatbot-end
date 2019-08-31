package rs.ac.bg.fon.chatbot.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import rs.ac.bg.fon.chatbot.db.domain.RecursiveJson;

public class JSONExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getAnnotation(RecursiveJson.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}