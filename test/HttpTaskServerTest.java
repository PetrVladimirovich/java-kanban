import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kanBan.models.business.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import kanBan.services.manager.Managers;
import kanBan.web.HttpTaskServer;
import kanBan.web.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private KVServer kvServer;
    private Gson gson;

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        gson = Managers.getGson();

        int epicTwo = httpTaskServer.httpTaskManager.createEpic(new Epic("SecondTest", "descripSecondTest"));
        int epicOne = httpTaskServer.httpTaskManager.createEpic(new Epic("firstTest", "descripFirstTest"));
        httpTaskServer.httpTaskManager.createSubTask(new SubTask("tree", "treeDesc", httpTaskServer.httpTaskManager.getEpics().get(epicOne), "14:00 03.11.22", 180));
        httpTaskServer.httpTaskManager.createTask(new Task("10", "10Desc", "14:00 15.11.22", 180));
        httpTaskServer.httpTaskManager.getByEpicId(1);
        httpTaskServer.httpTaskManager.getBySubTaskId(3);
        httpTaskServer.httpTaskManager.getBySubTaskId(3);
        httpTaskServer.httpTaskManager.getByEpicId(1);
    }

    @AfterEach
    public void tearDown() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    public void getTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200 , responce.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actualTask = gson.fromJson(responce.body(), taskType);

        assertNotNull(actualTask);
        assertEquals(1, actualTask.size());
        assertEquals(actualTask.get(0), httpTaskServer.httpTaskManager.getByTaskId(4));
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200 , responce.statusCode());

        Task actualTask = gson.fromJson(responce.body(), Task.class);

        assertNotNull(actualTask);
        assertEquals(actualTask, httpTaskServer.httpTaskManager.getByTaskId(4));
    }

    @Test
    public void postTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(new Task("111", "111Desc", "14:00 20.11.22", 180));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200 , responce.statusCode());
        assertEquals(2, httpTaskServer.httpTaskManager.getTasks().size());
        assertEquals("111", httpTaskServer.httpTaskManager.getByTaskId(5).getTitle());
    }

    @Test
    public void deleteTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responce.statusCode());
        assertTrue(httpTaskServer.httpTaskManager.getTasks().isEmpty());
    }

    @Test
    public void deleteTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responce.statusCode());
        assertTrue(httpTaskServer.httpTaskManager.getTasks().isEmpty());
    }

    @Test
    public void getSubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200 , responce.statusCode());

        Type taskType = new TypeToken<ArrayList<SubTask>>() {}.getType();
        List<SubTask> actualTask = gson.fromJson(responce.body(), taskType);

        assertNotNull(actualTask);
        assertEquals(1, actualTask.size());
        assertEquals(actualTask.get(0), httpTaskServer.httpTaskManager.getBySubTaskId(3));
    }

    @Test
    public void getSubTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200 , responce.statusCode());

        SubTask actualTask = gson.fromJson(responce.body(), SubTask.class);

        assertNotNull(actualTask);
        assertEquals(actualTask, httpTaskServer.httpTaskManager.getBySubTaskId(3));
    }

    @Test
    public void postSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(new SubTask("333", "333Desc", httpTaskServer.httpTaskManager.getEpics().get(1), "14:00 25.11.22", 180));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200 , responce.statusCode());
        assertEquals(2, httpTaskServer.httpTaskManager.getSubTasks().size());
        assertEquals("333", httpTaskServer.httpTaskManager.getBySubTaskId(5).getTitle());
    }

    @Test
    public void deleteSubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responce.statusCode());
        assertTrue(httpTaskServer.httpTaskManager.getSubTasks().isEmpty());
    }

    @Test
    public void deleteSubTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responce.statusCode());
        assertTrue(httpTaskServer.httpTaskManager.getSubTasks().isEmpty());
    }

    @Test
    public void getEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200 , responce.statusCode());

        Type taskType = new TypeToken<ArrayList<Epic>>() {}.getType();
        List<Epic> actualTask = gson.fromJson(responce.body(), taskType);

        assertNotNull(actualTask);
        assertEquals(2, actualTask.size());
        assertEquals(actualTask.get(0), httpTaskServer.httpTaskManager.getByEpicId(1));
    }

    @Test
    public void getEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200 , responce.statusCode());

        Epic actualTask = gson.fromJson(responce.body(), Epic.class);

        assertNotNull(actualTask);
        assertEquals(actualTask, httpTaskServer.httpTaskManager.getByEpicId(1));
    }

    @Test
    public void postEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(new Epic("222", "222Desc"));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200 , responce.statusCode());
        assertEquals(3, httpTaskServer.httpTaskManager.getEpics().size());
        assertEquals("222", httpTaskServer.httpTaskManager.getByEpicId(5).getTitle());
    }

    @Test
    public void deleteEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responce.statusCode());
        assertTrue(httpTaskServer.httpTaskManager.getEpics().isEmpty());
    }

    @Test
    public void deleteEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responce.statusCode());
        assertFalse(httpTaskServer.httpTaskManager.getEpics().isEmpty());
    }

    @Test
    public void getSubTasksByEpicId() throws IOException, InterruptedException {
        httpTaskServer.httpTaskManager.createSubTask(new SubTask("444", "treeDesc", httpTaskServer.httpTaskManager.getEpics().get(2), "14:00 25.11.22", 180));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200 , responce.statusCode());

        Type taskType = new TypeToken<ArrayList<SubTask>>() {}.getType();
        List<SubTask> actualTask = gson.fromJson(responce.body(), taskType);

        assertNotNull(actualTask);
        assertEquals(2, actualTask.size());
        assertEquals(actualTask.get(0), httpTaskServer.httpTaskManager.getBySubTaskId(3));
        assertEquals(actualTask.get(1), httpTaskServer.httpTaskManager.getBySubTaskId(5));
    }

    @Test
    public void getAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200 , responce.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actualTask = gson.fromJson(responce.body(), taskType);

        assertNotNull(actualTask);
        assertEquals(4, actualTask.size());
        assertEquals("SecondTest", httpTaskServer.httpTaskManager.getByEpicId(1).getTitle());
        assertEquals("10", httpTaskServer.httpTaskManager.getByTaskId(4).getTitle());
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responce = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200 , responce.statusCode());

        Type taskType = new TypeToken<LinkedList<Task>>() {}.getType();
        List<Task> actualTask = gson.fromJson(responce.body(), taskType);

        assertNotNull(actualTask);
        assertEquals(2, actualTask.size());
        assertEquals(actualTask.get(0).getTitle(), httpTaskServer.httpTaskManager.getBySubTaskId(3).getTitle());
        assertEquals(actualTask.get(1).getTitle(), httpTaskServer.httpTaskManager.getByEpicId(1).getTitle());
    }
}
