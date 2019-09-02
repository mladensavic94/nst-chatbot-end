package rs.ac.bg.fon.chatbot.response;

import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.domain.Professor;
import rs.ac.bg.fon.chatbot.db.services.ProfessorService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static rs.ac.bg.fon.chatbot.ParsingUtil.*;

@Component
@Order(value = 2)
public class ProfessorBasedHandler implements AnswerGeneratorHandler {

    private ProfessorService professorService;

    @Autowired
    public ProfessorBasedHandler(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @Override
    public TextMessage generateAnswer(String nlpResponse, Appointment targetedAppointment) {
        return getResponseBasedOnProfessorParameter(nlpResponse, targetedAppointment);
    }

    private TextMessage getResponseBasedOnProfessorParameter(String appointmentString, Appointment appointment) {
        Professor professor;
        TextMessage response = null;
        try {
            String professorString = parseProfessor(appointmentString);
            professor = professorService.findProfessorUsingStringDistance(professorString);
            if (professor != null) {
                appointment.setProfessor(professor);
                String text = String.format(I18nService.get("professor.appointment.day"), professor.getLastName());
                List<QuickReply> quickReplies = new ArrayList<>();
                professor.getListOfOfficeHours().stream()
                        .filter(officeHours -> officeHours.getBeginTime().after(new Date()))
                        .forEach(officeHours ->
                                quickReplies.add(TextQuickReply.create(convertToUserFriendly(officeHours.getBeginTime()), formatDateForWit(officeHours.getBeginTime()))));
                if (quickReplies.isEmpty()) {
                    response = TextMessage.create(String.format(I18nService.get("date.not.available"), appointment.getProfessor().getEmail()));
                } else {
                    response = TextMessage.create(text, Optional.of(quickReplies), Optional.empty());
                }
            } else {
                String text = I18nService.get("professor.available.names");
                List<QuickReply> quickReplies = new ArrayList<>();
                professorService.findAll().forEach(professor1 -> quickReplies.add(TextQuickReply.create(professor1.getLastName() + " " + professor1.getFirstName(), professor1.getLastName() + " " + professor1.getFirstName())));
                response = TextMessage.create(text, Optional.of(quickReplies), Optional.empty());
            }
        } catch (Exception e) {
            String text = I18nService.get("professor.question");
            List<QuickReply> quickReplies = new ArrayList<>();
            professorService.findAll().forEach(professor1 -> quickReplies.add(TextQuickReply.create(professor1.getLastName() + " " + professor1.getFirstName(), professor1.getLastName() + " " + professor1.getFirstName())));
            response = TextMessage.create(text, Optional.of(quickReplies), Optional.empty());
            e.printStackTrace();
        }
        return response;
    }

}
