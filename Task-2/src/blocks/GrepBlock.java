package com.company.blocks;

import com.company.BlockType;
import com.company.exceptions.WorkflowException;

import java.util.List;
import java.util.ArrayList;

public class GrepBlock implements BaseBlock {
    @Override
    public List<String> execute(List<String> text, String[] args) throws WorkflowException {
        if (args == null || args.length < 1) {
            throw new WorkflowException("Not enough arguments for the command");
        }
        if (text == null) {
            return null;
        }
        List<String> textWithKeyWord = new ArrayList<>();
        String keyWord = args[0];
        for (String line : text) {
            if(line.contains(" " + keyWord + " ")) {
                textWithKeyWord.add(line);
            }
        }
        return textWithKeyWord;
    }
    @Override
    public BlockType getType() {
        return BlockType.InputOutput;
    }
}
