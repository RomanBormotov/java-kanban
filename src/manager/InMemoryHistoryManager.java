package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    Map<Integer, Node<Task>> nodes = new HashMap<>();

    Node<Task> first;
    Node<Task> last;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId()); // сначала remove для очистки
        linkLast(task); // привязываем таск
        nodes.put(task.getId(), last); //
    }

    @Override
    public void remove(int id) {
        // Получить из мапы ноду по айди таска
        Node<Task> node = nodes.get(id);
        // Если такой ноды нет, то удалять нечего - выходим
        if (node == null) {
            return;
        }
        // 1. Если удаляемая нода НЕ ПЕРВАЯ
        // Перепривязать указатель node.prev.next = node.next - перепривязали ноду в сторону конца
        if (node != first) {
            node.prev.next = node.next;
            // 1.1 Если node.next == null (следующая, после удаляемой), тогда переопределим last
            if (node.next == null) {
                last = node.prev;
                return;
                // 1.2 Если node.next != null (из середины), тогда перепривзяать node.next.prev = node.prev
            } else {
                node.next.prev = node.prev;
                return;
            }
        }
        // 2. Если удаляемая нода ПЕРВАЯ
        // Переопределить first
        first = node.next;
        // 2.1 Если новый first == null, то переопределить last(null)
        if (first == null) {
            last = null;
        }
        // 2.2 Если новый first != null, то у новой first.prev = null
        else first.prev = null;
    }

    @Override
    public List<Task> getHistory() { //он же метод getTasks
        List<Task> history = new ArrayList<>();
        Node<Task> currentNode = first; //
        while (currentNode != null) {
            //Добавляем таски из ноды в список
            history.add((Task) currentNode.value); //(Task)
            currentNode = currentNode.next;
        }
        return history;
    }


    public void linkLast(Task task) { // привязывает новую ноду к последнему указателю. Добавляет ноду к концу двухсвязанного списка
        // Создать новую ноду (newNode) через конструктор
        Node<Task> newNode = new Node<>(task, last, null);
        // Проверить, если нод нет, то новую ноду положим в first
        if (first == null) {
            first = newNode;
        }
        // Eсли уже какие-то ноды есть, то нужно к последней ноде привязать новосозданную last.next = newNode
        else {
            last.next = newNode;
        }
        // Всегда привзяжем в конце last = новосозданная нода
        last = newNode;
    }

    static class Node<Task> {
        Task value;
        Node<Task> prev;
        Node<Task> next;

        public Node(Task value, Node<Task> prev, Node<Task> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

}
