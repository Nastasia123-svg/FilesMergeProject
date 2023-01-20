package com.bam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class FileLineReader implements Iterator<String> {

    private final BufferedReader bufferedReader;
    private String line, nextLine;
    private int num = 0;
    private String name;

    public FileLineReader(File file) throws IOException {
        bufferedReader = new BufferedReader(new FileReader(file));
        name = file.getName();
        line = bufferedReader.readLine();
        nextLine = bufferedReader.readLine();
        num++;
    }


    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public String next() {
        try {
            line = nextLine;
            nextLine = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        num++;
        return line;
    }

    public String getLine() {
        return line;
    }

    public String getNextLine() {
        return nextLine;
    }

    public int getNum() {
        return num;
    }

    public String getName() {
        return name;
    }
}
