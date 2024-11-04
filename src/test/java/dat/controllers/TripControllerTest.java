/*package dat.controllers;

import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.Category;
import dat.config.HibernateConfig; // Adjust this import based on your project structure
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class TripControllerTest {

    private static EntityManagerFactory emf;

    public static void main(String[] args) {
        // Initialize the EntityManagerFactory
        emf = HibernateConfig.getEntityManagerFactory();

        // Create test data
        createTestData();

        // Run tests
        testCreateTrip();
        testReadAllTrips();
        testReadTripById();
        testAssignGuideToTrip();
    }

    public static void createTestData() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create Guides
            Guide guide1 = new Guide("John", "Doe", "john.doe@example.com", "123-456-7890", 5.0);
            Guide guide2 = new Guide("Jane", "Smith", "jane.smith@example.com", "987-654-3210", 3.5);
            em.persist(guide1);
            em.persist(guide2);

            // Create Trips
            Trip trip1 = new Trip(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5), "Copenhagen", "Cultural Tour", 200, Category.CULTURAL);
            trip1.setGuide(guide1); // Assign guide to trip
            em.persist(trip1);

            Trip trip2 = new Trip(LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(15), "Oslo", "Nature Adventure", 300, Category.ADVENTURE);
            trip2.setGuide(guide2); // Assign guide to trip
            em.persist(trip2);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions as needed
        }
    }

    public static void testCreateTrip() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create a new Trip
            Trip newTrip = new Trip(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(6), "Barcelona", "City Exploration", 250, Category.CULTURAL);
            em.persist(newTrip); // Persist the trip
            em.getTransaction().commit();

            assert newTrip.getId() != 0 : "Trip was not created successfully!";
            System.out.println("Trip created successfully: " + newTrip.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testReadAllTrips() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Trip> trips = em.createQuery("SELECT t FROM Trip t", Trip.class).getResultList();
            assert trips.size() >= 2 : "Not enough trips in the database!";
            System.out.println("Total trips: " + trips.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testReadTripById() {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, 1L); // Replace with the actual ID
            assert trip != null : "Trip not found!";
            assert "Cultural Tour".equals(trip.getName()) : "Trip name does not match!";
            System.out.println("Trip found: " + trip.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testAssignGuideToTrip() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, 1L); // Replace with the actual ID
            Guide newGuide = new Guide("Alice", "Johnson", "alice.johnson@example.com", "555-123-4567", 4.0);
            em.persist(newGuide); // Persist the new guide
            trip.setGuide(newGuide); // Assign the new guide to the trip
            em.getTransaction().commit();

            assert newGuide.equals(trip.getGuide()) : "Guide assignment failed!";
            System.out.println("Guide assigned to trip: " + trip.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
