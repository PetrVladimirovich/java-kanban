package kanBan.services.manager.taskManagers;

import java.util.Map;
import java.util.List;;
import kanBan.models.business.*;
import java.util.TreeSet;

public interface TaskManager {

    void printTasks();

    void printSubTasks();

    void printEpics();

    Map<Integer, Task> getTasks();

    Map<Integer, SubTask> getSubTasks();

    Map<Integer, Epic> getEpics();

    void deleteTasks();

    void deleteSubTasks();

    void deleteEpics();

    Task getByTaskId(int id);

    SubTask getBySubTaskId(int id);

    Epic getByEpicId(int id);

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubTask(SubTask subTask);

    void checkStatusEpic(int idEpic);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void deleteByIdentifier(int id);

    List<SubTask> getAllEpicSubTasks(Epic epic);

    List<Task> getHistory();

    void checkStartTimeEpic(int idEpic);

    void checkEndTimeEpic(int idEpic);

    void checkDurationEpic(int idEpic);

    TreeSet<Task> getPrioritizedTasks();

}