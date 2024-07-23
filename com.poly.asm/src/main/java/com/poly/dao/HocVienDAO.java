package com.poly.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.poly.entity.HocVien;
import com.poly.utils.JdbcHelper;

public class HocVienDAO extends EduSysDAO<HocVien, Integer> {
    String INSERT_SQL = "INSERT INTO HocVien(MaKH, MaNH, Diem) VALUES(?, ?, ?)";
    String UPDATE_SQL = "UPDATE HocVien SET MaKH=?, MaNH=?, Diem=? WHERE MaHV=?";
    String DELETE_SQL = "DELETE FROM HocVien WHERE MaHV=?";
    String SELECT_ALL_SQL = "SELECT * FROM HocVien";
    String SELECT_BY_ID_SQL = "SELECT * FROM HocVien WHERE MaHV=?";

    @Override
    public void insert(HocVien entity) {
        JdbcHelper.executeUpdate(INSERT_SQL, entity.getMaKH(),
                entity.getMaNH(),
                entity.getDiem());
    }

    @Override
    public void update(HocVien entity) {
        JdbcHelper.executeUpdate(UPDATE_SQL,
                entity.getMaKH(),
                entity.getMaNH(),
                entity.getDiem(),
                entity.getMaHV());
    }

    @Override
    public void delete(Integer id) {
        JdbcHelper.executeUpdate(DELETE_SQL, id);
    }

    @Override
    public List<HocVien> selectAll() {
        return this.selectbySQL(SELECT_ALL_SQL);
    }

    @Override
    public HocVien selectbyId(Integer id) {
        List<HocVien> list = this.selectbySQL(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HocVien> selectbySQL(String sql, Object... args) {
        List<HocVien> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.executeQuery(sql, args);
            while (rs.next()) {
                HocVien entity = new HocVien();
                entity.setMaHV(rs.getInt("MaHV"));
                entity.setMaKH(rs.getInt("MaKH"));
                entity.setMaNH(rs.getString("MaNH"));
                entity.setDiem(rs.getDouble("Diem"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<HocVien> selectByKhoaHoc(int makh) {
        String SQL = "SELECT * FROM HocVien WHERE MaKH = ?";
        return this.selectbySQL(SQL, makh);
    }
}
