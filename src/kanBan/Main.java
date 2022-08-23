/*
    Добрый день! Вроде поправил)))

    Вопрос:  а такие большие классы как TasksManager можно делать? 
              и есть ли допустимые нормы по строкам для классов?

    Конструктор поменял в SubTask
*/
package kanBan;

import kanBan.services.manager.TasksManager;
import kanBan.models.business.*;
import kanBan.models.enums.StatusTask;

public class Main {

    public static void main(String[] args) {

        TasksManager tasksManager = new TasksManager();

        Epic epicOne = tasksManager.creatingEpic(new Epic("firstTest", "descripFirstTest"));
        Epic epicTwo = tasksManager.creatingEpic(new Epic("SecondTest", "descripSecondTest"));

        tasksManager.creatingSubTask(new SubTask("tree", "treeDesc", epicOne));
        tasksManager.creatingSubTask(new SubTask("four", "fourDesc", epicOne));
        tasksManager.creatingSubTask(new SubTask("five", "fiveDesc", epicTwo));

        tasksManager.printSubTasks();
        tasksManager.printEpics();

        tasksManager.getSubTasks().get(3).setStatus(StatusTask.DONE);
        tasksManager.updateSubTask(tasksManager.getSubTasks().get(3));

        tasksManager.getSubTasks().get(5).setStatus(StatusTask.IN_PROGRESS);
        tasksManager.updateSubTask(tasksManager.getSubTasks().get(5));

        tasksManager.printEpics();

        tasksManager.getSubTasks().get(4).setStatus(StatusTask.DONE);
        tasksManager.updateSubTask(tasksManager.getSubTasks().get(4));

        tasksManager.printEpics();

        tasksManager.deletingByIdentifier(2);

        tasksManager.printSubTasks();

        tasksManager.deletingByIdentifier(5);

        tasksManager.printSubTasks();
        tasksManager.printEpics();

    }
}