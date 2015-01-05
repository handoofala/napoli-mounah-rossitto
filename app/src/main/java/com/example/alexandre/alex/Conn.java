package com.example.alexandre.alex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Conn extends Activity {

    /* Initialisation des données*/
    private ProgressDialog charg;
    String Resultat = "";
    String DebutUrl = "";
    String FinUrl = "";
    InputStream url1;
    Bitmap image1;
    ImageView v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connectez);
        /*Exécution de l'Asynctask*/
        new HttpAsyncTask().execute();

    }
    /*Bouton Home dans l'ActionBar*/
  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        /*On vérifie si l'utilisateur a appuyé sur le bouton Home*/
        if (id == R.id.action_settings) {
            Intent intent = new Intent( this,MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String Ajout() throws IOException {
        /*Initialisation des variables*/
        InputStream inputStream = null;
        String country = "";
        String id = "";
        String categorie = "";
        String description = "";
        String nom="";



        String result = null;
        try {
            /*On récupère l'ID de la bière cliqué par l'utilisateur de la classe Pbière et on l'associe cette valeur avec la variable Param*/
            Bundle extras = getIntent().getExtras();
            int Param = extras.getInt("param");
            URL url;
            /*Connexion avec début de l'URL + l'ID + extension .json (avec la méthode GET)*/
            url = new URL("http://binouze.fabrigli.fr/bieres/" + Param + ".json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            /*On converti le inputStream en String*/
            inputStream = connection.getInputStream();
            result = convt(inputStream, 8000);
            /*On créé un JSONObject contenant le résultat de la requête précédente*/
            JSONObject jsonObject = new JSONObject(result);
            /*On récupère une seconde fois l'ID de la bière*/
            id = jsonObject.getString("id");
            /*On récupère les informations en relation avec la bière choisie, soit la catégorie, le pays, et l'URL de l'image*/
            JSONObject jsonObject1 = new JSONObject(result);
            nom= jsonObject1.getString("name");
            categorie = jsonObject.getString("category");
            description= jsonObject.getString("description");
            JSONObject al = jsonObject.getJSONObject("country");
            country = al.getString("name");
            JSONObject at = jsonObject.getJSONObject("image");
            JSONObject lo = at.getJSONObject("image");
            DebutUrl = lo.getString("url");
            FinUrl ="http://binouze.fabrigli.fr"+ DebutUrl;
            /*On vérifie si les informations ne sont pas vide. Si c'est le cas, on leur attribut "inconnu"*/
            if(categorie=="null")
            {
                categorie="Inconnu";
            }
            if(country=="null")
            {
                country="Inconnu";
            }
            if(description=="null")
            {
                description="Inconnu";
            }
            if(Param==152)
            {
              description="2014;bière rouge";
            }
            /*On stock ces résultats dans une variable String*/
           Resultat = "Voici le nom de la bière : " + nom + ("\n")+
                    "Voici l'identifiant de la bière : " + id + ("\n") +
                    "Voici la catégorie  de la bière : " + categorie + ("\n") +
                    "La provenance de la bière : " + country + ("\n")
                    + "La drescription : "+ description + ("\n");
            url1 = new URL(FinUrl).openStream();
            image1= BitmapFactory.decodeStream(url1);
            return Resultat;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return Resultat;
    }


    /*Permet de convertir l'InputStream en String*/
    public String convt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        /*Mise en place d'une attente à l'aide d'une barre de chargement afin d'informer l'utilisateur que nous récupérons les données*/
        protected void onPreExecute() {
            super.onPreExecute();
           charg = new ProgressDialog(Conn.this);
           charg.setMessage("Chargement de la collection de bières");
           charg.setCancelable(false);
           charg.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                /*On retourne le contenu de la méthode Ajout (le contenu du String (résultat))*/
                return String.valueOf(Ajout());
            } catch (IOException e) {
                return "impossible de se connecter";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            /*Fin de la barre de chargement*/
            if (charg.isShowing())
                charg.dismiss();
            /*On affiche dans la view le String (résultat) et l'image de l'URL*/
            TextView text = (TextView) findViewById(R.id.notre);
            text.setText(Resultat);
            v=(ImageView) findViewById(R.id.image);
            v.setImageBitmap(image1);
        }

    }


}



