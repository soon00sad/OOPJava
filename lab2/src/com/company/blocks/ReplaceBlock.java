package com.company.blocks;

import com.company.BlockType;
import com.company.exceptions.WorkflowException;

import java.util.List;

public class ReplaceBlock implements BaseBlock {
    @Override
    public List<String> execute(List<String> text, String[] args) throws WorkflowException {
        if (args == null || args.length < 2) {
            throw new WorkflowException("Not enough arguments for the command");
        }
        if (text == null) {
            return null;
        }
        String keyWord = args[0];
        String newWord = args[1];
        for (int i = 0; i < text.size(); i++) {
            String line = text.get(i);
            if (line.contains(keyWord)) {
                String newLine = line.replace(keyWord, newWord);
                text.set(i, newLine);
            }
        }
        return text;
    }
    @Override
    public BlockType getType() {
        return BlockType.InputOutput;
    }
}
