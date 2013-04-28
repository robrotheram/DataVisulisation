package cs235a5;

/** @brief Contains methods and varibles to store and access the data
 * the main body of the data is stored in a 2 dimentional datacell array

   
    @author Robert Fletcher
    @file DataSet.java
    @date April 2013
    */
public class DataSet {
   
    /**
     * Creates the 2 dimensional array of DataCells and sets the Width
     * and Height
     * @param int width
     * @param int height
     * @return boolean true if set correctly 
     */
    
    public boolean SetDataSet(int width, int height){
        m_numCols = width;
        m_numRows = height;
        m_dataSet = new DataCell [height][width];
        return true;
    }
    /**
     * Set the Column names in this class
     * @param String[] colNames 
     * @return boolean true
     * 
     */
     
    public boolean SetHeader(String[] colNames ){
        m_Header = colNames;
        return true;
    }
    /**
     * sets in the data array a MS_DataAtributs at position x ,y 
     * @param setDataCell mDA the cell of data
     * @param int x:  Position x in the 2 dimensional array;
     * @param int y:  Position y in the 2 dimensional array;
     * @return Boolean true
     */
    public boolean SetDataCell(DataCell mDA, int x, int y){
        m_dataSet[y][x] = mDA;
        return true;
    }
    /**
     * set the entire dataset
     * @param DataCell[][] the representation of the table data
     * @return Boolean true if run. 
     */
    public boolean SetDataSet(DataCell[][] data){
        m_dataSet = data;
        return true;
    }
    /**
     * Set the absolute Path to the file used to make This DataSet
     * @param filePath
     * @return boolean True if the setFile path; 
     */
    public boolean SetFilePath(String filePath){
        m_filePath = filePath;
        return true;
    }
    
    /**
     * Returns the dataAtribute and position x,y
     * @param int x:  Position x in the 2 dimensional array;
     * @param int y:  Position y in the 2 dimensional array;
     * @return MS_DataSet single cell of data
     */
    
    public DataCell GetCell(int x, int y){
        return m_dataSet[y][x];
    }
    
    /**
     * Outputs the entire dataset
     * @return MS_DataSet[][]  The entire dataset
     */
    public DataCell[][] GetDataSet(){
        return m_dataSet;
    }
    /**
     * Returns an Array representing a row in the Set at position y
     * 
     * @param y
     * @return MS_DataSet[]
     */
    public DataCell[] GetRow(int y){
        return m_dataSet[y];
    }
    
    /**
     * Returns the array of the column names
     * @return String[]
     */
    public String[] GetHeader(){
        return m_Header;
    }
    /**
     * Gets the a single column name at position i in the array
     * @param i
     * @return 
     */
    public String GetAColumnName(int i){
        return m_Header[i];
    }
    /**
     * Returns the number of columns in the dataset
     * @return int
     */
    public int GetNumOfColumns(){
        return m_numCols;
    }
    /**
     * Returns the number of rows that the dataset represents. 
     * @return Int
     */
    public int GetNumOfRows(){
        return m_numRows;
    }
    
    /**
     * Returns the absolute path to the file used to make the DataSet
     * @return String m_filePath
     */
    public String getFilePath(){
        return m_filePath;
    }
    /**
     * A check to see if the dataset is empty
     * @return Boolean True if empty
     */
    public boolean isEmpty(){
        boolean r =true;
        for(int i = 0; i<GetNumOfColumns();i++){
            for(int j = 0; j<GetNumOfRows();j++){
                if(GetCell(i,j)!=null){
                    if(!GetCell(i,j).GetString().equals("")){
                        r = false;
                    }
                }
            }
        }
        return r;
    }
    
    public int getID()
    {
        return m_id;
    }
    
    public DataSet(int i){
        m_id = i;
    }
    /**
     * 
     * 
     * Method to override the object toSrting method and return instead the Chart
     * @return 
     */
    @Override
    public String toString(){
        return m_filePath;
    }
    
    private DataCell[][] m_dataSet;
    private String[] m_Header;
    private int m_numCols, m_numRows;
    private String m_filePath;
    private final int m_id;
   
    
}
