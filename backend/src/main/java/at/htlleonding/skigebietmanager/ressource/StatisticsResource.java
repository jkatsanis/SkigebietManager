package at.htlleonding.skigebietmanager.ressource;

import at.htlleonding.skigebietmanager.services.StatisticsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

@Path("/api/statistics")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsResource {
    @Inject
    StatisticsService statisticsService;

    @GET
    public Map<String, Object> getAllStatistics() {
        return statisticsService.getAllStatistics();
    }
} 