package kanBan;

import kanBan.services.manager.*;
import kanBan.models.business.*;
import kanBan.models.enums.*;
import kanBan.services.manager.taskManagers.TaskManager;


public class Main {

    public static void main(String[] args) {
        TaskManager tasksManager = Managers.getDefault();

        int epicOne = tasksManager.createEpic(new Epic("firstTest", "descripFirstTest"));

        int epicTwo = tasksManager.createEpic(new Epic("SecondTest", "descripSecondTest"));

        tasksManager.createSubTask(new SubTask("tree", "treeDesc", tasksManager.getEpics().get(epicOne), "14:00 03.11.22", 180));

        tasksManager.createSubTask(new SubTask("four", "fourDesc", tasksManager.getEpics().get(epicOne),"14:00 04.11.22", 60));

        tasksManager.createSubTask(new SubTask("five", "fiveDesc", tasksManager.getEpics().get(epicOne), "09:00 04.11.22", 120));

        tasksManager.getBySubTaskId(5);
        tasksManager.getByEpicId(2);
        tasksManager.getBySubTaskId(3);
        tasksManager.getBySubTaskId(5);
        tasksManager.getByEpicId(1);
        tasksManager.getBySubTaskId(5);
        System.out.println(tasksManager.getHistory());

        tasksManager.getBySubTaskId(4);
        tasksManager.getBySubTaskId(5);
        tasksManager.getBySubTaskId(5);
        System.out.println(tasksManager.getHistory());
        tasksManager.getBySubTaskId(3);
        System.out.println(tasksManager.getHistory());
        tasksManager.deleteByIdentifier(3);
        System.out.println(tasksManager.getHistory());
        tasksManager.getBySubTaskId(5);
        System.out.println(tasksManager.getHistory());
        tasksManager.getBySubTaskId(4);
        tasksManager.deleteByIdentifier(2);
        System.out.println(tasksManager.getHistory());
        System.out.println(tasksManager.getAllEpicSubTasks(tasksManager.getByEpicId(1)));

    }
}