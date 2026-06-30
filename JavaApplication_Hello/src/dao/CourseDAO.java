package dao;

import config.DBConnection;
import model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public int create(Course co) {
        String sql = "INSERT INTO courses (code, courseName, sks, semester) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, co.getCode());
            ps.setString(2, co.getCourseName());
            ps.setInt(3, co.getSks());
            ps.setInt(4, co.getSemester());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[CourseDAO.create] " + e.getMessage());
            return 0;
        }
    }

    public List<Course> getAll() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY courseName";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("[CourseDAO.getAll] " + e.getMessage());
        }
        return list;
    }

    public List<Course> getPage(String keyword, int page, int pageSize) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses " +
                     "WHERE courseName LIKE ? OR code LIKE ? " +
                     "ORDER BY courseName LIMIT ? OFFSET ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw);
            ps.setInt(3, pageSize);
            ps.setInt(4, (page - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO.getPage] " + e.getMessage());
        }
        return list;
    }

    public int count(String keyword) {
        String sql = "SELECT COUNT(*) FROM courses WHERE courseName LIKE ? OR code LIKE ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO.count] " + e.getMessage());
        }
        return 0;
    }

    public int update(Course co) {
        String sql = "UPDATE courses SET courseName=?, sks=?, semester=? WHERE code=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, co.getCourseName());
            ps.setInt(2, co.getSks());
            ps.setInt(3, co.getSemester());
            ps.setString(4, co.getCode());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[CourseDAO.update] " + e.getMessage());
            return 0;
        }
    }

    public int delete(String code) {
        String sql = "DELETE FROM courses WHERE code=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[CourseDAO.delete] " + e.getMessage());
            return 0;
        }
    }

    public boolean codeExists(String code) {
        String sql = "SELECT COUNT(*) FROM courses WHERE code=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) { return false; }
    }

    private Course map(ResultSet rs) throws SQLException {
        return new Course(
            rs.getInt("courseID"),
            rs.getString("code"),
            rs.getString("courseName"),
            rs.getInt("sks"),
            rs.getInt("semester")
        );
    }
}