package dao;

import config.DBConnection;
import model.Lecturer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {

    public int create(Lecturer l) {
        String sql = "INSERT INTO lecturers (cardID, name, NIDN, expertise) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getIdCard());
            ps.setString(2, l.getName());
            ps.setString(3, l.getNidn());
            ps.setString(4, l.getExpertise());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[LecturerDAO.create] " + e.getMessage());
            return 0;
        }
    }

    public List<Lecturer> getAll() {
        List<Lecturer> list = new ArrayList<>();
        String sql = "SELECT * FROM lecturers ORDER BY name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            System.err.println("[LecturerDAO.getAll] " + e.getMessage());
        }
        return list;
    }

    public List<Lecturer> getPage(String keyword, int page, int pageSize) {
        List<Lecturer> list = new ArrayList<>();
        String sql = "SELECT * FROM lecturers " +
                     "WHERE name LIKE ? OR NIDN LIKE ? OR expertise LIKE ? " +
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
            System.err.println("[LecturerDAO.getPage] " + e.getMessage());
        }
        return list;
    }

    public int count(String keyword) {
        String sql = "SELECT COUNT(*) FROM lecturers " +
                     "WHERE name LIKE ? OR NIDN LIKE ? OR expertise LIKE ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw); ps.setString(3, kw);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[LecturerDAO.count] " + e.getMessage());
        }
        return 0;
    }

    public int update(Lecturer l) {
        String sql = "UPDATE lecturers SET cardID=?, name=?, expertise=? WHERE NIDN=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getIdCard());
            ps.setString(2, l.getName());
            ps.setString(3, l.getExpertise());
            ps.setString(4, l.getNidn());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[LecturerDAO.update] " + e.getMessage());
            return 0;
        }
    }

    public int delete(String nidn) {
        String sql = "DELETE FROM lecturers WHERE NIDN=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nidn);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[LecturerDAO.delete] " + e.getMessage());
            return 0;
        }
    }

    public boolean nidnExists(String nidn) {
        String sql = "SELECT COUNT(*) FROM lecturers WHERE NIDN=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nidn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) { return false; }
    }

    private Lecturer map(ResultSet rs) throws SQLException {
        return new Lecturer(
            rs.getInt("lecturerID"),
            rs.getString("cardID"),
            rs.getString("name"),
            rs.getString("NIDN"),
            rs.getString("expertise")
        );
    }
}