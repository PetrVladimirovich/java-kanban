package kanBan.services.manager.taskManagers;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.LinkedList;

import kanBan.exceptions.ManagerSaveException;
import kanBan.models.business.*;
import kanBan.models.enums.*;
import kanBan.services.manager.historyManager.HistoryManager;
import static kanBan.services.manager.consts.Constants.DATE_TIME_FORMATTER;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path file;
    private static final String HEADINGS_CSV = "id,type,name,status,description,startTime,duration,epic\n";

    public FileBackedTasksManager(final String pathFile) {
        super();
        this.file = Paths.get(pathFile);
    }
    public FileBackedTasksManager() {
        super();
        this.file = Paths.get("");
    }

    public static void main(String[] args) {
        FileBackedTasksManager man = new FileBackedTasksManager("test.csv");
        int epic = man.createEpic(new Epic("one", "oneDescription"));


        man.createSubTask(new SubTask("two", "twoDesc", man.getEpics().get(epic)));
        man.createSubTask(new SubTask("tree", "treeDesc", man.getEpics().get(epic)));
        man.createSubTask(new SubTask("four", "fourDesc", man.getEpics().get(epic)));
        man.getSubTasks().get(4).setStatus(StatusTask.DONE);
        man.updateSubTask(man.getSubTasks().get(4));
        man.getBySubTaskId(2);
        man.getSubTasks().get(3).setStatus(StatusTask.IN_PROGRESS);
        man.updateSubTask(man.getSubTasks().get(3));


        man.getBySubTaskId(4);
        man.getBySubTaskId(4);
        man.getBySubTaskId(2);
        System.out.println("\n$$$$$$$$$Здесь закончилась запись )))$$$$$$\n");
        FileBackedTasksManager qwe = FileBackedTasksManager.loadFromFile("test.csv");
        qwe.printEpics();
        qwe.printSubTasks();
        qwe.printTasks();
        System.out.println("===========================================\n\n");
        System.out.println(qwe.getHistory());

    }

    private static String historyToString(HistoryManager manager) {
        List<String> idsTasksHistory = new LinkedList<>();

        for (Task task : manager.getHistory()) {
            idsTasksHistory.add(Integer.toString(task.getId()));
        }
        String result = String.join(",", idsTasksHistory);

        return result;
    }

    private static Task fromString(String value) {
        String[] str = value.split(",");

        int id = Integer.parseInt(str[0]);
        TypeTask type = TypeTask.valueOf(str[1]);
        String title = str[2];
        StatusTask status = StatusTask.valueOf(str[3]);
        String description = str[4];
        LocalDateTime startTime;
        Duration duration;
        if (str[5].equals("Не задано")) {
            startTime = null;
        }else {
            startTime = LocalDateTime.parse(str[5], DATE_TIME_FORMATTER);
        }

        if (str[6].equals("Не задано")) {
            duration = null;
        }else {
            duration = Duration.ofMinutes(Integer.parseInt(str[6]));
        }

        if (TypeTask.TASK.toString().equals(str[1])) {
                return new Task(id, type, title, status, description, startTime, duration);

        }else if (TypeTask.EPIC.toString().equals(str[1])) {

                return new Epic(id, type, title, status, description, startTime, duration);

        }else if (TypeTask.SUBTASK.toString().equals(str[1])) {
            int idEpic = Integer.parseInt(str[7]);
                return new SubTask(id, type, title, status, description, idEpic, startTime, duration);
        }
        return null;
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> historyIds = new LinkedList<>();
        if (!value.isEmpty()) {
            String[] str = value.split(",");

            for (String string : str) {
                historyIds.add(Integer.parseInt(string));
            }
        }
        return historyIds;
    }

    private static void addTask(FileBackedTasksManager fb, Task task) {
        fb.getTasks().put(task.getId(), task);
    }

    private static void addSubTask(FileBackedTasksManager fb, SubTask task) {
        fb.getSubTasks().put(task.getId(), task);
        Epic currentEpic = fb.getEpics().get(task.getIdEpic());
        fb.checkDurationEpic(currentEpic.getId());
        fb.checkEndTimeEpic(currentEpic.getId());
        fb.checkStartTimeEpic(currentEpic.getId());
        currentEpic.addSubTask(task);
    }

    private static void addEpic(FileBackedTasksManager fb, Epic task) {
        fb.getEpics().put(task.getId(), task);
    }

    public static FileBackedTasksManager loadFromFile(final String pathFile) {
        try {
            Path file = Paths.get(pathFile);
            String backInMemory = Files.readString(file);
            FileBackedTasksManager restoredTasksManager = new FileBackedTasksManager(pathFile);
            String[] str = backInMemory.split("\n");

            for (int i = 1; i < str.length; i++) {
                if (str[i].equals("")) {
                    continue;
                }

                if ((str.length > 2) && (i == (str.length - 1))) {
                    List<Integer> historyIds = historyFromString(str[i]);

                    for (Integer id : historyIds) {
                        if (restoredTasksManager.getTasks().containsKey(id)) {
                            restoredTasksManager.getHistoryManager().add(restoredTasksManager.getByTaskId(id));

                        } else if (restoredTasksManager.getSubTasks().containsKey(id)) {
                            restoredTasksManager.getHistoryManager().add(restoredTasksManager.getBySubTaskId(id));

                        } else if (restoredTasksManager.getEpics().containsKey(id)) {
                            restoredTasksManager.getHistoryManager().add(restoredTasksManager.getByEpicId(id));
                        }
                    }

                    break;
                }

                Task task = fromString(str[i]);
                if (task == null) {
                    throw new ManagerSaveException("Труба!!!!");
                }
                if (task instanceof Epic) {
                    addEpic(restoredTasksManager, (Epic) task);
                }else if (task instanceof SubTask) {
                    addSubTask(restoredTasksManager, (SubTask) task);
                }else {
                    addTask(restoredTasksManager, task);
                }
            }

            return restoredTasksManager;
        }catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }

    }

    public void save() {
        Set<Task> taskSequence = new LinkedHashSet<>();
        taskSequence.addAll(getTasks().values());
        taskSequence.addAll(getEpics().values());
        taskSequence.addAll(getSubTasks().values());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toString(), StandardCharsets.UTF_8))) {
            writer.write(HEADINGS_CSV);
            int count = 1;
            for (Task task : taskSequence) {
                if (count != taskSequence.size()) {
                    writer.write(UtilTask.representForFile(task) + ",\n");
                }else {
                    writer.write(UtilTask.representForFile(task) + "\n\n");
                }
                count++;
            }
            writer.write(historyToString(getHistoryManager()));

        }catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public Task getByTaskId(int id) {
        Task task = super.getByTaskId(id);
        save();
        return task;
    }

    @Override
    public SubTask getBySubTaskId(int id) {
        SubTask task = super.getBySubTaskId(id);
        save();
        return task;
    }

    @Override
    public Epic getByEpicId(int id) {
        Epic task = super.getByEpicId(id);
        save();
        return task;
    }

    @Override
    public int createTask(Task task) {
        int taskId = super.createTask(task);
        save();
        return taskId;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        int taskId = super.createSubTask(subTask);
        save();
        return taskId;
    }

    @Override
    public int createEpic(Epic epic) {
        int taskId = super.createEpic(epic);
        save();
        return taskId;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteByIdentifier(int id) {
        super.deleteByIdentifier(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

}