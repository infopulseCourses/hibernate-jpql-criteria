import entity.Bank;
import entity.Bank_;
import entity.Client;
import entity.Client_;
import entity.Result;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
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

        entityManager2.getTransaction().commit();

        entityManager2.getTransaction().begin();

        //JPQL
       /* List<Bank> banks = entityManager2.createQuery("select distinct bnk from entity.Bank bnk join bnk.clients " +
                            "cls where cls.firstName = :name", Bank.class).setParameter("name","Client20").getResultList();

        banks.forEach(bank -> System.out.println(bank.getName()));*/

        // Criteria
        CriteriaBuilder builder = entityManager2.getCriteriaBuilder();

        CriteriaQuery<Result> criteriaQuery = builder.createQuery(Result.class);

        Root<Bank> rootBnk = criteriaQuery.from(Bank.class);

        Join<Bank,Client> join = rootBnk.join(Bank_.clients);
        Expression<Long> bankExpression = builder.count(join.get(Client_.id));

        criteriaQuery.multiselect(bankExpression,rootBnk.get(Bank_.name))
                .groupBy(rootBnk.get(Bank_.name))
                .orderBy(builder.desc(bankExpression));

        List<Result> bankList = entityManager2.createQuery(criteriaQuery).getResultList();

       /*select count(client2_.id) as col_0_0_, bank0_.name as col_1_0_
         from banks bank0_ inner join banks_clients clients1_ on bank0_.id=clients1_.banks_id
         inner join clients client2_ on clients1_.clients_id=client2_.id group by bank0_.name
         order by count(client2_.id) desc*/

        bankList.forEach(bank -> System.out.println(bank.getName() + " = " + bank.getCount()));



      /*  CriteriaQuery<Bank> criteriaQuery = builder.createQuery(Bank.class);
        Root<Bank> rootBnk = criteriaQuery.from(Bank.class);
        Join<Bank,Client> join =  rootBnk.join(Bank_.clients);

        ParameterExpression<String> nameExpression = builder.parameter(String.class);
        criteriaQuery.where(builder.equal(join.get(Client_.firstName),nameExpression));
        List<Bank> bankList = entityManager2.createQuery(criteriaQuery)
                .setParameter(nameExpression,"Client20")
                .getResultList();

        bankList.forEach(bank -> System.out.println(bank.getName()));*/

       /* CriteriaBuilder builder = entityManager2.getCriteriaBuilder();

        CriteriaQuery<Client> criteriaQuery = builder.createQuery(Client.class);
        Root<Client> root = criteriaQuery.from(Client.class);

        ParameterExpression<Long> count = builder.parameter(Long.class);
        criteriaQuery.where(builder.equal(root.get(Client_.count),count));

        List<Client> clientsCriteria = entityManager2.createQuery(criteriaQuery).setParameter(count,26l).getResultList();

        clientsCriteria.forEach(client -> System.out.println(client.getTotalSum()));*/

        entityManager2.getTransaction().commit();

        entityManager2.close();
        sessionFactory.close();
    }
}
