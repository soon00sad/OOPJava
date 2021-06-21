package com.company.blocks;

import com.company.BlockType;
import com.company.exceptions.WorkflowException;

import java.io.*;
import java.util.List;

public class WriteFileBlock implements BaseBlock {
    @Override
    public List<String> execute(List<String> text, String[] args) throws WorkflowException {
        if (args == null || args.length < 1) {
            throw new WorkflowException("Not enough arguments for the command");
        }
        try (FileWriter fileWriter = new FileWriter(args[0])) {
            for (String line : text) {
                fileWriter.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new WorkflowException("Can not write in file", e);
        }
        return null;
    }
    @Override
    public BlockType getType() {
        return BlockType.Input;
    }
}
