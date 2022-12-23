package hellojpa;

import org.hibernate.Hibernate;

import javax.lang.model.SourceVersion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    private static List<Address> addressHistory;

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("homeCity", "street1", "10000"));

            member.getFavoritefoods().add("치킨");
            member.getFavoritefoods().add("족발");
            member.getFavoritefoods().add("피자");

            member.getAddressHistory().add(new AddressEntity("old1", "street1", "10000"));
            member.getAddressHistory().add(new AddressEntity("old2", "street1", "10000"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("========START========");
            Member findMember = em.find(Member.class, member.getId());

//            // 치킨 -> 한식
//            findMember.getFavoritefoods().remove("치킨");
//            findMember.getFavoritefoods().add("한식");
//
//            findMember.getAddressHistory().remove(new AddressEntity("old1", "street1", "10000"));
//            findMember.getAddressHistory().add(new AddressEntity("newCity1", "street1", "10000"));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}

