package com.company.blocks;

import com.company.BlockType;
import com.company.exceptions.WorkflowException;

import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class ReadFileBlock implements BaseBlock {
    @Override
    public List<String> execute(List<String> text, String[] args) throws WorkflowException {
        if (args == null || args.length < 1) {
            throw new WorkflowException("Not enough arguments for the command");
        }
        try (FileReader fileReader = new FileReader(args[0]); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String currLine = bufferedReader.readLine();
            while (currLine != null) {
                text.add(currLine);
                currLine = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new WorkflowException("Can not read line", e);
        }
        return text;
    }
    @Override
    public BlockType getType() {
        return BlockType.Output;
    }
}
