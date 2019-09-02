package rs.ac.bg.fon.chatbot.config;

public class JWTConstants {

    public static final String SECRET = "L0ngRand0mStr1ngSecret";
    public static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
