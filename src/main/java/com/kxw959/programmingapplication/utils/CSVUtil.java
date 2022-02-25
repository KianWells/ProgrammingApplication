package com.kxw959.programmingapplication.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
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

    public List<String[]> getTextFromCSV(String path) throws IOException {
        Reader in = new FileReader(path);
        try(CSVReader reader = new CSVReader(in)){
            List<String[]> r = reader.readAll();
            r.forEach(x -> System.out.println(Arrays.toString(x)));
            return r;
        } catch (CsvException e) {
            e.printStackTrace();
            return null;
        }
    }
}
