package muhammad.learning;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class GenerateChart {

    @FXML
    public Button test;
    @FXML
    public GanttChart<Number,String>   mChart;
    @FXML
    NumberAxis xAxis;
    @FXML
    CategoryAxis yAxis;
    MainController mMainController;
    List<MainController.ComboColumn> mComboColumn;
    List<Graph> mGraph= new ArrayList<>();
    private int mClickedRow;
    private long mStartTime;
    private long mEndTime;

    private Stage stage = new Stage();
    public void closeStage()
    {
        if(stage.isShowing())
            stage.close();
    }

    public void setClickedRow(int clickedRow) { this.mClickedRow = clickedRow; }

    public void setMainController(MainController mainController) { this.mMainController = mainController; }

    public void displayChart()
    {
        List<String> columnNames = new ArrayList<>();
        for(int i=0; i<mMainController.getmComboList().size();i++)
        {
            columnNames.add(mMainController.getmComboList().get(i).getColumnName());
        }


        stage.setTitle("Chart Dialog");
        generateChartDate();
        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();
        mChart = new GanttChart(xAxis,yAxis);
        //test.setText("Sadad");
        mChart.setTitle("Starve/Block");
        mChart.setLegendVisible(false);
        
        xAxis.setLabel("Time");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        //xAxis.setTickUnit(500.0);
        //xAxis.setMinorTickCount(1);
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(mStartTime);
        xAxis.setUpperBound(mEndTime);
        long tick = (mEndTime-mStartTime)/10;
        xAxis.setTickUnit(tick);


        yAxis.setLabel("Roller Beds");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(columnNames));
       

        mChart.setBlockHeight( 50);
       /* String machine;

        machine = machines[0];
        XYChart.Series series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data(0, machine, new mainpackage.GanttChart.ExtraData( 1, "status-red")));
        series1.getData().add(new XYChart.Data(1, machine, new mainpackage.GanttChart.ExtraData( 1, "status-green")));
        series1.getData().add(new XYChart.Data(2, machine, new mainpackage.GanttChart.ExtraData( 1, "status-red")));
        series1.getData().add(new XYChart.Data(3, machine, new mainpackage.GanttChart.ExtraData( 1, "status-green")));

        machine = machines[1];
        XYChart.Series series2 = new XYChart.Series();
        series2.getData().add(new XYChart.Data(2, machine, new mainpackage.GanttChart.ExtraData( 1, "status-red")));

        machine = machines[2];
        XYChart.Series series3 = new XYChart.Series();
        series3.getData().add(new XYChart.Data(0, machine, new mainpackage.GanttChart.ExtraData( 1, "status-blue")));
        series3.getData().add(new XYChart.Data(1, machine, new mainpackage.GanttChart.ExtraData( 2, "status-red")));
        series3.getData().add(new XYChart.Data(3, machine, new mainpackage.GanttChart.ExtraData( 1, "status-green")));*/

        // chart.getData().addAll(series1, series2, series3);


        XYChart.Series<Number, String> blockedSeries = new XYChart.Series();
        XYChart.Series<Number, String> starvedSeries = new XYChart.Series();
        blockedSeries.setName("Blocked");
        starvedSeries.setName("Starved");
        for(int i=0; i<mGraph.size(); i++)
        {
            for (int j=0; j<mGraph.get(i).bar.size() ; j++)
            {
                long pos = convertStringDateToLong(mGraph.get(i).bar.get(j).startPos) /*+ mGraph.get(i).bar.get(j).endPos*/;
                long jumps = convertStringDateToLong(mGraph.get(i).bar.get(j).endPos) - pos;
                if(mGraph.get(i).bar.get(j).type == 1){
                    blockedSeries.getData().add(new XYChart.Data(pos, mGraph.get(i).rollerBed, new GanttChart.ExtraData(jumps, "status-red")));
                }
                else
                    starvedSeries.getData().add(new XYChart.Data(pos, mGraph.get(i).rollerBed, new GanttChart.ExtraData(jumps, "status-blue")));

            }
        }


       /* blockedSeries.getNode().getStyleClass().add("status-red");
        starvedSeries.getNode().getStyleClass().add("status-blue");*/
        mChart.getData().addAll(blockedSeries,starvedSeries);
        //mChart.setLegendVisible(true);
        mChart.getStylesheets().add("muhammad/learning/chart_style");



        for (XYChart.Series entry : mChart.getData()) {
            System.out.println("Entered!"+entry.getData().toString());
            Tooltip t = new Tooltip(entry.getData().toString());
            Node node = entry.getNode();
            Tooltip.install(node, t);
        }
        Set<Node> nod = mChart.lookupAll("Blocked");




        Scene scene = null;
        scene = new Scene(mChart, 600, 400);
        //mainpackage.GenerateChart gen = new mainpackage.GenerateChart();
        //gen.displayChart(mainController);

        stage.setScene(scene);
        stage.show();

    }

    public long convertStringDateToLong(String date)
    {
        long dateL =0;
        SimpleDateFormat f = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        try {
            Date d = f.parse(date);
            dateL = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateL;
    }
    private void generateChartDate()
    {
        RowWithSum r = mMainController.getmChartList().get(mClickedRow);
        mComboColumn = mMainController.getmComboList();
        initGraph();
        String firstCell = mMainController.getSheetData().get(r.getStartIndex()).get(0).getDateCellValue().toString();
        String lastCell = mMainController.getSheetData().get(r.getEndIndex()).get(0).getDateCellValue().toString();
        mStartTime = convertStringDateToLong(firstCell);
        mEndTime = convertStringDateToLong(lastCell);

        for(int i = r.getStartIndex(); i<= r.getEndIndex(); i++)
        {
            List<XSSFCell> row = mMainController.getSheetData().get(i);
            for(int j=0; j< mComboColumn.size(); j++)
            {
               XSSFCell cellBl =  row.get(mComboColumn.get(j).getIndexBlocked()+1);
               int valBl = (int)(Float.parseFloat(cellBl.toString()));
               XSSFCell cellSt =  row.get(mComboColumn.get(j).getIndexStarved()+1);
               int valSt = (int)(Float.parseFloat(cellSt.toString()));
               if(valBl != 0 || valSt != 0)
               {
                   if(mGraph.get(j).sum == -1)
                   {
                       mGraph.get(j).graphItem.startPos = mMainController.getSheetData().get(i+1).get(0).getDateCellValue().toString();
                       if(valBl ==1)
                           mGraph.get(j).graphItem.type =1;
                       else
                           mGraph.get(j).graphItem.type =-1;
                   }
                   mGraph.get(j).sum++;
               }
               else if (mGraph.get(j).sum != -1)
               {
                   mGraph.get(j).sum = -1;
                   mGraph.get(j).graphItem.endPos = mMainController.getSheetData().get(i).get(0).getDateCellValue().toString();
                   mGraph.get(j).addBar();
                   mGraph.get(j).graphItem.type =0;
               }

            }
        }
    }
    private  void initGraph()
    {
        for( int j=0; j<mComboColumn.size() ; j++) {
            Graph g = new Graph();
            g.rollerBed = mComboColumn.get(j).getColumnName();
            mGraph.add(g);
        }
    }


    class Graph{
        private String rollerBed;
        private int sum = -1;
        GraphItem graphItem = new GraphItem();
        List<GraphItem> bar = new ArrayList<>();
        public void addBar()
        {
            GraphItem item = new GraphItem();
            item.endPos = graphItem.endPos;
            item.startPos = graphItem.startPos;
            item.type = graphItem.type;
            bar.add(item);
        }

    }
    class GraphItem{
        private String startPos;
        private String endPos;
        private int type = 0;

    }

}
