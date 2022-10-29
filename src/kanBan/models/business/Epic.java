package kanBan.models.business;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Optional;
import kanBan.models.enums.*;
import java.time.LocalDateTime;
import java.time.Duration;

public class Epic extends Task {
    private final List<Integer> subTasksIds;
    private LocalDateTime endTime;

    public Epic(String title, String description) {

        super(title, description);
        this.type = TypeTask.EPIC;
        subTasksIds = new ArrayList<>();
    }

    public Epic(int id, TypeTask type, String title, StatusTask status, String description) {
        super(id, type, title, status, description);
        subTasksIds = new ArrayList<>();
    }

    public void setEndTime(LocalDateTime endTimeEpic) {
        this.endTime = endTimeEpic;
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        if (endTime != null) {
            return Optional.of(endTime);
        }else {
            return Optional.empty();
        }
    }

    public List<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void deleteSubTasksIds(Integer id) {
        subTasksIds.remove(id);
    }
    public void addSubTask(SubTask subTask) {
        if (!subTasksIds.contains(subTask.getId())) {
            subTasksIds.add(subTask.getId());
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
                + "   ID: " + id
                + "   Status: " + status
                + "   Старт: " + printStartTime()
                + "   Продолжительность: " + printDuration()
                + "   Окончание: " + printEndTime()
                + "   У Эпика "
                + subTasksIds.size() + " Подзадачи!\n";
    }
}