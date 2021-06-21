package finder.demo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CustomerNumberFinderTest {

    private final static CustomerNumberFinder finder = new CustomerNumberFinder();
    private List<CustomNumberEntity> goodEntities = new ArrayList<>();
    private List<CustomNumberEntity> badEntities = new ArrayList<>();

    @Test
    public void readFromFile()
    {
        goodEntities = finder.readFromFile("src/test/resources/testResources/customerNumbers.json");
        assertEquals(9, goodEntities.size());
    }

    @Test
    public void readFromFileWrongPath()
    {
        badEntities = finder.readFromFile("src/test/resources/test/customerNumbers.json");
        assertEquals(0, badEntities.size());
    }

    @Test
    public void readFromFileBadContent()
    {
        badEntities = finder.readFromFile("src/test/resources/testResources/wrongJsonContent.json");
        assertEquals(0, badEntities.size());
    }

    @Test
    public void contains()
    {
        goodEntities = finder.readFromFile("src/test/resources/testResources/customerNumbers.json");
        assertTrue(finder.contains(100, goodEntities));
    }

    @Test
    public void containsNot()
    {
        goodEntities = finder.readFromFile("src/test/resources/testResources/customerNumbers.json");
        assertFalse(finder.contains(1, goodEntities));
    }

    @Test
    public void containsEmptyList()
    {
        assertFalse(finder.contains(100, Collections.emptyList()));
    }

    @Test
    public void containsIWrongFormatNumberString()
    {
        badEntities = finder.readFromFile("src/test/resources/testResources/badContent.json");
        assertEquals(1, badEntities.size());
        assertFalse(finder.contains(100, goodEntities));
    }
}
