package model;

public class KRS {

    private int      krsID;
    private Student  student;
    private Course   course;
    private Lecturer lecturer;
    private double   nilaiSikap;
    private double   nilaiUTS;
    private double   nilaiUAS;
    private double   nilaiAkhir;
    private String   grade;
    private int      semester;

    public KRS(int krsID, Student student, Course course, Lecturer lecturer,
               double nilaiSikap, double nilaiUTS, double nilaiUAS, int semester) {
        this.krsID      = krsID;
        this.student    = student;
        this.course     = course;
        this.lecturer   = lecturer;
        this.nilaiSikap = nilaiSikap;
        this.nilaiUTS   = nilaiUTS;
        this.nilaiUAS   = nilaiUAS;
        this.semester   = semester;
        this.nilaiAkhir = hitungNilaiAkhir(nilaiSikap, nilaiUTS, nilaiUAS);
        this.grade      = hitungGrade(this.nilaiAkhir);
    }

    public KRS(Student student, Course course, Lecturer lecturer,
               double nilaiSikap, double nilaiUTS, double nilaiUAS, int semester) {
        this(0, student, course, lecturer, nilaiSikap, nilaiUTS, nilaiUAS, semester);
    }

    public static double hitungNilaiAkhir(double sikap, double uts, double uas) {
        return (sikap * 0.20) + (uts * 0.30) + (uas * 0.50);
    }

    public static String hitungGrade(double nilai) {
        if (nilai >= 85) return "A";
        if (nilai >= 75) return "B";
        if (nilai >= 60) return "C";
        if (nilai >= 45) return "D";
        return "E";
    }

    public int      getKrsID()      { return krsID;      }
    public Student  getStudent()    { return student;    }
    public Course   getCourse()     { return course;     }
    public Lecturer getLecturer()   { return lecturer;   }
    public double   getNilaiSikap() { return nilaiSikap; }
    public double   getNilaiUTS()   { return nilaiUTS;   }
    public double   getNilaiUAS()   { return nilaiUAS;   }
    public double   getNilaiAkhir() { return nilaiAkhir; }
    public String   getGrade()      { return grade;      }
    public int      getSemester()   { return semester;   }

    public void setKrsID(int krsID)           { this.krsID      = krsID;    }
    public void setNilaiSikap(double v)       { this.nilaiSikap = v; recalculate(); }
    public void setNilaiUTS(double v)         { this.nilaiUTS   = v; recalculate(); }
    public void setNilaiUAS(double v)         { this.nilaiUAS   = v; recalculate(); }
    public void setSemester(int semester)     { this.semester   = semester;  }
    public void setLecturer(Lecturer l)       { this.lecturer   = l;         }

    private void recalculate() {
        this.nilaiAkhir = hitungNilaiAkhir(nilaiSikap, nilaiUTS, nilaiUAS);
        this.grade      = hitungGrade(this.nilaiAkhir);
    }

    @Override
    public String toString() {
        return course.getCourseName() + " | " + grade + " | Sem-" + semester;
    }
}