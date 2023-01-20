package com.bam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FilesMergeMediator {

    private final List<FileLineReader> fileLineReaders;
    private Parameters.Mode mode = Parameters.Mode.ASCENDING;
    private Parameters.DataType dataType = Parameters.DataType.NULL;
    private File outputFile;

    private FilesMergeMediator(List<File> files) throws IOException {
        fileLineReaders = new ArrayList<>();

        if (files == null) {
            throw new NullPointerException("Input files is null");
        }

        for (File file : files) {
            fileLineReaders.add(new FileLineReader(file));
        }
    }


    public void start() throws IOException {
        switch (dataType) {
            case INTEGER -> sortIntegers();
            case STRING -> sortStrings();
        }
    }

    private void sortIntegers() throws IOException {
        FileWriter writer = new FileWriter(outputFile);

        while (fileLineReaders.size() > 0) {

            if(checkFileCorrect()) continue;

            Integer[] integers = new Integer[fileLineReaders.size()];
            for (int i = 0; i < fileLineReaders.size(); i++) {
                FileLineReader fileLineReader = fileLineReaders.get(i);
                integers[i] = Integer.valueOf(fileLineReader.getLine());
            }
            Utils.Element<Integer> element = getElement(integers);
            fileLineReaders.get(element.index()).next();
            writer.append(element.value().toString()).append("\n");

            checkFileLineReader();
        }
        writer.flush();
        writer.close();
    }

    private void sortStrings() throws IOException {
        FileWriter writer = new FileWriter(outputFile);
        while (fileLineReaders.size() > 0) {
            if(checkFileCorrect()) continue;

            String[] strings = new String[fileLineReaders.size()];
            for (int i = 0; i < fileLineReaders.size(); i++) {
                strings[i] = fileLineReaders.get(i).getLine();
            }
            Utils.Element<String> element = getElement(strings);
            fileLineReaders.get(element.index()).next();
            writer.append(element.value()).append("\n");
            checkFileLineReader();
        }
        writer.flush();
        writer.close();
    }

    private boolean checkFileCorrect() {
        List<FileLineReader> copy = new ArrayList<>(fileLineReaders);
        for (FileLineReader reader : copy){
            if (reader.getLine().isEmpty()){
                fileLineReaders.remove(reader);
                System.out.println("\033[0;31m" + "In file " + reader.getName() + " line:" + (reader.getNum() + 1) + " is empty . "
                        + "File processing terminated");
                return true;
            }
            if (checkNotSorted(reader)) {
                fileLineReaders.remove(reader);
                System.out.println("\033[0;31m" + "In file " + reader.getName() + " on " + (reader.getNum() + 1) + " line incorrect sequence. "
                        + "File processing terminated");
                return true;
            }

        }

        return false;
    }

    private <T> boolean checkNotSorted(FileLineReader fileLineReader) {

        if (fileLineReader.getNextLine() == null || fileLineReader.getNextLine().isEmpty()) return false;
        int v;
        if (dataType == Parameters.DataType.STRING){
            v = fileLineReader.getLine().compareTo(fileLineReader.getNextLine());
        }
        else{
            v = Integer.valueOf(fileLineReader.getLine()).compareTo(Integer.valueOf(fileLineReader.getNextLine()));
        }
        return mode == Parameters.Mode.DESCENDING ? v < 0 : v > 0;
    }


    private void checkFileLineReader() {
        fileLineReaders.removeIf(f -> f.getLine() == null);
    }

    private Utils.Element<String> getElement(String[] values) {
        if (mode == Parameters.Mode.DESCENDING) {
            return Utils.max(values, Comparator.reverseOrder());
        }
        return Utils.min(values, String::compareTo);
    }

    private Utils.Element<Integer> getElement(Integer[] values) {
        if (mode == Parameters.Mode.DESCENDING) {
            return Utils.max(values, Comparator.reverseOrder());
        }
        return Utils.min(values, Integer::compareTo);
    }


    public static class Builder {

        private FilesMergeMediator filesMergeMediator;

        public Builder inputs(List<File> files) throws IOException {
            filesMergeMediator = new FilesMergeMediator(files);
            return this;
        }

        public Builder output(File file) {
            filesMergeMediator.outputFile = file;
            return this;
        }

        public Builder setMode(Parameters.Mode mode) {
            filesMergeMediator.mode = mode;
            return this;
        }


        public Builder setDataType(Parameters.DataType dataType) {
            filesMergeMediator.dataType = dataType;
            return this;
        }

        public FilesMergeMediator build() {

            if (filesMergeMediator.dataType == Parameters.DataType.NULL)
                throw new InvalidParameterException("Command not contain data type parameter. Invoke method setDataType() in Builder");

            if (filesMergeMediator.outputFile == null)
                throw new NullPointerException("Output file is null. Invoke method output() in Builder");

            if (filesMergeMediator.fileLineReaders.size() == 0)
                throw new InvalidParameterException("Input files not found. Invoke method inputs() in Builder first");

            removeEmpty();

            return filesMergeMediator;
        }

        private void removeEmpty() {
            for(FileLineReader reader: filesMergeMediator.fileLineReaders){
                if (reader.getLine() == null){
                    filesMergeMediator.fileLineReaders.remove(reader);
                    System.out.println("\033[0;31m" + "File " + reader.getName() + "is empty. "
                            + "File processing terminated");
                }
            }
        }

    }


}
