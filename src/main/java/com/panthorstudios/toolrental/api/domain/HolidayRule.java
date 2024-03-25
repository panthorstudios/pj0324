package com.panthorstudios.toolrental.api.domain;

// Constructor for fixed-date holidays with weekend adjustment
public record HolidayRule(String countryCode, String type, String name, Integer month, Integer day, Integer dayOfWeek, Integer occurrence, String weekendFunctionCode) {}

