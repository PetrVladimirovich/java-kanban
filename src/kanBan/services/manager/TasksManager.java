package kanBan.services.manager;

import java.util.HashMap;
import kanBan.models.business.*;
import kanBan.models.enums.StatusTask;

public class TasksManager {
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";

    private int identifier;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, Epic> epics;

    public TasksManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public void printTasks() {

        for (Task task : tasks.values()) {
            System.out.println(task);
        }

        System.out.println();

    }

    public void printSubTasks() {

        for (SubTask subTask : subTasks.values()) {
            System.out.println(subTask);
        }

        System.out.println();

    }

    public void printEpics() {

        for (Epic epic : epics.values()) {

            System.out.println(epic);

            for (SubTask subTask : epic.getSubTasks().values()) {

                System.out.println("    " + subTask);

            }

            System.out.println();

        }
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

            System.out.println(ANSI_RED + "deletingTasks: --> Невозможно передать такой объект! <--\n" + ANSI_RESET);
            return;
        }

        tasks.clear();
    }

    public void deletingSubTasks() {

        if (subTasks == null) {

            System.out.println(ANSI_RED + "deletingSubTasks: --> Невозможно передать такой объект! <--\n" + ANSI_RESET);
            return;
        }

        subTasks.clear();
    }

    public void deletingEpics() {

        if (epics == null) {

            System.out.println(ANSI_RED + "deletingEpics: --> Невозможно передать такой объект! <--\n" + ANSI_RESET);
            return;
        }

        epics.clear();
    }

    public Task getByTaskId(int id) {

        Task task = null;

        if (tasks.containsKey(id)) {

            task = tasks.get(id);

        } else {

            System.out.println(ANSI_RED + "getByTaskId: --> Нет такого ID! <--\n" + ANSI_RESET);

        }

        return task;
    }

    public SubTask getBySubTaskId(int id) {

        SubTask task = null;

        if (subTasks.containsKey(id)) {

            task = subTasks.get(id);

        } else {

            System.out.println(ANSI_RED + "getBySubTaskId: --> Нет такого ID! <--\n" + ANSI_RESET);

        }

        return task;
    }

    public Epic getByEpicId(int id) {

        Epic task = null;

        if (epics.containsKey(id)) {

            task = epics.get(id);

        } else {

            System.out.println(ANSI_RED + "getByEpicId: --> Нет такого ID! <--\n" + ANSI_RESET);

        }

        return task;
    }

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

    private void checkingStatusEpic(int idEpic) {

        if (!epics.containsKey(idEpic)) {
            System.out.println(ANSI_RED + "checkingStatusEpic: --> Нет эпика с таким ID! <--\n" + ANSI_RESET);
            return;
        }

        Epic currentEpic = epics.get(idEpic);

        if (currentEpic.getSubTasks().isEmpty()) {
            currentEpic.setStatus(StatusTask.NEW);
            return;
        }

        int countDone = 0;
        int countNew = 0;

        for (SubTask subTask : currentEpic.getSubTasks().values()) {

            if (subTask.getIsStatus().equals(StatusTask.DONE)) {
                countDone++;

            }else if (subTask.getIsStatus().equals(StatusTask.NEW)) {
                countNew++;

            }
        }

        if (currentEpic.getSubTasks().size() == countDone) {

            currentEpic.setStatus(StatusTask.DONE);

        }else if (currentEpic.getSubTasks().size() == countNew) {

            currentEpic.setStatus(StatusTask.NEW);

        }else {

            currentEpic.setStatus(StatusTask.IN_PROGRESS);

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

            System.out.println(ANSI_RED + "deletingByIdentifier: --> Такого ID: " + id + " нету! <--\n" + ANSI_RESET);

        }
    }

    public HashMap<Integer, SubTask> getAllEpicSubTasks(Epic epic) {

        for (Epic epicTmp : epics.values()) {

            if (epicTmp.equals(epic)) {
                return epicTmp.getSubTasks();
            }
        }

        System.out.println(ANSI_RED + "getAllEpicSubTasks: --> Нет такого Эпика! <--\n" + ANSI_RESET);

        return null;
    }

}