import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Класс реализует многопоточный сервер, на котором выполняется операция сложения двух матриц
 */
public class Server {
  // Работаем с 10 потоками
  private static ExecutorService executeIt = Executors.newFixedThreadPool(10);

  public static void main(String[] args) {
    try {
      // Создаем серверный сокет
      ServerSocket server = new ServerSocket(3345);
      while (!server.isClosed()) {
        // Ищем новых клиентов
        Socket client = server.accept();
        // Отправляем их к моно-серверу (фабрика)
        executeIt.submit(new MonoServer(client));
      }
      // Убираемся после себя
      executeIt.shutdown();
      server.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
