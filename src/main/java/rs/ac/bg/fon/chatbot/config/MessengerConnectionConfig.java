package rs.ac.bg.fon.chatbot.config;

import com.github.messenger4j.Messenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MessengerConnectionConfig {

    @Value("facebook.token")
    private String token;
    @Value("facebook.appSecret")
    private String appSecret;

    @Bean
    public Messenger messenger(){
        return  Messenger.create(token, appSecret, "4ba374d8b7c30dae4326613e0817f691");
    }
}
