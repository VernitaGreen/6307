import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

/**
 * Класс описывает матрицы с вещественными значениями.
 *
 * @see #sum(Matrix, Matrix) позволяет складывать две матрицы одинаковой размерности
 * @see #readMatrix(File) позволяет читать матрицу из файла
 * //@see #printMatrix(Matrix, File) позволяет читать матрицу из файла
 */
public class Matrix implements Serializable {

  // Значения матрицы
  private double matrix[][];
  // Размерность матрицы
  private final int rowCount, columnCount;

  /**
   * Создает нулевую матрицу заданной размерности.
   *
   * @param rowCount    количество строк.
   * @param columnCount количество столбцов.
   */
  public Matrix(int rowCount, int columnCount) {
    // Проверка валидности на размерность матрицы
    if (rowCount <= 0 || columnCount <= 0) {
      throw new IllegalArgumentException("Некорректная размерность матрицы");
    }
    // Инициализация размерности матрицы
    this.rowCount = rowCount;
    this.columnCount = columnCount;
  }

  /**
   * Создает объект по значениям двумерного массива.
   *
   * @param matrix двумерный массив значений.
   */
  public Matrix(double[][] matrix) {
    // Проверка валидности матрицы
    checkMatrix(matrix);
    // Инициализация размерности матрицы
    this.rowCount = matrix.length;
    this.columnCount = matrix[0].length;
    // Инициализация значений матрицы
    this.matrix = copyMatrix(matrix);
  }

  /**
   * Создает объект-копию.
   *
   * @param matrix матрица.
   */
  public Matrix(Matrix matrix) {
    // Инициализация размерности матрицы
    this.rowCount = matrix.rowCount;
    this.columnCount = matrix.columnCount;
    // Инициализация значений матрицы
    this.matrix = copyMatrix(matrix.matrix);
  }

  /**
   * Проверка валидности размера матрицы.
   *
   * @param matrix матрица.
   */
  private void checkMatrix(double[][] matrix) {
    // Проверка, что матрица не нулевого размера
    if (matrix.length == 0 || matrix[0].length == 0) {
      throw new IllegalArgumentException("Некорректная матрица");
    }
  }

  /**
   * Глубокое копирование двумерного массива.
   *
   * @param matrix копируемый двумерный массив.
   * @return новый двумерный массив, который является копией заданного.
   */
  private double[][] copyMatrix(double[][] matrix) {
    // Размерность матрицы
    int rowCount = matrix.length;
    int columnCount = matrix[0].length;
    // Глубокое копирование матрицы
    double[][] result = new double[rowCount][columnCount];
    for (int row = 0; row < rowCount; row++) {
      System.arraycopy(matrix[row], 0, result[row], 0, columnCount);
    }
    return result;
  }

  /**
   * @return Количество строк в матрице
   */
  public int getRowCount() {
    return rowCount;
  }

  /**
   * @return Количество столбцов в матрице
   */
  public int getColumnCount() {
    return columnCount;
  }

  /**
   * Возвращает элемент на заданной позиции.
   *
   * @param rowIndex    индекс строки i
   * @param columnIndex индекс столбца j
   * @return Элемент, стоящий на позиции i, j
   */
  public double getElement(int rowIndex, int columnIndex) {
    // Проверка валидности заданных индексов
    checkIndex(rowIndex, columnIndex);
    return matrix[rowIndex][columnIndex];
  }

  /**
   * Заменяет элемент заданным на заданной позиции.
   *
   * @param rowIndex    индекс строки i
   * @param columnIndex индекс столбца j
   * @param element     значение элемента
   */
  public void setElement(int rowIndex, int columnIndex, double element) {
    // Проверка валидности заданных индексов
    checkIndex(rowIndex, columnIndex);
    // Обновление значения элемента
    matrix[rowIndex][columnIndex] = element;
  }

  /**
   * Проверяет валидность индексов.
   *
   * @param rowIndex    индекс строки
   * @param columnIndex индекс столбца
   */
  private void checkIndex(int rowIndex, int columnIndex) {
    // Проверка, что заданные индексы находяся в пределах нашей размерности
    if (rowIndex < 0 || rowIndex >= rowCount ||
        columnIndex < 0 || columnIndex >= columnCount) {
      throw new IllegalArgumentException("Некорректные индексы элемента");
    }
  }

  /**
   * Складывает две матрицы одинаковой размерности.
   *
   * @param firstMatrix  первая матрица.
   * @param secondMatrix вторая матрица.
   * @return матрица, элементы которой являются суммой соответствующих элементов
   * в двух исходных матрицах.
   */
  public static Matrix sum(Matrix firstMatrix, Matrix secondMatrix) {
    // Проверка, что заданные матрицы одинаковой размерности
    if (firstMatrix.rowCount != secondMatrix.rowCount ||
        firstMatrix.columnCount != secondMatrix.columnCount) {
      throw new IllegalArgumentException("Размерность матриц не совпадает");
    }
    // Создание новой матрицы
    int rowCount = firstMatrix.rowCount;
    int columnCount = firstMatrix.columnCount;
    double[][] matrix = new double[rowCount][columnCount];
    // Сложение заданных матриц в новую матрицу
    for (int row = 0; row < rowCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        matrix[row][column] = firstMatrix.matrix[row][column] + secondMatrix.matrix[row][column];
      }
    }
    return new Matrix(matrix);
  }

  /**
   * Читает матрицу из заданного файла.
   *
   * @param file файл, из которого происходит чтение матрицы.
   * @return матрица, которая находится в файле.
   */
  public static Matrix readMatrix(File file) throws FileNotFoundException {
    // Создание потока ввода, чтение матрицы из потока
    return readMatrix(new Scanner(file));
  }

  /**
   * Читает матрицу из заданного потока ввода.
   *
   * @param in поток ввода, из которого происходит чтение матрицы.
   * @return матрица, которая находится в потоке ввода.
   */
  public static Matrix readMatrix(Scanner in) throws NumberFormatException {
    // Создание нового объекта
    int rowCount = Integer.parseInt(in.next());
    int columnCount = in.nextInt();
    double[][] matrix = new double[rowCount][columnCount];
    // Считывание матрицы из заданного файла, запись в новый объект
    for (int row = 0; row < rowCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        matrix[row][column] = in.nextDouble();
      }
    }
    return new Matrix(matrix);
  }

  /**
   * Записывает заданную матрицу в указанный файл.
   *
   * @param matrix матрица, которую нужно записать в файл.
   * @param file   файл, в который нужно записать матрицу.
   */
  public static void printMatrix(Matrix matrix, File file) throws FileNotFoundException {
    // Создание потока вывода, вывод матрицы в поток
    printMatrix(matrix, new PrintWriter(file));
  }

  /**
   * Записывает заданную матрицу в указанный поток вывода.
   *
   * @param matrix матрица, которую нужно записать в файл.
   * @param out    поток вывода, в который нужно записать матрицу.
   */
  public static void printMatrix(Matrix matrix, PrintWriter out) {
    // Вывод размерности матрицы
    int rowCount = matrix.rowCount;
    int columnCount = matrix.columnCount;
    out.println(rowCount + " " + columnCount);
    // Вывод значений матрицы
    for (int row = 0; row < rowCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        out.print(matrix.matrix[row][column] + " ");
      }
      out.println();
    }
    out.flush();
  }

  /**
   * Записывает матрицу в формате: количество_строк количество_столбцов двумерный_массив_значений.
   *
   * @return строка, являющаяся описанием матрицы.
   */
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(rowCount).append(" ").append(columnCount).append("\n");
    for (int row = 0; row < rowCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        result.append(matrix[row][column]).append(" ");
      }
      result.append("\n");
    }
    return result.toString();
  }
}
