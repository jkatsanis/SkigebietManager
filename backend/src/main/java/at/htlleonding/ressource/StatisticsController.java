package at.htlleonding.ressource;

import at.htlleonding.repositories.BenutzerRepository;
import at.htlleonding.repositories.SkiliftRepository;
import at.htlleonding.repositories.TicketRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/statistics")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class StatisticsController {

    @Inject
    BenutzerRepository benutzerRepository;

    @Inject
    SkiliftRepository skiLiftRepository;

    @Inject
    TicketRepository ticketRepository;

    // Benutzer Statistiken
    @GET
    @Path("/users/ticket-count")
    public Response getTicketCountPerUser() {
        List<Object[]> results = benutzerRepository.getTicketCountPerUser();
        List<Map<String, Object>> formattedResults = results.stream()
            .map(row -> Map.of(
                "userId", row[0],
                "userName", row[1],
                "ticketCount", row[2]
            ))
            .collect(Collectors.toList());
        return Response.ok(formattedResults).build();
    }



    // Ticket Statistiken
    @GET
    @Path("/tickets/revenue-by-type")
    public Response getTotalRevenueByTicketType() {
        List<Object[]> results = ticketRepository.getTotalRevenueByTicketType();
        List<Map<String, Object>> formattedResults = results.stream()
            .map(row -> Map.of(
                "ticketType", row[0],
                "totalRevenue", row[1]
            ))
            .collect(Collectors.toList());
        return Response.ok(formattedResults).build();
    }

    @GET
    @Path("/tickets/sales-by-month")
    public Response getTicketSalesByMonth() {
        List<Object[]> results = ticketRepository.getTicketSalesByMonth();
        List<Map<String, Object>> formattedResults = results.stream()
            .map(row -> Map.of(
                "month", row[0],
                "ticketCount", row[1]
            ))
            .collect(Collectors.toList());
        return Response.ok(formattedResults).build();
    }

    // Gesamtübersicht
    @GET
    @Path("/overview")
    public Response getOverview() {
        Map<String, Object> overview = Map.of(
            "ticketCountPerUser", benutzerRepository.getTicketCountPerUser(),
            "totalRevenueByTicketType", ticketRepository.getTotalRevenueByTicketType()
        );
        return Response.ok(overview).build();
    }
} 