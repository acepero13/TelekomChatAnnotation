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
    
    public static void write(String text, File file)
    {
        FileWriter fileWriter = null;
        
        try {
            
            fileWriter = new FileWriter(file, false);
//            fileWriter.write("");
//            fileWriter.close();
            fileWriter.write(text);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
