package model;

public class Student extends Person {

    private int    studentID;
    private String nim;
    private String studyProgram;

    public Student(int studentID, String idCard, String name,
                   String nim, String studyProgram) {
        super(idCard, name);
        this.studentID   = studentID;
        this.nim         = nim;
        this.studyProgram = studyProgram;
    }

    public Student(String idCard, String name, String nim, String studyProgram) {
        this(0, idCard, name, nim, studyProgram);
    }

    public int    getStudentID()    { return studentID;   }
    public String getNim()          { return nim;          }
    public String getStudyProgram() { return studyProgram; }

    public void setStudentID(int id)           { this.studentID   = id;  }
    public void setNim(String nim)             { this.nim         = nim; }
    public void setStudyProgram(String sp)     { this.studyProgram = sp; }

    @Override
    public String toString() { return name + " (" + nim + ")"; }
}