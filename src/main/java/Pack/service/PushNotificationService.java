package Pack.service;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PushNotificationService {
   
    private static String firebaseSdkPath = "src\\main\\resources\\static\\poscoapp-firebase-adminsdk-nvbh5-c024db5217.json";
//    private static String firebaseSdkPath = "D:\\poscoapp-firebase-adminsdk-nvbh5-c024db5217.json";
//    private static String firebaseSdkPath = "/home/ubuntu/poscoapp-firebase-adminsdk-nvbh5-c024db5217.json";
	
    private static final String PROJECT_ID = "poscoapp";
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };
    public static final String MESSAGE_KEY = "message";
   
    private static HttpURLConnection getConnection() throws IOException {
        // [START use_access_token]
        URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        System.out.println(getAccessToken());
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken().getTokenValue());
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;
        // [END use_access_token]
    }

    private static AccessToken getAccessToken() throws IOException {
        //21.6.23 아직까지도 공식 홈페이지에서 Deprecated 된 해당 문장을 수정하지 않고있다.
        //22.01.04 공식 홈페이지에서 제대로 수정이 되었다.
    	System.out.println(firebaseSdkPath);
		FileInputStream serviceAccount =new FileInputStream(firebaseSdkPath);
//		FirebaseOptions options = new FirebaseOptions.Builder()
//		  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//		  .build();
//		
//		FirebaseApp.initializeApp(options);

    	
        GoogleCredentials googleCredential = GoogleCredentials
                .fromStream(serviceAccount)
                .createScoped(Arrays.asList(SCOPES));
        System.out.println(googleCredential);
        googleCredential.refresh();
        System.out.println(googleCredential.getAccessToken());
        return googleCredential.getAccessToken();
    }

    private static String inputstreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }
    
    public static void sendMessage(JsonObject fcmMessage) throws IOException {
        HttpURLConnection connection = getConnection();
        System.out.println(connection);
        connection.setDoOutput(true);
//        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(fcmMessage.toString().getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            String response = inputstreamToString(connection.getInputStream());
            System.out.println("Message sent to Firebase for delivery, response:");
            System.out.println(response);
        } else {
            System.out.println("Unable to send message to Firebase:");
            String response = inputstreamToString(connection.getErrorStream());
            System.out.println(response);
        }
    }
    
    private static JsonObject buildNotificationMessage(String title, String body, String topic) throws UnsupportedEncodingException {
        JsonObject jNotification = new JsonObject();
        jNotification.addProperty("title", title);// URLEncoder.encode(title,"UTF-8"));
        jNotification.addProperty("body", body);//URLEncoder.encode(body,"UTF-8"));

        JsonObject jMessage = new JsonObject();
        jMessage.add("notification", jNotification);
        /*
            firebase
            1. topic
            2. token
            3. condition -> multiple topic
         */
//        jFcm.add("Authorization", "aca9d4807c8248b2bc15c09ff8fe61e8fb672736c024db521798ee1578046705faa89f55beb3980f");
        jMessage.addProperty("topic", topic);
        //jMessage.addProperty("token", /* your test device token */);
        JsonObject jFcm = new JsonObject();
        jFcm.add(MESSAGE_KEY, jMessage);
        return jFcm;
    }
    
    public static void sendCommonMessage(String title, String body, String topic) throws IOException {
        JsonObject notificationMessage = buildNotificationMessage(title, body, topic);
        System.out.println("FCM request body for message using common notification object:");
        prettyPrint(notificationMessage);
        sendMessage(notificationMessage);
    }
    
    private static void prettyPrint(JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(jsonObject) + "\n");
    }
}