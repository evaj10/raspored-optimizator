package org.acme.schooltimetabling.domain;

public enum MeetingType {
    PRED("Pred."),
    AUD("aud. vežbe"),
    RAC("rač. vežbe"),
    LAB("lab. vežbe");

    public final String label;

    MeetingType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
