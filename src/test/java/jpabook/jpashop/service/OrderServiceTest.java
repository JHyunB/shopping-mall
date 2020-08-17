package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void order() throws Exception{
        //given
        Member member = createMember();

        Item book = createBook();

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, order.getStatus());
        assertEquals(1,order.getOrderItems().size());
        assertEquals(10000 * orderCount, order.getTotalPrice());
        assertEquals(8, book.getStockQuantity());
    }

    @Test
    public void cancel() throws Exception{
        //given
        Member member = createMember();
        Item book = createBook();

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, order.getStatus());
        assertEquals(10,book.getStockQuantity());
    }

    @Test
    public void overStock() throws Exception{
        //given
        Member member = createMember();
        Item book = createBook();

        //when
        int orderCount = 11;

        //then
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(),book.getId(),orderCount));


    }

    private Item createBook() {
        Item book = new Book();
        book.setName("책1");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가","123-123"));
        em.persist(member);
        return member;
    }

}