package manager;

import constants.Status;
import exceptions.ManagerException;
import tasks.*;
import util.Managers;

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

    private void removeFromSorted(Task task) {
        /*for (Task curTask : sortedTasks.values()) {
            if (curTask.equals(task)) {
                sortedTasks.remove(curTask.getStartTime());
            }
        }*/
        sortedTasks.remove(task.getStartTime());
    }

    private void removeFromSorted(Collection<? extends Task> tasks) {
        /*for (Task innerTask : sortedTasks.values()) {
            for (Task outTask : tasks) {
                if (innerTask.equals(outTask)) {
                    sortedTasks.remove(innerTask.getStartTime());
                }
             }
         }*/
        for (Task outTask : tasks) {
            sortedTasks.remove(outTask.getStartTime());
        }
    }

    private void addToSorted(Task task) {
        sortedTasks.put(task.getStartTime(), task);
    }

    private void updateSorted(Task task) {
        Map<LocalDateTime, Task> copy = new TreeMap<>(sortedTasks);
        for (Task curTask : copy.values()) {
            if (curTask.getId() == task.getId()) {
                sortedTasks.remove(curTask.getStartTime());
                sortedTasks.put(task.getStartTime(), task);
            }
        }
    }
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

    @Override
    public int getKeyID() {
        return keyID;
    }

    //2.2 Удаление всех задач
    @Override
    public void removeTasks() {
        removeFromSorted(tasks.values());
        tasks.clear();
        System.out.println("Таски успешно удалены");
    }

    @Override
    public void removeSubtasks() {
        removeFromSorted(subtasks.values());
        subtasks.clear();
        for (Epic e : epics.values()) {
            e.removeSubtasks();
        }
        System.out.println("Сабтаски успешно удалены");
    }
    //некорректно отрабатывал
    @Override
    public void removeEpics() {
        epics.clear();
        removeFromSorted(subtasks.values());
        subtasks.clear();
        System.out.println("Эпики успешно удалены");
    }

    @Override
    //2.3 Получение по идентификатору
    public Task getTaskOnID(int ID) {
        Task task = tasks.get(ID);
        if (task != null) {
            historyManager.add(tasks.get(ID));
        }
        return task;
    }

    @Override
    public Subtask getSubtaskOnID(int ID) {
        Subtask subtask = subtasks.get(ID);
        if (subtask != null) {
            historyManager.add(subtasks.get(ID));
        }
        return subtask;
    }

    @Override
    public Epic getEpicOnID(int ID) {
        historyManager.add(epics.get(ID));
        return epics.get(ID);
    }

    //2.4 Создание
    @Override
    public Task createTask(Task task) {
        if (task == null) return null;
        if (isCrossing(task)) return null;
        task.setId(keyID++);
        tasks.put(task.getId(), task);
        addToSorted(task);
        System.out.println("Таск успешно создан");
        return task;
    }

    @Override
    public Subtask createSubtask(int epicID, Subtask subtask) {
        if (subtask == null) return null;
        if (!epics.containsKey(epicID)) {
            System.out.println("Сабтаск не может быть создан, так как указанный Эпик отсутствует.");
            return null;
        }
        if (isCrossing(subtask)) return null;
        Epic epic = epics.get(epicID);
        if (epic.getStatus().equals(Status.DONE.toString())) {
            epic.setStatus(Status.IN_PROGRESS);
        }
        subtask.setEpicId(epicID);
        subtask.setId(keyID++);
        subtasks.put(subtask.getId(), subtask);
        addToSorted(subtask);
        epic.addSubtask(subtask);
        epic.setStartTime();
        epic.setDuration();
        System.out.println("Сабтаск успешно создан и добавлен в эпик");
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (epic == null) return null;
        epic.setId(keyID++);
        epics.put(epic.getId(), epic);
        System.out.println("Эпик успешно создан");
        return epic;
    }

    //2.5 Обновление
    @Override
    public void updateTask(int keyID, Task task) {
        if (task == null) {
            System.out.println("Переданный таск is null, обновление невозможно");
            return;
        }
        if (tasks.containsKey(keyID)) {
            if (isCrossing(task)) return;
            task.setStatus(Status.valueOf(tasks.get(keyID).getStatus()));
            task.setId(keyID);
            tasks.put(task.getId(), task);
            updateSorted(task);
            System.out.println("Таск успешно обновлен");
        } else {
            System.out.println("Данный таск не существует, в связи с " +
                    "этим обновить его невозможно. Попробуйте создать " +
                    "данный таск, либо проверьте ID, который вы указали");
        }
    }

    @Override
    public void updateSubtask(int keyID, Subtask subtask) {
        if (subtask == null) {
            System.out.println("Переданный сабтаск is null, обновление невозможно");
            return;
        }
        if (subtasks.containsKey(keyID)) {
            if (isCrossing(subtask)) return;
            subtask.setStatus(Status.valueOf(subtasks.get(keyID).getStatus()));
            subtask.setId(keyID);
            subtask.setEpicId(subtasks.get(keyID).getEpicId());
            subtasks.put(subtask.getId(), subtask);
            updateSorted(subtask);
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
        if (epic == null) {
            System.out.println("Переданный эпик is null, обновление невозможно");
            return;
        }
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
            removeFromSorted(tasks.get(ID));
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
            removeFromSorted(subtasks.get(ID));
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
    //некорректно отрабатывал, доработать с ХМ
    @Override
    public void removeEpicOnID(int ID) {
        if (epics.containsKey(ID)) {
            ArrayList<Subtask> copyES = new ArrayList<>(epics.get(ID).getSubtasks());
            for (Subtask subtask : copyES) {
                removeFromSorted(subtask);
                subtasks.remove(subtask.getId());
            }
            epics.remove(ID);
            historyManager.remove(ID);
            //historyManager.remove(subtask.getId());
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
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks.values());
    }

    //Util methods
    //ИСПОЛЬЗУЕМ ДАННЫЙ МЕТОД В FILEBACKEDTASKAMANGER В МЕТОДЕ LOADFROMFILE
    protected void startToSort() {
        sortedTasks.clear();
        for (Task task : tasks.values()) {
            sortedTasks.put(task.getStartTime(), task);
        }
        for (Task task : subtasks.values()) {
            sortedTasks.put(task.getStartTime(), task);
        }
    }

    private boolean isCrossing(Task task) {
        if (task.getStartTime() == null || task.getDuration() == null) {
            throw new ManagerException("у таска отсутствует время начала или длительность.., проверка невозможна");
        }
        LocalDateTime taskStart = task.getStartTime();
        LocalDateTime taskEnd = task.getEndTime();
        String message = "К сожалению, таск " + task.toString().toUpperCase() +"\nпересекается по времени с теми, которые уже добавлены...\n" +
                "Попробуйте выбрать для этого таска иной промежуток времени.";
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

