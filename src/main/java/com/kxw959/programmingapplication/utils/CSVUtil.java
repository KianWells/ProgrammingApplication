package com.kxw959.programmingapplication.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CSVUtil {
    public void addTextToCSV(String[][] questions, String fileName) throws IOException {
        FileWriter out = new FileWriter(fileName);
        try(CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)){
            for(int i = 0; i < questions.length; i++){
                printer.printRecord(Arrays.asList(questions[i]));
            }
            printer.close(true);
        }
    }
}
