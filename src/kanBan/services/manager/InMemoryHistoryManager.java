package kanBan.services.manager;

import java.util.*;

import kanBan.models.business.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> buffer;
    private final CustomLinkedList linkedHistory;

    public InMemoryHistoryManager() {
        buffer = new HashMap<>();
        linkedHistory = new CustomLinkedList();
    }

    @Override
    public void remove(int id) {
        if (buffer.containsKey(id)) {
            linkedHistory.removeNode(buffer.get(id));
            buffer.remove(id);
        }
    }

    @Override
    public void add(Task task) {
        Task taskTemp = new Task(task);

        if (buffer.containsKey(taskTemp.getId())) {
            linkedHistory.removeNode(buffer.get(taskTemp.getId()));
        }
        linkedHistory.linkLast(taskTemp);
    }

    @Override
    public List<Task> getHistory() {
        return linkedHistory.getTasks();
    }

    class CustomLinkedList {
        private Node<Task> head;
        private Node<Task> tail;
        private int size = 0;

        public void removeNode(Node node) {
            if (node.prev != null && node.next != null) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.data = null;
            }else if (node.prev != null && node.next == null) {
                tail = node.prev;
                node.prev.next = null;
                node.data = null;
            }else if (node.prev == null && node.next != null) {
                head = node.next;
                node.next.prev = null;
                node.data = null;
            }else if (node.prev == null && node.next == null) {
                node.data = null;
                if (size() == 0) {
                    return;
                }
            }

            size--;
        }

        public void linkLast(Task element) {
            final Node<Task> newNode;
            if (size == 0) {
                 newNode = new Node<>(null, element, null);
                head = newNode;
                tail = newNode;
            }else {
                final Node<Task> oldTail = tail;
                newNode = new Node<>(oldTail, element, null);
                tail = newNode;
                if (oldTail == null) {
                    head = newNode;
                } else
                    oldTail.next = newNode;
            }

            size++;
            buffer.put(element.getId(), newNode);
        }

        public List<Task> getTasks() {
            List<Task> arrHistory = new ArrayList<>();
            Node<Task> tempNode = head;

            for (int i = 0; i < size(); i++) {
                if (tempNode.data != null) {
                    arrHistory.add(tempNode.data);
                    if (tempNode.next != null)
                        tempNode = tempNode.next;
                }
            }

            return arrHistory;
        }

        public int size() {
            return this.size;
        }
    }
}
