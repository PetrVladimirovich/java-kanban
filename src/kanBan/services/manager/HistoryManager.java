package kanBan.services.manager;

import java.util.List;
import kanBan.models.business.*;

public interface HistoryManager {

    public void add(Task task);

    public List<Task> getHistory();

}
