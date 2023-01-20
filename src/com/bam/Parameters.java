package com.bam;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Parameters {

    private Mode mode = Mode.ASCENDING;
    private DataType dataType = DataType.NULL;

    public void parse(List<String> args){
        for (String param : args) {
            if(Mode.isContain(param)){
                mode = Mode.fromParameter(param);
            }
            if (DataType.isContain(param)){
                dataType = DataType.fromParameter(param);
            }
        }
    }

    public Mode getMode() {
        return mode;
    }

    public DataType getDataType() {
        return dataType;
    }

    public enum Mode{
        ASCENDING("-a"),
        DESCENDING("-d");

        String parameter;

        Mode(String parameter) {
            this.parameter = parameter;
        }

        public String parameter() {
            return parameter;
        }

        public static boolean isContain(String s){
            return Arrays.stream(values()).map(Mode::parameter).collect(Collectors.toList()).contains(s);
        }

        public static Mode fromParameter(String p){
            Optional<Mode> mode = Arrays.stream(values()).filter(v -> v.parameter.equals(p)).findFirst();
            return mode.orElse(Mode.ASCENDING);
        }

    }

    public enum DataType{
        INTEGER("-i"),
        STRING("-s"),
        NULL("");

        String parameter;

        DataType(String parameter) {
            this.parameter = parameter;
        }

        public String parameter() {
            return parameter;
        }

        public static boolean isContain(String s){
            return Arrays.stream(values()).map(DataType::parameter).collect(Collectors.toList()).contains(s);
        }

        public static DataType fromParameter(String p){
            Optional<DataType> dataType =  Arrays.stream(DataType.values()).filter(d -> d.parameter.equals(p)).findFirst();
            return dataType.orElse(DataType.NULL);
        }
    }
}
