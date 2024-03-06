package com.shoumh.core.common;

public enum CourseStatus {
    NORMAL,
    ENDED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}