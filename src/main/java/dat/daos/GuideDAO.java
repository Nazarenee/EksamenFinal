package dat.daos;

import dat.dtos.GuideDTO;
import dat.entities.Guide;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class GuideDAO implements IDAO<GuideDTO, Long> {
    private static GuideDAO dao;
    private static EntityManagerFactory emf;

    public static GuideDAO getInstance(EntityManagerFactory _emf) {
        if (dao == null) {
            emf = _emf;
            dao = new GuideDAO();
        }
        return dao;
    }

@Override
    public GuideDTO create(GuideDTO guideDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Guide guide = new Guide(guideDTO);
            em.persist(guide);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
        return guideDTO;
    }

    public Guide findById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Guide.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public GuideDTO delete(Long id) {
        return null;
    }

    @Override
    public GuideDTO read(Long id) {
        return null;
    }

    @Override
    public List<GuideDTO> readAll() {
        return null;
    }

    @Override
    public GuideDTO update(GuideDTO guideDTO, Long id) {
        return null;
    }


}
