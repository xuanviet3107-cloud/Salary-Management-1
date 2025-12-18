package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class ChuyenDuLieu {

    public static void main(String[] args) {
        System.out.println(">>> ĐANG BẮT ĐẦU CHUYỂN DỮ LIỆU TỪ SQL SERVER SANG SQLITE...");
        
        try {
            String sqlUrl = "jdbc:sqlserver://localhost:1433;databaseName=Konami;encrypt=true;trustServerCertificate=true";
            String sqlUser = "sa";
            String sqlPass = "123456";

            String sqliteUrl = "jdbc:sqlite:konami_data.db?date_string_format=yyyy-MM-dd";

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection connSQL = DriverManager.getConnection(sqlUrl, sqlUser, sqlPass);
            
            Class.forName("org.sqlite.JDBC");
            Connection connSQLite = DriverManager.getConnection(sqliteUrl);
            connSQLite.setAutoCommit(false); 

            Statement stmt = connSQLite.createStatement();
            stmt.executeUpdate("DELETE FROM NhanVien"); 
            stmt.executeUpdate("DELETE FROM PhongBan"); 
            stmt.close();

            System.out.println(">>> Đã xóa dữ liệu mẫu cũ trong SQLite.");

            String queryPB = "SELECT * FROM PhongBan";
            PreparedStatement pstSQL_PB = connSQL.prepareStatement(queryPB);
            ResultSet rsPB = pstSQL_PB.executeQuery();
            
            String insertPB = "INSERT INTO PhongBan (MaPB, TenPB) VALUES (?, ?)";
            PreparedStatement pstLite_PB = connSQLite.prepareStatement(insertPB);

            int countPB = 0;
            while (rsPB.next()) {
                pstLite_PB.setString(1, rsPB.getString("MaPB"));
                pstLite_PB.setString(2, rsPB.getString("TenPB"));
                pstLite_PB.executeUpdate();
                countPB++;
            }
            System.out.println(">>> Đã chuyển xong " + countPB + " Phòng ban.");

            String queryNV = "SELECT * FROM NhanVien";
            PreparedStatement pstSQL_NV = connSQL.prepareStatement(queryNV);
            ResultSet rsNV = pstSQL_NV.executeQuery();

            String insertNV = "INSERT INTO NhanVien (MaNV, HoTen, MaPB, LuongCoBan, HeSoLuong, NgayVaoLam, SoNgayDiTre, TienPhat, TienThuong) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstLite_NV = connSQLite.prepareStatement(insertNV);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int countNV = 0;
            while (rsNV.next()) {
                pstLite_NV.setString(1, rsNV.getString("MaNV"));
                pstLite_NV.setString(2, rsNV.getString("HoTen"));
                pstLite_NV.setString(3, rsNV.getString("MaPB"));
                pstLite_NV.setLong(4, rsNV.getLong("LuongCoBan"));
                pstLite_NV.setFloat(5, rsNV.getFloat("HeSoLuong"));
                
                try {
                    pstLite_NV.setString(6, sdf.format(rsNV.getDate("NgayVaoLam")));
                } catch (Exception e) {
                    pstLite_NV.setString(6, "2024-01-01"); 
                }

                pstLite_NV.setInt(7, rsNV.getInt("SoNgayDiTre"));
                pstLite_NV.setLong(8, rsNV.getLong("TienPhat"));
                pstLite_NV.setLong(9, rsNV.getLong("TienThuong"));
                
                pstLite_NV.addBatch();
                countNV++;
            }
            pstLite_NV.executeBatch();
            connSQLite.commit();
            
            System.out.println(">>> Đã chuyển xong " + countNV + " Nhân viên.");
            System.out.println(">>> THÀNH CÔNG RỰC RỠ! GIỜ CẬU CÓ THỂ CHẠY APP ĐƯỢC RỒI.");

            connSQL.close();
            connSQLite.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}