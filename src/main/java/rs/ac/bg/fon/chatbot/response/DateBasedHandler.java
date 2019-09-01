package rs.ac.bg.fon.chatbot.response;

import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.message.quickreply.QuickReply;
import com.github.messenger4j.send.message.quickreply.TextQuickReply;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.domain.OfficeHours;
import rs.ac.bg.fon.chatbot.db.services.AppointmentsService;
import rs.ac.bg.fon.chatbot.db.services.OfficeHoursService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static rs.ac.bg.fon.chatbot.ParsingUtil.*;

@Component
@Order(value = 3)
public class DateBasedHandler implements AnswerGeneratorHandler {

    private OfficeHoursService officeHoursService;
    private AppointmentsService appointmentsService;

    public DateBasedHandler(OfficeHoursService officeHoursService, AppointmentsService appointmentsService) {
        this.officeHoursService = officeHoursService;
        this.appointmentsService = appointmentsService;
    }

    @Override
    public TextMessage generateAnswer(String nlpResponse, Appointment targetedAppointment) {
        return getResponseBasedOnDateParameter(nlpResponse, targetedAppointment);
    }

    private TextMessage getResponseBasedOnDateParameter(String appointmentString, Appointment appointment) {
        TextMessage response = null;
        try {
            Date date = parseDate(appointmentString);
            OfficeHours officeHours = officeHoursService.filterByDate(date, appointment.getProfessor());
            System.out.println(officeHours);
            if (officeHours == null) {
                List<QuickReply> quickReplies = new ArrayList<>();
                officeHoursService.findAllByProfessorId(appointment.getProfessor().getEmail())
                        .forEach(officeHours1 -> {
                            if (officeHours1.getBeginTime().after(new Date()))
                                quickReplies.add(TextQuickReply.create(convertToUserFriendly(officeHours1.getBeginTime()), formatDateForWit(officeHours1.getBeginTime())));
                        });
                if (quickReplies.isEmpty()) {
                    response = TextMessage.create("Profesor trenutno nema zakazanih termina. Mozete se obratiti na: " + appointment.getProfessor().getEmail());
                } else {
                    response = TextMessage.create("U tom terminu nema konsultacija.", Optional.of(quickReplies), Optional.empty());
                }
            } else {
                appointment.setOfficeHours(officeHours);
                appointmentsService.updateDateTime(appointment);
            }
        } catch (Exception e) {
            System.out.println("Date not parsed " + e.getMessage());
            e.printStackTrace();

        }
        return response;
    }
}
