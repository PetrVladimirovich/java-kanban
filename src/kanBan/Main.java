package kanBan;

import kanBan.services.manager.*;
import kanBan.models.business.*;
import kanBan.models.enums.StatusTask;
import kanBan.services.manager.taskManagers.TaskManager;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        TaskManager tasksManager = Managers.getDefault();
        LocalDateTime ldt = LocalDateTime.of(2022, 10, 16, 10, 30);
        Duration dur = Duration.ofMinutes(60);

        Epic epicOne = tasksManager.createEpic(new Epic("firstTest", "descripFirstTest"));
        Epic epicTwo = tasksManager.createEpic(new Epic("SecondTest", "descripSecondTest"));

        SubTask sub3 = tasksManager.createSubTask(new SubTask("tree", "treeDesc", epicOne, "14:00 14.10.22", 360));

        SubTask sub4 = tasksManager.createSubTask(new SubTask("four", "fourDesc", epicOne, "10:30 14.10.22", 30));
        SubTask sub5 = tasksManager.createSubTask(new SubTask("five", "fiveDesc", epicOne));
        SubTask sub6 = tasksManager.createSubTask(new SubTask("six", "sixDesc", epicOne, "18:00 15.10.22", 120));
        tasksManager.printEpics();
        sub5.setStartTime(ldt);
        sub5.setDuration(dur);
        tasksManager.deleteByIdentifier(4);


        tasksManager.updateSubTask(sub5);
        tasksManager.printEpics();
    }
}