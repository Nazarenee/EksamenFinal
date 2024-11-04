package dat.daos;

import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TripDAO implements IDAO<TripDTO, Long>, iTripGuideDAO {
    private static TripDAO dao;
    private static EntityManagerFactory emf;

    public static TripDAO getInstance(EntityManagerFactory _emf) {
        if (dao == null) {
            emf = _emf;
            dao = new TripDAO();
        }
        return dao;
    }

    @Override
    public TripDTO create(TripDTO exampleEntity) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = new Trip(exampleEntity);
            em.getTransaction().begin();
            em.persist(trip);
            em.getTransaction().commit();
            return exampleEntity;
        }
    }

    @Override
    public TripDTO delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip exampleEntity = em.find(Trip.class, id);
            if (exampleEntity == null) {
                throw new EntityNotFoundException("Trip with ID " + id + " not found.");
            }
            em.remove(exampleEntity);
            em.getTransaction().commit();
            return new TripDTO(exampleEntity);
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    @Override
    public TripDTO read(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new EntityNotFoundException("Trip with ID " + id + " not found.");
            }
            return new TripDTO(trip);
        } finally {
            em.close();
        }
    }


    @Override
    public List<TripDTO> readAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trip> query = em.createQuery("SELECT d FROM Trip d", Trip.class);
            List<Trip> doctors = query.getResultList();

            List<TripDTO> tripDTOS = doctors.stream()
                    .map(TripDTO::new)
                    .collect(Collectors.toList());

            return tripDTOS;
        } finally {
            em.close();
        }
    }

    @Override
    public TripDTO update(TripDTO tripDTO, Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Trip trip = em.find(Trip.class, id);
            if (trip != null) {
                trip.setName(tripDTO.getName());
                trip.setPrice(tripDTO.getPrice());
                trip.setEndTime(tripDTO.getEndTime());
                trip.setStartTime(tripDTO.getStartTime());
                trip.setStartLocation(tripDTO.getStartLocation());
                em.merge(trip);
                em.getTransaction().commit();
                return tripDTO;
            }
            return null;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean validatePrimaryKey(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Trip.class, id) != null;
        }
    }

    @Override
    public void addGuideToTrip(int tripId, int guideId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Trip trip = em.find(Trip.class, tripId);
            if (trip == null) {
                throw new IllegalArgumentException("Trip not found with ID: " + tripId);
            }

            Guide guide = em.find(Guide.class, guideId);
            if (guide == null) {
                throw new IllegalArgumentException("Guide not found with ID: " + guideId);
            }

            trip.setGuide(guide);

            em.merge(trip);
            em.getTransaction().commit();
        } catch (Exception e) {
            // Log the exception message and stack trace for debugging
            System.err.println("Error adding guide to trip: " + e.getMessage());
            e.printStackTrace();  // This prints the stack trace to the console
            throw e;  // Rethrow to handle in the controller
        }
    }



    @Override
    public Set<TripDTO> getTripsByGuide(int guideId) {
        try (EntityManager em = emf.createEntityManager()) {
            Guide guide = em.find(Guide.class, guideId);
            if (guide == null) {
                throw new IllegalArgumentException("Guide not found with ID: " + guideId);
            }

            // Convert the list of trips to DTOs
            return guide.getTrips().stream()
                    .map(TripDTO::new)
                    .collect(Collectors.toSet());
        }
    }


    public List<TripDTO> filterTripByCategory(String category) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t WHERE t.category = :category", Trip.class);

            // Convert category string to enum
            Category categoryEnum;
            try {
                categoryEnum = Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Handle invalid category
                throw new RuntimeException("Invalid category: " + category, e);
            }

            query.setParameter("category", categoryEnum);
            List<Trip> trips = query.getResultList();

            return trips.stream()
                    .map(TripDTO::new)
                    .collect(Collectors.toList());
        }
    }



}
