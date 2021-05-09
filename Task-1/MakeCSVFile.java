package com.company;
import java.io.*;
import java.util.*;

public class MakeCSVFile {

    private int wordsCount = 0;
    private Map<String, Integer> wordsStatistic = new HashMap<String, Integer>();

    StringBuilder getWordFromFile(Reader file) throws IOException {
        StringBuilder fileWord = new StringBuilder();
        int symbol = file.read();
        while (!Character.isLetter((char) symbol)) {
            if (symbol != -1) {
                symbol = file.read();
            } else {
                return fileWord;
            }
        }
        while (Character.isLetter((char) symbol)) {
            fileWord.append((char) symbol);
            symbol = file.read();
        }
        return fileWord;
    }

    public void readFile(String inputFile) {
        Reader reader = null;
        try
        {
            reader = new InputStreamReader(new FileInputStream(inputFile));
            StringBuilder wordFromFile = new StringBuilder();
            while(!(wordFromFile = getWordFromFile(reader)).isEmpty()){
                if (wordsStatistic.containsKey(wordFromFile.toString())) {
                    wordsStatistic.put(wordFromFile.toString(), wordsStatistic.get(wordFromFile.toString()) + 1);
                    ++wordsCount;
                } else {
                    wordsStatistic.put(wordFromFile.toString(), 1);
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        }
        finally
        {
            if (null != reader)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    public void makeCSVFile(String outputFile) {
        Writer writer = null;
        try
        {
            writer = new OutputStreamWriter(new FileOutputStream(outputFile));
            List<Map.Entry<String, Integer>> list = new ArrayList<>(wordsStatistic.entrySet());
            list.sort(Map.Entry.comparingByValue());
            Collections.reverse(list);
            for (Map.Entry<String, Integer> word : list) {
                String statisticLine = word.getKey() + ";    " + word.getValue() + ";    " + (double)word.getValue() / wordsCount * 100 + "%\n";
                writer.write(statisticLine);
            }


        }
        catch (IOException e)
        {
            System.err.println("Error while writing file: " + e.getLocalizedMessage());
        }
        finally
        {
            if (null != writer)
            {
                try
                {
                    writer.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace(System.err);
                }
            }
        }

    }
}
