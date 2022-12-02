package org.acme.schooltimetabling.domain;

public enum Semestar {
    Z("zimski"),
    L("letnji");

    public final String label;

    Semestar(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
