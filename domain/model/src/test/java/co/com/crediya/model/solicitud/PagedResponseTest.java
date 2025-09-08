package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.PagedResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagedResponseTest {

    @Test
    void testPagedResponseCreation() {
        List<String> content = List.of("item1", "item2", "item3");
        PagedResponse<String> response = new PagedResponse<>(
                content, 0, 10, 25L, 3, false
        );

        assertEquals(content, response.content());
        assertEquals(0, response.page());
        assertEquals(10, response.size());
        assertEquals(25L, response.totalElements());
        assertEquals(3, response.totalPages());
        assertFalse(response.last());
    }

    @Test
    void testPagedResponseOfMethod() {
        List<String> content = List.of("A", "B", "C");
        PagedResponse<String> response = PagedResponse.of(content, 0, 5, 12L);

        assertEquals(content, response.content());
        assertEquals(0, response.page());
        assertEquals(5, response.size());
        assertEquals(12L, response.totalElements());
        assertEquals(3, response.totalPages());
        assertFalse(response.last());
    }

    @Test
    void testPagedResponseLastPage() {
        List<Integer> content = List.of(10, 11);
        PagedResponse<Integer> response = PagedResponse.of(content, 2, 5, 12L);

        assertEquals(2, response.page());
        assertEquals(3, response.totalPages());
        assertTrue(response.last());
    }

    @Test
    void testPagedResponseEmptyContent() {
        List<String> emptyContent = List.of();
        PagedResponse<String> response = PagedResponse.of(emptyContent, 0, 10, 0L);

        assertTrue(response.content().isEmpty());
        assertEquals(0, response.page());
        assertEquals(10, response.size());
        assertEquals(0L, response.totalElements());
        assertEquals(0, response.totalPages());
        assertTrue(response.last());
    }

    @Test
    void testPagedResponseSinglePage() {
        List<String> content = List.of("only");
        PagedResponse<String> response = PagedResponse.of(content, 0, 10, 1L);

        assertEquals(1, response.content().size());
        assertEquals(0, response.page());
        assertEquals(1, response.totalPages());
        assertTrue(response.last());
    }
}