package com.company;

import com.company.blocks.BaseBlock;
import com.company.exceptions.BlocksException;
import com.company.exceptions.WorkflowException;
import com.company.exceptions.DescriptionException;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkflowExecutor {
    private final HashMap<Integer, String> blockDescriptionLines;
    private int[] blocksOrder;
    private static final Logger log = Logger.getLogger(WorkflowExecutor.class.getName());
    private void readBlocksOrder(String currentLine) {
        blocksOrder = Arrays.stream(currentLine.replaceAll(" ", "").split("->"))
                .mapToInt(Integer::parseInt).toArray();
    }
    public WorkflowExecutor(InputStream inputStream) throws WorkflowException {
        log.info("Reading block's description...");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        BlocksDescriptionReader reader = new BlocksDescriptionReader(bufferedReader);
        blockDescriptionLines = reader.getBlocksDescription();
        log.info("Block's description were read successfully!");
        try {
            readBlocksOrder(bufferedReader.readLine());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Can not read block's order.", e);
            throw new DescriptionException("Can not read block's order.", e);
        }
    }
    public void execute() throws WorkflowException {
        log.info("Starting execution...");
        List<String> text = new ArrayList<>();
        for (int i = 0; i < blocksOrder.length; i++) {
            int blocksIndex = blocksOrder[i];
            String currentBlockDescription = blockDescriptionLines.get(blocksIndex);
            if (currentBlockDescription == null) {
                throw new BlocksException("Can not find block with " + blocksIndex + " index");
            }
            String[] blocksNameWithArgs = currentBlockDescription.split(" ");
            String blockName = blocksNameWithArgs[0];
            String[] blockArgs = null;
            if (blocksNameWithArgs.length > 1) {
                blockArgs = Arrays.copyOfRange(blocksNameWithArgs, 1, blocksNameWithArgs.length);
            }
            BaseBlock currentBlock;
            try {
                currentBlock = BlockFactory.getInstance().getBlock(blockName);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Can not find block with index = " + blocksIndex, e);
                throw new BlocksException("Can not find block.", e);
            }
            BlockType blockType = currentBlock.getType();
            if (i == 0) {
                if (blockType != BlockType.Output) {
                    throw new WorkflowException("First block should be Output type");
                }
            } else if (i == blocksOrder.length - 1) {
                if (blockType != BlockType.Input) {
                    throw new WorkflowException("Last block should be Input type");
                }
            } else {
                if (blockType != BlockType.InputOutput) {
                    throw new WorkflowException("The block should be InputOutput type");
                }
            }
            try {
                text = currentBlock.execute(text, blockArgs);
            } catch (WorkflowException e) {
                log.log(Level.SEVERE, "Can not execute block " + currentBlock, e);
                throw e;
            }
            log.info("Block " + currentBlock.getClass().getName() + " was successfully executed.");
        }
    }
}
