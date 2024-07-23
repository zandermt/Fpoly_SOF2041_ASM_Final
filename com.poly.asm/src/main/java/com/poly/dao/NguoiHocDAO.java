package com.poly.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.poly.entity.NguoiHoc;
import com.poly.utils.JdbcHelper;

public class NguoiHocDAO extends EduSysDAO<NguoiHoc, Integer> {
    String INSERT_SQL = "INSERT INTO NguoiHoc (MaNH, HoTen, NgaySinh, GioiTinh, DienThoai, Email, GhiChu, MaNV) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    String UPDATE_SQL = "UPDATE NguoiHoc SET HoTen=?, NgaySinh=?, GioiTinh=?, DienThoai=?, Email=?, GhiChu=?,MaNV=? WHERE MaNH=?";
    String DELETE_SQL = "DELETE FROM NguoiHoc WHERE MaNH=?";
    String SELECT_ALL_SQL = "SELECT * FROM NguoiHoc";
    String SELECT_BY_ID_SQL = "SELECT * FROM NguoiHoc WHERE MaNH=?";
    String SELECT_NOT_IN_COURSE_ID_SQL = "SELECT * FROM NguoiHoc WHERE HoTen LIKE ? AND MaNH NOT IN(SELECT MaNH FROM HocVien WHERE MaKH = ?)";
    
    @Override
    public void insert(NguoiHoc entity) {
        JdbcHelper.executeUpdate(INSERT_SQL,
                entity.getMaNH(),
                entity.getHoTen(),
                entity.getNgaySinh(),
                entity.isGioiTinh(),
                entity.getDienThoai(),
                entity.getEmail(),
                entity.getGhiChu(),
                entity.getMaNV());
    }

    @Override
    public void update(NguoiHoc entity) {
        JdbcHelper.executeUpdate(UPDATE_SQL,
                entity.getHoTen(),
                entity.getNgaySinh(),
                entity.isGioiTinh(),
                entity.getDienThoai(),
                entity.getEmail(),
                entity.getGhiChu(),
                entity.getMaNV(),
                entity.getMaNH());
    }

    public void delete(String id) {
       JdbcHelper.executeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<NguoiHoc> selectAll() {
        return this.selectbySQL(SELECT_ALL_SQL);
    }

    public NguoiHoc selectbyId(String id) {
        List<NguoiHoc> list = this.selectbySQL(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NguoiHoc> selectbySQL(String sql, Object... args) {
        List<NguoiHoc> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.executeQuery(sql, args);
            while (rs.next()) {
                NguoiHoc entity = new NguoiHoc();
                entity.setMaNH(rs.getString("MaNH"));
                entity.setHoTen(rs.getString("HoTen"));
                entity.setNgaySinh(rs.getDate("NgaySinh"));
                entity.setGioiTinh(rs.getBoolean("GioiTinh"));
                entity.setDienThoai(rs.getString("DienThoai"));
                entity.setEmail(rs.getString("Email"));
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setMaNV(rs.getString("MaNV"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<NguoiHoc> selectByKeyword(String keyword) {
        String SQL = "SELECT * FROM NguoiHoc WHERE HoTen LIKE ? OR MaNH LIKE ? OR DienThoai LIKE ? OR Email LIKE ? OR MaNV LIKE ?";
        String searchPattern = "%" + keyword + "%";
        return this.selectbySQL(SQL, searchPattern, searchPattern, searchPattern, searchPattern, searchPattern);
    }

    public List<NguoiHoc> selectNotInCourse(int makh, String keyword) {
        return this.selectbySQL(SELECT_NOT_IN_COURSE_ID_SQL, "%" + keyword + "%", makh);
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public NguoiHoc selectbyId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
