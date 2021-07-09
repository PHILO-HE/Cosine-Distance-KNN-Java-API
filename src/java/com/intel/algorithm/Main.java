package com.intel.algorithm;

/**
 * For test use.
 */
public class Main {

    public static void main(String[] args) {
        CosineDistanceKNN.search(6, args[0], args[1]);
        System.out.println("Indices Table: ");
        output(CosineDistanceKNN.getIndicesTable());
        System.out.println("Distances Table: ");
        output(CosineDistanceKNN.getDistancesTable());
    }

    public static <T> void output(Table<T> table) {
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        T[] data = table.getTableData();
        for (int i = 0; i < rowCount; i++) {
            for (int j =0; j < columnCount; j++) {
                int index = i * columnCount + j;
                System.out.print(data[index] + " ");
            }
            System.out.println();
        }
    }
}