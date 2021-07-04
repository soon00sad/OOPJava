package com.company;

import com.company.exceptions.WorkflowException;

import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("workflow.txt");
            WorkflowExecutor executor = new WorkflowExecutor(fileInputStream);
            executor.execute();
        } catch (WorkflowException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
