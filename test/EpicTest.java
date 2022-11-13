import kanBan.models.business.*;
import kanBan.models.enums.*;
import kanBan.services.manager.Managers;
import kanBan.services.manager.taskManagers.InMemoryTaskManager;
import kanBan.services.manager.taskManagers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    TaskManager taskManager;
    int idEpic;

    @BeforeEach
    public void beforeEach() {
         taskManager = new InMemoryTaskManager();
         idEpic = taskManager.createEpic(new Epic("one", "oneDescription"));
         taskManager.createSubTask(new SubTask("oneSubtask", "oneSubtask Description", taskManager.getEpics().get(idEpic))) ;
         taskManager.createSubTask(new SubTask("twoSubtask", "twoSubtask Description", taskManager.getEpics().get(idEpic)));
         taskManager.createSubTask(new SubTask("treeSubtask", "treeSubtask Description", taskManager.getEpics().get(idEpic)));
    }

    @Test
    public void emptyListOfSubtasks() {
        taskManager.deleteSubTasks();
        assertEquals(0, taskManager.getByEpicId(1).getSubTasksIds().size());
    }

    @Test
    public void allSubtasksWithNEWStatus() {
        assertEquals(StatusTask.NEW, taskManager.getEpics().get(idEpic).getStatus());
    }

    @Test
    public void allSubtasksWithDONEStatus() {
        taskManager.getSubTasks().get(2).setStatus(StatusTask.DONE);
        taskManager.getSubTasks().get(3).setStatus(StatusTask.DONE);
        taskManager.getSubTasks().get(4).setStatus(StatusTask.DONE);
        taskManager.checkStatusEpic(idEpic);

        assertEquals(StatusTask.DONE, taskManager.getEpics().get(idEpic).getStatus());
    }

    @Test
    public void allSubtasksWithNEWandDONEStatus() {

        taskManager.getSubTasks().get(3).setStatus(StatusTask.DONE);
        taskManager.getSubTasks().get(4).setStatus(StatusTask.DONE);
        taskManager.checkStatusEpic(idEpic);

        assertEquals(StatusTask.IN_PROGRESS, taskManager.getEpics().get(idEpic).getStatus());
    }

    @Test
    public void allSubtasksWithINPROGRESSStatus() {
        taskManager.getSubTasks().get(2).setStatus(StatusTask.IN_PROGRESS);
        taskManager.getSubTasks().get(3).setStatus(StatusTask.IN_PROGRESS);
        taskManager.getSubTasks().get(4).setStatus(StatusTask.IN_PROGRESS);
        taskManager.checkStatusEpic(idEpic);

        assertEquals(StatusTask.IN_PROGRESS, taskManager.getEpics().get(idEpic).getStatus());
    }

}