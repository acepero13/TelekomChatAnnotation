/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.reader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EmpaT
 */
public class Writer {

    private final File file;
    private final FileWriter fileWriter;

    public Writer(File file) throws IOException {
        this.file = file;
        fileWriter = new FileWriter(file, false);
    }
    public  void write(String text)
    {
        try {
            fileWriter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void close(){
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
