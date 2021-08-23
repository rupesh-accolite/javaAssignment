import java.util.*;
class Comparing implements Comparator<DateRange>{
    public int compare(DateRange interval1,DateRange interval2){
        if ((interval1.startDate).before(interval2.startDate))
            return -1;
        if((interval1.startDate).after(interval2.startDate))
            return 1;
        return 0;
    }
}
@Deprecated
public class DateMerger{
    public static List<DateRange> mergeDateRange(List<DateRange> dateRanges){
        Collections.sort(dateRanges,new Comparing());
        Stack<DateRange> stack = new Stack<>();
        for (DateRange x: dateRanges){
            if(stack.empty())
                stack.push(x);
            else{
                if((x.startDate).before(stack.peek().endDate)){
                    if(!(x.endDate).before(stack.peek().endDate)) {
                        DateRange temp = stack.pop();
                        stack.push(new DateRange(temp.startDate, x.endDate));
                    }
                }
                else{
                    stack.push(x);
                }
            }
        }
        return new ArrayList<DateRange>(stack);
    }
    public static String month(int month){
        switch (month){
            case 0:return "JAN"; case 1:return "FEB"; case 2:return "MAR";
            case 3:return "APR"; case 4:return "MAY"; case 5:return "JUN";
            case 6:return "JUL"; case 7:return "AUG"; case 8:return "SEP";
            case 9:return "OCT"; case 10:return "NOV"; case 11:return "DEC";
        }
        return "";
    }

    public static int month(String month){
        switch (month){
            case "JAN":return 0; case "FEB":return 1; case "MAR":return 2;
            case "APR":return 3; case "MAY":return 4; case "JUN":return 5;
            case "JUL":return 6; case "AUG":return 7; case "SEP":return 8;
            case "OCT":return 9; case "NOV":return 10; case "DEC":return 11;
        }
        return -1;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of dates: ");
        int noOfDates = Integer.parseInt(scanner.nextLine());

        List<DateRange> dateRanges = new ArrayList<>();
        System.out.println("Enter the start and end dates: ");
        for(int i=0;i<noOfDates;i++){
            String[] str = scanner.nextLine().split(" ");
            String[] startConvert = str[0].split("-");
            String[] endConvert = str[1].split("-");
            Date start = new Date(Integer.parseInt(startConvert[2]),month(startConvert[1]),Integer.parseInt(startConvert[0]));
            Date end = new Date(Integer.parseInt(endConvert[2]),month(endConvert[1]),Integer.parseInt(endConvert[0]));
            dateRanges.add(new DateRange(start,end));
        }
        List<DateRange> finalDateRanges = mergeDateRange(dateRanges);
        System.out.println();
        System.out.println("Dates after merging:");
        for (DateRange date: finalDateRanges ){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(date.startDate.getDate());
            stringBuilder.append("-");
            stringBuilder.append(month(date.startDate.getMonth()));
            stringBuilder.append("-");
            stringBuilder.append(date.startDate.getYear());
            stringBuilder.append(" ");
            stringBuilder.append(date.endDate.getDate());
            stringBuilder.append("-");
            stringBuilder.append(month(date.endDate.getMonth()));
            stringBuilder.append("-");
            stringBuilder.append(date.endDate.getYear());
            System.out.println(stringBuilder.toString());
        }

    }
}
