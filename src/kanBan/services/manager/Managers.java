package kanBan.services.manager;

import kanBan.services.manager.historyManager.HistoryManager;
import kanBan.services.manager.historyManager.InMemoryHistoryManager;
import kanBan.services.manager.taskManagers.InMemoryTaskManager;
import kanBan.services.manager.taskManagers.TaskManager;

public class Managers {
    private Managers() { }
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}