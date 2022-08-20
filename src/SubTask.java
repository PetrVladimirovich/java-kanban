import java.util.Arrays;
import java.util.Objects;

public class SubTask extends Task{
    private final int idEpic;

    public SubTask(String title, String description, boolean[] isStatus, int idEpic) {

        super(title, description, isStatus);

        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "idEpic=" + idEpic +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", isStatus=" + Arrays.toString(isStatus) +
                '}';
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
