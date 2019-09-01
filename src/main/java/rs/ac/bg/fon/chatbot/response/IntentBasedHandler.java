package rs.ac.bg.fon.chatbot.response;

import com.github.messenger4j.send.message.TextMessage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;

import static rs.ac.bg.fon.chatbot.ParsingUtil.parseIntent;

@Component
@Order(value = 1)
public class IntentBasedHandler implements AnswerGeneratorHandler {


    @Override
    public TextMessage generateAnswer(String nlpResponse, Appointment targetedAppointment) throws AnswerGeneratorHandlerException {
        String intent = parseIntent(nlpResponse);
        if ((intent != null && intent.equals("request")) || targetedAppointment != null) {
            return null;
        } else {
            throw new AnswerGeneratorHandlerException("Svrha nije prepoznata! Bot sluzi samo za konsultacije!");
        }
    }
}
