package controller;

import dao.StudentDAO;
import model.Student;
import java.util.List;

public class StudentController {

    private final StudentDAO dao = new StudentDAO();
    private static final int PAGE_SIZE = 5;

    public String create(Student s) {
        String err = validate(s);
        if (err != null) return err;
        if (dao.nimExists(s.getNim()))
            return "NIM " + s.getNim() + " sudah terdaftar!";
        return dao.create(s) > 0 ? "OK" : "Gagal simpan ke database.";
    }

    public List<Student> getAll() {
        return dao.getAll();
    }

    public List<Student> getPage(String keyword, int page) {
        return dao.getPage(keyword, page, PAGE_SIZE);
    }

    public int getTotalPages(String keyword) {
        int total = dao.count(keyword);
        return (int) Math.max(1, Math.ceil((double) total / PAGE_SIZE));
    }

    public String update(Student s) {
        if (s.getName() == null || s.getName().trim().isEmpty())
            return "Nama tidak boleh kosong.";
        return dao.update(s) > 0 ? "OK" : "Gagal update data.";
    }

    public String delete(String nim) {
        if (nim == null || nim.trim().isEmpty())
            return "Pilih mahasiswa terlebih dahulu.";
        return dao.delete(nim) > 0 ? "OK" : "Gagal hapus. Mungkin ada KRS terkait.";
    }

    private String validate(Student s) {
        if (s.getNim().trim().isEmpty())       return "NIM wajib diisi.";
        if (s.getName().trim().isEmpty())      return "Nama wajib diisi.";
        if (s.getIdCard().trim().isEmpty())    return "Card ID wajib diisi.";
        if (s.getStudyProgram().trim().isEmpty()) return "Program Studi wajib diisi.";
        return null;
    }
}