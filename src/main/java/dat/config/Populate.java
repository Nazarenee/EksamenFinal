package dat.config;

import dat.daos.TripDAO;
import dat.dtos.GuideDTO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Populate {

    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            TripDTO trip1 = createTrip1();
            TripDTO trip2 = createTrip2();
            System.out.println("TRIP 1 GUIDE : " + trip1.getGuide());
            System.out.println("TRIP 2 GUIDE : " + trip2.getGuide());
            TripDAO dao = TripDAO.getInstance(emf);
            dao.create(trip1);
            dao.create(trip2);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void populate()
    {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            TripDTO trip1 = createTrip1();
            TripDTO trip2 = createTrip2();
            System.out.println("TRIP 1 GUIDE : " + trip1.getGuide());
            System.out.println("TRIP 2 GUIDE : " + trip2.getGuide());
            TripDAO dao = TripDAO.getInstance(emf);
            dao.create(trip1);
            dao.create(trip2);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    private static TripDTO createTrip1() {
        TripDTO tripDTO = new TripDTO(LocalDateTime.of(2021, 1, 1, 10, 0), LocalDateTime.of(2021, 1, 1, 11, 0), "Beach Resort", "Best trip ever", 2000, Category.BEACH);

        GuideDTO gudieDTO = new GuideDTO("Joh","Doe","JohnDoe@gmail.com", "12345678",10.00);

        tripDTO.setGuide(gudieDTO);
        return tripDTO;
    }

    private static TripDTO createTrip2() {
        TripDTO tripDTO2 = new TripDTO(LocalDateTime.of(2021, 1, 1, 10, 0), LocalDateTime.of(2021, 1, 1, 11, 0), "Copehagen centrym", "Come see the great city of copenhagen!", 2000, Category.CITY);

        GuideDTO gudieDTO2 = new GuideDTO("Lebron","Flames","LebronFlames@gmail.com", "12345678",100.00);

        tripDTO2.setGuide(gudieDTO2);
        return tripDTO2;
    }
}
