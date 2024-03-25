package com.panthorstudios.toolrental.api.service;

import com.panthorstudios.toolrental.api.domain.Tool;
import com.panthorstudios.toolrental.api.domain.ToolType;
import com.panthorstudios.toolrental.properties.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing tools.
 */
@Service
public class ToolService {
    private static final Logger logger = LoggerFactory.getLogger(ToolService.class);

    private final AppProperties appProperties;

    /**
     * Constructor for ToolService.
     *
     * @param appProperties AppProperties object containing the properties of the tools.
     */
    public ToolService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /**
     * Retrieves a tool by its code.
     *
     * @param code The code of the tool.
     * @return An Optional containing the Tool if found, or an empty Optional if not found.
     */
    public Optional<Tool> getToolByCode(String code) {
        logger.info("Getting tool: {}, {}", code, this.appProperties.getToolsMap().get(code));
        return Optional.ofNullable(this.appProperties.getToolsMap().get(code));
    }

    /**
     * Retrieves all tools.
     *
     * @return An Optional containing a List of all Tools, or an empty Optional if no tools are found.
     */
    public List<Tool> getAllTools() {
        logger.info("Getting all tools");
        logger.info("Tools: {}", this.appProperties.getToolsMap());
        return List.copyOf(this.appProperties.getToolsMap().values());
    }

    /**
     * Retrieves all tool types.
     *
     * @return An Optional containing a List of all ToolTypes, or an empty Optional if no tool types are found.
     */
    public List<ToolType> getAllToolTypes() {
        logger.debug("Getting all tool types");
        logger.info("Tool types: {}", this.appProperties.getToolTypesMap());
        return List.copyOf(this.appProperties.getToolTypesMap().values());
    }

    /**
     * Retrieves the ToolType for a specific tool.
     *
     * @param tool The tool for which to get the tool type.
     * @return An Optional containing the ToolType if found, or an empty Optional if not found.
     */
    public Optional<ToolType> getToolTypeByTool(Tool tool) {
        if (tool== null) {
            logger.warn("Tool is null; couldn't get tool type");
            return Optional.empty();
        }
        logger.info("Getting tool type for tool with code: {}", tool.code());
        return Optional.ofNullable(this.appProperties.getToolTypesMap().get(tool.typeCode()));
    }

    /**
     * Checks if a tool exists by its code.
     *
     * @param code The code of the tool.
     * @return true if the tool exists, false otherwise.
     */
    public boolean toolExists(String code) {
        if (code == null) {
            return false;
        }
        return this.appProperties.getToolsMap().containsKey(code);
    }

    /**
     * Retrieves all tool codes.
     *
     * @return A List of all tool codes.
     */
    public List<String> getAllToolCodes() {
        return this.appProperties.getToolsMap().keySet().stream()
                .sorted()
                .collect(Collectors.toList());
    }

}