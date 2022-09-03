package kanBan.models.business;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subTasksIds;

    public Epic(String title, String description) {

        super(title, description);

        subTasksIds = new ArrayList<>();
    }

    public List<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setSubTasks(SubTask subTask) {

        if (!subTasksIds.contains(subTask.getTaskId())) {

            subTasksIds.add(subTask.getTaskId());

        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksIds, epic.subTasksIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksIds);
    }

    @Override
    public String toString() {

        return "   Epic " + title
                + "   ID: " + taskId
                + "   Status: " + status
                + "   У Эпика "
                + subTasksIds.size() + " Подзадачи!\n";
    }
}