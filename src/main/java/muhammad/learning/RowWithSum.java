package muhammad.learning;

import java.util.ArrayList;
import java.util.List;

public class RowWithSum {
    private String StartTime;
    private String EndTime;
    private int startIndex;
    private int endIndex;
    private List<Integer> sumRow;
    private int sum;
    public RowWithSum(){
        StartTime ="";
        EndTime="";
        sum =0;
        startIndex = 0;
        endIndex = 0;
        sumRow = new ArrayList<>();
    }

    public void setEndIndex(int endIndex) { this.endIndex = endIndex; }
    public int getEndIndex() { return endIndex; }
    public void setStartIndex(int startIndex) { this.startIndex = startIndex; }
    public int getStartIndex() { return startIndex; }
    public List<Integer> getSumRow() { return sumRow; }
    public void setSumRow(List<Integer> sumRow) { this.sumRow = sumRow; }
    public String getStartTime() {return StartTime;}
    public void setEndTime(String endTime) {EndTime = endTime;}
    public String getEndTime() {return EndTime;}
    public void setStartTime(String startTime) { StartTime = startTime; }
    public int getSum() { return sum; }
    public void setSum(int sum) { this.sum = sum; }
}
