package DI_StaticDI;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

import javafx.scene.control.Cell;

import java.util.Scanner;

public class AjustaDados {

    public String GetDados() throws IOException{

        // Cria um objeto FileWriter para o arquivo "data.csv"
        FileWriter fw = new FileWriter("data.csv");
        FileReader fr = new FileReader("data.csv");

        // Cria um objeto PrintWriter para escrever no arquivo
        PrintWriter pw = new PrintWriter(fw);

        //Cabeçalho
        
        pw.write("escreve");
        // Fecha o arquivo
        pw.close();
        fr.close();

        String teste = new String();
        return teste;
    }
    
}
