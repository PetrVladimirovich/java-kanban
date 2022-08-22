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

    public void deletingTasks() {

        if (tasks == null) {

            System.out.println("Невозможно передать такой объект!");
            return;
        }

        tasks.clear();
    }

    public void deletingSubTasks() {

        if (subTasks == null) {

            System.out.println("Невозможно передать такой объект!");
            return;
        }

        subTasks.clear();
    }

    public void deletingEpics() {

        if (epics == null) {

            System.out.println("Невозможно передать такой объект!");
            return;
        }

        epics.clear();
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

        Epic currentEpic = epics.get(subTask.getIdEpic());

        currentEpic.setSubTasks(subTask);

        checkingStatusEpic(subTask.getIdEpic());
    }

    private void checkingStatusEpic(int idEpic) {

        if (!epics.containsKey(idEpic)) {
            System.out.println("Нет эпика с таким ID!");
            return;
        }

        Epic currentEpic = epics.get(idEpic);

        if (currentEpic.getSubTasks().isEmpty()) {
            currentEpic.status = StatusTask.NEW;
            return;
        }

        int countDone = 0;
        int countNew = 0;

        for (SubTask subTask : currentEpic.getSubTasks().values()) {

            if (subTask.status.equals(StatusTask.DONE)) {
                countDone++;

            }else if (subTask.status.equals(StatusTask.NEW)) {
                countNew++;

            }
        }

        if (currentEpic.getSubTasks().size() == countDone) {

            currentEpic.status = StatusTask.DONE;

        }else if (currentEpic.getSubTasks().size() == countNew) {

            currentEpic.status = StatusTask.NEW;

        }else {

            currentEpic.status = StatusTask.IN_PROGRESS;

        }
    }

    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void updateSubTask(SubTask subTask) {

        subTasks.put(subTask.getTaskId(), subTask);

        Epic currentEpic = epics.get(subTask.getIdEpic());

        currentEpic.setSubTasks(subTask);

        checkingStatusEpic(subTask.getIdEpic());


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

            Epic currentEpic = epics.get(id);

            for (Integer idSubTask : currentEpic.getSubTasks().keySet()) {
                subTasks.remove(idSubTask);
            }

            epics.remove(id);

        } else {

            System.out.println("Такого ID нету!");

        }
    }

    public HashMap<Integer, SubTask> getAllEpicSubTasks(Epic epic) {

        for (Epic epicTmp : epics.values()) {

            if (epicTmp.equals(epic)) {
                return epicTmp.getSubTasks();
            }
        }

        System.out.println("Нет такого Эпика!");

        return null;
    }

}