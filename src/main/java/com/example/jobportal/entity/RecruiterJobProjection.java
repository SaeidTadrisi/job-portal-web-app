package com.example.jobportal.entity;

public interface RecruiterJobProjection {

    Long getTotalCandidates();

    int getJob_post_id();

    String getJob_title();

    int getLocationId();

    String getCity();

    String getState();

    String getCountry();

    int getCompanyId();

    String getCompanyName();
}
