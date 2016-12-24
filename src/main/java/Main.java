import entity.Bank;
import entity.Client;
import entity.Client_;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stepan.Kachan
 */
public class Main {
    public static void main( String[] args ) {
        SessionFactory sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("org.hibernate.tutorial.jpa");
        EntityManager entityManager = sessionFactory.createEntityManager();

        entityManager.getTransaction().begin();

        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();

        Bank bank1 = new Bank();
        Bank bank2 = new Bank();

        client1.setFirstName("Client1");
        client2.setFirstName("Client2");
        client3.setFirstName("Client3");

        client1.setLastName("ClientSurname1");
        client2.setLastName("ClientSurname2");
        client3.setLastName("ClientSurname3");

        client1.setPercents(15.4);
        client2.setPercents(10.05);
        client3.setPercents(25.2);

        client1.setDeposit(1500.1);
        client2.setDeposit(100.5);
        client3.setDeposit(500.1);

        List<Client> clientList1 = new ArrayList<>();
        clientList1.add(client1);
        clientList1.add(client2);

        List<Client> clientList2 = new ArrayList<>();
        clientList2.add(client2);
        clientList2.add(client3);

        bank1.setName("Mikhailovskiy");
        bank1.setClients(clientList1);

        bank2.setName("OshadBank");
        bank2.setClients(clientList2);

        entityManager.persist(client1);
        entityManager.persist(client2);
        entityManager.persist(client3);

        entityManager.persist(bank1);
        entityManager.persist(bank2);

        entityManager.getTransaction().commit();
        entityManager.close();

        EntityManager entityManager2 = sessionFactory.createEntityManager();
        entityManager2.getTransaction().begin();

       // CriteriaBuilder builder = entityManager2.getCriteriaBuilder();
       // CriteriaQuery<Client> criteriaQuery = builder.createQuery(Client.class);
     //   criteriaQuery.from(Client.class);
       // criteriaQuery.select(root);
       // List<Client> clients = entityManager2.createQuery(criteriaQuery).getResultList();


       // List<Client> clients = entityManager2.createQuery("from entity.Client",Client.class).getResultList();
      //  clients.forEach(client -> System.out.println(client.getTotalSum()));
        entityManager2.getTransaction().commit();

        entityManager2.getTransaction().begin();

      /*  List<Client> clientsJPQL = entityManager2.createQuery("from entity.Client c where c.id = :id ",Client.class)
                .setParameter("id", 20l)
                .getResultList();
        clientsJPQL.forEach(client -> System.out.println("Total sum = "+client.getTotalSum()));*/


        CriteriaBuilder builder = entityManager2.getCriteriaBuilder();
        CriteriaQuery<Client> criteriaQuery = builder.createQuery(Client.class);
        ParameterExpression<Long> id = builder.parameter(Long.class);
        Root<Client> root = criteriaQuery.from(Client.class);
        criteriaQuery.where(builder.equal(root.get(Client_.id),id));
        List<Client> clientsCriteria = entityManager2.createQuery(criteriaQuery).setParameter(id,26l).getResultList();
        clientsCriteria.forEach(client -> System.out.println(client.getTotalSum()));
        entityManager2.getTransaction().commit();

        entityManager2.close();
        sessionFactory.close();

    }
}
