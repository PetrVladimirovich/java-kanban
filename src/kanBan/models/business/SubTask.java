package kanBan.models.business;

import kanBan.models.enums.StatusTask;
import kanBan.models.enums.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private final int idEpic;

    public SubTask(String title, String description, Epic epic) {
        super(title, description);
        this.idEpic = epic.getId();
        this.type = TypeTask.SUBTASK;
    }

    public SubTask(String title, String description, Epic epic, String startTime, int duration) {
        super(title, description, startTime, duration);
        this.idEpic = epic.getId();
        this.type = TypeTask.SUBTASK;
    }

    public SubTask(int id, TypeTask type, String title, StatusTask status, String description, int idEpic, LocalDateTime startTime, Duration duration) {
        super(id, type, title, status, description, startTime, duration);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "    Подзадача: " + title
                + "    ID: " + id
                + "    Status: " + status
                + "    Начало задачи: " + printStartTime()
                + "    Продолжительностью: " + printDuration()
                + "    Принадлежит Эпику c ID: " + idEpic
                + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return idEpic == subTask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }
}
