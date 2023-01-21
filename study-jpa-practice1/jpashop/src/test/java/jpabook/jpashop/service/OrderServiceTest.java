package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        // 상품 주문시 상태는 ORDER
        assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        // 주문한 상품 종류 수가 정확해야 한다.
        assertEquals(1, getOrder.getOrderItems().size());
        // 주문 가격은 가격 * 수량이다.
        assertEquals(10000 * orderCount, getOrder.getTotalPrice());
        // 주문 수량만큼 재고가 줄어야 한다.
        assertEquals(8, book.getStockQuantity());
    }

   

    @Test()
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);

        int orderCount = 11;

        //when
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        assertThrows(NotEnoughStockException.class, () -> {

        });

    }
    
    @Test
    public void 주문취소() throws Exception {
        //given
        
        //when
        
        //then
    }
    
    @Test
    public void 상품주문_재고수량초과() throws Exception {
        //given
        
        //when
        
        //then
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-456"));
        em.persist(member);
        return member;
    }
}