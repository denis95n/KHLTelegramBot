
import model.ClubModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class KHL {

    public static String getKHLClub (Message message, ClubModel model) throws IOException {
        URL url = new URL("https://khl.api.webcaster.pro/api/khl_mobile/feed_v2?team_id_in[]=40&q[official_eq]=1&page=1");

        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";

        while (scanner.hasNext()){
            result += scanner.next();
        }

        JSONObject json = new JSONObject(result);
        JSONArray jsonArray = json.getJSONArray("feed");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i+8);
            model.setId(obj.getInt("id"));
            model.setDate(obj.getInt("date"));
            model.setBody(obj.getString("body"));
            model.setImage(obj.getString("image"));
            model.setTitle(obj.getString("title"));
            break;
        }

        scanner.close();


        return model.getImage() + model.getTitle(); //+ " " + model.getBody() + " " + model.getDate() + " " + model.getImage() + " " + model.getTitle();
    }


}
