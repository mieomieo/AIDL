// IADLStudent.aidl
package com.example.server;
parcelable Student;
interface IADLStudent {
    void addStudent(in Student student);
    void updateStudent(in Student student);
    List<Student> getStudentsSortedByName();
    List<Student> getStudentsSortedByAverageGradeDescending();
    List<Student> getStudentsSortedByAverageGradeOfClassDescending();
    List<Student> searchStudents(String keyword);
    List<Student> getAllStudents();
}