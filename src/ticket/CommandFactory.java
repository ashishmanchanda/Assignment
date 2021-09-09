package ticket;

import java.util.*;

public class CommandFactory {

    final static String CREATE_TICKET            = "create-ticket";
    final static String ASSIGN_TICKET            = "assign-ticket";
    final static String RESOLVE_TICKET           = "resolve-ticket";
    final static String VERIFY_TICKET_RESOLUTION = "verify-ticket-resolution";



    final static String   STATUS ="status";

   static Ticket createTicket(String[] inputs){
           Ticket t = new Ticket();
           t.setId(TicketingSystem.idCounter++);
           t.setType(inputs[1]);
           t.setDescription(inputs[2]);

       System.out.println(inputs[1]+"+input ");
       t.setStatus(("check-wallet-balance".equals(inputs[1]) || "change-language".equals(inputs[1])) ? Ticket.TicketStatus.RESOLVED : Ticket.TicketStatus.OPEN);
       t.setDescription("check-wallet-balance".equals(inputs[1]) ? "sent automatic SMS to customer" : "automatic IVR call made to the customer");

           System.out.println("input is" + inputs[2]);
           return t;

    }

    static String getTicketStatus(String input){
       String result;
        Optional<Ticket> statusTicketOptional = TicketingSystem.ticketList.stream().filter(ticket -> input.equals(String.valueOf(ticket.getId()))).findFirst();
        if (statusTicketOptional.isPresent()) {
            Ticket statusTicket = statusTicketOptional.get();
            result = "Ticket-" + statusTicket.getId() + " status:" + statusTicket.getStatus() + " comment:" + statusTicket.getDescription() + "resolved-by: "
                    + statusTicket.getResolvedBy() + "verified-by:" + statusTicket.getVerifiedBy();
        } else {
            result = "Error,no ticket found";
        }
        return  result;
    }

    static String verifyTicket(String input){
        Map<String, Ticket> verifyticketMap = TicketingSystem.getAssigneeToTickets();
        Ticket ticket=verifyticketMap.get(input);
        if(ticket.getVerifiedBy()==null && ticket.getResolvedBy()!=null){
            ticket.setVerifiedBy(input);
            ticket.setComment(input);
        }
       return  "Ticket-"+ticket.getId()+" verified by "+ticket.getResolvedBy()+" with comment "+ticket.getComment();
    }

    static  String resolveTicket(String[] inputs){
        Map<String, Ticket> ticketMap = TicketingSystem.getAssigneeToTickets();
        Ticket t=ticketMap.get(inputs[1]);
        if(t!=null && t.getResolvedBy()==null){
            t.setResolvedBy(inputs[1]);
            t.setComment(inputs[3]);
            return  "Ticket-"+t.getId()+" resolved by "+t.getResolvedBy()+" with comment "+t.getComment();
        }else {
            return "ticket cant be reolved";
        }

    }

    static  String assignTicket(String input) {
        TicketingSystem.Type type = TicketingSystem.nameToType.get(input);
            if (type !=null) {
                if(type.equals(TicketingSystem.Type.SUPERVISOR)) {
                    Optional<Ticket> ticketToBeAssignedOptional = TicketingSystem.getTicketList().stream().filter(ticket -> ticket.getStatus().equals(Ticket.TicketStatus.RESOLVED)).findFirst();
                    if(ticketToBeAssignedOptional.isPresent()) {
                        Ticket ticketToBeAssigned = ticketToBeAssignedOptional.get();
                        ticketToBeAssigned.setStatus(Ticket.TicketStatus.CLOSED);
                        Map<String, Ticket> ticketMap = TicketingSystem.getAssigneeToTickets();
                        ticketMap.put(input, ticketToBeAssigned);

                        return "Ticket-" + ticketToBeAssigned.getId() + " ->" + input;
                    }else{
                        return "ticket cant be assigned";
                    }
                }else if(type.equals(TicketingSystem.Type.EMPLOYEE)){
                    Optional<Ticket> ticketToBeAssignedOptional = TicketingSystem.getTicketList().stream().filter(ticket -> ticket.getStatus().equals(Ticket.TicketStatus.OPEN)).findFirst();
                    if(ticketToBeAssignedOptional.isPresent()) {
                        Ticket ticketToBeAssigned = ticketToBeAssignedOptional.get();
                        ticketToBeAssigned.setStatus(Ticket.TicketStatus.RESOLVED);
                        Map<String, Ticket> ticketMap = TicketingSystem.getAssigneeToTickets();
                        ticketMap.put(input, ticketToBeAssigned);
                        return "Ticket-" + ticketToBeAssigned.getId() + " ->" + input;
                    }
                }else{
                    return "ticket cant be assigned";
                }

            }
         else {
            return "No open ticket present";
        }
         return "No open ticket presen";
    }

    static String getStatus(String[] inputs){
        if(inputs.length==2) {
            return getTicketStatus(inputs[1]);
        }else if(inputs.length==1) {
            long openTickets = TicketingSystem.getTicketList().stream().filter(ticket -> ticket.getStatus().equals(Ticket.TicketStatus.OPEN)).count();
            long closedTickets = TicketingSystem.getTicketList().stream().filter(ticket -> ticket.getStatus().equals(Ticket.TicketStatus.RESOLVED)).count();
            long assignedTickets = TicketingSystem.getTicketList().stream().filter(ticket -> ticket.getStatus().equals(Ticket.TicketStatus.ASSIGNED)).count();
            long totalTickets = TicketingSystem.getTicketList().size();
            return openTickets+"- OPEN TICKETS"+closedTickets+"- CLOSED TICKETS"+assignedTickets+"- ASSIGNED TICKETS"+totalTickets+"- TOTAL TICKETS";
        }
        return "";
    }
    public static String processCommand(String[] inputs) {
        String result = "";

        switch (inputs[0]) {
            case CREATE_TICKET:
                if (inputs.length == 3) {
                    Ticket t = createTicket(inputs);
                    TicketingSystem.getTicketList().add(t);
                    result = String.valueOf(t.getId());
                }
                break;
            case STATUS:
                result = getStatus(inputs);
                break;
            case ASSIGN_TICKET:
                result = assignTicket(inputs[1]);
                break;
            case RESOLVE_TICKET:
                result = resolveTicket(inputs);
            case VERIFY_TICKET_RESOLUTION:
                result = verifyTicket(inputs[1]);
            default:
        }
        return result;
    }
}
