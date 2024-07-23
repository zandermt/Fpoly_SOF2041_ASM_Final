package com.poly.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.poly.entity.KhoaHoc;
import com.poly.utils.JdbcHelper;

public class KhoaHocDAO extends EduSysDAO<KhoaHoc, Integer> {
    String INSERT_SQL = "INSERT INTO KhoaHoc (MaCD, HocPhi, ThoiLuong, NgayKG, GhiChu, MaNV) VALUES (?, ?, ?, ?, ?, ?)";
    String UPDATE_SQL = "UPDATE KhoaHoc SET MaCD=?, HocPhi=?, ThoiLuong=?, NgayKG=?, GhiChu=?, MaNV=? WHERE MaKH=?";
    String DELETE_SQL = "DELETE FROM KhoaHoc WHERE MaKH=?";
    String SELECT_ALL_SQL = "SELECT * FROM KhoaHoc";
    String SELECT_BY_ID_SQL = "SELECT * FROM KhoaHoc WHERE MaKH=?";
    String SELECT_BY_MA_CD_SQL = "SELECT * FROM KhoaHoc WHERE MaCD=?";

    @Override
    public void insert(KhoaHoc entity) {
        JdbcHelper.executeUpdate(INSERT_SQL,
                entity.getMaCD(),
                entity.getHocPhi(),
                entity.getThoiLuong(),
                entity.getNgayKG(),
                entity.getGhiChu(),
                entity.getMaNV());
    }

    @Override
    public void update(KhoaHoc entity) {
        JdbcHelper.executeUpdate(UPDATE_SQL,
                entity.getMaCD(),
                entity.getHocPhi(),
                entity.getThoiLuong(),
                entity.getNgayKG(),
                entity.getGhiChu(),
                entity.getMaNV(),
                entity.getMaKH());
    }

    @Override
    public void delete(Integer id) {
        JdbcHelper.executeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<KhoaHoc> selectAll() {
        return this.selectbySQL(SELECT_ALL_SQL);
    }

    @Override
    public KhoaHoc selectbyId(Integer id) {
        List<KhoaHoc> list = this.selectbySQL(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<KhoaHoc> selectbySQL(String sql, Object... args) {
        List<KhoaHoc> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.executeQuery(sql, args);
            while (rs.next()) {
                KhoaHoc entity = new KhoaHoc();
                entity.setMaKH(rs.getInt("MaKH"));
                entity.setMaCD(rs.getString("MaCD"));
                entity.setHocPhi(rs.getDouble("HocPhi"));
                entity.setThoiLuong(rs.getInt("ThoiLuong"));
                entity.setNgayKG(rs.getDate("NgayKG"));
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setMaNV(rs.getString("MaNV"));
                entity.setNgayTao(rs.getDate("NgayTao"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<KhoaHoc> selectByChuyenDe(String macd) {
        String SQL = "SELECT * FROM KhoaHoc WHERE MaCD = ?";
        return this.selectbySQL(SQL, macd);
    }

    public List<Integer> selectYears() {
        String SQL = "SELECT DISTINCT year(NgayKG) Year FROM KhoaHoc ORDER BY Year DESC";
        List<Integer> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.executeQuery(SQL);
            while (rs.next()) {
                list.add(rs.getInt(1));
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<KhoaHoc> selectKhoaHocByChuyenDe(String maCD) {
        return this.selectbySQL(SELECT_BY_MA_CD_SQL, maCD);
    }
}
