package kanBan.models.business;

import java.util.Objects;
import kanBan.models.enums.StatusTask;

public class Task {
    protected String title;
    protected String description;
    protected int taskId;
    protected StatusTask status;


    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = StatusTask.NEW;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public StatusTask getIsStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId
                && Objects.equals(title, task.title)
                && Objects.equals(description, task.description)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(title, description, taskId, status);

        return result;
    }

    @Override
    public String toString() {
        return "Задача " + title
                + "   ID: " + taskId
                + "    status: " + status
                + "\n    Описание: [" + description + " ]\n";
    }
}