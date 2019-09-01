package rs.ac.bg.fon.chatbot.response;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.userprofile.UserProfile;
import com.github.messenger4j.webhook.Event;
import com.github.messenger4j.webhook.event.AttachmentMessageEvent;
import com.github.messenger4j.webhook.event.QuickReplyMessageEvent;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import com.github.messenger4j.webhook.event.attachment.Attachment;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.domain.Status;
import rs.ac.bg.fon.chatbot.db.services.AppointmentsService;

import java.util.List;

import static rs.ac.bg.fon.chatbot.ParsingUtil.parseIntent;
import static rs.ac.bg.fon.chatbot.db.domain.Status.*;

@Service
@PropertySource("classpath:application.properties")
public class ResponseService {


    private Messenger messenger;
    private AppointmentsService appointmentsService;
    private List<AnswerGeneratorHandler> answerHandlers;
    private NLPService nlpService;


    public ResponseService(Messenger messenger, AppointmentsService appointmentsService,
                           List<AnswerGeneratorHandler> answerHandlers,
                           NLPService nlpService) {
        this.appointmentsService = appointmentsService;
        this.answerHandlers = answerHandlers;
        this.nlpService = nlpService;
        this.messenger = messenger;
    }

    @Async
    public void run(Event event) {
        if (event.isTextMessageEvent()) {
            handleTextMessageEvents(event);
        } else if (event.isQuickReplyMessageEvent()) {
            handleQuickReplyMessageEvents(event);
        } else if (event.isAttachmentMessageEvent()) {
            handleAttachmentMessageEvents(event);
        }


    }

    public void sendResponse(MessagePayload message) {
        try {
            messenger.send(message);
        } catch (MessengerApiException | MessengerIOException e) {
            e.printStackTrace();
        }
    }

    private MessagePayload generateAnswer(String senderId, String text) throws MessengerIOException {
        if (isForConverastionRestart(senderId, text))
            return MessagePayload.create(senderId, TextMessage.create("Stanje obrisano, mozete poceti iz pocetka"));

        String appointmentString = nlpService.callAndParse(text);
        Appointment appointment = appointmentsService.findOrCreate(senderId);
        TextMessage response = null;
        try {
            for (AnswerGeneratorHandler answerHandler : answerHandlers) {
                TextMessage textMessage = answerHandler.generateAnswer(appointmentString, appointment);
                if (textMessage != null)
                    response = textMessage;
            }
        } catch (AnswerGeneratorHandlerException e) {
            response = TextMessage.create(e.getMessage());
        }
        Status appointmentStatus = appointment.getStatus();
        if (appointmentStatus.equals(DESCRIPTION_REQUESTED)) {
            appointment.setDescription(text);
        }
        if (appointmentStatus.equals(DESCRIPTION_MISSING)) {
            response = TextMessage.create("Unesite razlog dolaska na konsultacije ili prikacite neki dokument ili zip folder.");
            appointment.setStatus(DESCRIPTION_REQUESTED);
        }
        appointmentsService.save(appointment);
        if (appointmentStatus.equals(FULL)) {
            response = TextMessage.create("Zahtev za konsultacije poslat profesoru na odobrenje\nProfesor: " + appointment.getProfessor().getLastName() + " " + appointment.getProfessor().getFirstName() + "\nDatum: " + appointment.getDateAndTime());
        }
        return MessagePayload.create(senderId, response);
    }

    private boolean isForConverastionRestart(String senderId, String text) {
        if (text.equalsIgnoreCase("restart")) {
            appointmentsService.deleteByStudentID(senderId);
            return true;
        }
        return false;
    }

    private String getResponseBasedOnIntent(String appointmentString) {
        String response = null;
        try {
            response = parseIntent(appointmentString);
        } catch (Exception e) {
            System.out.println("Intent not parsed");
        }
        return response;
    }

    private void getUserNameAndLastName(String senderId, Appointment appointment) throws MessengerIOException {
        try {
            UserProfile userProfile = messenger.queryUserProfile(senderId);
            appointment.setName(userProfile.firstName() + " " + userProfile.lastName());
        } catch (MessengerApiException e) {
            e.printStackTrace();
        }
    }


    private void handleAttachmentMessageEvents(Event event) {
        AttachmentMessageEvent attachmentMessageEvent = event.asAttachmentMessageEvent();
        try {
            String text = "";
            for (Attachment attachment : attachmentMessageEvent.attachments()) {
                text = attachment.asRichMediaAttachment().url().toString();
            }
            MessagePayload response = generateAnswer(attachmentMessageEvent.senderId(), text);
            sendResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(MessagePayload.create(attachmentMessageEvent.senderId(), TextMessage.create("Ups! " + e.getMessage())));
        }
    }

    private void handleQuickReplyMessageEvents(Event event) {
        QuickReplyMessageEvent quickReplyMessageEvent = event.asQuickReplyMessageEvent();
        try {
            MessagePayload response = generateAnswer(quickReplyMessageEvent.senderId(), quickReplyMessageEvent.payload());
            sendResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(MessagePayload.create(quickReplyMessageEvent.senderId(), TextMessage.create("Ups! " + e.getMessage())));
        }
    }

    private void handleTextMessageEvents(Event event) {
        TextMessageEvent messageEvent = event.asTextMessageEvent();
        try {
            String text = checkForCyrillicAndParse(messageEvent);
            MessagePayload response = generateAnswer(messageEvent.senderId(),text);
            sendResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(MessagePayload.create(messageEvent.senderId(), TextMessage.create("Ups! " + e.getMessage())));
        }
    }

    private String checkForCyrillicAndParse(TextMessageEvent messageEvent) {
        String text = messageEvent.text();
        for(int i = 0; i < text.length(); i++) {
            if(Character.UnicodeBlock.of(text.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return "opravi da prebacuje u latinicu";
            }
        }
        return text;

    }
}
