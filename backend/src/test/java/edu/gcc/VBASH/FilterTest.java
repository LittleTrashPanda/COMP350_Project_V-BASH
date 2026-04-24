package edu.gcc.VBASH;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FilterTest {

    @Test
    void filterCourse() {
    }

    @Test
    void filterTime() {
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");

    }

    @Test
    void filterDay() {
    }

    @Test
    void filterDeptNullDept(){
        String dept = null;
        // dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // null dept filter - if no dept specified, course should pass the filter because the dept doesn't matter
        Filter testFilter = new Filter(dept, null, null,
                -1, 1, null, null);
        assertEquals(true, testFilter.filterDept(testGetCourse));
    }

    @Test
    void filterDept_MatchDept(){
        // dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // filter with matching department - should pass through the filter and be true
        Filter testFilter = new Filter("ASTR", null, null,
                -1, 1, null, null);
        assertEquals(true, testFilter.filterDept(testGetCourse));
    }

    @Test
    void filterDept_NotMatchDept(){
        // dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // filter with a dept that doesn't match - should not pass through the filter and be false
        Filter testFilter = new Filter("COMP", null, null,
                -1, 1, null, null);

        assertEquals(false, testFilter.filterDept(testGetCourse));
    }

    @Test
    void filterDept_CasingTest(){
        // dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // filter with the matching dept but lowercase - should still pass through filter
        Filter testFilter = new Filter("astr", null, null,
                -1, 1, null, null);

        assertEquals(true, testFilter.filterDept(testGetCourse));
    }

    @Test
    void filterDept_WhitespaceDept(){
        //dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // filter with matching dept but with whitespace - might be an issue depending on frontend - should still pass through filter
        Filter testFilter = new Filter(" ASTR ", null, null,
                -1, 1, null, null);
        assertEquals(true, testFilter.filterDept(testGetCourse));
    }

    @Test
    void filterCreditNum_NegativeOne(){
        //dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // -1 for credits is what we're calling null - course should pass through filter
        Filter testFilter = new Filter(null, null, null,
                -1, 1, null, null);

        assertEquals(true, testFilter.filterCreditNum(testGetCourse));

    }

    @Test
    void filterCreditNum_MatchCreditNum(){
        //dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // the credits match so the course should pass through the filter
        Filter testFilter = new Filter(null, null, null,
                3, 1, null, null);

        assertEquals(true, testFilter.filterCreditNum(testGetCourse));

    }

    @Test
    void filterCreditNum_NotMatchCreditNum(){
        // dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // the credits don't match so the course should not pass through the filter
        Filter testFilter = new Filter(null, null, null,
                1, 1, null, null);

        assertEquals(false, testFilter.filterCreditNum(testGetCourse));

    }

    @Test
    void filterCourseCode_NullCourseCode(){
        String courseCode = null;
        // dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // the course code is null so the course should pass through the filter
        Filter testFilter = new Filter(null, courseCode, null,
                -1, 1, null, null);
        assertEquals(true, testFilter.filterCourseCode(testGetCourse));


    }

    @Test
    void filterCourseCode_MatchCourseCode(){
        // dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // the course code matches so it should pass through the filter
        Filter testFilter = new Filter(null, "206", null,
                -1, 1, null, null);
        assertEquals(true, testFilter.filterCourseCode(testGetCourse));


    }

    @Test
    void filterCourseCode_WhitespaceCourseCode(){
        // dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // filter with matching course code but with whitespace - might be an issue depending on frontend - should still pass through filter
        Filter testFilter = new Filter(null, " 206 ", null,
                -1, 1, null, null);
        assertEquals(true, testFilter.filterCourseCode(testGetCourse));


    }

    @Test
    void filterCourseCode_NoMatchCourseCode(){
        // dummy course
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        // the coursecode does not match, so the course should not pass through the filter
        Filter testFilter = new Filter(null, "141", null,
                -1, 1, null, null);
        assertEquals(false, testFilter.filterCourseCode(testGetCourse));


    }

    /* @Test
    void filterKeyWordTD(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        Filter tester = new Filter(null, null, null, -1, null, null,
                null, null);
        assertEquals(true, tester.filterKeyWordTD(testGetCourse, "dummy"));
        assertEquals(true, tester.filterKeyWordTD(testGetCourse, "planets"));
        assertEquals(false, tester.filterKeyWordTD(testGetCourse, "what"));

    } */

    @Test
    void filterProf(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "A", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");
        Filter testerA = new Filter(null, null, "clem", -1, 1, null,
                null);
        Filter testerB = new Filter(null, null, "planets", -1, 1, null,
                null);
        assertEquals(true, testerA.filterProf(testGetCourse));
        assertEquals(false, testerB.filterProf(testGetCourse));
    }
}