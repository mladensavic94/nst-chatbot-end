package rs.ac.bg.fon.chatbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import rs.ac.bg.fon.chatbot.db.domain.Professor;
import rs.ac.bg.fon.chatbot.db.services.ProfessorService;

@SpringBootApplication
@ComponentScan(basePackages = "rs.ac.bg.fon.chatbot")
public class SpringChatbotApplication {

	@Autowired
	ProfessorService professorService;

	public static void main(String[] args) {
		SpringApplication.run(SpringChatbotApplication.class, args);
		Professor professor = new Professor(null, "mladen@gmail.com", "msavic", "mladen", "savic");
		(new SpringChatbotApplication()).professorService.saveProfessor(professor);

	}
}
