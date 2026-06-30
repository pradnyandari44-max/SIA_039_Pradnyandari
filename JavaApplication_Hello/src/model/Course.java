package model;

public class Course {

    private int    courseID;
    private String code;
    private String courseName;
    private int    sks;
    private int    semester;

    public Course(int courseID, String code, String courseName, int sks, int semester) {
        this.courseID   = courseID;
        this.code       = code;
        this.courseName = courseName;
        this.sks        = sks;
        this.semester   = semester;
    }

    public Course(String code, String courseName, int sks, int semester) {
        this(0, code, courseName, sks, semester);
    }

    public int    getCourseID()   { return courseID;   }
    public String getCode()       { return code;       }
    public String getCourseName() { return courseName; }
    public int    getSks()        { return sks;        }
    public int    getSemester()   { return semester;   }

    public void setCourseID(int id)          { this.courseID   = id;   }
    public void setCode(String code)         { this.code       = code; }
    public void setCourseName(String name)   { this.courseName = name; }
    public void setSks(int sks)              { this.sks        = sks;  }
    public void setSemester(int semester)    { this.semester   = semester; }

    @Override
    public String toString() { return courseName + " (" + code + ") - " + sks + " SKS"; }
}