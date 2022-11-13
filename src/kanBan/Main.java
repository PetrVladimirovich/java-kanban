package kanBan;

import kanBan.models.business.*;
import kanBan.web.HttpTaskServer;
import kanBan.web.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        System.out.println("KV сервер запущен!");
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        System.out.println("Сервер http запущен!");
        int epicOne = httpTaskServer.httpTaskManager.createEpic(new Epic("firstTest", "descripFirstTest"));

        int epicTwo = httpTaskServer.httpTaskManager.createEpic(new Epic("SecondTest", "descripSecondTest"));

        httpTaskServer.httpTaskManager.createSubTask(new SubTask("tree", "treeDesc", httpTaskServer.httpTaskManager.getEpics().get(epicOne), "14:00 03.11.22", 180));

        httpTaskServer.httpTaskManager.createSubTask(new SubTask("four", "fourDesc", httpTaskServer.httpTaskManager.getEpics().get(epicOne),"14:00 04.11.22", 60));

        httpTaskServer.httpTaskManager.createSubTask(new SubTask("five", "fiveDesc", httpTaskServer.httpTaskManager.getEpics().get(epicOne), "09:00 04.11.22", 120));

        httpTaskServer.httpTaskManager.getBySubTaskId(5);
        httpTaskServer.httpTaskManager.getByEpicId(2);
        httpTaskServer.httpTaskManager.getBySubTaskId(3);
        httpTaskServer.httpTaskManager.getBySubTaskId(5);
        httpTaskServer.httpTaskManager.getByEpicId(1);
        httpTaskServer.httpTaskManager.getBySubTaskId(5);
        System.out.println(httpTaskServer.httpTaskManager.getHistory());

        httpTaskServer.httpTaskManager.getBySubTaskId(4);
        httpTaskServer.httpTaskManager.getBySubTaskId(5);
        httpTaskServer.httpTaskManager.getBySubTaskId(5);
        System.out.println(httpTaskServer.httpTaskManager.getHistory());
        httpTaskServer.httpTaskManager.getBySubTaskId(3);
        System.out.println(httpTaskServer.httpTaskManager.getHistory());
        httpTaskServer.httpTaskManager.deleteByIdentifier(3);
        System.out.println(httpTaskServer.httpTaskManager.getHistory());
        httpTaskServer.httpTaskManager.getBySubTaskId(5);
        System.out.println(httpTaskServer.httpTaskManager.getHistory());
        httpTaskServer.httpTaskManager.getBySubTaskId(4);
        System.out.println(httpTaskServer.httpTaskManager.getHistory());
        System.out.println(httpTaskServer.httpTaskManager.getAllEpicSubTasks(httpTaskServer.httpTaskManager.getByEpicId(1)));

    }
}