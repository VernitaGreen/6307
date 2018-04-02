import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Класс клиента. Реализует чтение 2-х матриц из файлов, переданных в качестве аргументов
 * при запуске класса, отправляет их серверу, на котором происходит вычисление суммы данных матриц,
 * записывает в данный файл результат вычисления.
 *
 * @see #main(String[]) вызов функции.
 */
public class Client {
  /**
   * @param args первые два рагумента – путь к файлам с исходными матрицы
   *             (в каждом файле указана размерность матрицы, затем идет описание значений матрицы
   *             в виде двумерного массива), третий – путь к файлу, в котором в последствии должен лежать
   *             результат вычисления.
   */
  public static void main(String[] args) {
    // Инициализируем наши файлы
    File firstInputMatrixFile = new File(args[0]);
    File secondInputMatrixFile = new File(args[1]);
    File outputMatrixFile = new File(args[2]);
    try {
      // Читаем первые две матрицы
      Matrix firstMatrix = Matrix.readMatrix(firstInputMatrixFile);
      Matrix secondMatrix = Matrix.readMatrix(secondInputMatrixFile);
      // Подключаемся к серверу
      Socket socket = new Socket("localhost", 3345);
      // Инициализируем наши потоки данных
      Scanner in = new Scanner(socket.getInputStream());
      PrintWriter out = new PrintWriter(socket.getOutputStream());
      // Передаем обе матрицы на сервер
      Matrix.printMatrix(firstMatrix, out);
      Matrix.printMatrix(secondMatrix, out);
      // Ждем пока пользователь нажмет клавишу
      System.out.println("Нажмите Enter, чтобы продолжить");
      System.in.read();
      // Считываем матрицу сумму от сервера
      try {
        Matrix thirdMatrix = Matrix.readMatrix(in);
        Matrix.printMatrix(thirdMatrix, outputMatrixFile);
      } catch (Exception e) {
        PrintWriter outToResult = new PrintWriter(outputMatrixFile);
        outToResult.println("Невозможно выполнить действие");
        outToResult.flush();
        outToResult.close();
      }
      // Убираемся после себя
      in.close();
      out.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
