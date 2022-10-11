package kanBan.services.manager;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import kanBan.models.business.*;

public class FileBackedTasksManager extends InMemoryTaskManager{
    Path fileName;

    public FileBackedTasksManager(Path fileName) {
         super();
         this.fileName = fileName;
    }

    public void save() {

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
}
