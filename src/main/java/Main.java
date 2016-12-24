import entity.Admin;
import entity.User;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 * @author Stepan.Kachan
 */
public class Main {
    public static void main( String[] args ) {
        SessionFactory sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
        EntityManager entityManager = sessionFactory.createEntityManager();

        entityManager.getTransaction().begin();

        User user = new User();
        Admin admin = new Admin();

        user.setName("User1");
        admin.setName("Admin1");
        admin.setSurname("AdminSurname1");
        
        entityManager.persist(user);
        entityManager.persist(admin);

        entityManager.getTransaction().commit();
        entityManager.close();
        sessionFactory.close();

    }
}
