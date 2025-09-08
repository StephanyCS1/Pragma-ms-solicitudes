package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.SortSpec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SortSpecTest {

    @Test
    void testSortSpecCreation() {
        SortSpec sortAsc = new SortSpec("name", SortSpec.Direction.ASC);
        SortSpec sortDesc = new SortSpec("date", SortSpec.Direction.DESC);

        assertEquals("name", sortAsc.property());
        assertEquals(SortSpec.Direction.ASC, sortAsc.direction());
        assertEquals("date", sortDesc.property());
        assertEquals(SortSpec.Direction.DESC, sortDesc.direction());
    }

    @Test
    void testSortSpecEquality() {
        SortSpec sort1 = new SortSpec("amount", SortSpec.Direction.ASC);
        SortSpec sort2 = new SortSpec("amount", SortSpec.Direction.ASC);
        SortSpec sort3 = new SortSpec("amount", SortSpec.Direction.DESC);

        assertEquals(sort1, sort2);
        assertEquals(sort1.hashCode(), sort2.hashCode());
        assertNotEquals(sort1, sort3);
    }

    @Test
    void testSortSpecDirectionEnum() {
        assertEquals("ASC", SortSpec.Direction.ASC.name());
        assertEquals("DESC", SortSpec.Direction.DESC.name());
        assertEquals(2, SortSpec.Direction.values().length);
    }
}