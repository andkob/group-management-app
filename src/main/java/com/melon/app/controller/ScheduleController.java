package com.melon.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.melon.app.service.GoogleSheetsService;
import com.melon.app.service.objects.FormUpdateResponse;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final GoogleSheetsService googleSheetsService;

    @Autowired
    public ScheduleController(GoogleSheetsService googleSheetsService) {
        this.googleSheetsService = googleSheetsService;
    }

    @GetMapping("/schedule")
    public List<List<Object>> getScheduleData(@RequestParam(value = "sheetID") String spreadsheetID) throws IOException {
        System.out.println("getSchedule endpoint reached");
        return googleSheetsService.getDataFromSheet(spreadsheetID, "A1:Z");
    }

    @GetMapping("/poll-updates")
    public ResponseEntity<FormUpdateResponse> pollForUpdates(@RequestParam(value = "sheetID") String spreadsheetID,
                                                            @RequestParam(value = "lastKnownCount", defaultValue = "0") int lastKnownCount
                                                            ) throws IOException {
        return googleSheetsService.pollForUpdates(spreadsheetID, lastKnownCount);
    }

    @GetMapping("/testResponse")
    public String testResponse() {
        return "/testResponse endpoint reached";
    }
}