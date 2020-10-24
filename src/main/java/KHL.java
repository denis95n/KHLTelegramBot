
import model.ClubModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class KHL {

    public static String getKHLClub(Message message, ClubModel model) throws Exception {
        URL url = new URL("https://khl.api.webcaster.pro/api/khl_mobile/feed_v2?team_id_in[]=40&q[official_eq]=1&page=1");

        Scanner scanner = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();

        while (scanner.hasNext()) {
            result.append(scanner.next()).append(" ");
        }

        JSONObject json = new JSONObject(result.toString());
        JSONArray jsonArray = json.getJSONArray("feed");
        int lengthJson = jsonArray.length();

        for (int i = 0; i < lengthJson-1; i++) {//14
            JSONObject obj = jsonArray.getJSONObject(i);

            if (obj.has("title")) {
                if (obj.getString("title").contains("«Ак Барс». Всё об игре")) {
                    model.setId(obj.getInt("id"));
                    model.setDate(obj.getInt("date"));
                    model.setBody(getFirstLineToString(obj.getString("body")));
                    model.setImage(obj.getString("image"));
                    model.setTitle(obj.getString("title"));
                    break;
                }
            }
        }

        scanner.close();


        return model.getImage() + model.getTitle() + "\n\n" + model.getBody(); //+ " " + model.getBody() + " " + model.getDate() + " " + model.getImage() + " " + model.getTitle();
    }

    public static String getFirstLineToString (String body ) throws Exception{
        Scanner scanner = new Scanner(body.trim());

        return scanner.nextLine().replaceAll("\\<p>|</p>", "");
    }
}
