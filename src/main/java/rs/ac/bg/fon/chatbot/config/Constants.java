package rs.ac.bg.fon.chatbot.config;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

    public final static String TOKEN = "EAAZATKY8ZBibMBAAOMlZApfBivVElc1SI0hlwbAANfA2P7Jm100LoZBZBytTm5SAHd3PaD1ur3bE92WCAIS8ocYIvLCzfUeZCwIYjxsZC7Gb7LYDIzZBi6oZBVeLekM94BN07lZASHy2vX2F4ZCG2AUcMKmS4x92kZCCmgrAjnpTaQgZBFBn9GDJgg9o9oMDUpNT4O10ZD";
    public final static String APP_SECRET= "4ba374d8b7c30dae4326613e0817f691";
    public static String WIT_AI_VERSION = "20180320";
    public final static String URL_WIT_AI = "https://api.wit.ai/message?v=" + WIT_AI_VERSION + "&q=";

    static {
        WIT_AI_VERSION = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
}
