package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.GeneralResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GeneralResponseTest {

    @Test
    void testGeneralResponseCreation() {
        String data = "Success data";
        GeneralResponse<String> response = new GeneralResponse<>(200, data, null);

        assertEquals(200, response.status());
        assertEquals(data, response.data());
        assertNull(response.error());
    }

    @Test
    void testGeneralResponseWithError() {
        String errorMessage = "Something went wrong";
        GeneralResponse<Object> response = new GeneralResponse<>(500, null, errorMessage);

        assertEquals(500, response.status());
        assertNull(response.data());
        assertEquals(errorMessage, response.error());
    }

    @Test
    void testGeneralResponseWithComplexData() {
        List<String> dataList = List.of("item1", "item2", "item3");
        GeneralResponse<List<String>> response = new GeneralResponse<>(201, dataList, null);

        assertEquals(201, response.status());
        assertEquals(dataList, response.data());
        assertEquals(3, response.data().size());
        assertNull(response.error());
    }

    @Test
    void testGeneralResponseEquality() {
        GeneralResponse<String> response1 = new GeneralResponse<>(200, "test", null);
        GeneralResponse<String> response2 = new GeneralResponse<>(200, "test", null);

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }
}