package kanBan.services.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kanBan.services.manager.consts.Constants;
import kanBan.services.manager.historyManager.HistoryManager;
import kanBan.services.manager.historyManager.InMemoryHistoryManager;
import kanBan.services.manager.taskManagers.HttpTaskManager;
import kanBan.services.manager.taskManagers.TaskManager;
import kanBan.web.KVServer;

import java.io.IOException;

public class Managers {
    private Managers() { }

    public static TaskManager getDefault() {
        return new HttpTaskManager(Constants.getAddressKVServices());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson buildGson() {
        return new GsonBuilder().create();
    }

    public static KVServer getDefaultKVServer() throws IOException {
        return new KVServer();
    }

}