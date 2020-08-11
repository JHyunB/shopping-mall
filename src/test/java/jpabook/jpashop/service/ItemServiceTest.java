package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;

    @Test
    void saveItem() throws Exception{
        Item book = new Book();
        book.setName("java");
        Item album = new Album();
        album.setName("hello");

        itemService.saveItem(book);
        itemService.saveItem(album);

        assertEquals(book,itemRepository.findOne(book.getId()));
        assertEquals(album,itemRepository.findOne(album.getId()));
    }
}