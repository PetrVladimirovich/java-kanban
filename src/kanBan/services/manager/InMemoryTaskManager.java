package kanBan.services.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import kanBan.models.business.*;
import kanBan.models.enums.StatusTask;

public class InMemoryTaskManager implements TaskManager {

    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";

    private int identifier;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, SubTask> subTasks;
    private final Map<Integer, Epic> epics;
    private final HistoryManager history = Managers.getDefaultHistory();


    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public void printTasks() {

        for (Task task : tasks.values()) {
            System.out.println(task);
        }

        System.out.println();

    }

    @Override
    public void printSubTasks() {

        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTask);
        }

        System.out.println();

    }

    @Override
    public void printEpics() {

        for (Epic epic : epics.values()) {

            System.out.println(epic);

            for (Integer subTaskId : epic.getSubTasksIds()) {

                System.out.println("    " + subTasks.get(subTaskId));

            }

            System.out.println();

        }
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public void deletingTasks() {

        if (tasks == null) {

            System.out.println(ANSI_RED + "deletingTasks: --> Невозможно передать такой объект! <--\n" + ANSI_RESET);
            return;
        }

        tasks.clear();
    }

    @Override
    public void deletingSubTasks() {

        if (subTasks == null) {

            System.out.println(ANSI_RED + "deletingSubTasks: --> Невозможно передать такой объект! <--\n" + ANSI_RESET);
            return;
        }

        subTasks.clear();
    }

    @Override
    public void deletingEpics() {

        if (epics == null) {

            System.out.println(ANSI_RED + "deletingEpics: --> Невозможно передать такой объект! <--\n" + ANSI_RESET);
            return;
        }

        epics.clear();
    }

    @Override
    public Task getByTaskId(int id) {

        Task task = null;

        if (tasks.containsKey(id)) {

            task = tasks.get(id);
            history.add(task);

        } else {

            System.out.println(ANSI_RED + "getByTaskId: --> Нет такого ID! <--\n" + ANSI_RESET);

        }

        return task;
    }

    @Override
    public SubTask getBySubTaskId(int id) {

        SubTask task = null;

        if (subTasks.containsKey(id)) {

            task = subTasks.get(id);
            history.add(task);

        } else {

            System.out.println(ANSI_RED + "getBySubTaskId: --> Нет такого ID! <--\n" + ANSI_RESET);

        }

        return task;
    }

    @Override
    public Epic getByEpicId(int id) {

        Epic task = null;

        if (epics.containsKey(id)) {

            task = epics.get(id);
            history.add(task);

        } else {

            System.out.println(ANSI_RED + "getByEpicId: --> Нет такого ID! <--\n" + ANSI_RESET);

        }

        return task;
    }

    @Override
    public Task creatingTask(Task task) {

        if (task == null) {
            System.out.println(ANSI_RED + "creatingTask: --> Невозможно создать такой объект! <--\n" + ANSI_RESET);
            return null;
        }

        identifier++;

        task.setTaskId(identifier);

        tasks.put(identifier, task);

        return task;
    }

    @Override
    public Epic creatingEpic(Epic epic) {

        if (epic == null) {
            System.out.println(ANSI_RED + "creatingEpic: --> Невозможно создать такой объект! <--\n" + ANSI_RESET);
            return null;
        }

        identifier++;

        epic.setTaskId(identifier);

        epics.put(identifier, epic);

        return epic;
    }

    @Override
    public SubTask creatingSubTask(SubTask subTask) {

        if (subTask == null) {
            System.out.println(ANSI_RED + "creatingSubTask: --> Невозможно создать такой объект! <--\n" + ANSI_RESET);
            return null;
        }

        identifier++;

        subTask.setTaskId(identifier);

        subTasks.put(identifier, subTask);

        Epic currentEpic = epics.get(subTask.getIdEpic());

        currentEpic.setSubTasks(subTask);

        return subTask;
    }

    @Override
    public void checkingStatusEpic(int idEpic) {

        if (!epics.containsKey(idEpic)) {
            System.out.println(ANSI_RED + "checkingStatusEpic: --> Нет эпика с таким ID! <--\n" + ANSI_RESET);
            return;
        }

        Epic currentEpic = epics.get(idEpic);

        if (currentEpic.getSubTasksIds().isEmpty()) {
            currentEpic.setStatus(StatusTask.NEW);
            return;
        }

        int countDone = 0;
        int countNew = 0;

        for (Integer subTaskId : currentEpic.getSubTasksIds()) {

            if (subTasks.get(subTaskId).getIsStatus().equals(StatusTask.DONE)) {
                countDone++;

            }else if (subTasks.get(subTaskId).getIsStatus().equals(StatusTask.NEW)) {
                countNew++;

            }
        }

        if (currentEpic.getSubTasksIds().size() == countDone) {

            currentEpic.setStatus(StatusTask.DONE);

        }else if (currentEpic.getSubTasksIds().size() == countNew) {

            currentEpic.setStatus(StatusTask.NEW);

        }else {

            currentEpic.setStatus(StatusTask.IN_PROGRESS);

        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {

        subTasks.put(subTask.getTaskId(), subTask);

        Epic currentEpic = epics.get(subTask.getIdEpic());

        currentEpic.setSubTasks(subTask);

        checkingStatusEpic(subTask.getIdEpic());


    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void deletingByIdentifier(int id) {

        if (tasks.containsKey(id)) {

            tasks.remove(id);

        } else if (subTasks.containsKey(id)) {

            subTasks.remove(id);

        } else if (epics.containsKey(id)) {

            Epic currentEpic = epics.get(id);

            for (Integer idSubTask : currentEpic.getSubTasksIds()) {
                subTasks.remove(idSubTask);
            }

            epics.remove(id);

        } else {

            System.out.println(ANSI_RED + "deletingByIdentifier: --> Такого ID: " + id + " нету! <--\n" + ANSI_RESET);

        }
    }

    @Override
    public Map<Integer, SubTask> getAllEpicSubTasks(Epic epic) {

        Map<Integer, SubTask> subTasksEpic = new HashMap<>();

        for (Epic epicTmp : epics.values()) {

            if (epicTmp.equals(epic)) {
                for (Integer subTaskId : epicTmp.getSubTasksIds()) {
                    subTasksEpic.put(subTaskId, subTasks.get(subTaskId));
                }
            }
        }

        System.out.println(ANSI_RED + "getAllEpicSubTasks: --> Нет такого Эпика! <--\n" + ANSI_RESET);

        return null;
    }

}