import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        BlockingQueue blockingQueue = new BlockingQueue();    // Создаем экземпляр для работы с блокирующей очередью
        Thread worker = new Thread(new Runnable() {           // Создаем поток, который будет обрабатывать задачи очереди(WorkerThread)
            @Override
            public void run() {                               // В методе run запускаем бесконечный цикл, который будет брать задачи из очереди и исполнять их
                while (true){
                    Runnable task = blockingQueue.get();      // Берем задачу из очереди
                    task.run();                               // Исполняем задачу
                }
            }
        });
        worker.start();                                        // Запускаем поток

        // Добавляем в очередь 10 задач
        for (int i = 0; i < 10; i++) {
            blockingQueue.put(getTask());
        }

    }

    public static Runnable getTask(){
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("Task started: " + this); // Выводи задачу на консоль
                try {
                    Thread.sleep(1000);                      // Усыпляем поток на 1000 миллисекунж(1 секунда)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    // Создаем внутренний класс для реализации блокирующей очереди
    static class BlockingQueue {
        ArrayList<Runnable> tasks = new ArrayList<>();  // Массив задач

        /**
         * Метод для поллучения задачи из очереди
         * @return задача
         */
        public synchronized Runnable get(){
            while (tasks.isEmpty()){ // Поток спит, пока очередь пуста
                try {
                    wait();          // Один из методов класса Object, позволяющий усыпить поток
                }
                catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            }

            Runnable task = tasks.get(0); // Получаем нулевую задачу
            tasks.remove(task);           // Удаляем нулевую задачу, чтобы обеспечить алгоритм работы очереди
            return task;                  // Возвращаем полученную задачу
        }

        /**
         * Метод для добавления задачи в очередь
         * @param task
         */
        public synchronized void put(Runnable task){
            tasks.add(task); // Добавляем в массив задачу
            notify();        // Пробуждаем поток
        }

    }
}
