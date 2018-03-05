package rs.ac.bg.fon.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "rs.ac.bg.fon.chatbot")
public class SpringChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringChatbotApplication.class, args);
	}
}
