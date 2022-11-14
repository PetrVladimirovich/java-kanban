package kanBan.models.business;

import static kanBan.services.manager.consts.Constants.DATE_TIME_FORMATTER;

public class UtilTask {

    private UtilTask() { }

    public static String representForFile(Task task) {
        String startTime = (task.getStartTime().isPresent()) ? task.getStartTime().get().format(DATE_TIME_FORMATTER) : "Не задано";
        String duration = (task.getDuration().isPresent()) ? String.valueOf(task.getDuration().get().toMinutes()) : "Не задано";

        if (task instanceof SubTask) {
            SubTask sub = (SubTask) task;
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s", sub.getId(), sub.getType(),
                    sub.getTitle(), sub.getStatus(), sub.getDescription(), startTime, duration, sub.getIdEpic());
        }else {
            return String.format("%s,%s,%s,%s,%s,%s,%s", task.getId(), task.getType(),
                    task.getTitle(), task.getStatus(), task.getDescription(), startTime, duration);
        }
    }
}
