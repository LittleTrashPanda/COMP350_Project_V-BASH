package edu.gcc.VBASH;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {
    Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
            "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
            new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");

    @Test
    void getCourseName() {
        assertEquals("INTRO TO SKY MOTIONS & PLANETS", testGetCourse.getCourseName());
    }

    @Test
    void getDepartment() {
        assertEquals("ASTR", testGetCourse.getDepartment());
    }

    @Test
    void getCourseCode() {
        assertEquals("206", testGetCourse.getCourseCode());
    }

    @Test
    void getDescription() {
        assertEquals("Dummy description", testGetCourse.getDescription());
    }

    @Test
    void getProfessors() {
        assertArrayEquals(new String[]{"Clem, James L."}, testGetCourse.getProfessors());
    }

    @Test
    void getCredits() {
        assertEquals(3, testGetCourse.getCredits());
    }

    @Test
    void getDays() {
        assertEquals(110, testGetCourse.getDays());
    }

    @Test
    void getStartTimes() {
        assertArrayEquals(new int[]{13,13,13}, testGetCourse.getStartTimes());
    }

    @Test
    void getDuration() {
        assertArrayEquals(new int[]{50,50,50}, testGetCourse.getDuration());
    }

    @Test
    void getSemester() {
        assertEquals("2023_Fall", testGetCourse.getSemester());
    }

// ========willConflict Tests========

    @Test
    void conflictTimesAndDurations() {
        Course testConflictCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");

        assertTrue(testGetCourse.willConflict(testConflictCourse));
    }
}