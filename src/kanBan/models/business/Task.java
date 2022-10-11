package kanBan.models.business;

import java.util.Objects;
import kanBan.models.enums.StatusTask;
import kanBan.models.enums.TypeTask;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected StatusTask status;
    protected TypeTask type;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = StatusTask.NEW;
        this.type = TypeTask.TASK;
    }

    public Task(Task other) {
        this(other.getTitle(), other.getDescription());
        this.id = other.getId();
        this.status = other.getIsStatus();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return id == task.id
                && Objects.equals(title, task.title)
                && Objects.equals(description, task.description)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(title, description, id, status);

        return result;
    }

    @Override
    public String toString() {
        return "    Задача " + title
                + "   ID: " + id
                + "    status: " + status
                + "    Описание: [" + description + " ]\n";
    }
}