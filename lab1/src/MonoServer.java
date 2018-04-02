import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Класс реализует паттерн Фабрика. Является моно-сервером для обработки одного клиента.
 */
public class MonoServer implements Runnable {
  // Наш единственный клиент
  private final Socket client;

  /**
   * Создает моно-сервер по сокету клиента.
   * @param client сокет клиента
   */
  MonoServer(Socket client) {
    this.client = client;
  }

  /**
   * Складывает две матрицы, которые ему передает клиент, отправляет их сумму клиенту.
   */
  @Override
  public void run() {
    try {
      // Устанавливаем наши потоки данных через наш клиентский сокет
      Scanner in = new Scanner(client.getInputStream());
      PrintWriter out = new PrintWriter(client.getOutputStream());
      // Считываем наши матрицы, складываем их, записываем результат в выходной поток для клиента
      Matrix.printMatrix(Matrix.sum(Matrix.readMatrix(in), Matrix.readMatrix(in)), out);
      out.flush();
      // Убираемся после себя
      in.close();
      out.close();
      client.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
