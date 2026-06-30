package controller;

import dao.CourseDAO;
import model.Course;
import java.util.List;

public class CourseController {

    private final CourseDAO dao = new CourseDAO();
    private static final int PAGE_SIZE = 5;

    public String create(Course co) {
        String err = validate(co);
        if (err != null) return err;
        if (dao.codeExists(co.getCode()))
            return "Kode MK " + co.getCode() + " sudah ada!";
        return dao.create(co) > 0 ? "OK" : "Gagal simpan.";
    }

    public List<Course> getAll() { return dao.getAll(); }

    public List<Course> getPage(String keyword, int page) {
        return dao.getPage(keyword, page, PAGE_SIZE);
    }

    public int getTotalPages(String keyword) {
        return (int) Math.max(1, Math.ceil((double) dao.count(keyword) / PAGE_SIZE));
    }

    public String update(Course co) {
        if (co.getCourseName().trim().isEmpty()) return "Nama MK tidak boleh kosong.";
        if (co.getSks() <= 0) return "SKS harus lebih dari 0.";
        return dao.update(co) > 0 ? "OK" : "Gagal update.";
    }

    public String delete(String code) {
        if (code == null || code.trim().isEmpty())
            return "Pilih mata kuliah terlebih dahulu.";
        return dao.delete(code) > 0 ? "OK" : "Gagal hapus.";
    }

    private String validate(Course co) {
        if (co.getCode().trim().isEmpty())       return "Kode MK wajib diisi.";
        if (co.getCourseName().trim().isEmpty()) return "Nama MK wajib diisi.";
        if (co.getSks() <= 0)                    return "SKS harus lebih dari 0.";
        if (co.getSemester() <= 0)               return "Semester harus lebih dari 0.";
        return null;
    }
}