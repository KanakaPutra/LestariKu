package com.example.lestariku.Report;

public class Report {
    private String uid;
    private String name;
    private String date;
    private String location;
    private String reportText;

    // Constructor kosong wajib untuk Firebase
    public Report() {
    }

    public Report(String uid, String name, String date, String location, String reportText) {
        this.uid = uid;
        this.name = name;
        this.date = date;
        this.location = location;
        this.reportText = reportText;
    }

    // Getter dan Setter
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }
}
