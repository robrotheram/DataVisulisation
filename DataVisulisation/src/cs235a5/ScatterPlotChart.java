package cs235a5;

/**
 *
 * \file        ScatterPlotChart.java
 * \author      Xiaoxi Gu(Wyler) & Robert Fletcher
 * 
 * \date        15/04/2013
 *
 * \brief       The Scatter Plot Chart Class generates and displays an
 *              scatter plot chart by inheriting from the Chart class.
 *
 */

// Import JFreeChart and libraries 
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.Rectangle;
import java.awt.Paint;
import java.util.ArrayList;
import org.jfree.util.ShapeUtilities;


public class ScatterPlotChart extends Chart{
 
  private JFreeChart m_chart;
  private final int SCATTERPOINTSIZE = 3;
  
   /**Allows access for setting the dataset
  * Sets the data set to be used to produce a graph
  *
  * @param data dataset to be set
  * @return returns true is set if successful
  */
  @Override
  public boolean SetDataSet(DataSet data){
      super.SetData(data);
      return true;
  }
  
  /**Sets access for the x axis data 
   * Allows access for setting the x axis data to be used for making charts
   *
   * @param xData
   * @return
   */
  @Override
  public boolean SetXData(int xData){
      super.SetXData(xData);
      return true;
  }
  
  /**Sets access for the y axis data
   * Allows access for setting the y axis data to be used for making charts
   *
   * @param xData
   * @return
   */
  @Override
  public boolean SetYData(int yData){
      super.SetYData(yData);
      return true;
  }
  
  /** Sets the title of the Chart
   * Alllows access for setting a title to be used with the chart
   *
   * @param title
   * @return Chart title of the Chart;
   */
  @Override
  public boolean SetChartTitle(String title){
      super.SetChartTitle(title);
      return true;
  }
  
  /**Returns the colourmap selected
   * Sets the colour to be applied to the chart
   *
   * @param colours
   * @return colourmap set of the Chart;
   */
  @Override
  public boolean SetColourMap(ColourMap colours){
      super.SetColourMap(colours);
      return true;
  }
  
  /**Returns an author
   * sets the author's name to be used on the chart
   *
   * @param author
   * @return author of the Chart;
   */
  @Override
  public boolean SetAuthor(String author){
      super.SetAuthor(author);
      return true;
  }
  
  /**sets the description
   * sets a description to inform the user what the chart represents
   *
   * @param description
   * @return description of the Chart class;
   */
  @Override
  public boolean SetDescription(String description){
      super.SetDescription(description);
      return true;
  }
  
  /**Sets the Chart type
   * Sets the type of chart to the constant ScatterPlotChart
   * 
   * @return true of set correctly;
   */
  public boolean SetChartType(){
      return super.SetChartType(ChartType.SCATTERPLOTCHART);
  }
  
  /** Returns the chert type
   * returns what kind of chart is being created
   * 
   * @return the constant SCATTERPLOTCHART
   */
  public ChartType getChartType(){
      return super.GetChartType();
  }
  
  /**Returns the X Axis Column Position
   * Returns the X axis column position of data to be 
   * used on the graph
   * 
   * @return Int X Axis Column Position
   */
  public int GetXData(){
    return super.GetXColumnPosition();
  }

  /**Returns the Y Values for the Column
   * Returns the Y axis column position of data to be 
   * used on the graph
   * 
   * @return m_yValues The Y Axis Data
   */
  public int GetYData(){
    return super.GetYColumnPosition();
  }

  /**Returns the Description for the Chart
   * Returns the description of the what the chart represents
   * 
   * @return m_Description a description of the char thats been generated
   */
  @Override
  public String GetDescription(){
    return super.GetDescription();
  }

  /**Returns the name of the Chart
   * returns the chart title to identify what the chart is called
   * @return m_chartName the name of the chart
   */
  @Override
  public String GetTitle(){
    return super.GetChartTitle();
  }


     /**
     * constructor setting all class variables needed to create a column chart
     * @param ds - the data for the chart
     * @param xColPosition - the column data for the x axis from the dataset
     * @param yColPosition - the column data for the y axis from the dataset
     * @param title - chart title
     * @param rect - to display the chart in the windows native size
     * @param cm - the colour scheme for the chart
     * @param author - sets the author of the chart
     * @param description - gives the chart a description
     */
  public ScatterPlotChart (DataSet ds, int xColPosition, int yColPosition, String title, Rectangle rect, ColourMap cm, String author, String description)
  {
     super(ds, xColPosition, yColPosition, title, rect, cm, author, description,ChartType.SCATTERPLOTCHART);
      
  }
  
   /**
   * Creates a dataset of type XYSeries
   */
   public XYDataset convertDataSet(){
        int size = 0;                                                //Initialises a value for size
        int sum = 0;                                                 //initialises a value for sum
        int pos = 0;                                                 //initialises a value for position
        int j= 0;                                                    //initialises a increment for the for loop 
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries(super.GetTitle());
        
        DataCell preVal = super.GetDataSet().GetCell(0, 0);
        // Creates a new list for the found elements 
        super.m_foundElements = new ArrayList<String>();

        for(int i= 0; i < super.GetDataSet().GetNumOfRows()-1; i++ ){

            if(!super.isUnique(super.GetDataSet().GetCell(super.GetXColumnPosition(), i).toString())){
                for(j = pos; j < super.GetDataSet().GetNumOfRows()-1; j++ ){
                    if(preVal.toString().equals(super.GetDataSet().GetCell(super.GetXColumnPosition(), j).toString())){


                        //Check if datatype is a number
                        if(super.GetDataSet().GetCell(super.GetYColumnPosition(), j).GetDataType()==DataType.INTEGER){
                            sum += super.GetDataSet().GetCell(super.GetYColumnPosition(), j).GetInteger();  
                        }else if(super.GetDataSet().GetCell(super.GetYColumnPosition(), j).GetDataType()==DataType.DOUBLE){
                            sum += super.GetDataSet().GetCell(super.GetYColumnPosition(), j).GetDouble(); 
                        }
                    }
                }
                super.m_foundElements.add(super.GetDataSet().GetCell(super.GetXColumnPosition(), i).toString());

                //Add to chart dataSet
                 series.add(sum,preVal.GetInteger());



                preVal = super.GetDataSet().GetCell(super.GetXColumnPosition(), i++);
                sum=0;//
                pos++;             
                }
            }
        for(j = pos; j < super.GetDataSet().GetNumOfRows()-1; j++ ){
            if(super.m_foundElements.get(super.m_foundElements.size()-1).equals(super.GetDataSet().GetCell(super.GetXColumnPosition(), j).toString())){
                if(super.GetDataSet().GetCell(super.GetXColumnPosition(), j).GetDataType()==DataType.INTEGER){
                    sum += super.GetDataSet().GetCell(super.GetYColumnPosition(), j).GetInteger();  
                }else if(super.GetDataSet().GetCell(super.GetXColumnPosition(), j).GetDataType()==DataType.DOUBLE){
                    sum += super.GetDataSet().GetCell(super.GetYColumnPosition(), j).GetDouble(); 
                }
            }
        }
         series.add(sum,preVal.GetInteger());
        
        
        dataset.addSeries(series);
        return dataset;
    }
   

   /**
   *
   * Create the scatter chart using the ChartFactory built into JFreeChart
   * @return m_chart A chart of type JFreeChart
   */
   @Override                                                 
    public JFreeChart createChart(){
        final JFreeChart CHART = ChartFactory.createScatterPlot(
            super.GetTitle(),                                                            // chart title
            super.GetDataSet().GetAColumnName(super.GetXColumnPosition()),               // domain axis label
            super.GetDataSet().GetAColumnName(super.GetYColumnPosition()),               // range axis label
            convertDataSet(),            
            PlotOrientation.VERTICAL,                                                    // orientation
            true,                                                                        // include legend
            true,                                                                        // tooltips?
            false                                                                        // URLs?
        );
        
        final XYPlot plot = (XYPlot) CHART.getPlot();
        XYLineAndShapeRenderer renderer = new ScatterPlotChart.CustomRenderer(); 
        plot.setRenderer(renderer);
        return CHART;
    }
   
   
   
    /**
    * A renderer specific for this type of chart. Sets the colours that will 
    * be used when displaying the chart.
    */
    class CustomRenderer extends  XYLineAndShapeRenderer{
        private Paint[] colors;
        
        /**
        * The constructor for the custom renderer, which will set the colours
        * of the Area
        */
        public CustomRenderer(){ 
           ColourMap mappedColours = GetColourMap();
           this.setSeriesLinesVisible(0, false);
           this.setSeriesShapesVisible(0, true);
           
           this.setSeriesShape(0,ShapeUtilities.createDiagonalCross(SCATTERPOINTSIZE, 1));
           
           //this.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(0, 0,
               //    SCATTERPOINTSIZE, SCATTERPOINTSIZE));
           this.colors = new Paint[mappedColours.getColourArray().length];
           
           for(int i = 0; i<mappedColours.getColourArray().length;i++){
               this.colors[i] = mappedColours.getColour(i);
           }
        }
        
        /**
        * Accessor method for getting the colours of the area
        * @param row - the identifier of the row it is returning
        * @param column - the identifier of the area it is returning
        * @return the colour of each area 
        */
        @Override
        public Paint getItemPaint(final int row, final int column){
           return (this.colors[column % this.colors.length]); 
        } 
    }
    
   /**
   *  A method to return the chart created by this class so it can be outputted.
   * @return m_chart the chart created by this class
   */
   public JFreeChart GetJChart(){
     return m_chart;
   }
   
     /**
    * Method that calls the superclass Chart.java to get the charts chartPanel
    * @return 
    */
   public ChartPanel GetPanel(){
       return super.GetChartPannel();
   }
}
