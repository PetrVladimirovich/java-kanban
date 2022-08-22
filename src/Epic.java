import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task{
    private final HashMap<Integer, SubTask> subTasks;

    public Epic(String title, String description) {

        super(title, description);

        subTasks = new HashMap<>();
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(SubTask subTask) {
        subTasks.put(subTask.getTaskId(), subTask);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }

    @Override
    public String toString() {

        return "Epic{" +
                "subTasks=" + subTasks +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", isStatus=" + status +
                '}';
    }
}
