package rs.ac.bg.fon.chatbot.config;

import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

    @Value("${facebook.token}")
    public static String TOKEN;
    public final static String APP_SECRET= "4ba374d8b7c30dae4326613e0817f691";
    public static String WIT_AI_VERSION = "20180320";
    public final static String URL_WIT_AI = "https://api.wit.ai/message?v=" + WIT_AI_VERSION + "&q=";

    static {
        WIT_AI_VERSION = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
}
