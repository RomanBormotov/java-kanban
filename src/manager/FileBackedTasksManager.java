package manager;

import constants.Status;
import constants.TaskManagerType;
import exceptions.ManagerException;
import tasks.*;
import util.CSVTaskConverter;
import util.Managers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    public static void main(String[] args) {
        TaskManager fileBackedTasksManager = Managers.getDefault(TaskManagerType.FILE_BACKEND);
        Task task1 = new Task("Собрать вещи", "Уложить в коробки", Status.NEW, "05.05.2022 05:55", 300);
        Task task2 = new Task("Собрать вещи", "Заклеить коробки", Status.DONE, "05.05.2022 06:30", 360);
        Epic epic1 = new Epic("Переезд", "В другую квартиру", "12.12.2012 12:12", 999);
        Subtask subtask1 = new Subtask("Вызвать перевозчика", "Позвонить по номеру 000", Status.NEW, "13.01.2001 00:01", 5);
        Subtask subtask2 = new Subtask("Отдать ключи", "Вернуть ключи хозяину квартиры", Status.NEW, "29.05.2014 15:15", 20);
        Subtask subtask3 = new Subtask("Рассчитаться за аренду квартиры", "Подсчитать месяц оплаты", Status.DONE, "17.02.1997 11:11", 16000000);

        Epic epic2 = new Epic("Очистка квартиры", "Вызвать клининг","07.07.2007 07:07", 7);

        fileBackedTasksManager.createTask(task1);
        fileBackedTasksManager.createTask(task2);

        fileBackedTasksManager.createEpic(epic1);
        fileBackedTasksManager.createEpic(epic2);
        fileBackedTasksManager.createSubtask(epic1.getId(), subtask1);
        fileBackedTasksManager.createSubtask(epic1.getId(), subtask2);
        fileBackedTasksManager.createSubtask(epic1.getId(), subtask3);

        System.out.println(fileBackedTasksManager.getSubtasks());
        System.out.println(fileBackedTasksManager.getTasks());

        fileBackedTasksManager.getTaskOnID(task1.getId());
        fileBackedTasksManager.getSubtaskOnID(subtask1.getId());
        System.out.println(CSVTaskConverter.historyToString(fileBackedTasksManager.getHistory()));
        System.out.println("..............................................................");
        System.out.println("ПРОВЕРКА ЗАГРУЗКИ ВСЕЙ ИНФОРМАЦИИ ИЗ ФАЙЛА");
        System.out.println("..............................................................");
        TaskManager fileBackedTasksManager2 = loadFromFile(new File("tasks.csv"));
        System.out.println(fileBackedTasksManager2.getHistory());
        System.out.println(fileBackedTasksManager2.getEpics());

        System.out.println("..............................................................");
        System.out.println("ПРОВЕРКА СОРТИРОВКИ ВСЕХ ТАСКОВ ПО STARTTIME");
        System.out.println("..............................................................");

        System.out.println(fileBackedTasksManager2.getPrioritizedTasks());
    }

    private File file;

    public FileBackedTasksManager(File file) { // Передаем файл в конструктор
        this.file = file;
    }

    //восстанавливает менеджер из файла
    public static FileBackedTasksManager loadFromFile(File file) { // метод, который будет создавать FileBackedTasksManager
        FileBackedTasksManager fileBackedTasksManager =
                (FileBackedTasksManager) Managers.getDefault(TaskManagerType.FILE_BACKEND);
        int generatorId = 0;
        /* наполняем файл
        обернем все в try, пытаясь поймать IOExceptions
        прочитать из файла содержимое
        Files.readString(Path.of(path)); */
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder allInfo = new StringBuilder();
            String currentString;
            while ((currentString = reader.readLine()) != null) {
                allInfo.append(currentString).append("\n");
            }
            // сплитим по строкам в цикле
            String[] buffer = allInfo.toString().split("\n");
            /* проверить!, нельзя оставлять генератор ID нулевым, надо его восстановить, какой был последний
            Десериализуем таск из строки, например получили Task task = ...
            Если task.id > generatorId, то generatorId = task.id
            если мы наткнулись на пустую строку, то это - история, то парсим её
            добавить таск в соответсвующую мапу (switch по типу) */
            for (int i = 1; i < buffer.length-3 ; i++) {
                Task task = CSVTaskConverter.fromString(buffer[i]);
                if (task.getId() > generatorId) {
                    generatorId = task.getId();
                }
                // привязать сабтаски и эпики
                // проходимся по сабтаскам и связываем сабтаски и эпики
                switch (task.getTaskType()) {
                    case EPIC:
                        fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                        continue;
                    case TASK:
                        fileBackedTasksManager.tasks.put(task.getId(), task);
                        continue;
                    case SUBTASK:
                        fileBackedTasksManager.subtasks.put(task.getId(), (Subtask) task);
                }
            }
            for (Epic epic: fileBackedTasksManager.epics.values()) {
                for (Subtask subtask: fileBackedTasksManager.subtasks.values()) {
                    if (subtask.getEpicId() == epic.getId()) {
                        epic.addSubtask(subtask);
                    }
                }
            }
            List<Integer> history = new ArrayList<>();
            if (buffer[buffer.length-1].equals("isEmpty")) {
                System.out.println("Упс, история пуста");
            } else {
                history = CSVTaskConverter.historyFromString(buffer[buffer.length - 1]);
            }
            /* String[] history = buffer[buffer.length-1].split(",");
            дообработать историю
            пройтись по списку id из десериализованной истории и добавить в историю с помощью уже существующего метода
            historyManager.add(..) */
            for (int currentId: history) {

                if (fileBackedTasksManager.epics.containsKey(currentId)) {
                    fileBackedTasksManager.historyManager.add(fileBackedTasksManager.epics.get(currentId));
                } else if (fileBackedTasksManager.subtasks.containsKey(currentId)) {
                    fileBackedTasksManager.historyManager.add(fileBackedTasksManager.subtasks.get(currentId));
                } else if (fileBackedTasksManager.tasks.containsKey(currentId)) {
                    fileBackedTasksManager.historyManager.add(fileBackedTasksManager.tasks.get(currentId));
                }
            }
            fileBackedTasksManager.startToSort();
            // не забывать привзять новый generatorId
            fileBackedTasksManager.keyID = generatorId;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBackedTasksManager;
    }

    public void save () { // в методе Save будет происходить сохранение текущего состояния менеджера в файл.
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            fileWriter.write("id,type,   name,  status,  description, epicID, startTime, duration");
            fileWriter.newLine();
            /*Сериализация и запись тасков
            По очереди проходим в for кажду мапу с таском
            Внутри каждого for мы сериализуем таску CSVTaskFormater.toString(task)
            записать новую строку (доп перенос строки) */
            for (Task task: tasks.values()) {
                fileWriter.write(CSVTaskConverter.toString(task));
                fileWriter.newLine();
            }
            for (Subtask subtask: subtasks.values()) {
                fileWriter.write(CSVTaskConverter.toString(subtask));
                fileWriter.newLine();
            }
            for (Epic epic : epics.values()) {
                fileWriter.write(CSVTaskConverter.toString(epic));
                fileWriter.newLine();
            }
            fileWriter.newLine();
            fileWriter.write("History:");
            fileWriter.newLine();
            fileWriter.write(CSVTaskConverter.historyToString(getHistory()));

        } catch (IOException e) {
            throw new ManagerException("Сохранение не удалось");
        }
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public Task getTaskOnID(int ID) {
        historyManager.add(tasks.get(ID));
        save();
        return tasks.get(ID);
    }

    @Override
    public Subtask getSubtaskOnID(int ID) {
        historyManager.add(subtasks.get(ID));
        save();
        return subtasks.get(ID);
    }

    @Override
    public Epic getEpicOnID(int ID) {
        historyManager.add(epics.get(ID));
        save();
        return epics.get(ID);
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Subtask createSubtask(int epicID, Subtask subtask) {
        super.createSubtask(epicID, subtask);
        save();
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public void updateTask(int keyID, Task task) {
        super.updateTask(keyID, task);
        save();
    }

    @Override
    public void updateSubtask(int keyID, Subtask subtask) {
        super.updateSubtask(keyID, subtask);
        save();
    }

    @Override
    public void updateEpic(int keyID, Epic epic) {
        super.updateEpic(keyID, epic);
        save();
    }

    @Override
    public void removeTaskOnID(int ID) {
        super.removeTaskOnID(ID);
        save();
    }

    @Override
    public void removeSubtaskOnID(int ID) {
        super.removeSubtaskOnID(ID);
        save();
    }

    @Override
    public void removeEpicOnID(int ID) {
        super.removeEpicOnID(ID);
        save();
    }

    @Override
    public void updateStatus(Epic epic) {
        super.updateStatus(epic);
        save();
    }
}
