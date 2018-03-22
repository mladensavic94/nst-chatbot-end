package rs.ac.bg.fon.chatbot.config;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

    public final static String TOKEN = "EAAZATKY8ZBibMBALULxVjuFum4gkPbJEfNEOevHNLOlZBZC72KIYclVESzo4IBINgIAxrFbw7t6nfNVUWJxiYoiY3n2anXe4GAFG2ahLG06jrlarIjnXu2QOBZCrzyft9SW5NksaLwOsX98kEphRDMxbKpwBZC4HW0fuhqzZBPMr722u27vbtBC";
    public final static String APP_SECRET= "4ba374d8b7c30dae4326613e0817f691";
    public final static String URL_FACEBOOK = "https://graph.facebook.com/v2.6/me/messages?access_token=" + TOKEN;
    public static String WIT_AI_VERSION = "20180320";
    public final static String URL_WIT_AI = "https://api.wit.ai/message?v=" + WIT_AI_VERSION + "&q=";

    static {
        WIT_AI_VERSION = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
}
