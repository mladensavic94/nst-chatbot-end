package rs.ac.bg.fon.chatbot.response;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class I18nService {

    private static Map<String, String> store;

    public static String get(String key) {
        if (store == null)
            initStore();
        return store.get(key);
    }

    private static void initStore() {
        try {
            InputStream stream = new ClassPathResource("messages.properties").getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            store = reader.lines().collect(toMap((String s) -> {
                String[] split = s.split("=");
                return split[0].trim();
            }, (String s1) -> {
                String[] split = s1.split("=");
                return split[1].trim();
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
