package kanBan.models.business;

import java.util.Objects;
import kanBan.models.enums.StatusTask;
import kanBan.models.enums.TypeTask;
import java.time.Duration;
import java.time.LocalDateTime;
import static kanBan.services.manager.consts.Constants.DATE_TIME_FORMATTER;
import java.util.Optional;

public class Task implements Comparable<Task>{
    protected String title;
    protected String description;
    protected int id;
    protected StatusTask status;
    protected TypeTask type;
    protected LocalDateTime startTime;
    protected Duration duration;

    @Override
    public int compareTo(Task o) {
        if (this.startTime == null)
            return 1;
        if (o.startTime == null)
            return -1;
        return this.startTime.compareTo(o.startTime);
    }
    public StatusTask getStatus() {
        return status;
    }

    public TypeTask getType() {
        return type;
    }

    public void setType(TypeTask type) {
        this.type = type;
    }

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
        this.type = other.getType();
        if (other.getStartTime().isPresent()) {
            this.startTime = other.getStartTime().get();
        }
        if (other.getDuration().isPresent()) {
            this.duration = other.getDuration().get();
        }
    }

    public Task(int id, TypeTask type, String title, StatusTask status, String description, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, String startTime, int duration) {
        this.title = title;
        this.description = description;
        this.status = StatusTask.NEW;
        this.type = TypeTask.TASK;
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        this.duration = Duration.ofMinutes(duration);

    }

    public void setStartTime(LocalDateTime dateTime) {
        this.startTime = dateTime;
    }

    public void setDuration(Duration durationEpic) {
        this.duration = durationEpic;
    }

    public String printStartTime() {
        if (getStartTime().isPresent()) {
            return getStartTime().get().format(DATE_TIME_FORMATTER).toString();
        }else {
            return "Время начала не задано.";
        }
    }

    public String printEndTime() {
        if (getEndTime().isPresent()) {
            return getEndTime().get().format(DATE_TIME_FORMATTER).toString();
        }else {
            return "Время окончания не расчитано.";
        }
    }

    public String printDuration() {
        if (getDuration().isPresent()) {
            return "" + getDuration().get().toMinutes();
        }else {
            return "Продолжительность не задана.";
        }
    }

    public Optional<LocalDateTime> getEndTime() {
        if (startTime != null) {
            return Optional.of(startTime.plus(duration));
        }else {
            return Optional.empty();
        }
    }

    public Optional<LocalDateTime> getStartTime() {
        if (startTime != null) {
            return Optional.of(startTime);
        }else {
            return Optional.empty();
        }
    }

    public Optional<Duration> getDuration() {
        if (duration != null) {
            return Optional.of(duration);
        }else {
            return Optional.empty();
        }
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
                + "    Описание: " + description
                + "    Начало задачи: " + printStartTime()
                + "    Продолжительсть: " + printDuration() + "\n";
    }
}