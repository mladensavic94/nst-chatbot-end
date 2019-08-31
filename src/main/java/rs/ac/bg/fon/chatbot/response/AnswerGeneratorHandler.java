package rs.ac.bg.fon.chatbot.response;

import com.github.messenger4j.send.message.TextMessage;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;

public interface AnswerGeneratorHandler {

    TextMessage generateAnswer(String witResponse, Appointment targetedAppointment) throws AnswerGeneratorHandlerException;
}
