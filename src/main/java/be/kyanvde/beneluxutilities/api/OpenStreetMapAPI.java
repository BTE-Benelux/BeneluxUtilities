package be.kyanvde.beneluxutilities.api;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class OpenStreetMapAPI extends API{


    public static CompletableFuture<double[]> getCoordinatesFromAdress(String address) {
        CompletableFuture<double[]> completableFuture = new CompletableFuture<>();

        address = address.replaceAll(" ", "+");

        API.getAsync("https://nominatim.openstreetmap.org/search?q="+address+"&format=jsonv2", new ApiResponseCallback() {
            @Override
            public void onResponse(String response) {
                JSONArray responseArray = API.createJSONArray(response);
                JSONObject responseObject = (JSONObject) responseArray.get(0);

                double lat = Double.parseDouble((String) responseObject.get("lat"));
                double lon = Double.parseDouble((String) responseObject.get("lon"));

                completableFuture.complete(new double[]{lat, lon});
            }

            @Override
            public void onFailure(IOException e) {
                Bukkit.getLogger().severe("Failed to get the coordinates of an adress through the OSM API!");
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }
}
