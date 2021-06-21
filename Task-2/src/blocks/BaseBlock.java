package com.company.blocks;

import com.company.BlockType;
import com.company.exceptions.WorkflowException;

import java.util.List;

public interface BaseBlock {
    List<String> execute(List<String> text, String[] args) throws WorkflowException;
    BlockType getType();
}