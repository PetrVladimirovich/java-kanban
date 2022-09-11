package kanBan.services.manager;

import java.util.List;
import java.util.ArrayList;
import kanBan.models.business.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        Task taskTemp = new Task(task);
        if (history.size() == 10) {
            history.remove(0);
            history.add(taskTemp);
        }else {
            history.add(taskTemp);
        }
    }

    @Override
    public List<Task> getHistory() {

        return history;

    }
}
