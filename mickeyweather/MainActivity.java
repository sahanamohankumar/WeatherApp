package com.example.mickeyweather;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String apiKey = "449970616fbf8b3ea577c115df52d6bc";
    private double latitude;
    private double longitude;
    private String name;
    private String zipCode;
    TextView temp, date, feel,min,max,date2,feel2,min2,max2,date3,feel3,min3,max3,date4,feel4,min4,max4,date5,feel5,min5,max5,quote;
    Button button;
    EditText editText;
    MediaPlayer mediaPlayer;
    ImageView image1,image2,image3,image4,image5;
    private ArrayList<Integer> imageSources = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.avatarlove);
        mediaPlayer.start();
        temp = findViewById(R.id.temp);
        date = findViewById(R.id.date);
        feel = findViewById(R.id.feel);
        min = findViewById(R.id.minTemp);
        max = findViewById(R.id.maxTemp);
        editText = findViewById(R.id.editTextText);
        button = findViewById(R.id.button);
        date2 = findViewById(R.id.date2);
        feel2 = findViewById(R.id.desc2);
        min2 = findViewById(R.id.min2);
        max2 = findViewById(R.id.max2);
        date3 = findViewById(R.id.date3);
        feel3 = findViewById(R.id.desc3);
        min3 = findViewById(R.id.min3);
        max3 = findViewById(R.id.max3);
        date4 = findViewById(R.id.date4);
        feel4 = findViewById(R.id.desc4);
        min4 = findViewById(R.id.min4);
        max4 = findViewById(R.id.max4);
        date5 = findViewById(R.id.date5);
        feel5 = findViewById(R.id.desc5);
        min5 = findViewById(R.id.min5);
        max5 = findViewById(R.id.max5);
        quote=findViewById(R.id.quote);
        image1 = findViewById(R.id.imageView);
        image2 = findViewById(R.id.imageView2);
        image3 = findViewById(R.id.imageView3);
        image4 = findViewById(R.id.imageView4);
        image5 = findViewById(R.id.imageView5);
        imageSources.add(R.drawable.aangatla);
        imageSources.add(R.drawable.zuko);
        imageSources.add(R.drawable.kataratwo);
        imageSources.add(R.drawable.toph);
        imageSources.add(R.drawable.sokka);

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                zipCode = editText.getText().toString();
                if (isValidZipCode(zipCode)) {
                    new AsyncThread().execute(zipCode);
                    Log.d("button", "info: " + editText.getText());
                } else {
                    Toast.makeText(MainActivity.this, "Invalid ZIP code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class AsyncThread extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            String zipCode = strings[0];
            try {
                String geoUrlStr = "https://api.openweathermap.org/geo/1.0/zip?zip=" + zipCode + ",US&appid=" + apiKey;
                URL geoUrl = new URL(geoUrlStr);

                HttpURLConnection geoUrlConnection = (HttpURLConnection) geoUrl.openConnection();
                geoUrlConnection.setRequestMethod("GET");

                InputStream geoInputStream = geoUrlConnection.getInputStream();

                BufferedReader geoReader = new BufferedReader(new InputStreamReader(geoInputStream));
                StringBuilder geoResponse = new StringBuilder();
                String geoLine;

                while ((geoLine = geoReader.readLine()) != null) {
                    geoResponse.append(geoLine);
                }
                geoReader.close();
                geoInputStream.close();

                JSONObject geoJsonObject = new JSONObject(geoResponse.toString());
                latitude = geoJsonObject.getDouble("lat");
                longitude = geoJsonObject.getDouble("lon");
                name = geoJsonObject.getString("name");
                Log.d("mainactivity", "doInBackground: " + name);

                String forecastUrlStr = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&units=imperial";
                URL forecastUrl = new URL(forecastUrlStr);

                HttpURLConnection forecastUrlConnection = (HttpURLConnection) forecastUrl.openConnection();
                forecastUrlConnection.setRequestMethod("GET");

                InputStream forecastInputStream = forecastUrlConnection.getInputStream();

                BufferedReader forecastReader = new BufferedReader(new InputStreamReader(forecastInputStream));
                StringBuilder forecastResponse = new StringBuilder();
                String forecastLine;

                while ((forecastLine = forecastReader.readLine()) != null) {
                    forecastResponse.append(forecastLine);
                }
                forecastReader.close();
                forecastInputStream.close();

                return new JSONObject(forecastResponse.toString());
            } catch (Exception e) {
                Log.e("MainActivity", "Invalid zip code: " + zipCode);
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            ArrayList<Weather> WeatherList = new ArrayList<>();
            if (jsonObject != null) {
                try {
                    if (!isValidZipCode(zipCode)) {
                        Toast.makeText(MainActivity.this, "Invalid zip code", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONArray list = jsonObject.getJSONArray("list");

                    for (int i = 0; i < list.length(); i+=8) {
                        JSONObject item = list.getJSONObject(i);
                        String dtTxt = item.getString("dt_txt");
                        JSONObject main = item.getJSONObject("main");
                        double temperature = main.getDouble("temp");
                        double tempMin = main.getDouble("temp_min");
                        double tempMax = main.getDouble("temp_max");
                        JSONArray weatherArray = item.getJSONArray("weather");
                        JSONObject weather = weatherArray.getJSONObject(0);
                        String description = weather.getString("description");

                        Weather weatherObject = new Weather(tempMin, tempMax, description, temperature, latitude, longitude, dtTxt, name);
                        WeatherList.add(weatherObject);
                    }

                    date.setText(WeatherList.get(0).getDate().substring(5,10));
                    temp.setText(WeatherList.get(0).getCurrentTemp()+"");
                    min.setText(WeatherList.get(0).getMinTemp()+"");
                    max.setText(WeatherList.get(0).getMaxTemp()+"");
                    feel.setText(WeatherList.get(0).getDescription());
                    quote.setText(getQuote(WeatherList.get(0).getDescription()));
                   
                    date2.setText(WeatherList.get(1).getDate().substring(5,10));
                    min2.setText(WeatherList.get(1).getMinTemp()+"");
                    max2.setText(WeatherList.get(1).getMaxTemp()+"");
                    feel2.setText(WeatherList.get(1).getDescription());

                    date3.setText(WeatherList.get(2).getDate().substring(5,10));
                    min3.setText(WeatherList.get(2).getMinTemp()+"");
                    max3.setText(WeatherList.get(2).getMaxTemp()+"");
                    feel3.setText(WeatherList.get(2).getDescription());

                    date4.setText(WeatherList.get(3).getDate().substring(5,10));
                    min4.setText(WeatherList.get(3).getMinTemp()+"");
                    max4.setText(WeatherList.get(3).getMaxTemp()+"");
                    feel4.setText(WeatherList.get(3).getDescription());

                    date5.setText(WeatherList.get(4).getDate().substring(5,10));
                    min5.setText(WeatherList.get(4).getMinTemp()+"");
                    max5.setText(WeatherList.get(4).getMaxTemp()+"");
                    feel5.setText(WeatherList.get(4).getDescription());

                    String description = WeatherList.get(0).getDescription().toLowerCase();
                    if (description.contains("clear")) {
                        image1.setImageResource(R.drawable.aangatla);
                        image2.setImageResource(R.drawable.kataratwo);
                        image3.setImageResource(R.drawable.sokka);
                        image4.setImageResource(R.drawable.toph);
                        image5.setImageResource(R.drawable.zuko);
                    } else if (description.contains("cloud")) {
                        image1.setImageResource(R.drawable.zuko);
                        image2.setImageResource(R.drawable.aangatla);
                        image3.setImageResource(R.drawable.kataratwo);
                        image4.setImageResource(R.drawable.sokka);
                        image5.setImageResource(R.drawable.toph);
                    } else if (description.contains("rain")) {
                        image1.setImageResource(R.drawable.kataratwo);
                        image2.setImageResource(R.drawable.zuko);
                        image3.setImageResource(R.drawable.aangatla);
                        image4.setImageResource(R.drawable.toph);
                        image5.setImageResource(R.drawable.sokka);
                    } else if (description.contains("sun")) {
                        image1.setImageResource(R.drawable.toph);
                        image2.setImageResource(R.drawable.kataratwo);
                        image3.setImageResource(R.drawable.sokka);
                        image4.setImageResource(R.drawable.aangatla);
                        image5.setImageResource(R.drawable.zuko);
                    } else if (description.contains("wind")) {
                        image1.setImageResource(R.drawable.sokka);
                        image2.setImageResource(R.drawable.kataratwo);
                        image3.setImageResource(R.drawable.aangatla);
                        image4.setImageResource(R.drawable.toph);
                        image5.setImageResource(R.drawable.zuko);
                    } else if (description.contains("snow")) {
                        image1.setImageResource(R.drawable.sokka);
                        image2.setImageResource(R.drawable.aangatla);
                        image3.setImageResource(R.drawable.kataratwo);
                        image4.setImageResource(R.drawable.toph);
                        image5.setImageResource(R.drawable.zuko);
                    }
                } catch (JSONException e) {
                    Log.e("MainActivity", "Error parsing weather data: " + e.getMessage());
                    //Toast.makeText(MainActivity.this, "Invalid ZIP code", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("MainActivity", "No weather data found");
                //Toast.makeText(MainActivity.this, "Invalid ZIP code", Toast.LENGTH_SHORT).show();
            }
        }
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Toast.makeText(MainActivity.this, "Invalid ZIP code", Toast.LENGTH_SHORT).show();
        }

    }

    public String getQuote(String description) {
        if (description.contains("clear")) {
            return "The sky is clear, the stars are twinkling. It's a beautiful night. - Aang";
        } else if (description.contains("cloud")) {
            return "Sometimes clouds have two sides, a dark and light, and a silver lining in between. It's like a silver sandwich! - Zuko";
        } else if (description.contains("rain")) {
            return "Life is like the rain. Sometimes it's soft, sometimes it's fierce. But in the end, it always passes.- Katara" ;
        } else if (description.contains("sun")) {
            return "Sun is shining, sky is blue, the sun in the sky is like a giant eye, watching over you.- Uncle Iroh" ;
        } else if (description.contains("wind")) {
            return "Life is like the wind. Sometimes it breezes gently, and other times it howls. But you can always feel it.-Aang";
        } else if (description.contains("snow")) {
            return "The snow can be a reminder of the beauty and purity that exists in the world, even in the midst of chaos and darkness.-Katara";
        } else {
            return "Weather conditions are changing, stay updated.";
        }
    }
    public boolean isValidZipCode(String zipCode) {
        try {
            new AsyncTask<String, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(String... strings) {
                    String zipCode = strings[0];
                    try {
                        String geoUrlStr = "https://api.openweathermap.org/geo/1.0/zip?zip=" + zipCode + ",US&appid=" + apiKey;
                        URL geoUrl = new URL(geoUrlStr);

                        HttpURLConnection geoUrlConnection = (HttpURLConnection) geoUrl.openConnection();
                        geoUrlConnection.setRequestMethod("GET");

                        int responseCode = geoUrlConnection.getResponseCode();
                        return responseCode == 200;
                    } catch (IOException e) {
                        return false;
                    }
                }
                @Override
                protected void onPostExecute(Boolean isValidZipCode) {
                    super.onPostExecute(isValidZipCode);
                    if (!isValidZipCode) {
                        Toast.makeText(MainActivity.this, "Invalid ZIP code", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(zipCode);
            return true;
        } catch (Exception e) {
            Log.e("MainActivity", "Error checking ZIP code: " + e.getMessage());
            return false;
        }
    }
}