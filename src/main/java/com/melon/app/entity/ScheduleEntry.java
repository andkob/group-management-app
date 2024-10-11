package com.melon.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ScheduleEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule; // Reference to the parent schedule

    private String day; // Example property
    private String time; // Example property

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
