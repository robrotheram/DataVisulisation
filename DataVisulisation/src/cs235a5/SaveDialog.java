package cs235a5;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/** @brief This class is used to save a file

    This class gets the user to select a file, the program will then write the 
    * file using the data from the tabPannel class
    * classes
    @author Robert Fletcher
    @file SaveDialog.java
    @date April 2013
    */

public class SaveDialog {
 /**
     * Class Constructor 
     * @param DataSet the programs dataSet
     * @param TabPannel The programs TabPannel
     */
    
    public SaveDialog(DataSet[] db, TabPanel tp){
        if(!setDataSet(db)){
            System.out.println("SaveDialog.SetDataSet()-Failed to"
                    + " set the DataSet");
        }
        if(!setTabPannel(tp)){
            System.out.println("SaveDialog.SetTabPannel()-Failed to"
                    + " set the DataSet");
        }
    }
    
  /**
     * Sets the DataSet for the class
     * @param DataSet db
     * @return boolean True if set Correctly
     */
    private boolean setDataSet(DataSet[] db){
        m_db  = db;
        return true;
    }
    /**
     * Sets the Memory reference to the Programs TabPanel
     * @param TabPannel 
     * @return boolean True if set Correctly
     */
    private boolean setTabPannel(TabPanel tp){
        m_tp = tp;
        return true;
    
    }
    
/**
    * Gets the File the user wants to open.
    * @return File - The File the program will read.
    */ 
    
    public File getSaveFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
        "GMS Files Only", "GMS"));
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
         
        }else{
            return null;
        }
    }
    
    /**
     * This method will write the file in a XML format so that the openDialog 
     * class can read it.
     * @return boolean True if written  with no errors
     */
    public boolean SaveFile(File file){
        FileWriter fw = null;
        try {
          
            if(file !=null){
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            bw.write(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Visulisation>\n"
                 
            );
            for(int pos = 0; pos<m_db.length;pos++){
                bw.write("<Data>\n");
                bw.write("<Date>"+getDate()+"</Date>\n");
                bw.write("<File>"+m_db[pos].getFilePath()+"</File>\n");
                bw.write("<RawData>"+writeRawData(pos)+"</RawData>\n");
                bw.write("</Data>\n");
            }
            for(int i =0; i<m_tp.GetNumOfCharts();i++){
                Chart c = m_tp.GetTab(i);
                System.out.print("c=000000    "+c);
                ColourMap cm = c.GetColourMap();
                bw.write("<Chart>\n");
            
                bw.write("<ChartType>"+c.GetChartType().toString()+
                         "</ChartType>\n");
                bw.write("<DataSetID>"+c.GetData().getID()+"</DataSetID>\n");
                bw.write("<XColumn>"+c.GetXColumnPosition()+"</XColumn>\n");
                bw.write("<YColumn>"+c.GetYColumnPosition()+"</YColumn>\n");
                bw.write("<ChartTitle>"+c.GetTitle()+"</ChartTitle>\n");
                bw.write("<Author>"+c.GetAuthor()+"</Author>\n");
                bw.write("<Desc>"+c.GetDescription()+"</Desc>\n");
                bw.write("<Schemme>\n");
                for(int j =0; j<cm.getNumberOfColours();j++){
                    Color cl = cm.getColour(j);
                    System.err.println("Color = "+cl);
                    String r = Integer.toString(cl.getRed());
                    String g = Integer.toString(cl.getGreen());
                    String b = Integer.toString(cl.getBlue());
                    System.err.print("Red = "+r+" | ");
                    System.err.print("Green = "+g+" | ");
                    System.err.println("Blue = "+b);
                    bw.write("<Color>");
                    bw.write(r+",");
                    bw.write(g+",");
                    bw.write(b);
                    bw.write("</Color>\n");
                    
                }
                bw.write("</Schemme>\n");
                bw.write("</Chart>\n");
            }
            bw.write("</Visulisation>");
            bw.close();
            
            
            return true;
            }else{
             System.err.println("DNF");
             return false;
            }
            
            
        } catch (IOException ex) {
            System.err.print(ex);
            return false;
        }
    }
    
    private String getDate(){
         DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	   //get current date time with Date()
	   Date date = new Date();
	   System.out.println(dateFormat.format(date));
 
	   //get current date time with Calendar()
	   Calendar cal = Calendar.getInstance();
	   return dateFormat.format(cal.getTime());
    }
    
    private DataSet[] m_db;
    private TabPanel m_tp;
    private String writeRawData(int pos){
        String out = "";
        for(int i = 0;i<m_db[pos].GetHeader().length-1;i++){
           out+=m_db[pos].GetAColumnName(i)+",";
          
                   
        }
        out+=m_db[pos].GetAColumnName(m_db[pos].GetHeader().length-1)+"\n";
        
        for(int i = 0; i<m_db[pos].GetNumOfRows()-1;i++){
            for(int j = 0; j<m_db[pos].GetNumOfColumns()-1;j++){
                out+=m_db[pos].GetCell(j, i)+",";
            }
            out+=m_db[pos].GetCell(m_db[pos].GetNumOfColumns()-1,i)+"\n";
        }
        return out;
    }
}
