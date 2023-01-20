package com.bam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length > 0){

            List<String> arguments = new ArrayList<>();
            List<String> files = new ArrayList<>();

            for (String a : args) {
                if (a.startsWith("-")) {
                    arguments.add(a);
                } else {
                    if (!a.equals(""))
                        files.add(a);
                }
            }

            Parameters parameters = new Parameters();
            parameters.parse(arguments);

            FilesMergeMediator filesMergeMediator = new FilesMergeMediator.Builder()
                    .inputs(files.stream().skip(1).map(File::new).collect(Collectors.toList()))
                    .output(new File(files.get(0)))
                    .setDataType(parameters.getDataType())
                    .setMode(parameters.getMode())
                    .build();


                filesMergeMediator.start();

        }

    }
}
