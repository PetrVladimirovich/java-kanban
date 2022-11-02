import kanBan.models.business.*;
import kanBan.models.enums.StatusTask;
import kanBan.services.manager.Managers;
import kanBan.services.manager.taskManagers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private int epic;
    private int subTask1;
    private int subTask2;
    private int subTask3;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        epic = taskManager.createEpic(new Epic("one", "oneDescription"));
        subTask1 = taskManager.createSubTask(new SubTask("oneSubtask", "oneSubtask Description", taskManager.getEpics().get(epic))) ;
        subTask2 = taskManager.createSubTask(new SubTask("twoSubtask", "twoSubtask Description", taskManager.getEpics().get(epic)));
        subTask3 = taskManager.createSubTask(new SubTask("treeSubtask", "treeSubtask Description", taskManager.getEpics().get(epic)));
    }

    @Test
    public void epicsWithNormalBehavior() {

        final Map<Integer, Epic> epics = taskManager.getEpics();
        final Epic savedEpic = taskManager.getByEpicId(1);

        assertNotNull(savedEpic);
        assertNotNull(epics);
        assertEquals(1, epics.size());
        assertEquals(savedEpic, epics.get(1));

        taskManager.deleteEpics();
        assertTrue(taskManager.getEpics().isEmpty());
        assertTrue(taskManager.getSubTasks().isEmpty());

        assertNull(taskManager.getByEpicId(1));
    }

    @Test
    public void subTasksWithNormalBehavior() {
        final Map<Integer, SubTask> subTasks = taskManager.getSubTasks();
        final SubTask savedSubTask = taskManager.getBySubTaskId(2);

        assertNotNull(savedSubTask);
        assertNotNull(subTasks);
        assertEquals(3, subTasks.size());
        assertEquals(savedSubTask, subTasks.get(2));

        taskManager.deleteSubTasks();
        assertTrue(taskManager.getSubTasks().isEmpty());

        assertNull(taskManager.getBySubTaskId(1));
    }

    @Test
    public void tasksWithNormalBehavior() {
        Task task = new Task("firstTask", "firstTask Description");
        final int taskId = taskManager.createTask(task);

        final Task savedTask = taskManager.getByTaskId(taskId);

        assertNotNull(savedTask);
        assertEquals(task, savedTask);

        final Map<Integer, Task> tasks = taskManager.getTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(5));

        taskManager.deleteTasks();
        assertTrue(taskManager.getTasks().isEmpty());

        assertNull(taskManager.getByTaskId(1));
    }

    @Test
    public void calculationOfEpicStatus() {
        taskManager.checkStatusEpic(epic);
        assertEquals(StatusTask.NEW, taskManager.getEpics().get(1).getStatus());

        taskManager.getBySubTaskId(subTask1).setStatus(StatusTask.IN_PROGRESS);
        taskManager.getBySubTaskId(subTask2).setStatus(StatusTask.NEW);
        taskManager.getBySubTaskId(subTask3).setStatus(StatusTask.DONE);
        taskManager.checkStatusEpic(epic);

        assertEquals(StatusTask.IN_PROGRESS, taskManager.getEpics().get(1).getStatus());

        taskManager.getBySubTaskId(subTask1).setStatus(StatusTask.DONE);
        taskManager.getBySubTaskId(subTask2).setStatus(StatusTask.DONE);
        taskManager.getBySubTaskId(subTask3).setStatus(StatusTask.DONE);
        taskManager.checkStatusEpic(epic);

        assertEquals(StatusTask.DONE, taskManager.getEpics().get(1).getStatus());

        taskManager.getBySubTaskId(subTask1).setStatus(StatusTask.IN_PROGRESS);
        taskManager.getBySubTaskId(subTask2).setStatus(StatusTask.IN_PROGRESS);
        taskManager.getBySubTaskId(subTask3).setStatus(StatusTask.IN_PROGRESS);
        taskManager.checkStatusEpic(epic);

        assertEquals(StatusTask.IN_PROGRESS, taskManager.getEpics().get(1).getStatus());
    }

    @Test
    public void checkPresenceOfAnEpic() {
       final int idEpic = taskManager.getSubTasks().get(2).getIdEpic();
       assertEquals(1, idEpic);
    }

    @Test
    public void checkNormalTimeAndEndTime() {
        taskManager.createSubTask(new SubTask("four", "fourDesc", taskManager.getEpics().get(epic), "14:00 10.11.22", 120));
        taskManager.createSubTask(new SubTask("five", "fiveDesc", taskManager.getEpics().get(epic), "10:00 09.11.22", 360));
        taskManager.createSubTask(new SubTask("six", "sixDesc", taskManager.getEpics().get(epic), "12:00 10.11.22", 60));

        assertEquals("2022-11-09T10:00", taskManager.getEpics().get(epic).getStartTime().get().toString());
        assertEquals("2022-11-10T16:00", taskManager.getEpics().get(epic).getEndTime().get().toString());
    }

    @Test
    public void checkIntersectionOfTaskTimes() {
        taskManager.createSubTask(new SubTask("four", "fourDesc", taskManager.getEpics().get(epic), "14:00 10.11.22", 120));
        taskManager.createSubTask(new SubTask("five", "fiveDesc", taskManager.getEpics().get(epic), "10:00 09.11.22", 360));


        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> taskManager.createSubTask(new SubTask("six", "sixDesc", taskManager.getEpics().get(epic), "13:30 10.11.22", 60))
        );

        assertEquals("Время занято другой задачей!", ex.getMessage());

    }

    @Test
    public void checkPrioritizedTasks() {
        taskManager.createTask(new Task("five", "fiveDescription"));
        taskManager.createTask(new Task("six", "sixDescription"));

        assertEquals(taskManager.getSubTasks().get(2), taskManager.getPrioritizedTasks().first());
        assertEquals(taskManager.getTasks().get(6), taskManager.getPrioritizedTasks().last());
    }

    @Test
    public void checkGetAllEpicSubTasks() {
        List<SubTask> testList = new ArrayList<>();
        testList.add(new SubTask("oneSubtask", "oneSubtask Description", taskManager.getEpics().get(epic)));
        testList.get(0).setId(2);
        testList.add(new SubTask("twoSubtask", "twoSubtask Description", taskManager.getEpics().get(epic)));
        testList.get(1).setId(3);
        testList.add(new SubTask("treeSubtask", "treeSubtask Description", taskManager.getEpics().get(epic)));
        testList.get(2).setId(4);


        assertIterableEquals(testList, taskManager.getAllEpicSubTasks(taskManager.getByEpicId(epic)));

        taskManager.deleteByIdentifier(3);
        testList.remove(1);
        assertIterableEquals(testList, taskManager.getAllEpicSubTasks(taskManager.getByEpicId(epic)));
    }
}