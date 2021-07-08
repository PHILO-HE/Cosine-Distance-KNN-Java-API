package com.intel.algorithm;

/**
 * Maintains indices/distances obtained from cosine similarity KNN algorithm in
 * inference.
 */
public class Table<T> {

  private int rowCount;
  private int columnCount;

  private T[] data;

  public Table(int rowCount, int columnCount) {
    this.rowCount = rowCount;
    this.columnCount = columnCount;
    this.data = (T[])new Object[rowCount * columnCount];
  }

  public void setData(int ind, T d) {
    data[ind] = d;
  }
}
