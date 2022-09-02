package kanBan.services.manager;

import java.util.Map;
import java.util.List;
import kanBan.models.business.*;

public interface TaskManager {

    public void printTasks();

    public void printSubTasks();

    public void printEpics();

    public Map<Integer, Task> getTasks();

    public Map<Integer, SubTask> getSubTasks();

    public Map<Integer, Epic> getEpics();

    public void deletingTasks();

    public void deletingSubTasks();

    public void deletingEpics();

    public Task getByTaskId(int id);

    public SubTask getBySubTaskId(int id);

    public Epic getByEpicId(int id);

    public Task creatingTask(Task task);

    public Epic creatingEpic(Epic epic);

    public SubTask creatingSubTask(SubTask subTask);

    public void checkingStatusEpic(int idEpic);

    public void updateTask(Task task);

    public void updateSubTask(SubTask subTask);

    public void updateEpic(Epic epic);

    public void deletingByIdentifier(int id);

    public Map<Integer, SubTask> getAllEpicSubTasks(Epic epic);

    public List<Task> getHistory();

}
