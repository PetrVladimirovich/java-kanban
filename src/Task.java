import java.util.Arrays;
import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected int taskId;
    protected boolean[] isStatus;


    public Task(String title, String description, boolean[] isStatus) {
        this.title = title;
        this.description = description;
        this.isStatus = isStatus;
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

    public boolean[] getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(boolean[] isStatus) {
        this.isStatus = isStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId
                && Objects.equals(title, task.title)
                && Objects.equals(description, task.description)
                && Arrays.equals(isStatus, task.isStatus);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(title, description, taskId);
        result = 31 * result + Arrays.hashCode(isStatus);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", isStatus=" + Arrays.toString(isStatus) +
                '}';
    }
}
