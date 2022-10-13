package kanBan.services.manager;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedWriter;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.LinkedList;

import kanBan.exceptions.ManagerSaveException;
import kanBan.models.business.*;
import kanBan.models.enums.*;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private final Path file;
    private static final String HEADINGS_CSV = "id,type,name,status,description,epic\n";
    private Set<Task> taskSequence;

    public FileBackedTasksManager(final Path file) {
        super();
        this.file = file;
    }

    public static void main(String[] args) {
        Path pat = Paths.get("test.csv");
        FileBackedTasksManager man = new FileBackedTasksManager(pat);
        Epic epicOne = man.creatingEpic(new Epic("firstTest", "descripFirstTest"));
        Epic epicTwo = man.creatingEpic(new Epic("SecondTest", "descripSecondTest"));
        Task taskONE = man.creatingTask(new Task("taskwe", "qwe!231321312qwe"));

        man.creatingSubTask(new SubTask("four", "fourDesc", epicOne));
        man.creatingSubTask(new SubTask("five", "fiveDesc", epicOne));
        man.creatingSubTask(new SubTask("six", "sixDesc", epicOne));
        man.getSubTasks().get(6).setStatus(StatusTask.DONE);
        man.updateSubTask(man.getSubTasks().get(6));
        man.getBySubTaskId(4);
        man.getSubTasks().get(5).setStatus(StatusTask.IN_PROGRESS);
        man.updateSubTask(man.getSubTasks().get(5));
        man.getByEpicId(2);
        man.getByTaskId(3);
        man.getBySubTaskId(4);
        man.getBySubTaskId(4);
        man.getBySubTaskId(5);
        System.out.println("\n$$$$$$$$$Здесь закончилась запись )))$$$$$$\n");
        FileBackedTasksManager qwe = FileBackedTasksManager.loadFromFile(pat);
        qwe.printEpics();
        qwe.printSubTasks();
        qwe.printTasks();
        System.out.println("===========================================\n\n");
        System.out.println(qwe.getHistory());
    }
    private String toString(Task task) {
        if (task instanceof SubTask) {
            SubTask sub = (SubTask) task;
            return String.format("%s,%s,%s,%s,%s,%s", sub.getId(), sub.getType(),
                    sub.getTitle(), sub.getStatus(), sub.getDescription(), sub.getIdEpic());
        }else {
            return String.format("%s,%s,%s,%s,%s", task.getId(), task.getType(),
                    task.getTitle(), task.getStatus(), task.getDescription());
        }
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

        if (TypeTask.TASK.toString().equals(str[1])) {
            return new Task(id, type, title, status, description);

        }else if (TypeTask.EPIC.toString().equals(str[1])) {
            return new Epic(id, type, title, status, description);

        }else if (TypeTask.SUBTASK.toString().equals(str[1])) {
            int idEpic = Integer.parseInt(str[5]);
            return new SubTask(id, type, title, status, description, idEpic);
        }
        return null;
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> historyIds = new LinkedList<>();
        String[] str = value.split(",");

        for (String string : str) {
            historyIds.add(Integer.parseInt(string));
        }
        return historyIds;
    }

    private static void addTask(FileBackedTasksManager fb, Task task) {
        fb.getTasks().put(task.getId(), task);
    }

    private static void addSubTask(FileBackedTasksManager fb, SubTask task) {
        fb.getSubTasks().put(task.getId(), task);
        Epic currentEpic = fb.getEpics().get(task.getIdEpic());
        currentEpic.addSubTask(task);
    }

    private static void addEpic(FileBackedTasksManager fb, Epic task) {
        fb.getEpics().put(task.getId(), task);
    }

    public static FileBackedTasksManager loadFromFile(final Path file) {
        try {
            String backInMemory = Files.readString(file);
            FileBackedTasksManager restoredTasksManager = new FileBackedTasksManager(file);
            String[] str = backInMemory.split("\n");

            for (int i = 1; i < str.length; i++) {
                if (str[i].equals("")) {
                    continue;
                }

                if (i == (str.length - 1)) {
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
                    addEpic(restoredTasksManager, (Epic)task);
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
        taskSequence = new LinkedHashSet<>();
        taskSequence.addAll(getTasks().values());
        taskSequence.addAll(getEpics().values());
        taskSequence.addAll(getSubTasks().values());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toString(), StandardCharsets.UTF_8))) {
            writer.write(HEADINGS_CSV);
            int count = 1;
            for (Task task : taskSequence) {
                if (count != taskSequence.size()) {
                    writer.write(toString(task) + ",\n");
                }else {
                    writer.write(toString(task) + "\n\n");
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
    public Task creatingTask(Task task) {
        super.creatingTask(task);
        save();
        return task;
    }

    @Override
    public SubTask creatingSubTask(SubTask subTask) {
        super.creatingSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public Epic creatingEpic(Epic epic) {
        super.creatingEpic(epic);
        save();
        return epic;
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
    public void deletingByIdentifier(int id) {
        super.deletingByIdentifier(id);
        save();
    }

    @Override
    public void deletingTasks() {
        super.deletingTasks();
        save();
    }

    @Override
    public void deletingSubTasks() {
        super.deletingSubTasks();
        save();
    }

    @Override
    public void deletingEpics() {
        super.deletingEpics();
        save();
    }

}