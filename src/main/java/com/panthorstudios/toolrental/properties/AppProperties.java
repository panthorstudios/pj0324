package com.panthorstudios.toolrental.properties;

import com.panthorstudios.toolrental.api.domain.HolidayRule;
import com.panthorstudios.toolrental.api.domain.Tool;
import com.panthorstudios.toolrental.api.domain.ToolType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.List;
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String mode;
    private Integer storeId;
    private Integer terminalId;
    private Map<String, Tool> toolsMap;
    private Map<String, ToolType> toolTypesMap;
    private List<HolidayRule> holidayRulesList;

    public AppProperties(Map<String, Tool> toolsMap, Map<String, ToolType> toolTypesMap) {
        this.toolsMap = toolsMap;
        this.toolTypesMap = toolTypesMap;
    }

}