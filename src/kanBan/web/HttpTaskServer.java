package kanBan.web;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import kanBan.models.business.Epic;
import kanBan.models.business.SubTask;
import kanBan.models.business.Task;
import kanBan.services.manager.Managers;
import kanBan.services.manager.taskManagers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static kanBan.services.manager.consts.Constants.PORT;

public class HttpTaskServer {

    private HttpServer server;
    private Gson gson;
    public TaskManager httpTaskManager;

    public HttpTaskServer() throws IOException, InterruptedException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.httpTaskManager = taskManager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.gson = Managers.buildGson();

        server.createContext("/tasks/task/", this::handleTask);
        server.createContext("/tasks/subtask/", this::handleSubTask);
        server.createContext("/tasks/epic/", this::handleEpic);
        server.createContext("/tasks/subtask/epic/", this::handleSubTasksByEpicId);
        server.createContext("/tasks/history/", this::handleHistory);
        server.createContext("/tasks/", this::handleAllTasks);


    }

    private void handleAllTasks(HttpExchange httpExchange) {
        try {
            System.out.println(httpExchange.getRequestURI());
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
                switch (requestMethod) {
                    case "GET": {
                        if (path.equals("/tasks/")) {
                            String answer = gson.toJson(httpTaskManager.getPrioritizedTasks());
                            sendText(httpExchange, answer);
                            return;
                        }else {
                            System.out.println("Неизвестный ресурс!!! --> " + path);
                            httpExchange.sendResponseHeaders(400, 0);
                            return;
                        }
                    }

                    default: {
                        System.out.println("Ожидается запрос GET, получили: " + requestMethod);
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                }

        } catch (Exception exception) {
            System.out.println("Сервер упал!");
        } finally {
            httpExchange.close();
        }
    }

    private void handleHistory(HttpExchange httpExchange) {
        try {
            System.out.println(httpExchange.getRequestURI());
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (path.equals("/tasks/history/")) {
                       String historyJson = gson.toJson(httpTaskManager.getHistory());
                       sendText(httpExchange, historyJson);
                       return;

                    }else {
                        System.out.println("Неизвестный ресурс!!! --> " + path);
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                }

                default: {
                    System.out.println("Ожидается запрос GET, получили: " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }

        } catch (Exception exception) {
            System.out.println("Сервер упал!");
        } finally {
            httpExchange.close();
        }
    }

    private void handleSubTasksByEpicId(HttpExchange httpExchange) {
        try {
            System.out.println(httpExchange.getRequestURI());
            String path = httpExchange.getRequestURI().getPath();
            String query = (httpExchange.getRequestURI().getQuery() == null) ? "" : httpExchange.getRequestURI()
                                                                                                .getQuery();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^id=\\d+$", query)) {
                        int id = parseInt(query.replaceFirst("id=", ""));
                        if (id != -1 && httpTaskManager.getEpics().containsKey(id)) {
                            String responce = gson.toJson(httpTaskManager.getAllEpicSubTasks(httpTaskManager.getByEpicId(id)));
                            sendText(httpExchange, responce);
                        }else {
                            System.out.println("Такого ресурса нет на сервере: " + httpExchange.getRequestURI());
                            httpExchange.sendResponseHeaders(400, 0);
                            return;
                        }
                    }
                    return;
                }

                default: {
                    System.out.println("Ожидается запрос GET, получили: " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }

        } catch (Exception exception) {
            System.out.println("Сервер упал!");
        } finally {
            httpExchange.close();
        }
    }

    private void handleTask(HttpExchange httpExchange) {
       try {
           System.out.println(httpExchange.getRequestURI());
           String path = httpExchange.getRequestURI().getPath();
           String query = (httpExchange.getRequestURI().getQuery() == null) ? "" : httpExchange.getRequestURI()
                                                                                                .getQuery();
           String requestMethod = httpExchange.getRequestMethod();
           switch (requestMethod) {
               case "GET": {
                   if (Pattern.matches("^id=\\d+$", query)) {
                       int id = parseInt(query.replaceFirst("id=", ""));
                       if (id != -1 && httpTaskManager.getTasks().containsKey(id)) {
                           String responce = gson.toJson(httpTaskManager.getByTaskId(id));
                           sendText(httpExchange, responce);
                       }else {
                           System.out.println("Такого ресурса нет на сервере: " + httpExchange.getRequestURI());
                           httpExchange.sendResponseHeaders(400, 0);
                           return;
                       }
                       return;
                   }

                   if (Pattern.matches("^/tasks/task/$", path) && query.isEmpty()) {
                       String responce = gson.toJson(httpTaskManager.getTasks().values());
                       sendText(httpExchange, responce);
                       return;
                   }

                   System.out.println("Такого ресурса нет на сервере: " + httpExchange.getRequestURI());
                   httpExchange.sendResponseHeaders(400, 0);
                   return;
               }
               case "POST": {
                   if (Pattern.matches("^/tasks/task/$", path)) {
                       Task task = gson.fromJson(readText(httpExchange), Task.class);
                       if (task.getId() > 0 && httpTaskManager.getTasks().containsKey(task.getId())) {
                           httpTaskManager.updateTask(task);
                           httpExchange.sendResponseHeaders(200, 0);
                       }else {
                           httpTaskManager.createTask(task);
                           httpExchange.sendResponseHeaders(200, 0);
                       }
                       return;
                   }
                   System.out.println("Такого ресурса нет на сервере: " + path);
                   httpExchange.sendResponseHeaders(400, 0);
                   break;
               }
               case "DELETE": {
                   if (Pattern.matches("^/tasks/task/$", path) && query.isEmpty()) {
                       httpTaskManager.deleteTasks();
                       httpExchange.sendResponseHeaders(200, 0);
                       return;
                   }

                   if (Pattern.matches("^id=\\d+$", query)) {
                       int id = parseInt(query.replaceFirst("id=", ""));
                       if (id != -1 && httpTaskManager.getTasks().containsKey(id)) {
                           httpTaskManager.deleteByIdentifier(id);
                           httpExchange.sendResponseHeaders(200, 0);
                       }else {
                           System.out.println("Такого ресурса нет на сервере: " + httpExchange.getRequestURI());
                           httpExchange.sendResponseHeaders(400, 0);
                           return;
                       }
                       return;
                   }
                   System.out.println("Такого ресурса нет на сервере: " + path);
                   httpExchange.sendResponseHeaders(400, 0);
                   break;
               }
               default: {
                   System.out.println("Ожидаются запросы GET POST или DELETE, получили: " + requestMethod);
                   httpExchange.sendResponseHeaders(405, 0);
               }
           }

       } catch (Exception exception) {
           System.out.println("Сервер упал!");
       } finally {
           httpExchange.close();
       }

    }

    private void handleEpic(HttpExchange httpExchange) {
        try {
            System.out.println(httpExchange.getRequestURI());
            String path = httpExchange.getRequestURI().getPath();
            String query = (httpExchange.getRequestURI().getQuery() == null) ? "" : httpExchange.getRequestURI()
                                                                                                .getQuery();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^id=\\d+$", query)) {
                        int id = parseInt(query.replaceFirst("id=", ""));
                        if (id != -1 && httpTaskManager.getEpics().containsKey(id)) {
                            String responce = gson.toJson(httpTaskManager.getByEpicId(id));
                            sendText(httpExchange, responce);
                        }else {
                            System.out.println("Такого ресурса нет на сервере: " + httpExchange.getRequestURI());
                            httpExchange.sendResponseHeaders(400, 0);
                            return;
                        }
                        return;
                    }

                    if (Pattern.matches("^/tasks/epic/$", path) && query.isEmpty()) {
                        String responce = gson.toJson(httpTaskManager.getEpics().values());
                        sendText(httpExchange, responce);
                        return;
                    }

                    System.out.println("Такого ресурса нет на сервере: " + httpExchange.getRequestURI());
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        Epic task = gson.fromJson(readText(httpExchange), Epic.class);
                        if (task.getId() > 0 && httpTaskManager.getEpics().containsKey(task.getId())) {
                            httpTaskManager.updateEpic(task);
                            httpExchange.sendResponseHeaders(200, 0);
                        }else {
                            httpTaskManager.createEpic(task);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                        return;
                    }
                    System.out.println("Такого ресурса нет на сервере: " + path);
                    httpExchange.sendResponseHeaders(400, 0);
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/epic/$", path) && query.isEmpty()) {
                        httpTaskManager.deleteEpics();
                        httpExchange.sendResponseHeaders(200, 0);
                        return;
                    }

                    if (Pattern.matches("^id=\\d+$", query)) {
                        int id = parseInt(query.replaceFirst("id=", ""));
                        if (id != -1 && httpTaskManager.getEpics().containsKey(id)) {
                            httpTaskManager.deleteByIdentifier(id);
                            httpExchange.sendResponseHeaders(200, 0);
                        }else {
                            System.out.println("Такого ресурса нет на сервере: " + httpExchange.getRequestURI());
                            httpExchange.sendResponseHeaders(400, 0);
                            return;
                        }
                        return;
                    }
                    System.out.println("Такого ресурса нет на сервере: " + path);
                    httpExchange.sendResponseHeaders(400, 0);
                    break;
                }
                default: {
                    System.out.println("Ожидаются запросы GET POST или DELETE, получили: " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }

        } catch (Exception exception) {
            System.out.println("Сервер упал!");
        } finally {
            httpExchange.close();
        }
    }

    private void handleSubTask(HttpExchange httpExchange) {
            try {
                System.out.println(httpExchange.getRequestURI());
                String path = httpExchange.getRequestURI().getPath();
                String query = (httpExchange.getRequestURI().getQuery() == null) ? "" : httpExchange.getRequestURI()
                                                                                                    .getQuery();
                String requestMethod = httpExchange.getRequestMethod();
                switch (requestMethod) {
                    case "GET": {
                        if (Pattern.matches("^id=\\d+$", query)) {
                            int id = parseInt(query.replaceFirst("id=", ""));
                            if (id != -1 && httpTaskManager.getSubTasks().containsKey(id)) {
                                String responce = gson.toJson(httpTaskManager.getBySubTaskId(id));
                                sendText(httpExchange, responce);
                            }else {
                                System.out.println("Такого ресурса нет на сервере: " + httpExchange.getRequestURI());
                                httpExchange.sendResponseHeaders(400, 0);
                                return;
                            }
                            return;
                        }

                        if (Pattern.matches("^/tasks/subtask/$", path) && query.isEmpty()) {
                            String responce = gson.toJson(httpTaskManager.getSubTasks().values());
                            sendText(httpExchange, responce);
                            return;
                        }

                        System.out.println("Такого ресурса нет на сервере: " + httpExchange.getRequestURI());
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    case "POST": {
                        if (Pattern.matches("^/tasks/subtask/$", path)) {
                            SubTask task = gson.fromJson(readText(httpExchange), SubTask.class);
                            if (task.getId() > 0 && httpTaskManager.getSubTasks().containsKey(task.getId())) {
                                httpTaskManager.updateSubTask(task);
                                httpExchange.sendResponseHeaders(200, 0);
                            }else {
                                httpTaskManager.createSubTask(task);
                                httpExchange.sendResponseHeaders(200, 0);
                            }
                            return;
                        }
                        System.out.println("Такого ресурса нет на сервере: " + path);
                        httpExchange.sendResponseHeaders(400, 0);
                        break;
                    }
                    case "DELETE": {
                        if (Pattern.matches("^/tasks/subtask/$", path) && query.isEmpty()) {
                            httpTaskManager.deleteSubTasks();
                            httpExchange.sendResponseHeaders(200, 0);
                            return;
                        }

                        if (Pattern.matches("^id=\\d+$", query)) {
                            int id = parseInt(query.replaceFirst("id=", ""));
                            if (id != -1 && httpTaskManager.getSubTasks().containsKey(id)) {
                                httpTaskManager.deleteByIdentifier(id);
                                httpExchange.sendResponseHeaders(200, 0);
                            }else {
                                System.out.println("Такого ресурса нет на сервере: " + httpExchange.getRequestURI());
                                httpExchange.sendResponseHeaders(400, 0);
                                return;
                            }
                            return;
                        }
                        System.out.println("Такого ресурса нет на сервере: " + path);
                        httpExchange.sendResponseHeaders(400, 0);
                        break;
                    }
                    default: {
                        System.out.println("Ожидаются запросы GET POST или DELETE, получили: " + requestMethod);
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                }

            } catch (Exception exception) {
                System.out.println("Сервер упал!");
            } finally {
                httpExchange.close();
            }
    }

    public void start() {
        System.out.println("Запускаем HttpTaskServer на порту " + PORT);
        System.out.println("http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private int parseInt(String path) {
        try {
            return Integer.parseInt(path);
        } catch(NumberFormatException e) {
            return -1;
        }
    }

}