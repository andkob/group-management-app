package com.melon.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.melon.app.service.GoogleSheetsService;

@RestController
public class ScheduleController {

    @Autowired
    private GoogleSheetsService googleSheetsService;

    @GetMapping("/schedule")
    public List<List<Object>> getScheduleData(@RequestParam String spreadsheetID) throws IOException {
        return googleSheetsService.getDataFromSheet(spreadsheetID, "A1:Z");
    }
}