package dao;

import config.DBConnection;
import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KRSDAO {

    private static final String SELECT_SQL =
        "SELECT k.krsID, k.nilaiSikap, k.nilaiUTS, k.nilaiUAS, k.nilaiAkhir, k.grade, k.semester, " +
        "s.studentID, s.cardID AS sCard, s.name AS sName, s.NIM, s.studyProgram, " +
        "co.courseID, co.code, co.courseName, co.sks, co.semester AS coSem, " +
        "l.lecturerID, l.cardID AS lCard, l.name AS lName, l.NIDN, l.expertise " +
        "FROM krs k " +
        "JOIN students s  ON k.studentID  = s.studentID " +
        "JOIN courses co  ON k.courseCode = co.code " +
        "JOIN lecturers l ON k.lecturerID = l.lecturerID ";

    public int create(KRS krs) {
        String sql = "INSERT INTO krs (studentID, courseCode, nilaiSikap, nilaiUTS, nilaiUAS, " +
                     "nilaiAkhir, grade, lecturerID, semester) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,    krs.getStudent().getStudentID());
            ps.setString(2, krs.getCourse().getCode());
            ps.setDouble(3, krs.getNilaiSikap());
            ps.setDouble(4, krs.getNilaiUTS());
            ps.setDouble(5, krs.getNilaiUAS());
            ps.setDouble(6, krs.getNilaiAkhir());
            ps.setString(7, krs.getGrade());
            ps.setInt(8,    krs.getLecturer().getLecturerID());
            ps.setInt(9,    krs.getSemester());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[KRSDAO.create] " + e.getMessage());
            return 0;
        }
    }

    public List<KRS> getAll() {
        List<KRS> list = new ArrayList<>();
        String sql = SELECT_SQL + "ORDER BY s.name, k.semester";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("[KRSDAO.getAll] " + e.getMessage());
        }
        return list;
    }

    public List<KRS> getByStudent(int studentID) {
        List<KRS> list = new ArrayList<>();
        String sql = SELECT_SQL + "WHERE k.studentID=? ORDER BY k.semester";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, studentID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("[KRSDAO.getByStudent] " + e.getMessage());
        }
        return list;
    }

    public int update(KRS krs) {
        String sql = "UPDATE krs SET nilaiSikap=?, nilaiUTS=?, nilaiUAS=?, " +
                     "nilaiAkhir=?, grade=?, lecturerID=?, semester=? WHERE krsID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, krs.getNilaiSikap());
            ps.setDouble(2, krs.getNilaiUTS());
            ps.setDouble(3, krs.getNilaiUAS());
            ps.setDouble(4, krs.getNilaiAkhir());
            ps.setString(5, krs.getGrade());
            ps.setInt(6,    krs.getLecturer().getLecturerID());
            ps.setInt(7,    krs.getSemester());
            ps.setInt(8,    krs.getKrsID());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[KRSDAO.update] " + e.getMessage());
            return 0;
        }
    }

    public int delete(int krsID) {
        String sql = "DELETE FROM krs WHERE krsID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, krsID);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[KRSDAO.delete] " + e.getMessage());
            return 0;
        }
    }

    private KRS map(ResultSet rs) throws SQLException {
        Student student = new Student(
            rs.getInt("studentID"), rs.getString("sCard"),
            rs.getString("sName"), rs.getString("NIM"), rs.getString("studyProgram")
        );
        Course course = new Course(
            rs.getInt("courseID"), rs.getString("code"),
            rs.getString("courseName"), rs.getInt("sks"), rs.getInt("coSem")
        );
        Lecturer lecturer = new Lecturer(
            rs.getInt("lecturerID"), rs.getString("lCard"),
            rs.getString("lName"), rs.getString("NIDN"), rs.getString("expertise")
        );
        return new KRS(
            rs.getInt("krsID"), student, course, lecturer,
            rs.getDouble("nilaiSikap"), rs.getDouble("nilaiUTS"),
            rs.getDouble("nilaiUAS"), rs.getInt("semester")
        );
    }
}