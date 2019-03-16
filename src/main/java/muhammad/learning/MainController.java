package muhammad.learning;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class MainController {
    @FXML
    public Button browse;
    @FXML
    TableView<ObservableList> mTableView;
    private List<String> mColumnNameList;
    private List<ComboColumn> mComboList;
    public List<ComboColumn> getmComboList() { return mComboList; }
    private XSSFWorkbook mWb;
    public XSSFWorkbook getMWb() { return mWb; }
    private List<RowWithSum> mChartList;
    public List<RowWithSum> getmChartList() { return mChartList; }
    public TableView<ObservableList> getTableView() { return mTableView; }
    List<List<XSSFCell>> mSheetData;
    public List<List<XSSFCell>> getSheetData() { return mSheetData; }
    private static boolean mReAsign = true;
    public void onBrowseClicked(ActionEvent e)
    {
        mTableView.getItems().clear();
        mColumnNameList =null;
        mComboList =null;
        mWb = null;
        mChartList = null;
        mSheetData =null;
        
        
        mSheetData = new ArrayList<>();
        mColumnNameList = new ArrayList<>();
        mComboList = new ArrayList<>();
        mChartList = new ArrayList<>();
        /*ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(tableView);*/
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel File");
        fileChooser.getExtensionFilters().addAll(
                /*new FileChooser.ExtensionFilter("XLS", "*.xls"),*/
                new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
        File file = fileChooser.showOpenDialog(browse.getScene().getWindow());
        
        if(file!= null && file.canRead())
        {
            try {
                FileThread thread = new FileThread(file);
                thread.start();
                thread.join();
                if(!thread.isAlive())
                {
                    System.out.println("thread finished");
                    mTableView.getColumns().clear();
                    for(int i=0;i</*mColumnNameList*/mComboList.size();i++) {
                        final int j = i;
                        TableColumn tabCol = new TableColumn();
                        tabCol.setText(/*mColumnNameList*/mComboList.get(i).columnName);
                        tabCol.setPrefWidth(100);
                        tabCol.setResizable(true);
                        tabCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                                return new SimpleStringProperty(param.getValue().get(j).toString());
                            }
                        });
                        /*tabCol.setCellValueFactory(new PropertyValueFactory<>("sumRow"));*/
                        mTableView.getColumns().add(i, tabCol);
                    }
                    final int k= /*mColumnNameList*/mComboList.size();
                    TableColumn startTime = new TableColumn();
                    startTime.setText("Start Time");
                    startTime.setPrefWidth(100);
                    startTime.setResizable(true);
                    startTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(k).toString());
                        }
                    });
                    /*startTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));*/
                    mTableView.getColumns().add(/*mColumnNameList*/mComboList.size(), startTime);
                    final int p = /*mColumnNameList*/mComboList.size()+1;
                    TableColumn endTime = new TableColumn();
                    endTime.setText("End Time");
                    endTime.setPrefWidth(100);
                    endTime.setResizable(true);
                    endTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(p).toString());
                        }
                    });
                    /*tableView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                           int index = observableValue.getValue().intValue();
                           mainpackage.RowWithSum r = mChartList.get(index);

                        }
                    });*/
                    /*endTime.setCellValueFactory(new PropertyValueFactory<>("EndTime"));*/
                    mTableView.getColumns().add(/*mColumnNameList*/ mComboList.size()+1, endTime);
                    mTableView.getItems().addAll(/*data*/newData());
                    mTableView.setRowFactory(tv -> new TableRow<ObservableList>() {
                        @Override
                        protected void updateItem(ObservableList row, boolean empty) {
                            super.updateItem(row, empty);
                            int cols = mTableView.getColumns().size();
                            if (row != null) {
                                long diff = calDateDiff(row.get(cols-2).toString(), row.get(cols-1).toString());
                                if(diff > (10*60))
                                    setStyle("-fx-background-color: rgb(244, 122, 66);");
                                else if(diff>6*60)
                                    setStyle("-fx-background-color: rgb(255, 255, 0);");
                                else if(diff>4*60)
                                    setStyle("-fx-background-color: rgb(0, 187, 255);");
                                else if(diff>2*60)
                                    setStyle("-fx-background-color: rgb(0, 255, 187);");
                                else
                                    setStyle("");
                                
                            }
                            else
                                setStyle("");
                            /*if (row != null && row.get(0).toString().equalsIgnoreCase("-34")) {
                                setStyle("-fx-background-color: yellow;");
                            } else {
                                setStyle("");
                            }*/
                        }
                    });
                    
                    
                }
            }catch (Exception ex)
            {
                System.out.println("Exception" + ex);
            }
        }
    }
    public long calDateDiff(String date1, String date2)
    {
        long diff =0;
        SimpleDateFormat f = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        try {
            Date d1 = f.parse(date1);
            Date d2 = f.parse(date2);
            diff = d2.getTime() - d1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff/1000;
    }
   /* private ObservableList<ObservableList> data()
    {
        ObservableList<ObservableList> rowSet = FXCollections.observableArrayList();
        for (int j=0;j<mChartList.size();j++) {
            ObservableList<String> row = FXCollections.observableArrayList();
            mainpackage.RowWithSum r = mChartList.get(j);
            for (int i = 0; i < r.getSumRow().size(); i++) {
                row.add(Integer.toString(r.getSumRow().get(i)));
            }
            row.add(r.getStartTime());
            row.add(r.getEndTime());
            rowSet.add(row);
        }
        return rowSet;
    }*/
    
    private ObservableList<ObservableList> newData()
    {
        ObservableList<ObservableList> rowSet = FXCollections.observableArrayList();
        for (int j=0;j<mChartList.size();j++) {
            ObservableList<String> row = FXCollections.observableArrayList();
            RowWithSum r = mChartList.get(j);
            /*for (int i = 0; i < r.getSumRow().size(); i++) {
                row.add(Integer.toString(r.getSumRow().get(i)));
            }*/
            for(int i = 0; i<mComboList.size();i++)
            {
                ComboColumn col = mComboList.get(i);
                int value = 0;
                int blockVal = r.getSumRow().get(mComboList.get(i).indexBlocked);
                int starveVal = r.getSumRow().get(mComboList.get(i).indexStarved);
                value = blockVal + starveVal*(-1);
                row.add(Integer.toString(value));
                
            }
            row.add(r.getStartTime());
            row.add(r.getEndTime());
            rowSet.add(row);
        }
        return rowSet;
    }
    
    //thread to load the file
    private class FileThread extends Thread{
        File file;
        public FileThread(File fil) {
            this.file = fil;
        }
        public void run()
        {
            
            try{
                InputStream fis = new FileInputStream(file);
                mWb = new XSSFWorkbook(fis);
                Sheet sheet = mWb.getSheetAt(0);
                Iterator rows = sheet.iterator();
                
                
                
                while (rows.hasNext()) {
                    XSSFRow row = (XSSFRow) rows.next();
                    Iterator cells = row.cellIterator();
                    List<XSSFCell> data = new ArrayList<>();
                    while (cells.hasNext()) {
                        XSSFCell cell = (XSSFCell) cells.next();
                        data.add(cell);
                    }
                    mSheetData.add(data);
                }
            }catch (Exception ex)
            {
                System.out.println("exception" + ex.toString());
            }
            
            showExcelData();
        }
    }
    
    //Itterate through the file and generate data from it.
    private void showExcelData() {
        // Iterates the data and print it out to the console.
        List<Integer> sumRow = new ArrayList<>();
        
        String startTime = "";
        
        boolean isFirstRow = true;
        boolean firstElement = true;
        RowWithSum rowWithSum ;
        int rowNum =0;
        int startInd =0;
        for (List<XSSFCell> data : mSheetData) {
            if(isFirstRow) {
                isFirstRow = false;
                setColumnName(data);
            }
            else
                /*for (int j = 0; j < data.size(); j++)*/ {
                rowWithSum = sum(data, sumRow);
                if(rowWithSum.getSum() != 0 && firstElement)
                {
                    startTime = data.get(0).getDateCellValue().toString();
                    startInd = rowNum;
                    firstElement = false;
                }
                else if(rowWithSum.getSum() == 0 && listSum(sumRow)!=0)
                {
                    firstElement = true;
                    rowWithSum.setStartTime(startTime);
                    rowWithSum.setEndTime( data.get(0).getDateCellValue().toString());
                    rowWithSum.setStartIndex(startInd);
                    rowWithSum.setEndIndex(rowNum);
                    print(rowWithSum);
                    for(int k=0; k<rowWithSum.getSumRow().size();k++) {
                        rowWithSum.getSumRow().set(k,0);
                    }
                }
            }
            rowNum++;
        }
    }
    
    //generate column names from first row.
    public void setColumnName(List<XSSFCell> row) {
        for (int i = 1; i < row.size(); i++) {
            String ColName = row.get(i).toString();
            String[] result = ColName.split("/");
            int length = result.length;
            String name = result[length-3] + "/ "+ result[length-1];
            for (int j=0 ; j<mColumnNameList.size(); j++)
            {
                String str = mColumnNameList.get(j);
                if(str.contains(result[length-3])){
                    ComboColumn col= new ComboColumn();
                    if(result[length-1].equals("Starved")) {
                        col.indexStarved = i-1;
                        col.indexBlocked = j;
                    }
                    else{
                        col.indexStarved = j;
                        col.indexBlocked = i-1;
                    }
                    col.columnName = result[length-3];
                    
                    mComboList.add(col);
                }
            }
            mColumnNameList.add(name);
        }
    }
    
    public class ComboColumn
    {
        private int indexBlocked;
        private int indexStarved;
        private String columnName;
        public String getColumnName() { return columnName; }
        
        public int getIndexBlocked() { return indexBlocked; }
        public int getIndexStarved() { return indexStarved; }
    }
    
    //calculate sum of the row.
    public int listSum(List<Integer> list)
    {
        int s =0;
        for(int i=0; i<list.size(); i++) {
            s+=list.get(i);
        }
        return s;
    }
    
    //calculate and generate required list
    public void print(RowWithSum row){
       /* for(int i=0; i<row.sumRow.size(); i++)
        {
            System.out.print(row.sumRow.get(i)+ "  ");
        }
        System.out.print(row.StartTime +" "+row.EndTime);
        System.out.println("");*/
        
        RowWithSum rr = new RowWithSum();
        rr.setEndTime(row.getEndTime());
        rr.setStartTime(row.getStartTime());
        rr.setSum(row.getSum());
        rr.setSumRow(new ArrayList(row.getSumRow()));
        rr.setEndIndex(row.getEndIndex());
        rr.setStartIndex(row.getStartIndex());
        
        mChartList.add(rr);
       /* testClass cl = new testClass();
        cl.testList = new ArrayList<>(row.getSumRow());
        mTestClassList.add(cl);
        if(mChartList.size() == 228) {
            int i;
            i=0;
        }*/
    }
    
    //gets the row and sum of previous calculated rows and calculate for next itteration
    private RowWithSum sum(List<XSSFCell> row, List<Integer> sumRow)
    {
        int s =0;
        RowWithSum rowWithSum = new RowWithSum();
        int sumRowSize = sumRow.size();
        for(int i=1;i<row.size(); i++) {
            int value = (int)(Float.parseFloat(row.get(i).toString()));
            if(sumRowSize==row.size()-1)
                sumRow.set(i-1 , sumRow.get(i-1)+value);
            else
                sumRow.add(value);
            s += value;
        }
        rowWithSum.setSum(s);
        
        rowWithSum.setSumRow(sumRow);
        return rowWithSum;
    }
    
    
}
