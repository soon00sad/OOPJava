package com.company;

import com.company.exceptions.DescriptionException;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlocksDescriptionReader {
    HashMap<Integer, String> blockDescriptionLines;
    private static final Logger log = Logger.getLogger(WorkflowExecutor.class.getName());
    public BlocksDescriptionReader(BufferedReader inputReader) throws DescriptionException {
        blockDescriptionLines = new HashMap<>();
        try {
            String currentDescriptionLine = inputReader.readLine();
            while (!currentDescriptionLine.startsWith("desc")) {
                currentDescriptionLine = inputReader.readLine();
                if (currentDescriptionLine == null) {
                    throw new DescriptionException("Can not find the beginning of the blocks description.");
                }
            }
            while (true) {
                currentDescriptionLine = inputReader.readLine();
                if (currentDescriptionLine == null) {
                    throw new DescriptionException("Can not find end of the blocks description.");
                }
                if (currentDescriptionLine.equals("csed")) {
                    break;
                }
                String[] indexAndBlock = currentDescriptionLine.split(" = ", 2);
                if (indexAndBlock.length < 2) {
                    throw new DescriptionException("Can not read command from description.");
                }
                Integer blocksIndex = Integer.valueOf(indexAndBlock[0]);
                if (blockDescriptionLines.containsKey(blocksIndex)) {
                    log.log(Level.SEVERE, "Repeated block's index in description: " + currentDescriptionLine
                            + "; similar string: " + blocksIndex + " = " + blockDescriptionLines.get(blocksIndex));
                    throw new DescriptionException("Repeated block's index in description.");
                }
                blockDescriptionLines.put(blocksIndex, indexAndBlock[1]);
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error in block's description reading.", e);
            throw new DescriptionException("Error in block's description reading.", e);
        }
    }
    HashMap<Integer, String> getBlocksDescription() {
        return blockDescriptionLines;
    }
}
