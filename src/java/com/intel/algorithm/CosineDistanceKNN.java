package com.intel.algorithm;

/**
 * Java API for cosine distance KNN algorithm.
 *
 * Not support multi-thread.
 */
public class CosineDistanceKNN {

  // load generated shared library: libknn.so.
  static {
    System.loadLibrary("knn");
  }

  private static Table<Integer> indicesTable;
  private static Table<Float> distancesTable;

  /**
   * 0 means no issue, -1 means issue occurs.
   *
   * Two csv files under oneDAL project can be used for test:
   * k_nearest_neighbors_train_data.csv
   * k_nearest_neighbors_test_data.csv
   *
   * @param neighborCount neighbor count for KNN algorithm.
   * @param trainDataPath path to train data's csv file.
   * @param queryDataPath path to test data's csv file.
   **/
  public native static int search(int neighborCount,
    String trainDataPath, String queryDataPath);

//      const std::size_t rows_train_count = (argc < 1) ? 1000000 : std::stoi(argv[1]);
//    const std::size_t columns_count = (argc < 2) ? 512 : std::stoi(argv[2]);
//    const std::size_t rows_query_count = (argc < 3) ? 100 : std::stoi(argv[3]);
//    const std::size_t neighbors_count = (argc < 4) ? 10 : std::stoi(argv[4]);
//
//  std::cout << "rows_train_count: " << rows_train_count << " "
//    << "columns_count: " << columns_count << " "
//    << "rows_query_count: " << rows_query_count << " "
//    << "neighbors_count: " << neighbors_count << "\n";

  /**
   * benchmark test. train data and query data is generated randomly inside the
   * c++ program.
   *
   * @param rows_train_count  row count for train data
   * @param columns_count  columns count
   * @param rows_query_count  row count for query data
   * @param neighbors_count  neighbors count in KNN algorithm
   * @return
   */
  public native static int benchmark(int rows_train_count, int columns_count,
    int rows_query_count, int neighbors_count);

  /**
   * Call by native code for init indicesTable.
   * Applicable for small amount of data.
   */
  private static void initIndicesTable(int rowCount, int columnCount) {
    indicesTable = new Table<>(rowCount, columnCount);
  }

  /**
   * Call by native code for init distancesTable.
   * Applicable for small amount of data.
   */
  private static void initDistancesTable(int rowCount, int columnCount) {
    distancesTable = new Table<>(rowCount, columnCount);
  }

  /**
   * Call by native code.
   */
  private static void setIndices(int ind, int indice) {
    indicesTable.setData(ind, indice);
  }

  /**
   * Call by native code.
   */
  private static void setDistances(int ind, float distance) {
    distancesTable.setData(ind, distance);
  }

  /**
   * API exposed to user.
   */
  public static Table<Integer> getIndicesTable() {
    return indicesTable;
  }

  /**
   * API exposed to user.
   */
  public static Table<Float> getDistancesTable() {
    return distancesTable;
  }

}
