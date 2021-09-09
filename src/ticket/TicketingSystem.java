package ticket;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.*;

public class TicketingSystem{
    enum Type{
     SUPERVISOR,
     EMPLOYEE;
    }

    static int idCounter=1;
   static List<Ticket> ticketList=new LinkedList<>();
    static List<Ticket> openTicketList=new LinkedList<>();
    static List<Ticket> closedTicketList=new LinkedList<>();
    static List<Ticket> resolvedTicket=new LinkedList<>();

static    Map<String,Type> nameToType=new HashMap<>();

   static Map<String,Ticket> assigneeToTickets =new HashMap<>();


    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        TicketingSystem.idCounter = idCounter;
    }

    public static List<Ticket> getTicketList() {
        return ticketList;
    }

    public static void setTicketList(List<Ticket> ticketList) {
        TicketingSystem.ticketList = ticketList;
    }

    public static Map<String, Ticket> getAssigneeToTickets() {
        return assigneeToTickets;
    }

    public static void setAssigneeToTickets(Map<String, Ticket> assigneeToTickets) {
        TicketingSystem.assigneeToTickets = assigneeToTickets;
    }

    public static void main(String[] args) {


        nameToType.put("Harry",Type.SUPERVISOR);
        nameToType.put("Amit",Type.SUPERVISOR);
        nameToType.put("Ankur",Type.EMPLOYEE);
        nameToType.put("Tom",Type.EMPLOYEE);

        Scanner sc=new Scanner(System.in);

        while (true) {
            String input = sc.nextLine();
            String[] commands = input.split(" ");
            String output = CommandFactory.processCommand(commands);
            if ("".equals(output)) {
                break;
            } else {
                System.out.println(output);
                continue;
            }
        }


    }
}
