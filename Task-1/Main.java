package com.company;


public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.print("Write at least two arguments.");
        } else {
            MakeCSVFile CSVFile = new MakeCSVFile();
            CSVFile.readFile("input.txt");
            CSVFile.makeCSVFile("output.csv");
        }
    }
}
