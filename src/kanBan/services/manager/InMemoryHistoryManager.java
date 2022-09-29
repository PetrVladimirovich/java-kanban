package kanBan.services.manager;

import java.util.*;

import kanBan.models.business.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> buffer;
    private final CustomLinkedList<Task> linkedHistory;

    public InMemoryHistoryManager() {
        buffer = new HashMap<>();
        linkedHistory = new CustomLinkedList<>();
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

        //Task taskTemp = new Task(task);

        if (buffer.containsKey(task.getId())) {
            linkedHistory.removeNode(buffer.get(task.getId()));
        }
        linkedHistory.linkLast(task);
        buffer.put(task.getId(), linkedHistory.tail);
    }

    @Override
    public List<Task> getHistory() {
        return linkedHistory.getTasks();
    }

    private class CustomLinkedList<T extends Task> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        public void removeNode(Node<T> node) {
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

        public void linkLast(T element) {
            final Node<T> newNode;
            if (size == 0) {
                 newNode = new Node<>(null, element, null);
                head = newNode;
                tail = newNode;
            }else {
                final Node<T> oldTail = tail;
                newNode = new Node<>(oldTail, element, null);
                tail = newNode;
                if (oldTail == null) {
                    head = newNode;
                } else
                    oldTail.next = newNode;
            }

            size++;
        }

        public List<T> getTasks() {
            List<T> arrHistory = new ArrayList<>();
            Node<T> tempNode = head;

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
