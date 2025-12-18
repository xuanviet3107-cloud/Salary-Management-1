package logic;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhook {

    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1451057706835378344/wzeFzpek6mZyns6Kuxm3SRq2WEsjFaTvrZGAGHmHN_k50RqGlXQuLC7MI9HAHQc9NTJM";

    public static void guiThongBao(String tieuDe, String noiDung) {
        if (WEBHOOK_URL.contains("...")) {
            System.err.println("Chưa cài đặt Discord Webhook URL!");
            return;
        }

        try {
            String jsonPayload = "{\"content\": \"🚨 **BÁO LỖI HỆ THỐNG**\\n**Tiêu đề:** " + tieuDe + "\\n**Nội dung:** " + noiDung + "\"}";

            URL url = java.net.URI.create(WEBHOOK_URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            conn.getResponseCode(); 
            conn.disconnect();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}