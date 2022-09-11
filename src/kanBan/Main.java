package kanBan;

import kanBan.services.manager.*;
import kanBan.models.business.*;
import kanBan.models.enums.StatusTask;

public class Main {

    public static void main(String[] args) {

        TaskManager tasksManager = Managers.getDefault();

        Epic epicOne = tasksManager.creatingEpic(new Epic("firstTest", "descripFirstTest"));
        Epic epicTwo = tasksManager.creatingEpic(new Epic("SecondTest", "descripSecondTest"));

        tasksManager.creatingSubTask(new SubTask("tree", "treeDesc", epicOne));
        tasksManager.creatingSubTask(new SubTask("four", "fourDesc", epicOne));
        tasksManager.creatingSubTask(new SubTask("five", "fiveDesc", epicTwo));

        tasksManager.creatingTask(new Task("six", "sixDesc"));
        tasksManager.creatingTask(new Task("seven", "sevenDesc"));
        tasksManager.creatingTask(new Task("eight", "eightDesc"));
        tasksManager.creatingTask(new Task("nine", "nineDesc"));

        tasksManager.printSubTasks();
        tasksManager.printEpics();

        tasksManager.getSubTasks().get(3).setStatus(StatusTask.DONE);
        tasksManager.updateSubTask(tasksManager.getSubTasks().get(3));

        tasksManager.getSubTasks().get(5).setStatus(StatusTask.IN_PROGRESS);
        tasksManager.updateSubTask(tasksManager.getSubTasks().get(5));

        tasksManager.printEpics();

        tasksManager.getSubTasks().get(4).setStatus(StatusTask.DONE);
        tasksManager.updateSubTask(tasksManager.getSubTasks().get(4));
    /*
    tasksManager.printEpics();

    tasksManager.deletingByIdentifier(2);

    tasksManager.printSubTasks();

    tasksManager.deletingByIdentifier(5);

    tasksManager.printSubTasks();
    tasksManager.printEpics();
    */
        tasksManager.getBySubTaskId(5);
        tasksManager.getBySubTaskId(5);
        tasksManager.getBySubTaskId(5);
        tasksManager.getBySubTaskId(5);
        tasksManager.getSubTasks().get(5).setTitle("kykky");
        tasksManager.getBySubTaskId(5);
        tasksManager.getBySubTaskId(4);
        tasksManager.getByTaskId(7);
        tasksManager.getByTaskId(6);
        tasksManager.getBySubTaskId(3);
        tasksManager.getBySubTaskId(3);
        tasksManager.getByEpicId(2);
        tasksManager.getByEpicId(1);
        tasksManager.getBySubTaskId(3);

        System.out.println("================= История запросов!!! ===============\n");
        int count = 1;
        for (Task task : tasksManager.getHistory()) {
            System.out.printf("%d : %s\n\n", count, task);
            ++count;
        }
    }
}