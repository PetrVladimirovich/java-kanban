package kanBan.services.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kanBan.services.manager.historyManager.HistoryManager;
import kanBan.services.manager.historyManager.InMemoryHistoryManager;
import kanBan.services.manager.taskManagers.HttpTaskManager;
import kanBan.services.manager.taskManagers.TaskManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {
    private Managers() { }

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078/");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        return new GsonBuilder().create();
    }

}