import java.util.HashMap;

public class TasksManager {
    private int identifier;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, Epic> epics;

    public TasksManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void deletingTasks(HashMap<Integer, Task> tasks) {

        if (tasks == null) {

            System.out.println("Невозможно передать такой объект!");
            return;
        }

        tasks.clear();
    }

    public Task getByTaskId(int id) {

        Task task = null;

        if (tasks.containsKey(id)) {

            task = tasks.get(id);

        } else {

            System.out.println("Нет такого ID!");

        }

        return task;
    }

    public SubTask getBySubTaskId(int id) {

        SubTask task = null;

        if (subTasks.containsKey(id)) {

            task = subTasks.get(id);

        } else {

            System.out.println("Нет такого ID!");

        }

        return task;
    }

    public Epic getByEpicId(int id) {

        Epic task = null;

        if (epics.containsKey(id)) {

            task = epics.get(id);

        } else {

            System.out.println("Нет такого ID!");

        }

        return task;
    }

    public void creatingTask(Task task) {

        if (task == null) {
            System.out.println("Невозможно создать такой объект!");
            return;
        }

        identifier++;

        task.setTaskId(identifier);

        tasks.put(identifier, task);
    }

    public void creatingEpic(Epic epic) {

        if (epic == null) {
            System.out.println("Невозможно создать такой объект!");
            return;
        }

        identifier++;

        epic.setTaskId(identifier);

        epics.put(identifier, epic);
    }

    public void creatingSubTask(SubTask subTask) {

        if (subTask == null) {
            System.out.println("Невозможно создать такой объект!");
            return;
        }

        identifier++;

        subTask.setTaskId(identifier);

        subTasks.put(identifier, subTask);
    }

    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getTaskId(), subTask);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    public void deletingByIdentifier(int id) {

        if (tasks.containsKey(id)) {

            tasks.remove(id);

        } else if (subTasks.containsKey(id)) {

            subTasks.remove(id);

        } else if (epics.containsKey(id)) {

            Epic oneEpic = epics.get(id);

            for (Integer idSubTask : oneEpic.getSubTasks().keySet()) {
                subTasks.remove(idSubTask);
            }

            epics.remove(id);

        } else {

            System.out.println("Такого ID нету!");

        }
    }

    public HashMap<Integer, SubTask> getAllEpicSubTasks(Epic epic) {

        for (Epic epicTmp : epics.values()) {

            if (epicTmp == epic) {
                return epicTmp.getSubTasks();
            }
        }

        System.out.println("Нет такого Эпика!");

        return null;
    }

}