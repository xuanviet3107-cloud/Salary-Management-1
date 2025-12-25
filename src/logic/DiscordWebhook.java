package logic;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhook {

    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1451057706835378344/wzeFzpek6mZyns6Kuxm3SRq2WEsjFaTvrZGAGHmHN_k50RqGlXQuLC7MI9HAHQc9NTJM"; 

    public static boolean guiThongBao(String tieuDe, String noiDung) {
        // [Bước 0] Kiểm tra URL
        if (WEBHOOK_URL.contains("...")) {
            System.err.println("Chưa cài URL Webhook!");
            return false; 
        }

        HttpURLConnection conn = null;

        try {
            // [Bước 1] Xử lý ký tự đặc biệt & Null (Đã thêm check null/empty kỹ hơn)
            if (tieuDe == null) tieuDe = "Thông báo";
            if (noiDung == null) noiDung = "Không có nội dung";
            
            String safeTieuDe = escapeJson(tieuDe);
            String safeNoiDung = escapeJson(noiDung);

            // [Bước 2] Tạo JSON Payload
            String jsonPayload = "{\"content\": \"🚨 **BÁO LỖI HỆ THỐNG**\\n**Tiêu đề:** " + safeTieuDe + "\\n**Nội dung:** " + safeNoiDung + "\"}";

            // [Bước 3] Tạo kết nối (URL chuẩn)
            URL url = java.net.URI.create(WEBHOOK_URL).toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("User-Agent", "Java-DiscordWebhook-Bot");
            conn.setDoOutput(true);

            // [Bước 4] Gửi dữ liệu đi
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // [Bước 5] Kiểm tra phản hồi
            int responseCode = conn.getResponseCode();
            
            if (responseCode >= 200 && responseCode < 300) {
                return true; // ✅ Thành công
            } else {
                System.err.println("❌ Lỗi HTTP Discord: " + responseCode);
                return false; // ❌ Thất bại
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // [QUAN TRỌNG] Ngắt kết nối ở đây để đảm bảo luôn chạy
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static String escapeJson(String raw) {
        if (raw == null) return "";
        return raw.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}