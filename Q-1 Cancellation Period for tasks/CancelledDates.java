import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Comparing implements Comparator<Plan>{
    public int compare(Plan interval1, Plan interval2){
        if ((interval1.getStartDate()).isBefore(interval2.getStartDate()))
            return -1;
        if((interval1.getStartDate()).isAfter(interval2.getStartDate()))
            return 1;
        return 0;
    }
}

public class CancelledDates {

    public static HashMap<Integer,ArrayList<Plan>> map = new HashMap<>();

    public static List<Plan> getCancelledPeriodsForTask(List<Plan> oldPlanList, List<Plan> newPlanList){
        Stack<Plan> stack = new Stack<>();
        int oldTasks = oldPlanList.size(), newTasks = newPlanList.size();
        for(int i=0;i<oldTasks;i++){
            ArrayList<Plan> arrayList = map.get(oldPlanList.get(i).getTaskId());
            if(arrayList==null)
                stack.push(oldPlanList.get(i));
            else if(arrayList.size()==1) {
                if(newPlanList.get(i).getStartDate().isBefore(oldPlanList.get(i).getStartDate()) &&
                        newPlanList.get(i).getEndDate().isAfter(oldPlanList.get(i).getEndDate()))
                    continue;
                else if ((oldPlanList.get(i).getStartDate().isAfter(newPlanList.get(i).getStartDate()) &&
                        oldPlanList.get(i).getStartDate().isAfter(newPlanList.get(i).getEndDate())) ||
                        (oldPlanList.get(i).getEndDate().isBefore(newPlanList.get(i).getStartDate()) &&
                                oldPlanList.get(i).getEndDate().isBefore(newPlanList.get(i).getEndDate()))) {
                    stack.push(oldPlanList.get(i));
                }
                else if (oldPlanList.get(i).getStartDate().isBefore(newPlanList.get(i).getStartDate()) &&
                        oldPlanList.get(i).getEndDate().isAfter(newPlanList.get(i).getEndDate())) {
                    Plan plan1 = new Plan();
                    plan1.setTaskId(oldPlanList.get(i).getTaskId());
                    plan1.setStartDate(oldPlanList.get(i).getStartDate());
                    plan1.setEndDate(newPlanList.get(i).getStartDate().minusDays(1));
                    stack.push(plan1);
                    Plan plan2 = new Plan();
                    plan2.setTaskId(oldPlanList.get(i).getTaskId());
                    plan2.setStartDate(newPlanList.get(i).getEndDate().plusDays(1));
                    plan2.setEndDate(oldPlanList.get(i).getEndDate());
                    stack.push(plan2);
                } else if (oldPlanList.get(i).getStartDate().isAfter(newPlanList.get(i).getStartDate()) &&
                        oldPlanList.get(i).getEndDate().isAfter(newPlanList.get(i).getEndDate())) {
                    Plan plan = new Plan();
                    plan.setTaskId(oldPlanList.get(i).getTaskId());
                    plan.setStartDate(newPlanList.get(i).getEndDate().plusDays(1));
                    plan.setEndDate(oldPlanList.get(i).getEndDate());
                    stack.push(plan);
                } else if (oldPlanList.get(i).getEndDate().isAfter(newPlanList.get(i).getStartDate()) &&
                        oldPlanList.get(i).getEndDate().isBefore(newPlanList.get(i).getEndDate())) {
                    Plan plan = new Plan();
                    plan.setTaskId(oldPlanList.get(i).getTaskId());
                    plan.setStartDate(oldPlanList.get(i).getStartDate());
                    plan.setEndDate(newPlanList.get(i).getStartDate().minusDays(1));
                    stack.push(plan);
                }
            }
            else{
                Collections.sort(arrayList,new Comparing());
                for (int j=0;j<=arrayList.size();j++){
                    Plan plan = new Plan();
                    plan.setTaskId(oldPlanList.get(i).getTaskId());
                    if(j==0)
                        plan.setStartDate(oldPlanList.get(i).getStartDate());
                    else
                        plan.setStartDate(arrayList.get(j-1).getEndDate().plusDays(1));
                    if(j==arrayList.size())
                        plan.setEndDate(oldPlanList.get(i).getEndDate());
                    else
                        plan.setEndDate(arrayList.get(j).getStartDate().minusDays(1));
                    stack.push(plan);
                }
            }
        }
        return new ArrayList<>(stack);

    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        System.out.println("Enter the number of tasks:");
        int tasks = Integer.parseInt(scanner.nextLine());
        List<Plan> oldPlanList = new ArrayList<>();
        List<Plan> newPlanList = new ArrayList<>();
        System.out.println("Enter the old plan:");
        for (int i=0;i<tasks;i++){
            String[] str = scanner.nextLine().split(" ");
            Plan plan = new Plan();
            plan.setTaskId(Integer.parseInt(str[0]));
            plan.setStartDate(LocalDate.parse(str[1].trim(),formatter));
            plan.setEndDate(LocalDate.parse(str[2].trim(),formatter));
            oldPlanList.add(plan);
        }
        System.out.println("Enter the number of new tasks:");
        int newTasks = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter the new plan:");
        for (int i=0;i<newTasks;i++){
            String[] str = scanner.nextLine().split(" ");
            Plan plan = new Plan();
            plan.setTaskId(Integer.parseInt(str[0]));
            plan.setStartDate(LocalDate.parse(str[1].trim(),formatter));
            plan.setEndDate(LocalDate.parse(str[2].trim(),formatter));
            ArrayList<Plan> al = new ArrayList<>();
            if(map.get(Integer.parseInt(str[0]))!=null){
                al = map.get(Integer.parseInt(str[0]));
            }
            al.add(plan);
            newPlanList.add(plan);
            map.put(Integer.parseInt(str[0]),al);
        }

        List<Plan> finalPlan = getCancelledPeriodsForTask(oldPlanList,newPlanList);

        System.out.println();
        System.out.println("Cancellation period for tasks");
        for(Plan x: finalPlan){
            System.out.print(x.getTaskId()+" ");
            System.out.print(x.getStartDate().getDayOfMonth()+"-"+x.getStartDate().getMonth()+"-"+x.getStartDate().getYear()+" ");
            System.out.println(x.getEndDate().getDayOfMonth()+"-"+x.getEndDate().getMonth()+"-"+x.getEndDate().getYear());
        }

    }
}
