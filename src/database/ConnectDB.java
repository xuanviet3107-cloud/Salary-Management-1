package database;

import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ConnectDB {
    
    private static final String CONFIG_FILE = "database.config";

    public static Connection getConnection() {
        Connection conn = null;
        Properties props = new Properties();
        
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            props.load(in);
        } catch (Exception e) {
            props.setProperty("dbType", "SQL Server");
            props.setProperty("host", "localhost");
            props.setProperty("port", "1433");
            props.setProperty("dbName", "Konami");
            props.setProperty("user", "sa");
            props.setProperty("pass", "sai_mat_khau_de_hien_bang_cau_hinh");
        }

        conn = tryConnect(props);

        while (conn == null) {
            boolean userMuonThoat = showConfigDialog(props);
            if (userMuonThoat) {
                System.exit(0);
            }
            conn = tryConnect(props);
        }
        
        return conn;
    }

    private static Connection tryConnect(Properties props) {
        try {
            String type = props.getProperty("dbType", "SQL Server");
            
            if (type.equals("SQLite")) {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:konami_data.db?date_string_format=yyyy-MM-dd";
                Connection c = DriverManager.getConnection(url);
                
                taoBangSQLiteNeuChuaCo(c);
                
                saveConfig(props);
                System.out.println(">>> KẾT NỐI SQLITE THÀNH CÔNG!");
                return c;
            } else {
                String host = props.getProperty("host");
                String port = props.getProperty("port");
                String dbName = props.getProperty("dbName");
                String user = props.getProperty("user");
                String pass = props.getProperty("pass");

                String dbURL = "jdbc:sqlserver://" + host + ":" + port +
                               ";databaseName=" + dbName +
                               ";encrypt=true;trustServerCertificate=true;";
                
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                Connection c = DriverManager.getConnection(dbURL, user, pass);
                
                saveConfig(props);
                System.out.println(">>> KẾT NỐI SQL SERVER THÀNH CÔNG!");
                return c;
            }
        } catch (Exception e) {
            System.out.println(">>> Kết nối thất bại: " + e.getMessage());
            return null;
        }
    }

    private static void taoBangSQLiteNeuChuaCo(Connection c) {
        try {
            Statement stmt = c.createStatement();
            
            String sqlPhong = "CREATE TABLE IF NOT EXISTS PhongBan (" +
                              "MaPB TEXT PRIMARY KEY, " +
                              "TenPB TEXT)";
            stmt.execute(sqlPhong);

            String sqlNV = "CREATE TABLE IF NOT EXISTS NhanVien (" +
                           "MaNV TEXT PRIMARY KEY, " +
                           "HoTen TEXT, " +
                           "MaPB TEXT, " +
                           "LuongCoBan INTEGER, " +
                           "HeSoLuong REAL, " +
                           "NgayVaoLam TEXT, " + 
                           "SoNgayDiTre INTEGER DEFAULT 0, " +
                           "TienPhat INTEGER DEFAULT 0, " +
                           "TienThuong INTEGER DEFAULT 0, " +
                           "FOREIGN KEY(MaPB) REFERENCES PhongBan(MaPB))";
            stmt.execute(sqlNV);

            String sqlCheckData = "SELECT COUNT(*) FROM PhongBan";
            java.sql.ResultSet rs = stmt.executeQuery(sqlCheckData);
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO PhongBan VALUES ('PB01', 'Phòng Kỹ Thuật')");
                stmt.execute("INSERT INTO PhongBan VALUES ('PB02', 'Phòng Nhân Sự')");
                stmt.execute("INSERT INTO PhongBan VALUES ('PB03', 'Phòng Kinh Doanh')");
                
                // Định dạng ngày tháng trong SQLite là chuỗi YYYY-MM-DD
                stmt.execute("INSERT INTO NhanVien (MaNV, HoTen, MaPB, LuongCoBan, HeSoLuong, NgayVaoLam) VALUES ('NV01', 'Nguyễn Văn Mẫu', 'PB01', 10000000, 1.5, '2023-01-01')");
                stmt.execute("INSERT INTO NhanVien (MaNV, HoTen, MaPB, LuongCoBan, HeSoLuong, NgayVaoLam) VALUES ('NV02', 'Trần Thị Demo', 'PB02', 8000000, 1.2, '2024-05-15')");
                System.out.println(">>> Đã khởi tạo dữ liệu mẫu (kèm Ngày vào làm) cho SQLite!");
            }
            
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean showConfigDialog(Properties props) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        String[] types = {"SQL Server", "SQLite"};
        JComboBox<String> cbType = new JComboBox<>(types);
        cbType.setSelectedItem(props.getProperty("dbType"));

        JTextField txtHost = new JTextField(props.getProperty("host"));
        JTextField txtPort = new JTextField(props.getProperty("port"));
        JTextField txtDbName = new JTextField(props.getProperty("dbName"));
        JTextField txtUser = new JTextField(props.getProperty("user"));
        JPasswordField txtPass = new JPasswordField(props.getProperty("pass"));

        panel.add(new JLabel("Loại CSDL:"));      panel.add(cbType);
        panel.add(new JLabel("Host (IP):"));    panel.add(txtHost);
        panel.add(new JLabel("Port:"));         panel.add(txtPort);
        panel.add(new JLabel("Database Name:"));panel.add(txtDbName);
        panel.add(new JLabel("SQL User:"));     panel.add(txtUser);
        panel.add(new JLabel("Password:"));     panel.add(txtPass);

        int result = JOptionPane.showConfirmDialog(null, panel, 
                "⚠️ Lỗi kết nối CSDL! Kiểm tra lại thông số", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            props.setProperty("dbType", cbType.getSelectedItem().toString());
            props.setProperty("host", txtHost.getText());
            props.setProperty("port", txtPort.getText());
            props.setProperty("dbName", txtDbName.getText());
            props.setProperty("user", txtUser.getText());
            props.setProperty("pass", new String(txtPass.getPassword()));
            return false; 
        }
        return true; 
    }

    private static void saveConfig(Properties props) {
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.store(out, "Konami App Database Configuration");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}