package manager;

import tasks.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    //поля
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected int keyID = 0;

    protected HashMap<Integer, Task> tasks = new HashMap<>(); // хранит задачи типа Task
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>(); // хранит задачи типа Subtask
    protected HashMap<Integer, Epic> epics = new HashMap<>(); // хранит задачи типа Epic
    protected TreeMap<LocalDateTime, Task> sortedTasks = new TreeMap<>();

    //методы
    //2.1 Получение списка всех задач
    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    //2.2 Удаление всех задач
    @Override
    public void removeTasks() {
        tasks.clear();
        System.out.println("Таски успешно удалены");
    }

    @Override
    public void removeSubtasks() {
        subtasks.clear();
        for (Epic e : epics.values()) {
            e.removeSubtasks();
        }
        System.out.println("Сабтаски успешно удалены");
    }

    @Override
    public void removeEpics() {
        epics.clear();
        for (Subtask s : subtasks.values()) {
            s.removeEpicId();
        }
        System.out.println("Эпики успешно удалены");
    }

    @Override
    //2.3 Получение по идентификатору
    public Task getTaskOnID(int ID) {
        historyManager.add(tasks.get(ID));
        return tasks.get(ID);
    }

    @Override
    public Subtask getSubtaskOnID(int ID) {
        historyManager.add(subtasks.get(ID));
        return subtasks.get(ID);
    }

    @Override
    public Epic getEpicOnID(int ID) {
        historyManager.add(epics.get(ID));
        return epics.get(ID);
    }

    //2.4 Создание
    @Override
    public void createTask(Task task) {
        if (isCrossing(task)) return;
        task.setId(keyID++);
        tasks.put(task.getId(), task);
        System.out.println("Таск успешно создан");
    }

    @Override
    public void createSubtask(int epicID, Subtask subtask) {
        if (!epics.containsKey(epicID)) {
            System.out.println("Сабтаск не может быть создан, так как указанный Эпик отсутствует.");
            return;
        }
        if (isCrossing(subtask)) return;
        Epic epic = epics.get(epicID);
        if (epic.getStatus().equals(Status.DONE.toString())) {
            epic.setStatus(Status.IN_PROGRESS);
        }
        subtask.setEpicId(epicID);
        subtask.setId(keyID++);
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtask(subtask);
        epic.setStartTime();
        epic.setDuration();
        System.out.println("Сабтаск успешно создан и добавлен в эпик");
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(keyID++);
        epics.put(epic.getId(), epic);
        System.out.println("Эпик успешно создан");
    }

    //2.5 Обновление
    @Override
    public void updateTask(int keyID, Task task) {
        if (tasks.containsKey(keyID)) {
            if (isCrossing(task)) return;
            task.setStatus(Status.valueOf(tasks.get(keyID).getStatus()));
            task.setId(keyID);
            tasks.put(task.getId(), task);
            System.out.println("Таск успешно обновлен");
        } else {
            System.out.println("Данный таск не существует, в связи с " +
                    "этим обновить его невозможно. Попробуйте создать " +
                    "данный таск, либо проверьте ID, который вы указали");
        }
    }

    @Override
    public void updateSubtask(int keyID, Subtask subtask) {
        if (subtasks.containsKey(keyID)) {
            if (isCrossing(subtask)) return;
            subtask.setStatus(Status.valueOf(subtasks.get(keyID).getStatus()));
            subtask.setId(keyID);
            subtask.setEpicId(subtasks.get(keyID).getEpicId());
            subtasks.put(subtask.getId(), subtask);
            System.out.println("Сабтаск успешно обновлен");
            Epic epic = epics.get(subtask.getEpicId());
            updateStatus(epic);
            epic.setDuration();
            epic.setStartTime();
        } else {
            System.out.println("Данный Сабтаск не существует, в связи с " +
                    "этим обновить его невозможно. Попробуйте создать " +
                    "данный Сабтаск, либо проверьте ID, который вы указали");
        }
    }

    @Override
    public void updateEpic(int keyID, Epic epic) {
        if (epics.containsKey(keyID)) {
            updateStatus(epic);
            epic.setId(keyID);
            epic.setSubtasks(epics.get(keyID).getSubtasks());
            epic.setDuration();
            epic.setStartTime();
            epics.put(epic.getId(), epic);
            System.out.println("Эпик успешно обновлен");
        } else {
            System.out.println("Данный Эпик не существует, в связи с " +
                    "этим обновить его невозможно. Попробуйте создать " +
                    "данный Эпик, либо проверьте ID, который вы указали");
        }
    }

    @Override
    //2.6 Удаление по идентификатору
    public void removeTaskOnID(int ID) {
        if (tasks.containsKey(ID)) {
            tasks.remove(ID);
            historyManager.remove(ID);
            System.out.println("Таск успешно удален");
            return;
        }
        System.out.println("Указанного таска не существует, проверьте правильность введенного ID");
    }

    @Override
    public void removeSubtaskOnID(int ID) {
        if (subtasks.containsKey(ID)) {
            Subtask subtask = subtasks.get(ID);
            Epic epic = epics.get(subtask.getEpicId());
            subtasks.remove(ID);
            epic.removeSubtask(subtask);
            epic.setDuration();
            epic.setStartTime();
            historyManager.remove(ID);
            System.out.println("Сабтаск успешно удален");
            return;
        }
        System.out.println("Указанного Сабтаска не существует, проверьте правильность введенного ID");
    }

    @Override
    public void removeEpicOnID(int ID) {
        if (epics.containsKey(ID)) {
            epics.remove(ID);
            historyManager.remove(ID);

            ArrayList<Subtask> copy = new ArrayList<>(subtasks.values());
            for (Subtask subtask : copy) {
                if (subtask.getEpicId() == ID) {
                    subtasks.remove(subtask.getId());
                    historyManager.remove(subtask.getId());
                }
            }
            System.out.println("Эпик и его Сабтаски успешно удалены");
            return;
        }
        System.out.println("Указанного Эпика не существует, проверьте правильность введенного ID");
    }

    @Override //3.1 Получение списка всех подзадач определённого эпика
    public List<Subtask> getEpicSubtasks(int ID) {
        if (epics.containsKey(ID)) {
            /* ArrayList<Subtask> result = new ArrayList<>();
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getEpicId() == ID) {
                    result.add(subtask);
                }
            }
            return result;*/

            return epics.get(ID).getSubtasks();
        }
        System.out.println("Указанного Эпика не существует, проверьте правильность введенного ID");
        return null;
    }

    @Override //4 Управление статусами (делается приватным, согласно условию тз)
    public void updateStatus(Epic epic) {
        List<Subtask> subtasks = getEpicSubtasks(epic.getId());
        if (subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int countNew = 0;
        int countDone = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus().equals("NEW")) {
                countNew++;
            } else if (subtask.getStatus().equals("DONE")) {
                countDone++;
            }
        }
        if (countNew == subtasks.size()) {
            epic.setStatus(Status.NEW);
            return;
        }
        if (countDone == subtasks.size()) {
            epic.setStatus(Status.DONE);
            return;
        }
        epic.setStatus(Status.IN_PROGRESS);
    }

    @Override
    public List<Task> getHistory() {
        System.out.println("История задач");
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        startToSort();
        return new ArrayList<>(sortedTasks.values());
    }

    //Util methods

    private void startToSort() {
        sortedTasks.clear();
        for (Task task : tasks.values()) {
            sortedTasks.put(task.getStartTime(), task);
        }
        for (Task task : subtasks.values()) {
            sortedTasks.put(task.getStartTime(), task);
        }
    }

    private boolean isCrossing(Task task) {
        LocalDateTime taskStart = task.getStartTime();
        LocalDateTime taskEnd = task.getEndTime();
        String message = "К сожалению, таск " + task.toString().toUpperCase() +"\nпересекается по времени с теми, которые уже добавлены...\n" +
                "Попробуйте выбрать для этого таска иной промежуток времени.";
        startToSort();
        for (Task current : sortedTasks.values()) {
            boolean checkStart = taskStart.isAfter(current.getStartTime()) && taskStart.isBefore(current.getEndTime());
            boolean checkEnd = taskEnd.isAfter(current.getStartTime()) && taskEnd.isBefore(current.getEndTime());
            if (checkStart || checkEnd) {
                System.out.println(message);
                return true;
            }
        }
        return false;
    }
}

