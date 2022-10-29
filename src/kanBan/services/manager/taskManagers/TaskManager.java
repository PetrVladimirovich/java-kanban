package kanBan.services.manager.taskManagers;

import java.util.Map;
import java.util.List;
import kanBan.models.business.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface TaskManager {
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
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

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subTask);

    void checkStatusEpic(int idEpic);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void deleteByIdentifier(int id);

    Map<Integer, SubTask> getAllEpicSubTasks(Epic epic);

    List<Task> getHistory();

    void checkStartTimeEpic(int idEpic);

    void checkEndTimeEpic(int idEpic);

    void checkDurationEpic(int idEpic);

}