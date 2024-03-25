package com.panthorstudios.toolrental.api.controller;

import com.panthorstudios.toolrental.api.domain.Tool;
import com.panthorstudios.toolrental.api.domain.ToolType;
import com.panthorstudios.toolrental.api.service.ToolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ToolController {

    private static final Logger logger = LoggerFactory.getLogger(ToolController.class);
    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @GetMapping("/tools")
    public ResponseEntity<List<Tool>> getAllTools() {
        List<Tool> tools = toolService.getAllTools();
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/tool-types")
    public ResponseEntity<List<ToolType>> getAllToolTypes() {
        List<ToolType> toolTypes = toolService.getAllToolTypes();
        return ResponseEntity.ok(toolTypes);
    }
    @GetMapping("/tools/{code}")
    public ResponseEntity<Tool> getTool(@PathVariable("code") String code) {

        Optional<Tool> toolOptional = toolService.getToolByCode(code);
        if (toolOptional.isPresent()) {
            return ResponseEntity.ok(toolOptional.get());
        }
        logger.warn("Tool not found: {}", code);
        return ResponseEntity.notFound().build();
    }
}


