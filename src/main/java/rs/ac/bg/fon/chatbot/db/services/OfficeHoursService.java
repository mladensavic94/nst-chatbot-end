package rs.ac.bg.fon.chatbot.db.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.chatbot.db.domain.OfficeHours;
import rs.ac.bg.fon.chatbot.db.services.repositories.OfficeHoursRepository;

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
}
