package dao;

import config.DBConnection;
import model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // CREATE
    public int create(Student s) {
        String sql = "INSERT INTO students (cardID, NIM, name, studyProgram) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getIdCard());
            ps.setString(2, s.getNim());
            ps.setString(3, s.getName());
            ps.setString(4, s.getStudyProgram());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[StudentDAO.create] " + e.getMessage());
            return 0;
        }
    }

    public List<Student> getAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("[StudentDAO.getAll] " + e.getMessage());
        }
        return list;
    }

    public List<Student> getPage(String keyword, int page, int pageSize) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students " +
                     "WHERE name LIKE ? OR NIM LIKE ? OR studyProgram LIKE ? " +
                     "ORDER BY name LIMIT ? OFFSET ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw); ps.setString(3, kw);
            ps.setInt(4, pageSize);
            ps.setInt(5, (page - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO.getPage] " + e.getMessage());
        }
        return list;
    }

    public int count(String keyword) {
        String sql = "SELECT COUNT(*) FROM students " +
                     "WHERE name LIKE ? OR NIM LIKE ? OR studyProgram LIKE ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw); ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO.count] " + e.getMessage());
        }
        return 0;
    }

    public int update(Student s) {
        String sql = "UPDATE students SET cardID=?, name=?, studyProgram=? WHERE NIM=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getIdCard());
            ps.setString(2, s.getName());
            ps.setString(3, s.getStudyProgram());
            ps.setString(4, s.getNim());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[StudentDAO.update] " + e.getMessage());
            return 0;
        }
    }

    public int delete(String nim) {
        String sql = "DELETE FROM students WHERE NIM=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nim);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[StudentDAO.delete] " + e.getMessage());
            return 0;
        }
    }

    public boolean nimExists(String nim) {
        String sql = "SELECT COUNT(*) FROM students WHERE NIM=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nim);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private Student map(ResultSet rs) throws SQLException {
        return new Student(
            rs.getInt("studentID"),
            rs.getString("cardID"),
            rs.getString("name"),
            rs.getString("NIM"),
            rs.getString("studyProgram")
        );
    }
}