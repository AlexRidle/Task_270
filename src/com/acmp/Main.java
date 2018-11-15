package com.acmp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static String variable;
    private static String convertedVariable = "Error!";

    public static void main(String[] args){
        try {
            readFile();
            convertVariable();
            writeFile();
        } catch (IOException | TaskException e) {
            e.printStackTrace();
        }
    }

    private static void readFile() throws IOException, TaskException {
        File file = new File("INPUT.txt");
        Scanner inputFile = new Scanner(file);
        variable = inputFile.nextLine();
        if (variable.length() > 100) {
            throw new TaskException("Please, check INPUT.txt file. Variable length should be less than 100 characters.");
        }
    }

    private static void convertVariable(){
        String[] patterns = new String[]{"\\b([a-z])+(_([a-z])+)+\\b", "\\b([a-z])+([a-zA-Z])+\\b", "\\b([a-z])+\\b"};
        Matcher wordMatcher;
        for(int i = 0; i < patterns.length; i++){
            wordMatcher = Pattern.compile(patterns[i]).matcher(variable);
            if(wordMatcher.find()){
                setConvertedVariable(i);
            }
        }
    }

    private static void setConvertedVariable(int i){
        StringBuilder tempVariable = new StringBuilder();
        Matcher converterMatcher;
        int last = 0;
        switch(i){
            case 0: //C++
                converterMatcher = Pattern.compile("_[a-z]").matcher(variable);
                while(converterMatcher.find()){
                    tempVariable
                            .append(variable, last, converterMatcher.start())
                            .append(converterMatcher.group(0).toUpperCase());
                    last = converterMatcher.end();
                }
                tempVariable.append(variable.substring(last));
                convertedVariable = tempVariable.toString().replaceAll("_", "");
                break;
            case 1: //Java
                convertedVariable = variable;
                convertedVariable = convertedVariable.replaceAll("[A-Z]","_$0");
                convertedVariable = convertedVariable.toLowerCase();
                break;
            case 2: //good for both types
                convertedVariable = variable;
                break;
        }
    }

    private static void writeFile() {
        File file = new File("OUTPUT.txt");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
            writer.print(convertedVariable);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }
}

class TaskException extends Exception {

    public TaskException(String message) {
        super(message);
    }

}