package dat.daos;

import dat.config.HibernateConfig;
import dat.entities.ExampleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FoobarDAOTest {
    private static FoobarDAO foobarDAO;
    private static EntityManagerFactory emfTest;
    private EntityManager entityManager;

    private ExampleEntity testExampleEntity;
    private Foobar testFoobar;

    @BeforeAll
    void setUpAll() {
        emfTest = HibernateConfig.getEntityManagerFactoryForTest();
        foobarDAO = FoobarDAO.getInstance(emfTest);
    }

    @BeforeEach
    void setUp() {
        entityManager = emfTest.createEntityManager();

        // Initialize ExampleEntity and Foobar for tests
        testExampleEntity = ExampleEntity.builder()
                .name("Test Example")
                .age(30)
                .foobars(new ArrayList<>())
                .build();

        testFoobar = Foobar.builder()
                .name("Test Foobar")
                .foobar("foobar_value")
                .exampleEntity(testExampleEntity)
                .build();

        testExampleEntity.getFoobars().add(testFoobar);
    }

    @AfterEach
    void tearDown() {
        if (entityManager != null) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM Foobar").executeUpdate();
            entityManager.createQuery("DELETE FROM ExampleEntity").executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    @AfterAll
    public static void tearDownAll() {
        if (emfTest != null) {
            emfTest.close();
        }
    }

    @Test
    public void testPersistFoobar() {
        entityManager.getTransaction().begin();
        entityManager.persist(testExampleEntity);
        entityManager.persist(testFoobar);
        entityManager.getTransaction().commit();

        Foobar retrievedFoobar = entityManager.find(Foobar.class, testFoobar.getId());
        assertNotNull(retrievedFoobar);
        assertEquals("Test Foobar", retrievedFoobar.getName());
    }

    @Test
    void create() {
        Foobar foobarEntity = Foobar.builder()
                .name("New Foobar")
                .foobar("new_value")
                .exampleEntity(testExampleEntity)
                .build();

        Foobar createdEntity = foobarDAO.create(foobarEntity);

        assertNotNull(createdEntity);
        assertEquals("New Foobar", createdEntity.getName());
    }

    @Test
    void delete() {
        entityManager.getTransaction().begin();
        entityManager.persist(testExampleEntity);
        entityManager.persist(testFoobar);
        entityManager.getTransaction().commit();

        Foobar deletedEntity = foobarDAO.delete(testFoobar.getId());
        assertNotNull(deletedEntity);

        entityManager.clear();
        entityManager.getTransaction().begin();
        Foobar foundFoobar = entityManager.find(Foobar.class, testFoobar.getId());
        entityManager.getTransaction().commit();
        assertNull(foundFoobar);
    }

    @Test
    void getById() {
        entityManager.getTransaction().begin();
        entityManager.persist(testExampleEntity);
        entityManager.persist(testFoobar);
        entityManager.getTransaction().commit();

        Foobar retrievedEntity = foobarDAO.read(testFoobar.getId());
        assertNotNull(retrievedEntity);
        assertEquals("Test Foobar", retrievedEntity.getName());
    }

    @Test
    void getAll() {
        entityManager.getTransaction().begin();
        entityManager.persist(testExampleEntity);
        entityManager.persist(testFoobar);
        entityManager.getTransaction().commit();

        List<Foobar> allFoobars = foobarDAO.readAll();
        assertFalse(allFoobars.isEmpty());
    }

    @Test
    void update() {
        entityManager.getTransaction().begin();
        entityManager.persist(testExampleEntity);
        entityManager.persist(testFoobar);
        entityManager.getTransaction().commit();

        testFoobar.setName("Updated Foobar");
        testFoobar.setFoobar("updated_value");

        Foobar resultEntity = foobarDAO.update(testFoobar, testFoobar.getId());

        assertNotNull(resultEntity);
        assertEquals("Updated Foobar", resultEntity.getName());
    }
}
