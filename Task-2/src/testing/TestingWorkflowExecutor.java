package testing;

import org.junit.Test;
import com.company.WorkflowExecutor;
import com.company.exceptions.BlocksException;
import com.company.exceptions.WorkflowException;
import com.company.exceptions.DescriptionException;

import java.io.*;

public class TestingWorkflowExecutor {
    @Test
    public void commonTest() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile workflow.txt
                2 = writefile out_file.txt
                3 = sort
                4 = replace JavaScript ASM
                csed
                1 -> 3 -> 4 -> 2""").getBytes());
        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }

    @Test(expected = BlocksException.class)
    public void sequenceTest1() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile workflow.txt
                2 = writefile out_file.txt
                3 = put
                csed
                1 -> 3 -> 2""").getBytes());
        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }

    @Test(expected = BlocksException.class)
    public void sequenceTest2() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile workflow.txt
                2 = writefile out_file.txt
                3 = sort
                4 = replace JavaScript ASM
                csed
                1 -> 3 -> 4 -> 7 -> 2""").getBytes());

        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }


    @Test(expected = WorkflowException.class)
    public void sequenceTest3() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile workflow.txt
                2 = writefile out_file.txt
                3 = sort
                4 = replace JavaScript ASM
                csed
                1 -> 2 -> 3""").getBytes());
        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();

    }
    
    @Test(expected = DescriptionException.class)
    public void descriptionTest2() throws WorkflowException {
        InputStream testStream = new ByteArrayInputStream(("""
                desc
                1 = readfile workflow.txt
                1 = writefile out_file.txt
                1 = sort
                1 = replace JavaScript ASM
                csed
                1 -> 3 -> 4 -> 2""").getBytes());
        WorkflowExecutor workflow = new WorkflowExecutor(testStream);
        workflow.execute();
    }
}
