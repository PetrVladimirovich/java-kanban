package kanBan;

import kanBan.services.manager.*;
import kanBan.models.business.*;
import kanBan.services.manager.taskManagers.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager tasksManager = Managers.getDefault();

        Epic epicOne = tasksManager.createEpic(new Epic("firstTest", "descripFirstTest"));
        Epic epicTwo = tasksManager.createEpic(new Epic("SecondTest", "descripSecondTest"));

        tasksManager.createSubTask(new SubTask("tree", "treeDesc", epicOne));
        tasksManager.createSubTask(new SubTask("four", "fourDesc", epicOne));
        tasksManager.createSubTask(new SubTask("five", "fiveDesc", epicOne));


        tasksManager.getBySubTaskId(5);
        tasksManager.getByEpicId(2);
        tasksManager.getBySubTaskId(3);
        tasksManager.getBySubTaskId(5);
        tasksManager.getByEpicId(1);
        tasksManager.getBySubTaskId(5);
        System.out.println(tasksManager.getHistory());
        tasksManager.updateTask(new Task("eleven", "ElevenDesc"));
        tasksManager.updateSubTask(new SubTask("ten", "tenDesc", epicOne));
        epicOne.setDescription("OneFirst");
        tasksManager.updateEpic(epicOne);
        System.out.println(tasksManager.getHistory());
        tasksManager.getBySubTaskId(4);
        tasksManager.getBySubTaskId(5);
        tasksManager.updateEpic(new Epic("cool", "coolDesc"));
        tasksManager.getBySubTaskId(5);
        System.out.println(tasksManager.getHistory());
        tasksManager.getBySubTaskId(3);
        System.out.println(tasksManager.getHistory());
        tasksManager.deleteByIdentifier(3);
        System.out.println(tasksManager.getHistory());
        tasksManager.getSubTasks().get(5).setTitle("kykky");
        tasksManager.getBySubTaskId(5);
        System.out.println(tasksManager.getHistory());
        tasksManager.getBySubTaskId(4);
        tasksManager.deleteByIdentifier(1);
        System.out.println(tasksManager.getHistory());
    }
}