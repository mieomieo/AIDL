// IADLStudent.aidl
package com.example.server;

// Declare any non-default types here with import statements

interface IADLStudent {
    void addStudent(inout Student student);
    void updateStudent(inout Student student);
    List<Student> getStudentsSortedByName();
    List<Student> getStudentsSortedByAverageGradeDescending();
    List<Student> sortStudentsByAverageGradeOfClassDescending();
    List<Student> searchStudents(String keyword);
    List<Student> getAllStudents();

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}