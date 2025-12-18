package ui;
																		// Giao diện Admin - Việt
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NhanVienUI extends JFrame {

    protected JTable table;
    protected DefaultTableModel model;
    protected JButton btnThem, btnSua, btnXoa, btnPhat, btnLoad, btnMoTinhLuong;
    
    protected JLabel lblMa, lblTen, lblPhong, lblLuong, lblHS, lblSort;
    protected JButton btnLamMoi, btnTangLuong, btnThongKe, btnBaoLoi;
    protected JButton btnTimKiem, btnQuanLyTK, btnGiamLuong, btnChotThang, btnXuatExcel, btnThuongNong;
    protected JButton btnSortMa, btnSortTen, btnSortLuong;
    
    protected JTextField txtMaNV;
    protected JTextField txtHoTen;
    protected JTextField txtPhongBan;
    protected JTextField txtLuongCoBan;
    protected JTextField txtHeSo;

    private static final long serialVersionUID = 2L;

    public NhanVienUI() {											// Hàm khởi tạo
        initUI();
    }

    protected void initUI() {										// Hàm 'Hiển thị'

        setTitle("Phần mềm Quản lý Nhân sự & Tiền lương Konami Enterprise");                // Khung
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(240, 242, 245));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(null);
        pnlHeader.setBackground(new Color(0, 102, 204));
        pnlHeader.setBounds(0, 0, 1000, 50);
        getContentPane().add(pnlHeader);
        
        JLabel lblTieuDe = new JLabel("HỆ THỐNG QUẢN TRỊ NHÂN SỰ");
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTieuDe.setForeground(Color.WHITE);
        lblTieuDe.setHorizontalAlignment(SwingConstants.CENTER);
        lblTieuDe.setBounds(0, 0, 1000, 50);
        pnlHeader.add(lblTieuDe);

        JPanel pnlInput = new JPanel();
        pnlInput.setLayout(null);
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBounds(10, 60, 965, 100);
        TitledBorder border = javax.swing.BorderFactory.createTitledBorder(null, " Thông Tin Nhân Viên ", 
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 13), new Color(0, 102, 204));
        pnlInput.setBorder(border);
        getContentPane().add(pnlInput);
        
        lblMa = new JLabel("Mã NV:");                                     	// Thùng chứa 'Mã NV'
        lblMa.setBounds(20, 25, 60, 25);
        lblMa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlInput.add(lblMa);
        txtMaNV = new JTextField();
        txtMaNV.setBounds(80, 25, 100, 25);
        txtMaNV.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlInput.add(txtMaNV);

        lblTen = new JLabel("Họ Tên:");                                     // Thùng chứa 'Họ Tên'
        lblTen.setBounds(200, 25, 60, 25);
        lblTen.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlInput.add(lblTen);
        txtHoTen = new JTextField();
        txtHoTen.setBounds(260, 25, 160, 25);
        txtHoTen.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlInput.add(txtHoTen);

        lblPhong = new JLabel("Phòng:");                                    // Thùng chứa 'Phòng'
        lblPhong.setBounds(440, 25, 60, 25);
        lblPhong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlInput.add(lblPhong);
        txtPhongBan = new JTextField();
        txtPhongBan.setBounds(500, 25, 120, 25);
        txtPhongBan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlInput.add(txtPhongBan);

        lblLuong = new JLabel("Lương:");                                    // Thùng chứa 'Lương'
        lblLuong.setBounds(20, 60, 60, 25);
        lblLuong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlInput.add(lblLuong);
        txtLuongCoBan = new JTextField();
        txtLuongCoBan.setBounds(80, 60, 100, 25);
        txtLuongCoBan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlInput.add(txtLuongCoBan);

        lblHS = new JLabel("Hệ số:");                                       // Thùng chứa 'Hệ số'
        lblHS.setBounds(200, 60, 60, 25);
        lblHS.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlInput.add(lblHS);
        txtHeSo = new JTextField();
        txtHeSo.setBounds(260, 60, 60, 25);
        txtHeSo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlInput.add(txtHeSo);

        btnThem = new JButton("➕ Thêm");                                 // Nút 'Thêm'
        btnThem.setBounds(650, 60, 95, 30);
        btnThem.setFont(new Font("Dialog", Font.BOLD, 12));
        btnThem.setBackground(new Color(76, 175, 80));
        btnThem.setForeground(Color.WHITE);
        pnlInput.add(btnThem);
        
        btnSua = new JButton("✏️ Sửa");                                    // Nút 'Sửa'
        btnSua.setBounds(750, 60, 95, 30);
        btnSua.setFont(new Font("Dialog", Font.BOLD, 12));
        btnSua.setBackground(new Color(33, 150, 243));
        btnSua.setForeground(Color.WHITE);
        pnlInput.add(btnSua);
        
        btnXoa = new JButton("🗑️ Xóa");                                    // Nút 'Xóa'
        btnXoa.setBounds(850, 60, 95, 30);
        btnXoa.setFont(new Font("Dialog", Font.BOLD, 12));
        btnXoa.setBackground(new Color(244, 67, 54));
        btnXoa.setForeground(Color.WHITE);
        pnlInput.add(btnXoa);
        
        btnLamMoi = new JButton("🔄 Làm Mới");                                  // Nút 'Làm Mới'
        btnLamMoi.setBounds(780, 22, 120, 30);
        btnLamMoi.setFont(new Font("Dialog", Font.BOLD, 12));
        btnLamMoi.setBackground(new Color(224, 224, 224));
        pnlInput.add(btnLamMoi);
        
        btnTimKiem = new JButton("🔍 Tìm Kiếm");                                // Nút 'Tìm Kiếm'
        btnTimKiem.setBounds(650, 22, 120, 30);
        btnTimKiem.setBackground(new Color(255, 193, 7));   
        btnTimKiem.setFont(new Font("Dialog", Font.BOLD, 12));
        pnlInput.add(btnTimKiem);

        btnSortMa = new JButton("Mã NV");                           			// Nút 'Sắp xếp Mã NV'
        btnSortMa.setBounds(160, 165, 80, 25);
        btnSortMa.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        getContentPane().add(btnSortMa);

        btnSortTen = new JButton("Họ Tên");                                 	// Nút 'Sắp xếp Họ Tên'
        btnSortTen.setBounds(250, 165, 80, 25);
        btnSortTen.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        getContentPane().add(btnSortTen);

        btnSortLuong = new JButton("Lương");                                	// Nút 'Sắp xếp Lương'
        btnSortLuong.setBounds(340, 165, 80, 25);
        btnSortLuong.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        getContentPane().add(btnSortLuong);
        
        btnPhat = new JButton("⚠️ Cập nhật Phạt");                          		// Nút 'Cập nhật Phạt'                                             
        btnPhat.setBounds(165, 570, 145, 35);                                                                                                   
        btnPhat.setFont(new Font("Dialog", Font.BOLD, 12));
        btnPhat.setBackground(new Color(233, 30, 99));
        btnPhat.setForeground(Color.WHITE);
        getContentPane().add(btnPhat);                      
        
        btnTangLuong = new JButton("💰 Tăng Lương");                             // Nút 'Tăng lương'
        btnTangLuong.setFont(new Font("Dialog", Font.BOLD, 12));
        btnTangLuong.setBounds(320, 570, 145, 35);
        getContentPane().add(btnTangLuong);
        
        btnGiamLuong = new JButton("💸 Giảm Lương");                    			// Nút 'Giảm lương'
        btnGiamLuong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnGiamLuong.setFont(new Font("Dialog", Font.BOLD, 12));
        btnGiamLuong.setBounds(475, 570, 145, 35); 
        getContentPane().add(btnGiamLuong);
        
        btnMoTinhLuong = new JButton("💰 Phiếu Lương");                     		// Nút 'Phiếu lương'
        btnMoTinhLuong.setFont(new Font("Dialog", Font.BOLD, 12));
        btnMoTinhLuong.setBounds(630, 570, 145, 35); 
        btnMoTinhLuong.setBackground(new Color(156, 39, 176));
        btnMoTinhLuong.setForeground(Color.WHITE);
        getContentPane().add(btnMoTinhLuong);
        
        btnThongKe = new JButton("📊 Thống Kê");                                // Nút 'Thống Kê'
        btnThongKe.setBounds(320, 620, 145, 35); 
        btnThongKe.setFont(new Font("Dialog", Font.BOLD, 12));  
        getContentPane().add(btnThongKe);
        
        btnQuanLyTK = new JButton("🔐 Quản lý TK");                 				// Nút 'Quản lý TK'
        btnQuanLyTK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnQuanLyTK.setBounds(10, 620, 145, 35);
        btnQuanLyTK.setFont(new Font("Dialog", Font.BOLD, 12));
        btnQuanLyTK.setBackground(Color.PINK);
        btnQuanLyTK.setVisible(false);
        getContentPane().add(btnQuanLyTK);

        btnLoad = new JButton("📂 Tải danh sách");                            	// Nút 'Tải danh sách'
        btnLoad.setBounds(800, 165, 175, 25);
        btnLoad.setFont(new Font("Dialog", Font.BOLD, 12));
        getContentPane().add(btnLoad);
        
        btnBaoLoi = new JButton("⚠️ Báo Lỗi");                          			// Nút 'Báo lỗi'
        btnBaoLoi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnBaoLoi.setFont(new Font("Dialog", Font.BOLD, 12));
        btnBaoLoi.setForeground(Color.RED);
        btnBaoLoi.setBounds(830, 620, 145, 35); 
        getContentPane().add(btnBaoLoi);
        
        btnChotThang = new JButton("🔄 Chốt Tháng");                    			// Nút 'Chốt tháng'
        btnChotThang.setFont(new Font("Dialog", Font.BOLD, 12));
        btnChotThang.setBackground(new Color(255, 99, 71));
        btnChotThang.setBounds(165, 620, 145, 35);
        btnChotThang.setForeground(Color.WHITE);
        getContentPane().add(btnChotThang);

        btnXuatExcel = new JButton("📊 Xuất Excel");                    			// Nút 'Xuất Excel'
        btnXuatExcel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnXuatExcel.setFont(new Font("Dialog", Font.BOLD, 12));
        btnXuatExcel.setBackground(new Color(60, 179, 113));
        btnXuatExcel.setBounds(475, 620, 145, 35);
        btnXuatExcel.setForeground(Color.WHITE);
        getContentPane().add(btnXuatExcel);
            
        btnThuongNong = new JButton("💰 Thưởng Nóng");              				// Nút 'Thưởng nóng'
        btnThuongNong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnThuongNong.setFont(new Font("Dialog", Font.BOLD, 12));
        btnThuongNong.setBounds(10, 570, 145, 35);
        btnThuongNong.setBackground(new Color(255, 152, 0));
        getContentPane().add(btnThuongNong);
        
        lblSort = new JLabel("Sắp xếp theo:");                                  // Nhãn 'Sắp xếp theo'
        lblSort.setBounds(10, 165, 100, 25);
        lblSort.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        getContentPane().add(lblSort);
        
        String[] columns = {"Mã NV", "Họ Tên", "Phòng Ban", "Lương Cứng", "Hệ Số", "Tổng Nhận"};        // Cột
        model = new DefaultTableModel(columns, 0);                  // Model
        
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model) {									// Table
            private static final long serialVersionUID = 2L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };                                  
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane sp = new JScrollPane(table);                    // Thanh cuộn
        sp.setBounds(10, 195, 965, 360); 
        getContentPane().add(sp);
    }
}