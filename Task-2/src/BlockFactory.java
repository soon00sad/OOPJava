package com.company;

import com.company.blocks.BaseBlock;
import com.company.exceptions.BlocksException;
import com.company.exceptions.FactoryException;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.reflect.InvocationTargetException;

public class BlockFactory {
    private static final Logger log = Logger.getLogger(BlockFactory.class.getName());
    private final Properties config = new Properties();
    private static volatile BlockFactory factory;
    private BlockFactory() throws IOException, FactoryException {
        String cfgName = "config.cfg";
        var configStream = BlockFactory.class.getClassLoader().getResourceAsStream(cfgName);
        if (configStream == null) {
            throw new FactoryException("Can not find config. " + "It should be named "
                      + cfgName + " and be located in src.");
        }
        config.load(configStream);
    }
    public static BlockFactory getInstance() throws IOException, FactoryException {
        if (factory == null) {
            synchronized (BlockFactory.class) {
                if (factory == null) {
                    factory = new BlockFactory();
                }
            }
        }
        return factory;
    }
    public BaseBlock getBlock(String blockName) throws BlocksException {
        if (!config.containsKey(blockName)) {
            log.severe("Block with name " + blockName + " not found in config");
            throw new BlocksException("Block with name " + blockName + " not found in config");
        }
        BaseBlock newBlock;
        try {
            var blockClass = Class.forName(config.getProperty(blockName));
            newBlock = (BaseBlock) blockClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "Factory can not find class by name", e);
            throw new BlocksException("Factory can not find class by name", e);
        } catch (NoSuchMethodException e) {
            log.log(Level.SEVERE, "Factory can not find constructor in block " + blockName, e);
            throw new BlocksException("Factory can not find constructor in block", e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            log.log(Level.SEVERE, "Factory can not create new instance of " + blockName, e);
            throw new BlocksException("Factory can not create new instance of", e);
        }
        return newBlock;
    }
}
