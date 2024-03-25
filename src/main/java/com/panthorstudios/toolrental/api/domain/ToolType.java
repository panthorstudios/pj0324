package com.panthorstudios.toolrental.api.domain;

import java.math.BigDecimal;
public record ToolType(String typeCode, String label, BigDecimal dailyCharge, boolean chargeableOnWeekdays, boolean chargeableOnWeekends, boolean chargeableOnHolidays) { }

