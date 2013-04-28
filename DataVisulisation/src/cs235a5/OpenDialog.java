
package cs235a5;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** @brief This class is used to open a file

    This class gets the user to select a file, the program will then read it and
    * add the charts to the tabPannel
    * classes
    @author Robert Fletcher
    @file OpenDialog.java
    @date April 2013
    */

public class OpenDialog {
    /**
     * Class Constructor 
     * @param DataSet the programs dataSet
     * @param TabPannel The programs TabPannel
     */
    public OpenDialog(DataSet[] db, TabPanel tp,VisualiserGUI cnt){
        if(!setDataSet(db)){
            System.out.println("SaveDialog.SetDataSet()-Failed to"
                    + " set the DataSet");
        }
        if(!setTabPannel(tp)){
            System.out.println("SaveDialog.SetTabPannel()-Failed to"
                    + " set the DataSet");
        }
        m_cnt = cnt;
    }
    
    /**
     * Sets the DataSet for the class
     * @param DataSet db
     * @return boolean True if set Correctly
     */
    private boolean setDataSet(DataSet[] db){
        m_db = db;
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
    public File getOpenFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
        "XML Files Only", "xml"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
         
        }else{
            return null;
        }
    }
    
   
    /**
     * XML Parser, will read through the XML file and make replace the old 
     * DataSet and fill the tabPanel with new charts
     * @return boolean True if parsed with no errors
     */
    public boolean ReadFile(File file){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            //File file = new File("/Users/Robert/Desktop/save.xml");
            boolean parse = false;
            if(file!=null){
                if (file.exists()) {
                      Document doc = db.parse(file);
                      Element docEle = doc.getDocumentElement();

                      NodeList studentList = docEle.getElementsByTagName("Data");

                      if (studentList != null && studentList.getLength() > 0) {
                          for (int i = 0; i < studentList.getLength(); i++) {
                              Node node = studentList.item(i);
                              if (node.getNodeType() == Node.ELEMENT_NODE) {

                                  Element e = (Element) node;
                                  NodeList nodeList =

                                          e.getElementsByTagName("Date");
                                  System.out.println("Data: " + nodeList.item(0)
                                          .getChildNodes().item(0).getNodeValue());

                                  nodeList = e.getElementsByTagName("RawData");
                                  String rawData = nodeList.item(0).getChildNodes().item(0).getNodeValue();
                                  
                                  CSVReader r = new CSVReader(
                                                                m_db[i], 
                                                                writeCSVFILE(rawData),
                                                                ",");

                                  if(r.ParseFile()){
                                       parse = true;
                                       m_cnt.displayTable();
                                  }
                              }
                          }
                      }
                     
                      if(parse){
                        studentList = docEle.getElementsByTagName("Chart");
                        System.out.println("Total Charts: " 
                                + studentList.getLength());

                        if (studentList != null && studentList.getLength() > 0) {
                            System.err.println("Error 1");
                            for (int i = 0; i < studentList.getLength(); i++) {
                                System.err.println("Error 2");
                                Node node = studentList.item(i);
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                    System.err.println("Error 3");
                                    Element e = (Element) node;
                                    NodeList nodeList = 
                                            e.getElementsByTagName("ChartType");
                                    String type = 
                           nodeList.item(0).getChildNodes().item(0).getNodeValue();
                                    System.out.println(type);
                                    
                                    nodeList = e.getElementsByTagName("DataSetID");
                                    int id =Integer.parseInt(nodeList.item(0).getChildNodes().item(0).getNodeValue());
                                    
                                    
                                    nodeList = e.getElementsByTagName("XColumn");
                                    int x = Integer.parseInt
                         (nodeList.item(0).getChildNodes().item(0).getNodeValue());

                                    nodeList = e.getElementsByTagName("YColumn");
                                    int y = Integer.parseInt
                         (nodeList.item(0).getChildNodes().item(0).getNodeValue());

                                    nodeList = e.getElementsByTagName("ChartTitle");
                                    String title =
                        nodeList.item(0).getChildNodes().item(0).getNodeValue();

                                    nodeList = e.getElementsByTagName("Author");
                                    String author =
                        nodeList.item(0).getChildNodes().item(0).getNodeValue();

                                    nodeList = e.getElementsByTagName("Desc");
                                    String desc =
                        nodeList.item(0).getChildNodes().item(0).getNodeValue();

                                    nodeList = e.getElementsByTagName("Color");
                                    int c = nodeList.getLength();
                                    Color[] clrArr = new Color[c];
                                 for(int j = 0; j <c; j++){
                                    String[] rgbStr =
                nodeList.item(j).getChildNodes().item(0).getNodeValue().split(",");

                                    int red = Integer.parseInt(rgbStr[RED]);
                                    int green = Integer.parseInt(rgbStr[GREEN]);
                                    int blue = Integer.parseInt(rgbStr[BLUE]);
                                    clrArr[j] =  new Color(red,green,blue);

                                }
                                addChart(type,id,x,y,title,author,desc,clrArr);


                                }

                            }
                        }
                       return true; 
                      }
                  }else{
                      System.err.println("FNF: ");
                      return false; 
                  }
            }else{
               System.err.println("FNF: ");
                      return false;  
            }
            } catch (SAXException ex) {
                return false; 
            } catch (IOException ex) {
               return false; 
            } catch (ParserConfigurationException ex) {
                return false; 

            }
  
        return false;
    }
    /**
     * Adds the chart with the parameters given from the file to the TabPannel 
     * @param ChartType Type of Chart
     * @param int x axis column Position from the DataSet
     * @param int y axis column Position from the DataSet
     * @param String Chart Title
     * @param String Chart Author
     * @param String Chart Description
     * @param Color[] Array of Colors used in the ColourMap for the Chart  
     */
    private void addChart(String ct,int id,
            int x, int y,
            String title,
            String author, 
            String desc, 
            Color[] clrs){
        
       if(ct.equals(ChartType.AREACHART.toString())){
           AreaChart c = new AreaChart(
                   m_db[id],
                   x,
                   y,
                   title,
                   m_tp.getBounds(),
                   new ColourMap(clrs),
                   author,
                   desc);
           m_tp.AddTab(title, c);
       }else if(ct.equals(ChartType.BARCHART.toString())){
           ColumnChart c = new ColumnChart(
                   m_db[id],
                   x,
                   y,
                   title,
                   m_tp.getBounds(),
                   new ColourMap(clrs),
                   author,
                   desc);
           m_tp.AddTab(title, c);
      }else if(ct.equals(ChartType.BUBBLECHART.toString())){
           BubbleChart c = new BubbleChart(
                   m_db[id],
                   x,
                   y,
                   title,
                   m_tp.getBounds(),
                   new ColourMap(clrs),
                   author,
                   desc);
           m_tp.AddTab(title, c);
       }else if(ct.equals(ChartType.LINECHART.toString())){
           LineChart c = new LineChart(
                   m_db[id],
                   x,
                   y,
                   title,
                   m_tp.getBounds(),
                   new ColourMap(clrs),
                   author,
                   desc);
           m_tp.AddTab(title, c);
       }else if(ct.equals(ChartType.PIECHART.toString())){
           PieChart c = new PieChart(
                   m_db[id],
                   x,
                   y,
                   title,
                   m_tp.getBounds(),
                   new ColourMap(clrs),
                   author,
                   desc);
           m_tp.AddTab(title, c);
       }else if(ct.equals(ChartType.POLARCHART.toString())){
          PolarPlot c = new PolarPlot(
                   m_db[id],
                   x,
                   y,
                   title,
                   m_tp.getBounds(),
                   new ColourMap(clrs),
                   author,
                   desc);
           m_tp.AddTab(title, c);
       }else if(ct.equals(ChartType.SCATTERPLOTCHART.toString())){
           ScatterPlotChart c = new ScatterPlotChart(
                   m_db[id],
                   x,
                   y,
                   title,
                   m_tp.getBounds(),
                   new ColourMap(clrs),
                   author,
                   desc);
           m_tp.AddTab(title, c);
       }
       
        
        
    }
    
    
    
    
    private DataSet[] m_db;
    private TabPanel m_tp;
    private final int RED = 0;
    private final int GREEN = 1;
    private final int BLUE = 2;
    private VisualiserGUI m_cnt;
    /**
     * Method to get the current time and date and return it as a string
     * @return String  - the current time and Data
     */
     private String getDate(){
               DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
               //get current date time with Date()
               Date date = new Date();
               System.out.println(dateFormat.format(date));
         
               //get current date time with Calendar()
               Calendar cal = Calendar.getInstance();
               return(dateFormat.format(cal.getTime()));
               
           
           
     }
    
    private File writeCSVFILE(String input){ 
        BufferedWriter bw = null;
        try {
            File file = new File(System.getProperty("user.dir")+"/"+getDate()+".csv");
            System.out.print(file.getAbsoluteFile());
            // if file doesnt exists, then create it
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(
                          CloudIO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));

            int read = 0;
            byte[] bytes = new byte[1024];
            bw.write(input);
            
            return file;
        } catch (IOException ex) {
            Logger.getLogger(OpenDialog.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(OpenDialog.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
   

    
}
