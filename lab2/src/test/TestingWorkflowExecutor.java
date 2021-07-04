package test;

import org.junit.Test;
import com.company.WorkflowExecutor;
import com.company.exceptions.BlocksException;
import com.company.exceptions.WorkflowException;
import com.company.exceptions.DescriptionException;

import java.io.*;

public class TestingWorkflowExecutor {
    @Test
    public void generalTest() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile in.txt
                2 = writefile out.txt
                3 = sort
                10 = replace sun sky
                csed
                1 -> 3 -> 10 -> 3 -> 2""").getBytes());
        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }

    @Test(expected = DescriptionException.class)
    public void descriptionTest1() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile in.txt
                2 = writefile out.txt
                3 = sort""").getBytes());
        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }

    @Test(expected = DescriptionException.class)
    public void descriptionTest2() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile in.txt
                1 = writefile out.txt
                1 = sort
                1 = replace JavaScript ASM
                csed
                1 -> 3 -> 4 -> 2""").getBytes());
        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }

    @Test(expected = WorkflowException.class)
    public void orderTest() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile in.txt
                2 = writefile out.txt
                3 = sort
                4 = replace JavaScript ASM
                csed
                1 -> 2 -> 4""").getBytes());
        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();

    }

    @Test(expected = BlocksException.class)
    public void blocksTest() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile in.txt
                2 = writefile out.txt
                3 = sort
                4 = replace JavaScript ASM
                csed
                1 -> 3 -> 4 -> 5 -> 2""").getBytes());
        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }
}