package rs.ac.bg.fon.chatbot.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.domain.OfficeHours;
import rs.ac.bg.fon.chatbot.db.domain.Professor;
import rs.ac.bg.fon.chatbot.db.services.repositories.OfficeHoursRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OfficeHoursService {

    private final
    OfficeHoursRepository officeHoursRepository;

    @Autowired
    public OfficeHoursService(OfficeHoursRepository officeHoursRepository) {
        this.officeHoursRepository = officeHoursRepository;
    }

    public void saveOfficeHours(OfficeHours officeHours) {
        officeHoursRepository.save(officeHours);
    }

    public Iterable<OfficeHours> findAll() {
        return officeHoursRepository.findAll();
    }

    public Iterable<OfficeHours> findAllByProfessorId(String email) {
        return officeHoursRepository.findAllByProfessorEmail(email);
    }

    public OfficeHours getOfficeHoursByDateForProfessor(Professor professor, String s) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        try {
            Date date = df.parse(s);
            System.out.println(date);
            for (OfficeHours officeHours : officeHoursRepository.findAllByProfessorEmail(professor.getEmail())) {
                if (officeHours.getBeginTime().before(date) && officeHours.getEndTime().after(date))
                    return officeHours;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("UKNOWN");
    }
}
