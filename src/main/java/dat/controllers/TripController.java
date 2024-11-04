package dat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.constants.Message;
import dat.daos.TripDAO;
import dat.dtos.PackingItemDTO;
import dat.dtos.TripDTO;
import dat.entities.PackingItemsResponse;
import dat.entities.Trip;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class TripController {
    private static TripController instance;
    private static TripDAO dao;
    private static final String PACKING_API_URL = "https://packingapi.cphbusinessapps.dk/packinglist/";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;


    public TripController() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
            TripDTO doctor = dao.read((long) id);
            ctx.json(doctor);
        }catch (Exception e){
            ctx.json(new Message(HttpStatus.NOT_FOUND.getCode(), e.getMessage()));
        }
    }

    public void create(Context ctx) {
        try {
            Trip trip = ctx.bodyAsClass(Trip.class);
            TripDTO createdTrip = dao.create(new TripDTO(trip));
            ctx.json(createdTrip);
        } catch (Exception e) {
            ctx.json(new Message(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
        }
    }


    public void update(Context ctx){
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO doctor = ctx.bodyAsClass(TripDTO.class);
            TripDTO updatedDoctor = dao.update(doctor, (long) id);
            ctx.json(updatedDoctor);
        }catch (Exception e){
            ctx.json(new Message(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
        }
    }

    public void delete(Context ctx){
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.delete((long) id);
            ctx.json(new Message(HttpStatus.OK.getCode(), "Resource deleted."));
        }catch (Exception e){
            ctx.json(new Message(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
        }
    }

    public void addGuideToTrip(Context ctx) {
        int tripId = Integer.parseInt(ctx.pathParam("tripId"));
        int guideId = Integer.parseInt(ctx.pathParam("guideId"));

        try {
            dao.addGuideToTrip(tripId, guideId);
            ctx.status(200).result("Guide added to trip successfully.");
        } catch (IllegalArgumentException e) {
            ctx.status(404).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result("An error occurred while adding the guide to the trip: " + e.getMessage());
            e.printStackTrace();
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
            context.status(500).json( "Internal server error.");
        }
    }

    public void getAllPackingItems(Context context) {
        try {
            System.out.println("Fetching packing items...");
            List<PackingItemDTO> packingItems = fetchAllPackingItems();
            context.json(packingItems);
        } catch (IOException | InterruptedException e) {
            context.status(500).json("Error fetching packing items.");
            e.printStackTrace();
        }
    }

    private List<PackingItemDTO> fetchAllPackingItems() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create("https://packingapi.cphbusinessapps.dk/packinglist/beach"))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response body: " + response.body());

        System.out.println("Fetching all packing items.");
        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch packing items, status code: " + response.statusCode());
        }

        PackingItemsResponse itemsResponse = objectMapper.readValue(response.body(), PackingItemsResponse.class);
        return itemsResponse.getItems();
    }


}
