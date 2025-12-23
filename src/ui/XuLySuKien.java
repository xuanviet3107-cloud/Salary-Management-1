package ui; 

import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entity.NhanVien;
import logic.XuLyTangLuong;

public class XuLySuKien {
  
    private QuanLyNhanVien solve;

    public XuLySuKien(QuanLyNhanVien solve) {
        this.solve = solve;
    }

    public void xuLyGiamLuong() {
        int row = solve.table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(solve, "Vui lòng chọn nhân viên cần giảm lương!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maNV = solve.table.getValueAt(row, 0).toString();
        String hoTen = solve.table.getValueAt(row, 1).toString();
        String luongCuStr = solve.table.getValueAt(row, 3).toString().replace(",", "").replace(" VNĐ", "").trim();
        double luongCu = Double.parseDouble(luongCuStr);

        String input = JOptionPane.showInputDialog(solve, "Nhập % muốn giảm cho " + hoTen + ":", "10");
        if (input == null || input.trim().isEmpty()) return;

        double phanTram = 0;
        try {
            phanTram = Double.parseDouble(input);
            if (phanTram <= 0 || phanTram >= 100) {
                JOptionPane.showMessageDialog(solve, "Phần trăm giảm phải lớn hơn 0 và nhỏ hơn 100!");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(solve, "Vui lòng nhập số hợp lệ!");
            return;
        }

        double luongMoi = luongCu * (1 - phanTram / 100);
        String msg = String.format("CẢNH BÁO GIẢM LƯƠNG\n\nNhân viên: %s\nLương cũ: %,.0f VNĐ\nLương mới: %,.0f VNĐ\n(Giảm: %.1f%%)\n\nXác nhận thực hiện?", hoTen, luongCu, luongMoi, phanTram);
        int confirm = JOptionPane.showConfirmDialog(solve, msg, "Xác Nhận Giảm Lương", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {

                solve.dao.tangLuong(maNV, -phanTram);
                JOptionPane.showMessageDialog(solve, "Đã giảm lương thành công!");
                solve.loadData("NV.MaNV ASC");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(solve, "Lỗi: " + ex.getMessage());
            }
        }
    }

    public void xuLyThuongNong() {
        String[] options = {"Toàn Công Ty", "Theo Phòng Ban", "Hủy"};
        int choice = JOptionPane.showOptionDialog(solve, 
            "Chọn phạm vi áp dụng thưởng:", 
            "Hệ Thống Thưởng & Phúc Lợi", 
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 2 || choice == -1) return;

        String[] lyDoList = {
            "Thưởng Nóng (Đột xuất)", "Thưởng Quý 1 (Jan-Mar)", "Thưởng Quý 2 (Apr-Jun)",
            "Thưởng Quý 3 (Jul-Sep)", "Thưởng Quý 4 (Oct-Dec)", "Thưởng Hoàn Thành Dự Án",
            "Thưởng Sinh Nhật Công Ty"
        };
        
        String lyDoChon = (String) JOptionPane.showInputDialog(solve, 
            "Chọn loại hình khen thưởng:", "Danh Mục Thưởng", 
            JOptionPane.PLAIN_MESSAGE, null, lyDoList, lyDoList[0]);

        if (lyDoChon == null) return;

        String moneyStr = JOptionPane.showInputDialog(solve, "Nhập số tiền thưởng cho mỗi nhân viên (VNĐ):", "1000000");
        if (moneyStr == null || moneyStr.trim().isEmpty()) return;

        long tienThuong = 0;
        try {
            tienThuong = Long.parseLong(moneyStr.replace(",", "").replace(".", ""));
            if (tienThuong <= 0) {
                JOptionPane.showMessageDialog(solve, "Tiền thưởng phải lớn hơn 0!");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(solve, "Nhập tiền sai định dạng!");
            return;
        }

        if (choice == 0) {
            if (solve.dao.congTienThuong(tienThuong)) {
                solve.dao.ghiLichSu("ALL", lyDoChon, "Mức thưởng: " + String.format("%,d", tienThuong), solve.taiKhoanHienTai);
                JOptionPane.showMessageDialog(solve, "✅ Đã chi thưởng [" + lyDoChon + "] cho TOÀN CÔNG TY!");
                solve.loadData("NV.MaNV ASC");
            } else {
                JOptionPane.showMessageDialog(solve, "❌ Lỗi kết nối!");
            }
        } 
        else if (choice == 1) {
            java.util.Set<String> danhSachPhong = new java.util.HashSet<>();
            for (int i = 0; i < solve.table.getRowCount(); i++) {
                danhSachPhong.add(solve.table.getValueAt(i, 2).toString());
            }
            if (danhSachPhong.isEmpty()) {
                JOptionPane.showMessageDialog(solve, "Danh sách trống!");
                return;
            }
            String[] cacPhong = danhSachPhong.toArray(new String[0]);
            String phongDuocChon = (String) JOptionPane.showInputDialog(solve, "Chọn phòng ban:", "Danh Sách Phòng", JOptionPane.QUESTION_MESSAGE, null, cacPhong, cacPhong[0]);

            if (phongDuocChon != null) {
                if (solve.dao.congTienThuongTheoPhong(phongDuocChon, tienThuong)) {
                    solve.dao.ghiLichSu("DEPT", lyDoChon, "Phòng: " + phongDuocChon + " - Mức thưởng: " + String.format("%,d", tienThuong), solve.taiKhoanHienTai);    
                    JOptionPane.showMessageDialog(solve, "✅ Đã chi thưởng [" + lyDoChon + "] cho phòng " + phongDuocChon.toUpperCase() + "!");
                    solve.loadData("NV.MaNV ASC");
                } else {
                    JOptionPane.showMessageDialog(solve, "❌ Lỗi cập nhật!");
                }
            }
        }
    }

    public void xuLyChotThang() {
        int confirm = JOptionPane.showConfirmDialog(solve, "BẠN CÓ CHẮC MUỐN CHỐT SỔ THÁNG NÀY?\n\nHành động này sẽ:\n- Xóa hết số ngày đi trễ.\n- Xóa hết tiền phạt.\n- Xóa hết tiền thưởng.\n\nĐể bắt đầu tính lương cho tháng mới.", "Cảnh báo Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (solve.dao.resetThangMoi()) {
                JOptionPane.showMessageDialog(solve, "✅ Đã reset dữ liệu cho tháng mới!");
                solve.loadData("NV.MaNV ASC");
            } else {
                JOptionPane.showMessageDialog(solve, "❌ Lỗi hệ thống!");
            }
        }
    }

    public void xuLyXuatExcel() {
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel File (*.csv)", "csv"));
        int userSelection = fileChooser.showSaveDialog(solve);
        
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(filePath), java.nio.charset.StandardCharsets.UTF_8))) {
                bw.write("\uFEFF"); 
                for (int i = 0; i < solve.table.getColumnCount(); i++) {
                    bw.write(solve.table.getColumnName(i));
                    if (i < solve.table.getColumnCount() - 1) bw.write(",");
                }
                bw.newLine();
                for (int i = 0; i < solve.table.getRowCount(); i++) {
                    for (int j = 0; j < solve.table.getColumnCount(); j++) {
                        String val = solve.table.getValueAt(i, j).toString();
                        val = val.replace(",", ""); 
                        bw.write(val);
                        if (j < solve.table.getColumnCount() - 1) bw.write(",");
                    }
                    bw.newLine();
                }
                JOptionPane.showMessageDialog(solve, "✅ Xuất file Excel thành công!\n" + filePath);
                java.awt.Desktop.getDesktop().open(new java.io.File(filePath));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(solve, "❌ Lỗi khi xuất file: " + ex.getMessage());
            }
        }
    }
    
    private String layTenNgayLe(LocalDate date) {
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        if (day == 1 && month == 1) return "Tết Dương Lịch";
        if (day == 30 && month == 4) return "Ngày Giải Phóng (30/4)";
        if (day == 1 && month == 5) return "Quốc Tế Lao Động (1/5)";
        if (day == 2 && month == 9) return "Quốc Khánh (2/9)";
        if (day == 25 && month == 12) return "Giáng Sinh (Noel)";
        return null;
    }



public void xuLyChamCongNgayLe() {
    LocalDate today = LocalDate.now();
    
    String tenLeDuongLich = layTenNgayLe(today); 

    int row = solve.table.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(solve, "Vui lòng chọn nhân viên để chấm công!");
        return;
    }

    String maNV = solve.table.getValueAt(row, 0).toString();
    String hoTen = solve.table.getValueAt(row, 1).toString();
    
    Object[] options = {
        "Lễ Âm Lịch / Thủ Công",  
        "Theo Dương Lịch (Auto)",  
        "❌ Hủy Bỏ"                   
    };

    int choice = JOptionPane.showOptionDialog(solve,
        "Hôm nay là: " + today + "\n" +
        "Hệ thống phát hiện lễ dương lịch: " + (tenLeDuongLich != null ? tenLeDuongLich : "Không có") + "\n\n" +
        "Bạn muốn chấm công cho [" + hoTen + "] theo chế độ nào?",
        "Chế Độ Chấm Công",
        JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[1]); 

    long heSoLuongNgay = 1; 
    String lyDo = "";
    
    if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) {
        return;
    }

    if (choice == 0) {
        String tenLeInput = JOptionPane.showInputDialog(solve, 
            "Nhập tên ngày lễ (Ví dụ: Mùng 1 Tết, Giỗ Tổ...):", 
            "Xác nhận Lễ Đặc Biệt", 
            JOptionPane.INFORMATION_MESSAGE);
            
        if (tenLeInput == null || tenLeInput.trim().isEmpty()) return;
        
        heSoLuongNgay = 3;
        lyDo = "Trực lễ: " + tenLeInput + " (x" + heSoLuongNgay + ")";
    }

    else if (choice == 1) {
        if (tenLeDuongLich != null) {

            heSoLuongNgay = 3;
            lyDo = "Trực lễ: " + tenLeDuongLich + " (x3 Auto)";
        } else {
            heSoLuongNgay = 1;
            lyDo = "Làm thêm ngày thường (" + today + ")";
        }
    }

    NhanVien nv = solve.dao.getNhanVienTheoMa(maNV); 
    long luong1Ngay = (long) ((nv.getLuongCoBan() * nv.getHeSoLuong()) / 26);
    long tienCongThem = luong1Ngay * heSoLuongNgay;

    int confirm = JOptionPane.showConfirmDialog(solve, 
        "XÁC NHẬN CHẤM CÔNG:\n" +
        "- Nhân viên: " + hoTen + "\n" +
        "- Chế độ: " + lyDo + "\n" +
        "- Tiền cộng thêm: " + String.format("%,d VNĐ", tienCongThem),
        "Thực Thi", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        solve.dao.congTienThuongChoNhanVien(maNV, tienCongThem);
        solve.dao.ghiLichSu(maNV, "Chấm công", lyDo + " - Tiền: " + String.format("%,d", tienCongThem), solve.taiKhoanHienTai);
        
 
        solve.loadData("NV.MaNV ASC"); 
        JOptionPane.showMessageDialog(solve, "✅ Đã chấm công thành công!");
    }
}
    
    public void xuLyTimKiemDaNang() { 
        // Lấy dữ liệu từ các ô nhập liệu của Sếp
        solve.lastMa = solve.txtMaNV.getText().trim();
        solve.lastTen = solve.txtHoTen.getText().trim();
        
        String selectedPhong = solve.cboPhongBan.getSelectedItem() != null ? solve.cboPhongBan.getSelectedItem().toString() : "";
        solve.lastPhong = selectedPhong.equals("Tất cả Phòng Ban") ? "" : selectedPhong;
        
        solve.lastLuong = solve.cboLuongCoBan.getSelectedItem() != null ? solve.cboLuongCoBan.getSelectedItem().toString() : "";
        
        solve.lastHeSo = solve.cboHeSo.getSelectedItem() != null ? solve.cboHeSo.getSelectedItem().toString() : "";
        
        solve.reloadTable("NV.MaNV ASC"); // Hàm này phải Public bên QuanLyNhanVien mới gọi được
    }
    
    public void xuLyTangLuong() { 
        int row = solve.table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(solve, "Vui lòng chọn nhân viên cần tăng lương!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maNV = solve.table.getValueAt(row, 0).toString();
        String hoTen = solve.table.getValueAt(row, 1).toString();
        String luongCuStr = solve.table.getValueAt(row, 3).toString().replace(",", "").replace(" VNĐ", "").trim();
        double luongCu = Double.parseDouble(luongCuStr);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Chọn hình thức tăng lương cho: " + hoTen));

        String[] options = { 
            "KPI Loại A (Xuất sắc - 20%)", 
            "KPI Loại B (Giỏi - 10%)", 
            "KPI Loại C (Khá - 5%)", 
            "Theo Thâm Niên (Tự động)", 
            "Nhập tay %" 
        };
        JComboBox<String> cboOption = new JComboBox<>(options);
        panel.add(cboOption);

        int result = JOptionPane.showConfirmDialog(solve, panel, "Xét Duyệt Tăng Lương", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            double luongMoi = 0;
            double phanTram = 0;
            int selectedIndex = cboOption.getSelectedIndex();

            try {
                if (selectedIndex == 0) luongMoi = XuLyTangLuong.tinhLuongTheoKPI(luongCu, "A");
                else if (selectedIndex == 1) luongMoi = XuLyTangLuong.tinhLuongTheoKPI(luongCu, "B");
                else if (selectedIndex == 2) luongMoi = XuLyTangLuong.tinhLuongTheoKPI(luongCu, "C");
                else if (selectedIndex == 3) { 
                    NhanVien nvFull = solve.dao.getNhanVienTheoMa(maNV); 
                    if (nvFull != null && nvFull.getNgayVaoLam() != null) {
                        luongMoi = XuLyTangLuong.tinhLuongTheoThamNien(luongCu, nvFull.getNgayVaoLam());
                        if (luongMoi == luongCu) {
                            JOptionPane.showMessageDialog(solve, "Nhân viên này thâm niên dưới 1 năm, chưa đủ điều kiện tăng!");
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(solve, "Không tìm thấy dữ liệu ngày vào làm!");
                        return;
                    }
                } else { 
                    String input = JOptionPane.showInputDialog(solve, "Nhập % muốn tăng:", "5");
                    if (input == null || input.trim().isEmpty()) return;
                    phanTram = Double.parseDouble(input);
                    luongMoi = luongCu * (1 + phanTram / 100);
                }

                if (selectedIndex <= 3) {
                    phanTram = ((luongMoi - luongCu) / luongCu) * 100;
                }

                String msg = String.format("Lương cũ: %,.0f VNĐ\nLương mới: %,.0f VNĐ\n(Tăng: %.1f%%)\n\nXác nhận cập nhật?",
                    luongCu, luongMoi, phanTram);

                int confirm = JOptionPane.showConfirmDialog(solve, msg, "Xác Nhận", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    solve.dao.tangLuong(maNV, phanTram);
                    String lyDo = options[selectedIndex]; 
                    String chiTiet = String.format("<html>- Lý do: %s<br>- Lương cũ: %,.0f VNĐ<br>- Lương mới: <font color='red'><b>%,.0f VNĐ</b></font> (Tăng %.1f%%)</html>", lyDo, luongCu, luongMoi, phanTram);
                    solve.dao.ghiLichSu(maNV, "Tăng Lương", chiTiet, solve.taiKhoanHienTai);
                    JOptionPane.showMessageDialog(solve, "Đã tăng lương thành công!");
                    solve.loadData("NV.MaNV ASC");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(solve, "Lỗi: " + ex.getMessage());
            }
        }
    }
    
    public void xuLyPhat() {
        int row = solve.table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(solve, "Vui lòng chọn nhân viên cần phạt!", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maNV = solve.table.getValueAt(row, 0).toString();
        String hoTen = solve.table.getValueAt(row, 1).toString();
        
        String input = JOptionPane.showInputDialog(solve, 
            "Nhập số ngày đi trễ của " + hoTen + ":\n(Ví dụ: 1, 2, 3...)", 
            "Xử Lý Vi Phạm", JOptionPane.QUESTION_MESSAGE);
            
        if (input != null && !input.trim().isEmpty()) {
            try {
                int soNgay = Integer.parseInt(input.trim());
                if (soNgay < 0) {
                    JOptionPane.showMessageDialog(solve, "Số ngày không được âm!");
                    return;
                }
                solve.dao.capNhatPhat(maNV, soNgay); 
                solve.dao.ghiLichSu(maNV, "Phạt đi trễ", "Số ngày trễ: " + soNgay, solve.taiKhoanHienTai);
                JOptionPane.showMessageDialog(solve, "✅ Đã ghi nhận phạt cho: " + hoTen);
                solve.loadData("NV.MaNV ASC");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(solve, "Vui lòng nhập số nguyên hợp lệ!");
            }
        }
    }
}