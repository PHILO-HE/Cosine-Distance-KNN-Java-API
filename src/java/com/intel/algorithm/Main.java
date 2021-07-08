package com.intel.algorithm;

public class Main {

    public static void main(String[] args) {
        CosineSimilarityKNN.calculate(6, args[0], args[1]);
        System.out.println("Indices Table: ");
        output(CosineSimilarityKNN.getIndicesTable());
        System.out.println("Distances Table: ");
        output(CosineSimilarityKNN.getDistancesTable());
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