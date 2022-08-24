import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner userInput = new Scanner(System.in);
        final String API_KEY = "b7b4730ac54f4a839351c448d1240aa7";
        Map<String, String> paramsQueryIngredients = new HashMap<>();
        Map<String, String> paramsQueryURL = new HashMap<>();  
        String queryIngredients = "https://api.spoonacular.com/recipes/complexSearch?sort=random&number=3&";
        String queryRecipes = "https://api.spoonacular.com/recipes/";

        paramsQueryURL.put("apiKey", API_KEY);
        paramsQueryURL.put("includeNutrition", "false");

        System.out.println("Ingredients que la recette doit contenir (séparés par une virgule et en anglais) : ");
        String ingredients = userInput.nextLine();
        paramsQueryIngredients.put("apiKey", API_KEY);
        paramsQueryIngredients.put("includeIngredients", ingredients);  
        String encodedQueryIngredients = encodeQuery(queryIngredients, paramsQueryIngredients);
        JSONObject queryResponse = getJSONFromAPIResponse(encodedQueryIngredients);
        JSONArray resultsArray = queryResponse.getJSONArray("results");
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject recipe = resultsArray.getJSONObject(i);
            int recipeID = recipe.getInt("id");
            String encodedQueryRecipes = encodeQuery(queryRecipes + recipeID + "/information?", paramsQueryURL);
            JSONObject recipeData = getJSONFromAPIResponse(encodedQueryRecipes);
            String recipeURL = recipeData.getString("sourceUrl");
            System.out.println(recipeURL);
        }
        userInput.close();
    }

    public static String encodeQuery(String baseQuery, Map<String, String> params){
        String paramsAsString = params.keySet().stream()
        .map(key -> {
            try {
                return key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return key;
        })
        .collect(Collectors.joining("&"));      
        String encodedQuery = baseQuery + paramsAsString;
        return encodedQuery;
    }
    
    public static JSONObject getJSONFromAPIResponse(String query) throws IOException, JSONException {
        URL url = new URL(query);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36"); 
        conn.connect();
        int responsecode = conn.getResponseCode();
        if (responsecode != 200) {
            throw new RuntimeException("Le serveur a renvoyé la réponse HTTP suivante : " + responsecode);
        } else {
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String data;
            while ((data = reader.readLine()) != null)
            return new JSONObject(data);
        }
        return null;
	}
}

