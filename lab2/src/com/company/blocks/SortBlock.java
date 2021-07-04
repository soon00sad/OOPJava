package com.company.blocks;

import com.company.BlockType;
import com.company.exceptions.WorkflowException;

import java.util.List;
import java.util.stream.Collectors;

public class SortBlock implements BaseBlock {
    @Override
    public List<String> execute(List<String> text, String[] args) throws WorkflowException {
        if (text == null) {
            return null;
        }

        return text.stream().sorted(String::compareTo).collect(Collectors.toList());
    }

    @Override
    public BlockType getType() {
        return BlockType.InputOutput;
    }
}
