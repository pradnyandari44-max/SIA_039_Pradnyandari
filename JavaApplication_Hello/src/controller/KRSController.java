package controller;

import dao.KRSDAO;
import model.KRS;
import java.util.List;

public class KRSController {

    private final KRSDAO dao = new KRSDAO();

    public String create(KRS krs) {
        String err = validate(krs);
        if (err != null) return err;
        return dao.create(krs) > 0 ? "OK" : "Gagal simpan KRS.";
    }

    public List<KRS> getAll() { return dao.getAll(); }

    public List<KRS> getByStudent(int studentID) {
        return dao.getByStudent(studentID);
    }

    public String update(KRS krs) {
        String err = validate(krs);
        if (err != null) return err;
        return dao.update(krs) > 0 ? "OK" : "Gagal update KRS.";
    }

    public String delete(int krsID) {
        if (krsID <= 0) return "Pilih data KRS terlebih dahulu.";
        return dao.delete(krsID) > 0 ? "OK" : "Gagal hapus KRS.";
    }

    private String validate(KRS krs) {
        if (krs.getStudent()  == null) return "Mahasiswa belum dipilih.";
        if (krs.getCourse()   == null) return "Mata kuliah belum dipilih.";
        if (krs.getLecturer() == null) return "Dosen belum dipilih.";
        if (krs.getNilaiSikap() < 0 || krs.getNilaiSikap() > 100) return "Nilai Sikap harus 0-100.";
        if (krs.getNilaiUTS()   < 0 || krs.getNilaiUTS()   > 100) return "Nilai UTS harus 0-100.";
        if (krs.getNilaiUAS()   < 0 || krs.getNilaiUAS()   > 100) return "Nilai UAS harus 0-100.";
        if (krs.getSemester() <= 0)                                return "Semester tidak valid.";
        return null;
    }
}