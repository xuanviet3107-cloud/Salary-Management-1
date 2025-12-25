package logic;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class ThuatToanTangLuong {

    public static double tinhPhuCapThamNien(Date ngayVaoLam) {
        if (ngayVaoLam == null) return 0;
        
        LocalDate start = ngayVaoLam.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int soNam = Period.between(start, LocalDate.now()).getYears();

        if (soNam >= 5) {
            return 0.05 + ((soNam - 5) * 0.01); // 5 năm = 5%, mỗi năm sau +1%
        }
        return 0; // Dưới 5 năm chưa có
    }

    
    public static float deXuatHeSoMoi(java.util.Date ngayVaoLam, float heSoHienTai) {
        // Mặc định hệ số khởi điểm là Đại học (2.34)
        float heSoKhoiDiem = 2.34f; 
        
        if (ngayVaoLam == null) return heSoKhoiDiem;

        LocalDate start;
        if (ngayVaoLam instanceof java.sql.Date) {
            start = ((java.sql.Date) ngayVaoLam).toLocalDate();
        } else {
            start = ngayVaoLam.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        // 1. Tính thâm niên
        int soNam = Period.between(start, LocalDate.now()).getYears();
        
        // 2. Tính số bậc đáng lẽ phải tăng (3 năm 1 bậc)
        int soLanTang = soNam / 3; 
        if (soLanTang > 9) soLanTang = 9; // Chặn trần

        // 3. [THUẬT TOÁN ĐOÁN TRACK LƯƠNG]
        // Ước lượng hệ số gốc = Hệ số hiện tại - (Số lần đã tăng * 0.33)
        // Ví dụ: Bà Lựu (1.86, 0 năm) -> Gốc = 1.86 - 0 = 1.86
        float heSoGocUocLuong = heSoHienTai - (soLanTang * 0.33f);

        // Phân loại dựa trên ước lượng
        if (heSoGocUocLuong < 1.95f) {
            heSoKhoiDiem = 1.86f; // Track Trung cấp / Cán sự
        } else if (heSoGocUocLuong < 2.2f) {
            heSoKhoiDiem = 2.10f; // Track Cao đẳng
        } else {
            heSoKhoiDiem = 2.34f; // Track Đại học
        }

        // 4. Tính hệ số đề xuất theo Track đúng
        float heSoMoi = heSoKhoiDiem + (soLanTang * 0.33f);
        
        // Làm tròn
        return (float) (Math.round(heSoMoi * 100.0) / 100.0);
    }

    public static double tinhLuongTheoKPI(double luongCu, String xepLoaiKPI) {
        double phanTramTang = 0;
        
        switch (xepLoaiKPI.toUpperCase()) { // toUpperCase để chấp nhận cả 'a' và 'A'
            case "A": // Xuất sắc
                phanTramTang = 0.20; 
                break;
            case "B": // Giỏi
                phanTramTang = 0.10; 
                break;
            case "C": // Khá
                phanTramTang = 0.05; 
                break;
            default: // D hoặc F thì nhịn
                phanTramTang = 0;
        }
        
        return luongCu * (1 + phanTramTang);
    }
}