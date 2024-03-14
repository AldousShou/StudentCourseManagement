package com.shoumh.core.common.course;

public enum CourseStatus {
    NORMAL,
    ENDED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}