package controller;

import dao.LecturerDAO;
import model.Lecturer;
import java.util.List;

public class LecturerController {

    private final LecturerDAO dao = new LecturerDAO();
    private static final int PAGE_SIZE = 5;

    public String create(Lecturer l) {
        String err = validate(l);
        if (err != null) return err;
        if (dao.nidnExists(l.getNidn()))
            return "NIDN " + l.getNidn() + " sudah terdaftar!";
        return dao.create(l) > 0 ? "OK" : "Gagal simpan ke database.";
    }

    public List<Lecturer> getAll() { return dao.getAll(); }

    public List<Lecturer> getPage(String keyword, int page) {
        return dao.getPage(keyword, page, PAGE_SIZE);
    }

    public int getTotalPages(String keyword) {
        return (int) Math.max(1, Math.ceil((double) dao.count(keyword) / PAGE_SIZE));
    }

    public String update(Lecturer l) {
        if (l.getName().trim().isEmpty()) return "Nama tidak boleh kosong.";
        return dao.update(l) > 0 ? "OK" : "Gagal update data.";
    }

    public String delete(String nidn) {
        if (nidn == null || nidn.trim().isEmpty())
            return "Pilih dosen terlebih dahulu.";
        return dao.delete(nidn) > 0 ? "OK" : "Gagal hapus.";
    }

    private String validate(Lecturer l) {
        if (l.getNidn().trim().isEmpty())      return "NIDN wajib diisi.";
        if (l.getName().trim().isEmpty())      return "Nama wajib diisi.";
        if (l.getIdCard().trim().isEmpty())    return "Card ID wajib diisi.";
        if (l.getExpertise().trim().isEmpty()) return "Bidang keahlian wajib diisi.";
        return null;
    }
}