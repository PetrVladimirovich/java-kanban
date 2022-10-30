package kanBan;

import kanBan.services.manager.*;
import kanBan.models.business.*;
import kanBan.models.enums.*;
import kanBan.services.manager.taskManagers.TaskManager;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        TaskManager tasksManager = Managers.getDefault();
        LocalDateTime ldt = LocalDateTime.of(2022, 11, 14, 16, 00);
        Duration dur = Duration.ofMinutes(360);

        Epic epicOne = tasksManager.createEpic(new Epic("firstTest", "descripFirstTest"));
        Epic epicTwo = tasksManager.createEpic(new Epic("SecondTest", "descripSecondTest"));

        tasksManager.createSubTask(new SubTask("tree", "treeDesc", epicOne, "14:00 14.11.22", 360));

        tasksManager.createSubTask(new SubTask("four", "fourDesc", epicOne, "10:30 01.11.22", 30));
        tasksManager.createSubTask(new SubTask("five", "fiveDesc", epicOne));
        tasksManager.updateSubTask(new SubTask(3, TypeTask.SUBTASK, "tree", StatusTask.IN_PROGRESS, "treeDesc", epicOne.getId(), ldt, dur));
        System.out.println(tasksManager.getBySubTaskId(3));
        System.out.println(tasksManager.getBySubTaskId(3).printStartTime());
        tasksManager.deleteByIdentifier(3);
        tasksManager.createSubTask(new SubTask("six", "sixDesc", epicOne, "18:00 30.10.22", 120));
        tasksManager.createTask(new Task("seven", "sevenDesc", "18:00 10.11.22", 180));
        tasksManager.createTask(new Task("eight", "eightDesc", "06:00 20.11.22", 300));
        tasksManager.createTask(new Task("OOOOP", "eightDesc", "17:00 14.11.22", 10));
        tasksManager.createTask(new Task("nine", "nineDesc"));

        System.out.println(tasksManager.getPrioritizedTasks());




    }
}