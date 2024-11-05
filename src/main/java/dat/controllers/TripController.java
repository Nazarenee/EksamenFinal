package dat.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.constants.Message;
import dat.constants.Message2;
import dat.daos.TripDAO;
import dat.dtos.TripDTO;
import dat.entities.Trip;
import dat.services.PackingService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.net.http.HttpClient;
import java.util.List;

public class TripController {
    private static TripController instance;
    private static TripDAO dao;
    private static final String PACKING_API_URL = "https://packingapi.cphbusinessapps.dk/packinglist/";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static PackingService packingService = new PackingService();


    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static TripController getInstance() {
        if (instance == null) {
            instance = new TripController();
            EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
            dao = TripDAO.getInstance(emf);
        }
        return instance;
    }

    public void readAll(Context ctx) {
        List<TripDTO> foobarEntities = dao.readAll();

        if (foobarEntities.isEmpty()) {
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(new Message(HttpStatus.NOT_FOUND.getCode(), "No resources found."));
        } else {
            ctx.status(HttpStatus.OK).json(foobarEntities);
        }
    }


    public void read(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO trip = dao.read((long) id);

            String category = String.valueOf(trip.getCategory()).toLowerCase();
            String packingListResponse = packingService.fetchPackingList(category);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            JsonNode packingListNode = objectMapper.readTree(packingListResponse);

            ObjectNode combinedResponse = objectMapper.createObjectNode();
            combinedResponse.set("trip", objectMapper.valueToTree(trip));
            combinedResponse.set("packingList", packingListNode);

            ctx.json(combinedResponse);
        } catch (Exception e) {
            ctx.json(new Message(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
        }
    }


    public void create(Context ctx) {
        try {
            TripDTO trip = ctx.bodyAsClass(TripDTO.class);
            TripDTO createdTrip = dao.createNoGuide((trip));
            ctx.json(createdTrip);
        } catch (Exception e) {
            ctx.json(new Message(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
        }
    }


    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO doctor = ctx.bodyAsClass(TripDTO.class);
            TripDTO updatedTrip = dao.update(doctor, (long) id);
            ctx.json(updatedTrip);
        } catch (Exception e) {
            ctx.json(new Message(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
        }
    }

    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.delete((long) id);
            ctx.json(new Message(HttpStatus.OK.getCode(), "Resource deleted."));
        } catch (Exception e) {
            ctx.json(new Message(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
        }
    }

    public void populate(Context context) {
        try {
            Populate.populate();
            context.status(200).result("Database populated successfully.");
        } catch (Exception e) {
            context.status(500).result("An error occurred while populating the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void filterTripByCategory(Context context) {
        String category = context.pathParam("category");
        try {
            List<TripDTO> trips = dao.filterTripByCategory(category);
            context.json(trips);
        } catch (RuntimeException e) {
            context.status(400).json(e.getMessage());
        } catch (Exception e) {
            context.status(500).json("Internal server error.");
        }
    }

    public void PackingListByCategory(Context context) {
        String category = context.pathParam("category");
        String responseBody = packingService.fetchPackingList(category);
        context.json(responseBody);
    }


    public void addGuideToTrip(Context ctx) {
        try {
            long tripId = Long.parseLong(ctx.pathParam("tripId"));
            long guideId = Long.parseLong(ctx.pathParam("guideId"));

            TripDTO updatedTrip = dao.addGuideToTrip(tripId, guideId);
            ctx.json(updatedTrip);
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(new Message(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
        }
    }


    public void getPackingListWeight(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO trip = dao.read((long) id);

            if (trip == null) {
                ctx.json(new Message(HttpStatus.NOT_FOUND.getCode(), "Trip not found."));
                return;
            }

            String category = String.valueOf(trip.getCategory()).toLowerCase();
            double totalWeight = packingService.calculatePackingListWeight(category);

            ctx.json(new Message2(HttpStatus.OK.getCode(), "Total weight calculated.", totalWeight));
        } catch (Exception e) {
            ctx.json(new Message(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
        }
    }

}


