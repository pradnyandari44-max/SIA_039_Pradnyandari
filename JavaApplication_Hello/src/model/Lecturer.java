package model;

public class Lecturer extends Person {

    private int    lecturerID;
    private String nidn;
    private String expertise;

    public Lecturer(int lecturerID, String idCard, String name,
                    String nidn, String expertise) {
        super(idCard, name);
        this.lecturerID = lecturerID;
        this.nidn       = nidn;
        this.expertise  = expertise;
    }

    public Lecturer(String idCard, String name, String nidn, String expertise) {
        this(0, idCard, name, nidn, expertise);
    }

    public int    getLecturerID() { return lecturerID; }
    public String getNidn()       { return nidn;       }
    public String getExpertise()  { return expertise;  }

    public void setLecturerID(int id)         { this.lecturerID = id;        }
    public void setNidn(String nidn)          { this.nidn       = nidn;      }
    public void setExpertise(String expertise){ this.expertise  = expertise; }

    @Override
    public String toString() { return name + " | " + nidn; }
}