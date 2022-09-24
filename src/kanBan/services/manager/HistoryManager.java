package kanBan.services.manager;

import java.util.List;
import kanBan.models.business.*;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
