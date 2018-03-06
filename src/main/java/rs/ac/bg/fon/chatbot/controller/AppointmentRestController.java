package rs.ac.bg.fon.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import rs.ac.bg.fon.chatbot.ParsingUtil;
import rs.ac.bg.fon.chatbot.db.domain.Appointment;
import rs.ac.bg.fon.chatbot.db.services.AppointmentsService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/rest")
public class AppointmentRestController {

    @Autowired
    AppointmentsService appointmentsService;

    @RequestMapping(value = "/appointments", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllAppointments(@RequestParam String email){
        try{
            Iterable<Appointment> serviceAllByEmail = appointmentsService.findAllByEmail(email);
            String response = ParsingUtil.parseListToJson(serviceAllByEmail);
            Map<String, String> userInfo = getUserInfo(serviceAllByEmail.iterator().next().getStudentID());
            response = ParsingUtil.setJsonField(response,userInfo);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private Map<String,String> getUserInfo(String userID) {
        final  String TOKEN = "EAAZATKY8ZBibMBAEsVy3ZA4F73jSboFAfukwl9Qa66VfFtXiAR7TTYRcSLjBjryTBZAu88j3ZAocIXyX2VdXe2EgVVUw0BdUXsiXdWEKodcr1Dh11LrUDNrTi2aTW3FKLCbVHtSRgRRR6uQJ3Jd4C47RvIibFS7wLu6xTFW6c2e9FQQ0qSsjE";
        final  String URL = "https://graph.facebook.com/v2.6/"+ userID+ "?fields=first_name,last_name&access_token=" + TOKEN;
        RestTemplate getUserInfo = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> json = getUserInfo.exchange(URL, HttpMethod.GET, requestEntity, String.class);
        Map<String, String> map = new HashMap<>();
        map.put("first_name", ParsingUtil.getJsonField(json.getBody(), "first_name"));
        map.put("last_name", ParsingUtil.getJsonField(json.getBody(), "last_name"));
        return map;

    }

    @RequestMapping(value = "/appointments/save", method = RequestMethod.POST)
    public ResponseEntity<Object> saveAppointment(@RequestBody String json){
        try {
            Appointment appointment = ParsingUtil.parseJsonToDomainObject(json, Appointment.class);
            appointmentsService.save(appointment);
            return ResponseEntity.status(HttpStatus.OK).body(appointment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
