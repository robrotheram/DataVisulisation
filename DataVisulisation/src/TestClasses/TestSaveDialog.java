
package TestClasses;

import TestUI.Test;
import cs235a5.DataCell;
import cs235a5.DataSet;
import cs235a5.SaveDialog;
import cs235a5.TabPanel;

/** @brief This class is will Test all the methods in the SaveDialog class
 
  The Class contains a number of methods to text all the methods in the 
  * SaveDialog class
    @author Robert Fletcher
    @file  TestSaveDialog.java
    @see SaveDialog.java
    @date April 2013
    */

public class TestSaveDialog {
    private final String CLASSNAME = "SaveDialog";
    private DataSet[] db = new DataSet[]{new DataSet(0),new DataSet(1)};
    private SaveDialog m_SD = new SaveDialog(db, new TabPanel());
    private final int w = 4;
    private final int h = 10;
    
    public TestSaveDialog(){
        String[] title  = new String [] {"Title A", "Title B", "Title C", "Title D"};
        db[0].SetDataSet(w, h);
        db[1].SetDataSet(w, h);
        for(int i = 0; i<h;i++){
            for(int j = 0;j<w;j++){
                db[0].SetDataCell(new DataCell(" "), j, i);
                db[1].SetDataCell(new DataCell(" "), j, i);
            }
        }
        db[0].SetHeader(title);
        db[1].SetHeader(title);
        
    }
    /**
     * Method to test the SaveDialog SaveFile() method  and returns a Test object containing the 
     * test tile, Class being tested, method being tested,test description, 
     * Input data expected output, if the test has been run and if the test 
     * is passed.
     * @param boolean if the method is being run
     * @return Test 
     */
    public Test TestSaveFile(boolean run){
            Test theTest = new Test(
                 "Test to Write the save File",//Test Title
                 CLASSNAME,//Class Name
                 "SaveFile",//Method Being Tested
                 "A test to see if the class can save the xml file for the"
                    + " program to rebuild", //Description
                 "", //Input Data
                 "True - meansing that the save the file successful" //Expected output
                 );
            if(run){
                theTest.hasRun();
                if(m_SD.SaveFile(m_SD.getSaveFile())){
                    theTest.setPassed(true);
                }else{
                    theTest.setPassed(false);
                }
            }
            
        return theTest;
        }
        
    /**
     * Main method to run this test 
     * @param String[] the command line arguments
     */
    
    public static void main(String[] args){
        TestSaveDialog TSD = new TestSaveDialog();
            System.out.println("Test SaveFile"+
                TSD.TestSaveFile(true).getResult());
                
        }
    }
    

