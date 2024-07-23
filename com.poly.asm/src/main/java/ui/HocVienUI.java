/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package ui;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import com.poly.dao.ChuyenDeDAO;
import com.poly.dao.HocVienDAO;
import com.poly.dao.KhoaHocDAO;
import com.poly.dao.NguoiHocDAO;
import com.poly.entity.ChuyenDe;
import com.poly.entity.HocVien;
import com.poly.entity.KhoaHoc;
import com.poly.entity.NguoiHoc;
import com.poly.utils.Auth;
import com.poly.utils.Msgbox;

/**
 *
 * @author Zander
 */
public class HocVienUI extends javax.swing.JInternalFrame {

    /**
     * Creates new form HocVien
     */
    public HocVienUI() {
        initComponents();
        setTitle("Học viên");
        fillComboboxChuyenDe();
    }

    ChuyenDeDAO cdDAO = new ChuyenDeDAO();
    KhoaHocDAO khDAO = new KhoaHocDAO();
    NguoiHocDAO nhDAO = new NguoiHocDAO();
    HocVienDAO hvDAO = new HocVienDAO();

    void fillComboboxChuyenDe(){
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboChuyenDe.getModel();
        model.removeAllElements();
        try{
            List<ChuyenDe> list = cdDAO.selectAll();
            for(ChuyenDe cd : list){
                model.addElement(cd);
            }
            fillComboboxKhoaHoc();
        }catch(Exception e){
            Msgbox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void fillComboboxKhoaHoc(){
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboKhoaHoc.getModel();
        model.removeAllElements();
        try{
            ChuyenDe chuyenDe = (ChuyenDe) cboChuyenDe.getSelectedItem();
            if(chuyenDe != null){
                List<KhoaHoc> list = khDAO.selectKhoaHocByChuyenDe(chuyenDe.getMaCD());
                for(KhoaHoc khoaHoc : list){
                    model.addElement(khoaHoc);
                }
            }
            fillTableHocVien();
        }catch(Exception e){
            Msgbox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    public void fillTableNguoiHoc(){
        DefaultTableModel model = (DefaultTableModel) tblNguoiHoc.getModel();
        model.setRowCount(0);
        try{
            KhoaHoc kh = (KhoaHoc) cboKhoaHoc.getSelectedItem();
            if(kh != null){
                System.out.println("MaKH:" + kh.getMaKH());
                String keyword = txtTimKiem.getText();
                List<NguoiHoc> list = nhDAO.selectByKeyword(keyword);
                for(NguoiHoc nh : list){
                    Object[] row = {
                        nh.getMaNH(),
                        nh.getHoTen(),
                        nh.getNgaySinh(),
                        nh.isGioiTinh() ? "Nam" : "Nữ",
                        nh.getDienThoai(),
                        nh.getEmail()
                    };
                    model.addRow(row);
                }
            }
        }catch(Exception e){
             Msgbox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }
    
    public void fillTableHocVien(){
        DefaultTableModel model = (DefaultTableModel) tblHocVien.getModel();
        model.setRowCount(0);
        try{
            KhoaHoc kh = (KhoaHoc) cboKhoaHoc.getSelectedItem();
            if(kh != null){
                System.out.println("MaKH:" + kh.getMaKH());
                List<HocVien> list = hvDAO.selectByKhoaHoc(kh.getMaKH());
                System.out.println("list:"+list.size());
                for(int i = 0; i<list.size();i++){
                    HocVien hv = list.get(i);
                    String hoTen = nhDAO.selectbyId(hv.getMaNH()).getHoTen();
                    Object[] row = {
                        i + 1, hv.getMaHV(),
                        hv.getMaNH(),
                        hoTen,
                        hv.getDiem()
                };
                    model.addRow(row);
                }
            }
            fillTableNguoiHoc();
        }catch(Exception e){
             Msgbox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }
    
    void addHocVien(){
        KhoaHoc khoaHoc = (KhoaHoc)cboKhoaHoc.getSelectedItem();
        for(int row : tblNguoiHoc.getSelectedRows()){
            HocVien hv = new HocVien();
            hv.setMaKH(khoaHoc.getMaKH());
            hv.setDiem(0);
            hv.setMaNH((String)tblNguoiHoc.getValueAt(row, 0));
            System.out.println("=>"+hv.getMaKH()+"-"+hv.getMaNH()+"-"+hv.getDiem());
            hvDAO.insert(hv);
        }
        fillTableHocVien();
        tabs.setSelectedIndex(0);
    }
    
    void removeHocVien(){
        if(!Auth.isManager()){
            Msgbox.alert(this, "Bạn không đủ quyền để xoá học viên!");
        }else{
            if(Msgbox.confirm(this, "Bạn muốn xoá các học viên được chọn!")){
                for(int row:tblHocVien.getSelectedRows()){
                    int maHV = (Integer)tblHocVien.getValueAt(row, 1);
                    hvDAO.delete(maHV);
                }
                fillTableHocVien();
            }
        }
    }
    
    void updateDiem(){
        for(int i = 0; i < tblHocVien.getRowCount();i++){
            int maHV = (Integer)tblHocVien.getValueAt(i, 1);
            HocVien hocVien = hvDAO.selectbyId(maHV);
            hocVien.setDiem(Double.parseDouble(tblHocVien.getValueAt(i, 4).toString()));
            hvDAO.update(hocVien);
        }
        Msgbox.alert(this, "Cập nhật điểm thành công!");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboChuyenDe = new javax.swing.JComboBox<>();
        cboKhoaHoc = new javax.swing.JComboBox<>();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHocVien = new javax.swing.JTable();
        btnSuaDiem = new javax.swing.JButton();
        btnXoaHV = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNguoiHoc = new javax.swing.JTable();
        btnThemHV = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("CHUYÊN ĐỀ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("KHOÁ HỌC");

        cboChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChuyenDeActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblHocVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TT", "Mã HV", "Mã NH", "Họ tên", "Điểm"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblHocVien);

        btnSuaDiem.setText("Cập nhật điểm");
        btnSuaDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaDiemActionPerformed(evt);
            }
        });

        btnXoaHV.setText("Xoá khỏi khoá học");
        btnXoaHV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnXoaHV)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSuaDiem)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSuaDiem)
                    .addComponent(btnXoaHV))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        tabs.addTab("Học viên", jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Tìm kiếm");

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        tblNguoiHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã NH", "Họ và tên", "Giới tính", "Ngày sinh", "Điện thoại", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblNguoiHoc);

        btnThemHV.setText("Thêm vào khoá học");
        btnThemHV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemHVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                    .addComponent(txtTimKiem)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnThemHV)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThemHV)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        tabs.addTab("Người học", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabs)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(cboKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemHVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemHVActionPerformed
        addHocVien();
    }//GEN-LAST:event_btnThemHVActionPerformed

    private void btnXoaHVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaHVActionPerformed
        removeHocVien();
    }//GEN-LAST:event_btnXoaHVActionPerformed

    private void btnSuaDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaDiemActionPerformed
        updateDiem();
    }//GEN-LAST:event_btnSuaDiemActionPerformed

    private void cboChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChuyenDeActionPerformed
        fillComboboxKhoaHoc();
    }//GEN-LAST:event_cboChuyenDeActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        fillTableNguoiHoc();
    }//GEN-LAST:event_txtTimKiemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSuaDiem;
    private javax.swing.JButton btnThemHV;
    private javax.swing.JButton btnXoaHV;
    private javax.swing.JComboBox<String> cboChuyenDe;
    private javax.swing.JComboBox<String> cboKhoaHoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblHocVien;
    private javax.swing.JTable tblNguoiHoc;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
