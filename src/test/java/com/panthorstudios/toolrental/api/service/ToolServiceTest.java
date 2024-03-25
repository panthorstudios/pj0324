package com.panthorstudios.toolrental.api.service;

import com.panthorstudios.toolrental.api.domain.Tool;
import com.panthorstudios.toolrental.api.domain.ToolType;
import com.panthorstudios.toolrental.properties.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "app.mode=web")
class ToolServiceTest {
    private ToolService toolService;

    @Autowired
    private AppProperties appProperties;

    @BeforeEach
    void setUp() {
        toolService = new ToolService(appProperties);
    }

    /**
     * Test that a Ladder tool is returned correctly
     */
    @Test
    void getToolByCode_Ladder() {
        Optional<Tool> result = toolService.getToolByCode("LADW");
        assertTrue(result.isPresent());
        result.ifPresent(tool -> {
            assertEquals("LADW", tool.code());
            assertEquals("LADDER", tool.typeCode());
            assertEquals("Werner", tool.brand());
        });
    }
    /**
     * Test that a Chainsaw tool is returned correctly
     */
    @Test
    void getToolByCode_Chainsaw() {
        Optional<Tool> result = toolService.getToolByCode("CHNS");
        assertTrue(result.isPresent());
        result.ifPresent(tool -> {
            assertEquals("CHNS", tool.code());
            assertEquals("CHAINSAW", tool.typeCode());
            assertEquals("Stihl", tool.brand());
        });
    }
    /**
     * Test that a Jackhammer tool is returned correctly
     */
    @Test
    void getToolByCode_Jackhammer() {
        Optional<Tool> result = toolService.getToolByCode("JAKD");
        assertTrue(result.isPresent());
        result.ifPresent(tool -> {
            assertEquals("JAKD", tool.code());
            assertEquals("JACKHAMMER", tool.typeCode());
            assertEquals("DeWalt", tool.brand());
        });
    }
    /**
     * Test that a non-existent tool code returns nothing in the Optional
     */
    @Test
    void getToolByCode_nonExistingTool() {
        Optional<Tool> result = toolService.getToolByCode("NON_EXISTENT");
        assertFalse(result.isPresent());
    }

    /**
     * Test that the correct ToolType is returned for a Ladder tool
     */
    @Test
    void getToolTypeByTool_Ladder() {
        Tool tool = new Tool("LADW", "LADDER", "Werner");
        Optional<ToolType> result = toolService.getToolTypeByTool(tool);
        assertTrue(result.isPresent());
        result.ifPresent(toolType -> {
            assertEquals("LADDER", toolType.typeCode());
            assertEquals(new BigDecimal("1.99"), toolType.dailyCharge());
            assertTrue(toolType.chargeableOnWeekdays());
            assertTrue(toolType.chargeableOnWeekends());
            assertFalse(toolType.chargeableOnHolidays());
        });
    }

    /**
     * Test that the correct ToolType is returned for a Chainsaw tool.
     */
    @Test
    void getToolTypeByTool_Chainsaw() {
        Tool tool = new Tool("CHNS", "CHAINSAW", "Stihl");
        Optional<ToolType> result = toolService.getToolTypeByTool(tool);
        assertTrue(result.isPresent());
        result.ifPresent(toolType -> {
            assertEquals("CHAINSAW", toolType.typeCode());
            assertEquals(new BigDecimal("1.49"), toolType.dailyCharge());
            assertTrue(toolType.chargeableOnWeekdays());
            assertFalse(toolType.chargeableOnWeekends());
            assertTrue(toolType.chargeableOnHolidays());
        });
    }
    /**
     * Test that the correct ToolType is returned for a Jackhammer tool.
     */
    @Test
    void getToolTypeByTool_Jackhammer() {
        Tool tool = new Tool("JAKD", "JACKHAMMER", "DeWalt");
        Optional<ToolType> result = toolService.getToolTypeByTool(tool);
        assertTrue(result.isPresent());
        result.ifPresent(toolType -> {
            assertEquals("JACKHAMMER", toolType.typeCode());
            assertEquals(new BigDecimal("2.99"), toolType.dailyCharge());
            assertTrue(toolType.chargeableOnWeekdays());
            assertFalse(toolType.chargeableOnWeekends());
            assertFalse(toolType.chargeableOnHolidays());
        });
    }
    /**
     * Test that nothing is returned in the Optional for a tool with an unknown type.
     */
    @Test
    void getToolTypeByTool_nonExistingToolType() {
        Tool tool = new Tool("UNKNOWN", "NON_EXISTENT", "NoBrand");
        Optional<ToolType> result = toolService.getToolTypeByTool(tool);
        assertFalse(result.isPresent());
    }
    /**
     * Test get all tools
     */
    @Test
    void testGetAllTools() {
        List<Tool> tools = toolService.getAllTools();
        assertNotNull(tools);
        assertEquals(4,tools.size());
    }

    /**
     * Test get all tool types
     */
    @Test
    void testGetAllToolTypes() {
        List<ToolType> toolTypes = toolService.getAllToolTypes();
        assertNotNull(toolTypes);
        assertEquals(3,toolTypes.size());
    }
    /**
     * Test get all tool codes
     */
    @Test
    void testGetAllToolCodes() {
        List<String> toolCodes = toolService.getAllToolCodes();
        assertNotNull(toolCodes);
        assertEquals(4,toolCodes.size());
        assertTrue(toolCodes.contains("CHNS"));
        assertTrue(toolCodes.contains("LADW"));
        assertTrue(toolCodes.contains("JAKR"));
        assertTrue(toolCodes.contains("JAKD"));
    }
    /**
     * Test known tool code returns true
     */
    @Test
    void testToolExists_existingToolType() {
        String toolCode = "CHNS";
        assertTrue(toolService.toolExists(toolCode));
    }
    /**
     * Test unknown tool code returns false
     */
    @Test
    void testToolExists_nonExistingToolType() {
        String toolCode = "UNKNOWN";
        assertFalse(toolService.toolExists(toolCode));
    }
    /**
     * Test null tool code returns false
     */
    @Test
    void testToolExists_nullToolType() {
        assertFalse(toolService.toolExists(null));
    }
}