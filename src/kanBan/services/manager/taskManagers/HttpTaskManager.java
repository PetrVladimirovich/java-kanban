package kanBan.services.manager.taskManagers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kanBan.models.business.Epic;
import kanBan.models.business.SubTask;
import kanBan.models.business.Task;
import kanBan.services.manager.Managers;
import kanBan.web.KVTaskClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private static Gson gson;
    private String apiToken;
    private KVTaskClient kvTaskClient;

    public HttpTaskManager(final String kvServerURL) {
        super();
        this.gson = Managers.getGson();
        this.kvTaskClient = new KVTaskClient(kvServerURL);
        this.apiToken = kvTaskClient.getApiToken();
    }

    @Override
    public void save() {
        kvTaskClient.put("tasks", gson.toJson(getTasks().values()));
        kvTaskClient.put("subtasks", gson.toJson(getSubTasks().values()));
        kvTaskClient.put("epics", gson.toJson(getEpics().values()));
        kvTaskClient.put("history", gson.toJson(getHistory()));
    }

    public static HttpTaskManager loadFromServer(String kvServerUrl, String apiToken) {
        HttpTaskManager httpTaskManager = new HttpTaskManager(kvServerUrl);
        KVTaskClient taskClient = new KVTaskClient(kvServerUrl, apiToken);

        List<Task> tasksFromServer = getTasksFromServer(taskClient);
        Map<Integer, Task> tasksForNewManager = tasksFromServer.stream()
                                                            .collect(Collectors.toMap(Task::getId, task -> task));

        List<Epic> epicsFromServer = getEpicsFromServer(taskClient);
        Map<Integer, Epic> epicsForNewManager = epicsFromServer.stream()
                                                             .collect(Collectors.toMap(Task::getId, task -> task));

        List<SubTask> subTasksForServer = getSubTasksFromServer(taskClient);
        Map<Integer, SubTask> subTasksForNewManager = subTasksForServer.stream()
                                                             .collect(Collectors.toMap(Task::getId, task -> task));

        List<Task> history = getHistoryFromServer(taskClient);
        httpTaskManager.setTasks(tasksForNewManager);
        httpTaskManager.setEpics(epicsForNewManager);
        httpTaskManager.setSubTasks(subTasksForNewManager);

        int id = getMaxId(tasksFromServer, epicsFromServer, subTasksForServer);
        httpTaskManager.setIdentifier(id);
        history.forEach(httpTaskManager.getHistoryManager()::add);
        return httpTaskManager;
    }

    private void setSubTasks(Map<Integer, SubTask> subTasksForNewManager) {
        for (SubTask task : subTasksForNewManager.values()) {
            getSubTasks().put(task.getId(), task);
        }
    }

    private void setEpics(Map<Integer, Epic> epicsForNewManager) {
        for (Epic task : epicsForNewManager.values()) {
            getEpics().put(task.getId(), task);
        }
    }

    private void setTasks(Map<Integer, Task> tasksForNewManager) {
        for (Task task : tasksForNewManager.values()) {
            getTasks().put(task.getId(), task);
        }
    }

    private static int getMaxId(List<Task> tasksFromServer,
                                List<Epic> epicsFromServer,
                                List<SubTask> subTasksForServer) {
        int maxId = 0;
        for (Task task : tasksFromServer) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        for (SubTask task : subTasksForServer) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        for (Epic task : epicsFromServer) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }

        return maxId;
    }

    private static List<Task> getHistoryFromServer(KVTaskClient taskClient) {
        Type type = new TypeToken<List<Task>>() {}.getType();
        List<Task> result = gson.fromJson(taskClient.load("history"), type);
        return (result == null) ? new ArrayList<>() : result;
    }

    private static List<SubTask> getSubTasksFromServer(KVTaskClient taskClient) {
        Type type = new TypeToken<List<SubTask>>() {}.getType();
        List<SubTask> result = gson.fromJson(taskClient.load("subtasks"), type);
        return (result == null) ? new ArrayList<>() : result;
    }

    private static List<Epic> getEpicsFromServer(KVTaskClient taskClient) {
        Type type = new TypeToken<List<Epic>>() {}.getType();
        List<Epic> result = gson.fromJson(taskClient.load("epics"), type);
        return (result == null) ? new ArrayList<>() : result;
    }

    private static List<Task> getTasksFromServer(KVTaskClient taskClient) {
        Type type = new TypeToken<List<Task>>() {}.getType();
        List<Task> result = gson.fromJson(taskClient.load("tasks"), type);
        return (result == null) ? new ArrayList<>() : result;

    }
}