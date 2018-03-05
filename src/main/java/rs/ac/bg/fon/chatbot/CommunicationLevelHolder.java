package rs.ac.bg.fon.chatbot;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.chatbot.db.domain.Level;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Component("communicationLevelHolder")
public class CommunicationLevelHolder {

    private Map<String, EnumSet<Level>> communicationLevel = new HashMap<>();

    public synchronized Map<String, EnumSet<Level>> getCommunicationLevel() {
        return communicationLevel;
    }

    public synchronized void addNewCommunication(String id, EnumSet<Level> levels){
        communicationLevel.put(id, levels);
    }

    public synchronized EnumSet<Level> getCommunicationLevel(String id){
        return communicationLevel.get(id);
    }

    public Level getFirstMissing(EnumSet<Level> levels){
        for (Level level : EnumSet.allOf(Level.class)){
            if (!levels.contains(level)) return level;
        }
        return null;
    }
}
