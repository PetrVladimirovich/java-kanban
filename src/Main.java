public class Main {

    public static void main(String[] args) {

        TasksManager tasksManager = new TasksManager();

        tasksManager.creatingEpic(new Epic("firstTest", "descripFirstTest"));
        tasksManager.creatingEpic(new Epic("SecondTest", "descripSecondTest"));

        tasksManager.creatingSubTask(new SubTask("tree", "treeDesc", 1));
        tasksManager.creatingSubTask(new SubTask("four", "fourDesc", 1));
        tasksManager.creatingSubTask(new SubTask("five", "fiveDesc", 2));

        System.out.println(tasksManager.getTasks());
        System.out.println(tasksManager.getSubTasks());
        System.out.println(tasksManager.getEpics());

        tasksManager.getSubTasks().get(3).setStatus(StatusTask.DONE);
        tasksManager.updateSubTask(tasksManager.getSubTasks().get(3));

        tasksManager.getSubTasks().get(5).setStatus(StatusTask.IN_PROGRESS);
        tasksManager.updateSubTask(tasksManager.getSubTasks().get(5));

        System.out.println(tasksManager.getEpics());

        tasksManager.getSubTasks().get(4).setStatus(StatusTask.DONE);
        tasksManager.updateSubTask(tasksManager.getSubTasks().get(4));

        System.out.println(tasksManager.getEpics());

        tasksManager.deletingByIdentifier(2);

        System.out.println(tasksManager.getSubTasks());

        tasksManager.deletingByIdentifier(5);

        System.out.println(tasksManager.getTasks());
        System.out.println(tasksManager.getSubTasks());
        System.out.println(tasksManager.getEpics());

    }
}
