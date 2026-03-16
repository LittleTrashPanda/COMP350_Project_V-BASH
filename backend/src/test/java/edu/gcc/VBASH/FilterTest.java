package edu.gcc.VBASH;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FilterTest {

    @Test
    void filterCourse() {
    }

    @Test
    void filterTime() {
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,-1,13,-1,13}, new int[]{50,-1,50,-1,50}, "2023_Fall");

    }

    @Test
    void filterDay() {
    }

    @Test
    void filterDeptNullDept(){
        String dept = null;
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter(dept, null, null,
                -1, null, null, null, null);
        assertEquals(true, testFilter.filterDept(testGetCourse));
    }

    @Test
    void filterDept_MatchDept(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter("ASTR", null, null,
                -1, null, null, null, null);
        assertEquals(true, testFilter.filterDept(testGetCourse));
    }

    @Test
    void filterDept_NotMatchDept(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter("COMP", null, null,
                -1, null, null, null, null);

        assertEquals(false, testFilter.filterDept(testGetCourse));
    }

    @Test
    void filterDept_CasingTest(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter("astr", null, null,
                -1, null, null, null, null);

        assertEquals(true, testFilter.filterDept(testGetCourse));
    }

    @Test
    void filterDept_WhitespaceDept(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter(" ASTR ", null, null,
                -1, null, null, null, null);
        assertEquals(true, testFilter.filterDept(testGetCourse));
    }

    @Test
    void filterCreditNum_NegativeOne(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter(null, null, null,
                -1, null, null, null, null);

        assertEquals(true, testFilter.filterCreditNum(testGetCourse));

    }

    @Test
    void filterCreditNum_MatchCreditNum(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter(null, null, null,
                3, null, null, null, null);

        assertEquals(true, testFilter.filterCreditNum(testGetCourse));

    }

    @Test
    void filterCreditNum_NotMatchCreditNum(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter(null, null, null,
                1, null, null, null, null);

        assertEquals(false, testFilter.filterCreditNum(testGetCourse));

    }
// I'm unsure if this should be a test in Filter or Course, but I'm leaning towards Course
//    @Test
//    void filterCreditNum_InvalidCreditNum(){
//        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
//                "206", "Dummy description", new String[]{"Clem, James L."}, 7, 110,
//                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
//        Filter testFilter = new Filter(null, null, null,
//                7, null, null, null, null);
//
//        assertEquals(false, testFilter.filterCreditNum(testGetCourse));
//
//    }

    @Test
    void filterCourseCode_NullCourseCode(){
        String courseCode = null;
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter(null, courseCode, null,
                -1, null, null, null, null);
        assertEquals(true, testFilter.filterCourseCode(testGetCourse));


    }

    @Test
    void filterCourseCode_MatchCourseCode(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter(null, "206", null,
                -1, null, null, null, null);
        assertEquals(true, testFilter.filterCourseCode(testGetCourse));


    }

    @Test
    void filterCourseCode_WhitespaceCourseCode(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter(null, " 206 ", null,
                -1, null, null, null, null);
        assertEquals(true, testFilter.filterCourseCode(testGetCourse));


    }

    @Test
    void filterCourseCode_NoMatchCourseCode(){
        Course testGetCourse = new Course("INTRO TO SKY MOTIONS & PLANETS", "ASTR",
                "206", "Dummy description", new String[]{"Clem, James L."}, 3, 110,
                new int[]{13,13,13}, new int[]{50,50,50}, "2023_Fall");
        Filter testFilter = new Filter(null, "141", null,
                -1, null, null, null, null);
        assertEquals(false, testFilter.filterCourseCode(testGetCourse));


    }

    @Test
    void filterKeyWordTD(){
    }

    @Test
    void filterProf(){
    }
}