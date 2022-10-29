package kanBan.services.manager.taskManagers;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Optional;
import java.time.Duration;
import java.util.List;
import kanBan.models.business.*;
import kanBan.models.enums.StatusTask;
import kanBan.services.manager.historyManager.HistoryManager;
import kanBan.services.manager.Managers;

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
    public void checkStartTimeEpic(int idEpic) {
        Optional<LocalDateTime> minDate = subTasks.values().stream()
                                                .filter(i -> idEpic == i.getIdEpic())
                                                .filter(i -> i.getStartTime().isPresent())
                                                .map(e -> e.getStartTime().get())
                                                .min(LocalDateTime::compareTo);

        if (minDate.isPresent() && epics.containsKey(idEpic)) {
            Epic epic = epics.get(idEpic);
            epic.setStartTime(minDate.get());
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void checkEndTimeEpic(int idEpic) {
        LocalDateTime maxTime = LocalDateTime.of(1970, 1, 1, 0, 0);
        Duration duration = Duration.ofMinutes(0);

        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStartTime().isPresent() && (subTask.getIdEpic() == idEpic)) {
                if (maxTime.isBefore(subTask.getStartTime().get())) {
                    maxTime = subTask.getStartTime().get();
                    duration = subTask.getDuration().get();
                }
            }
        }

        if (epics.containsKey(idEpic)) {
            Epic epic = epics.get(idEpic);
            epic.setEndTime(maxTime.plus(duration));
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void checkDurationEpic(int idEpic) {
        Duration duration = Duration.ofMinutes(0);

        for (SubTask subTask : subTasks.values()) {
            if (subTask.getDuration().isEmpty() && (subTask.getIdEpic() == idEpic)) {
                continue;
            }else {
                duration = duration.plus(subTask.getDuration().get());
            }
        }

        if (epics.containsKey(idEpic)) {
            Epic epic = epics.get(idEpic);
            epic.setDuration(duration);
            epics.put(epic.getId(), epic);
        }
    }

    public HistoryManager getHistoryManager() {
        return this.history;
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
    public void deleteTasks() {
        if (tasks == null) {
            System.out.println(ANSI_RED + "deleteTasks: --> Невозможно передать такой объект! <--\n" + ANSI_RESET);
            return;
        }
        tasks.clear();
    }

    @Override
    public void deleteSubTasks() {
        if (subTasks == null) {
            System.out.println(ANSI_RED + "deleteSubTasks: --> Невозможно передать такой объект! <--\n" + ANSI_RESET);
            return;
        }
        subTasks.clear();
    }

    @Override
    public void deleteEpics() {
        if (epics == null) {
            System.out.println(ANSI_RED + "deleteEpics: --> Невозможно передать такой объект! <--\n" + ANSI_RESET);
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
    public Task createTask(Task task) {
        if (task == null) {

            System.out.println(ANSI_RED + "createTask: --> Невозможно создать такой объект! <--\n" + ANSI_RESET);
            return null;
        }
        identifier++;

        task.setId(identifier);

        tasks.put(identifier, task);

        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epic == null) {
            System.out.println(ANSI_RED + "createEpic: --> Невозможно создать такой объект! <--\n" + ANSI_RESET);
            return null;
        }

        identifier++;

        epic.setId(identifier);

        epics.put(identifier, epic);

        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        if (subTask == null) {
            System.out.println(ANSI_RED + "createSubTask: --> Невозможно создать такой объект! <--\n" + ANSI_RESET);
            return null;
        }

        identifier++;

        subTask.setId(identifier);

        subTasks.put(identifier, subTask);

        Epic currentEpic = epics.get(subTask.getIdEpic());
        checkDurationEpic(currentEpic.getId());
        checkEndTimeEpic(currentEpic.getId());
        checkStartTimeEpic(currentEpic.getId());

        currentEpic.addSubTask(subTask);

        return subTask;
    }

    @Override
    public void checkStatusEpic(int idEpic) {
        if (!epics.containsKey(idEpic)) {
            System.out.println(ANSI_RED + "checkStatusEpic: --> Нет эпика с таким ID! <--\n" + ANSI_RESET);
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
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            history.add(task);
        } else {
            System.out.println(ANSI_RED + "updateTask: --> Не могу обновить! Нету такой ТАСКИ! <--\n" + ANSI_RESET);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);

            Epic currentEpic = epics.get(subTask.getIdEpic());

            checkStatusEpic(subTask.getIdEpic());
            checkDurationEpic(currentEpic.getId());
            checkEndTimeEpic(currentEpic.getId());
            checkStartTimeEpic(currentEpic.getId());
            currentEpic.addSubTask(subTask);
            history.add(subTask);
        } else {
            System.out.println(ANSI_RED + "updateSubTask: --> Не могу обновить! Нету такой САБТАСКИ! <--\n" + ANSI_RESET);
        }

    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            checkStatusEpic(epic.getId());
            history.add(epic);
        }else {
            System.out.println(ANSI_RED + "updateEpic: --> Не могу обновить! Нет такого ЭПИКА! <--\n" + ANSI_RESET);
        }

    }

    @Override
    public void deleteByIdentifier(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            history.remove(id);

        } else if (subTasks.containsKey(id)) {
            int epicId = subTasks.get(id).getIdEpic();
            epics.get(epicId).deleteSubTasksIds(id);
            subTasks.remove(id);
            checkDurationEpic(epicId);
            checkEndTimeEpic(epicId);
            checkStartTimeEpic(epicId);
            history.remove(id);

        } else if (epics.containsKey(id)) {
            Epic currentEpic = epics.get(id);

            for (Integer idSubTask : currentEpic.getSubTasksIds()) {
                subTasks.remove(idSubTask);
                history.remove(idSubTask);
            }
            epics.remove(id);
            history.remove(id);

        } else {
            System.out.println(ANSI_RED + "deleteByIdentifier: --> Такого ID: " + id + " нету! <--\n" + ANSI_RESET);
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