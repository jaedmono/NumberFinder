package finder.demo;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class OtherNumberFinderTest {

    private final static NumberFinder testedClass = new OtherNumberFinder();
    private List<CustomNumberEntity> goodEntities;
    private List<CustomNumberEntity> badEntities;


    @Test
    public void readFromFileLoadFileData()
    {
        goodEntities = testedClass.readFromFile("src/test/resources/testResources/customerNumbers.json");
        assertEquals(9, goodEntities.size());
    }

    @Test
    public void readFromFileWrongPath()
    {
        badEntities = testedClass.readFromFile("src/test/resources/test/customerNumbers.json");
        assertEquals(0, badEntities.size());
    }

    @Test
    public void readFromFileBadContent()
    {
        badEntities = testedClass.readFromFile("src/test/resources/testResources/wrongJsonContent.json");
        assertEquals(0, badEntities.size());
    }

    @Test
    public void containsMethodReturnTrue()
    {
        goodEntities = testedClass.readFromFile("src/test/resources/testResources/customerNumbers.json");
        assertTrue(testedClass.contains(100, goodEntities));
    }

    @Test
    public void containsMethodReturnFalse()
    {
        goodEntities = testedClass.readFromFile("src/test/resources/testResources/customerNumbers.json");
        assertFalse(testedClass.contains(1, goodEntities));
    }

    @Test
    public void containsMethodProcessingEmptyListReturnFalse()
    {
        assertFalse(testedClass.contains(100, Collections.emptyList()));
    }

    @Test
    public void containsMethodWrongFormatNumberString()
    {
        badEntities = testedClass.readFromFile("src/test/resources/testResources/badContent.json");
        assertEquals(1, badEntities.size());
        assertFalse(testedClass.contains(100, badEntities));
    }
}
