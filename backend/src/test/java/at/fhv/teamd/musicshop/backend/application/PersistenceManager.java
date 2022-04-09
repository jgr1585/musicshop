package at.fhv.teamd.musicshop.backend.application;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManager {
    private static EntityManagerFactory emf;

    private PersistenceManager() {}

    public static EntityManager getEntityManagerInstance() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("Test");
        }

        return emf.createEntityManager();
    }
}
