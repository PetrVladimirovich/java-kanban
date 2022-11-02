import kanBan.models.business.Epic;
import kanBan.models.business.SubTask;
import kanBan.models.business.Task;
import kanBan.services.manager.taskManagers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    InMemoryTaskManager taskManager;
    int epic;
    int subTask1;
    int subTask2;
    int subTask3;
    int subTask4;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
        epic = taskManager.createEpic(new Epic("one", "oneDescription"));
        subTask1 = taskManager.createSubTask(new SubTask("oneSubtask", "oneSubtask Description", taskManager.getEpics().get(epic))) ;
        subTask2 = taskManager.createSubTask(new SubTask("twoSubtask", "twoSubtask Description", taskManager.getEpics().get(epic)));
        subTask3 = taskManager.createSubTask(new SubTask("treeSubtask", "treeSubtask Description", taskManager.getEpics().get(epic)));
        subTask4 = taskManager.createSubTask(new SubTask("fourSubtask", "fourSubtask Description", taskManager.getEpics().get(epic)));
    }

    @Test
    public void emptyHistoryManager() {
        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    public void noDuplication() {
        taskManager.getBySubTaskId(2);
        taskManager.getBySubTaskId(4);
        taskManager.getBySubTaskId(3);
        taskManager.getBySubTaskId(2);
        taskManager.getBySubTaskId(1);
        taskManager.getBySubTaskId(4);
        taskManager.getBySubTaskId(3);
        List<Task> testList = new ArrayList<>();
        testList.add(taskManager.getBySubTaskId(subTask1));
        testList.add(taskManager.getByEpicId(epic));
        testList.add(taskManager.getBySubTaskId(subTask2));
        testList.add(taskManager.getBySubTaskId(subTask3));

        assertIterableEquals(testList, taskManager.getHistory());
    }

    @Test
    public void deletingFromHistory() {

        taskManager.getBySubTaskId(2);
        taskManager.getBySubTaskId(1);
        taskManager.getBySubTaskId(4);
        taskManager.getBySubTaskId(3);
        taskManager.getBySubTaskId(5);

        List<Task> testList = new ArrayList<>();
        testList.add(taskManager.getByEpicId(epic));
        testList.add(taskManager.getBySubTaskId(subTask3));
        testList.add(taskManager.getBySubTaskId(subTask2));
        testList.add(taskManager.getBySubTaskId(subTask4));


        taskManager.getHistoryManager().remove(2);
        assertIterableEquals(testList, taskManager.getHistory());

        taskManager.getHistoryManager().remove(5);
        testList.remove(3);
        assertIterableEquals(testList, taskManager.getHistory());

        taskManager.getHistoryManager().remove(4);
        testList.remove(1);
        assertIterableEquals(testList, taskManager.getHistory());
    }

}
