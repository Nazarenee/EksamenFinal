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
class ExampleDAOTest {
    private static ExampleDAO exampleDAO;
    private static EntityManagerFactory emfTest;
    private EntityManager entityManager;
    private ExampleEntity testExampleEntity;
    private Foobar testFoobar;

    @BeforeAll
    void setUpAll() {
        emfTest = HibernateConfig.getEntityManagerFactoryForTest();
        exampleDAO = ExampleDAO.getInstance(emfTest);
    }

    @BeforeEach
    void setUp() {
        entityManager = emfTest.createEntityManager();

        // Start transaction for persisting test data
        entityManager.getTransaction().begin();

        // Initialize ExampleEntity and Foobar with proper relationships
        testExampleEntity = ExampleEntity.builder()
                .name("Test Example")
                .age(30)
                .foobars(new ArrayList<>())
                .build();

        testFoobar = Foobar.builder()
                .name("Test Foobar")
                .foobar("foobar_value")
                .exampleEntity(testExampleEntity) // Set ExampleEntity for proper association
                .build();

        // Add Foobar to ExampleEntity list
        testExampleEntity.getFoobars().add(testFoobar);

        // Persist both entities
        entityManager.persist(testExampleEntity);
        entityManager.persist(testFoobar);

        // Commit the transaction
        entityManager.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        if (entityManager != null) {
            entityManager.getTransaction().begin();
            // Delete all Foobar entities to avoid FK constraint issues
            entityManager.createQuery("DELETE FROM Foobar").executeUpdate();
            // Now delete all ExampleEntity entries
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
    public void testPersistExampleEntity() {
        entityManager.getTransaction().begin();
        entityManager.persist(testExampleEntity);
        entityManager.getTransaction().commit();

        ExampleEntity retrievedExample = entityManager.find(ExampleEntity.class, testExampleEntity.getId());
        assertNotNull(retrievedExample);
        assertEquals("Test Example", retrievedExample.getName());
    }

    @Test
    void create() {
        ExampleEntity exampleEntity = ExampleEntity.builder()
                .name("New Example")
                .age(25)
                .foobars(new ArrayList<>())
                .build();
        ExampleEntity createdEntity = exampleDAO.create(exampleEntity);

        assertNotNull(createdEntity);
        assertEquals("New Example", createdEntity.getName());
    }

    @Test
    void delete() {
        entityManager.getTransaction().begin();
        entityManager.persist(testExampleEntity);
        entityManager.getTransaction().commit();

        ExampleEntity deletedEntity = exampleDAO.delete(testExampleEntity.getId());
        assertNotNull(deletedEntity);

        entityManager.clear();

        entityManager.getTransaction().begin();
        ExampleEntity foundEntity = entityManager.find(ExampleEntity.class, testExampleEntity.getId());
        entityManager.getTransaction().commit();
        assertNull(foundEntity);
    }

    @Test
    void getById() {
        entityManager.getTransaction().begin();
        entityManager.persist(testExampleEntity);
        entityManager.getTransaction().commit();

        ExampleEntity retrievedEntity = exampleDAO.read(testExampleEntity.getId());
        assertNotNull(retrievedEntity);
        assertEquals("Test Example", retrievedEntity.getName());
    }

    @Test
    void getAll() {
        entityManager.getTransaction().begin();
        entityManager.persist(testExampleEntity);
        entityManager.getTransaction().commit();

        List<ExampleEntity> allExamples = exampleDAO.readAll();
        assertFalse(allExamples.isEmpty());
    }

    @Test
    void update() {
        entityManager.getTransaction().begin();
        entityManager.persist(testExampleEntity);
        entityManager.getTransaction().commit();

        testExampleEntity.setName("Updated Example");
        testExampleEntity.setAge(35);

        ExampleEntity resultEntity = exampleDAO.update(testExampleEntity, testExampleEntity.getId());

        assertNotNull(resultEntity);
        assertEquals("Updated Example", resultEntity.getName());
    }
}
