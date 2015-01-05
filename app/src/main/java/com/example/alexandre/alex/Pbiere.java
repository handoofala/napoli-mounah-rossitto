package com.example.alexandre.alex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Pbiere extends Activity {
    /* initialisation des variables */
    private ProgressDialog charg;
    ArrayList<Integer> IdList = new ArrayList<Integer>();
    ArrayList<String> NameList = new ArrayList<String>();
    ListView listV;
    EditText  BarRecherche;
    ArrayAdapter adapt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biere);
        /* Execution de l'Asynctask */
        new HttpAsyncTask().execute();

        listV = (ListView) findViewById(R.id.listP);
        BarRecherche = (EditText) findViewById(R.id.input);
        /* Mise en place de la barre de recherche */
        BarRecherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s,int arg1,int arg2,int arg3){
                /* on associe la barre de recherche avec la  liste de bières*/
                Pbiere.this.adapt.getFilter().filter(s);
            }
            @Override
            public void beforeTextChanged(CharSequence s,int arg1,int arg2,int arg3){

            }

            @Override
            public void afterTextChanged(Editable arg0){

            }
        });

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Récupération de la position lorsqu'on utilise (ou non) la barre de recherche*/
                String[] maBierre = ((TextView) view).getText().toString().split("-");
                Log.i("Bierre: ", maBierre[1]);
                /*Lorsqu'on appuie sur une bière, on lance l'Activity Conn.class en envoyant la position de l'item de la liste*/
                Intent i = new Intent(getApplicationContext(), Conn.class);
                i.putExtra("param", Integer.parseInt(maBierre[0]));
                Toast.makeText(getApplicationContext(), maBierre[0],
                        Toast.LENGTH_SHORT).show();
                startActivity(i);

            }

        });

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
            Intent intent = new Intent(Pbiere.this,MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<String> Ajout() throws IOException {
        /*Initialisation des variables*/
        InputStream inputStream = null;
        Image alt;
        String nom="";
        int id;


        String result = null;
        try {
            /*On essaie de se connecter aux 152 urls Json afin de pouvoir récupérer les données placés dans ces fichiers*/

            for(int i=1;i<153;i++) {
                /*Connection avec la méthode GET aux urls*/
                URL url = new URL("http://binouze.fabrigli.fr/bieres/" + i + ".json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int response = connection.getResponseCode();
                /*On converti le inputStream en String*/
                inputStream = connection.getInputStream();
                result = convt(inputStream, 3000);
                /*On créé un JSONObject contenant le résultat de la requête précédente*/
                JSONObject jsonObject = new JSONObject(result);

                /*On récupère le nom et l'ID du JSONObject*/
                nom = jsonObject.getString("name");
                id = jsonObject.getInt("id");
                /*On concatène les deux variables dans la variable String bierre*/
                String bierre = id +"- " + nom;

                NameList.add(bierre);
                IdList.add(id);
            }

            return NameList;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return NameList;
    }



    /*Permet de convertir un InputStream en String*/
    public String convt(InputStream stream, int len) throws IOException,UnsupportedEncodingException {
        Reader reader = null;
        reader =new InputStreamReader(stream,"UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*Mise en place d'une attente à l'aide d'une barre de chargement afin d'informer l'utilisateur que nous récupérons les données*/
            charg = new ProgressDialog(Pbiere.this);
            charg.setMessage("Chargement de la collection de bières");
            charg.setCancelable(false);
            charg.show();

        }
        @Override
        protected String doInBackground(String... urls) {


            try {
                /*On retourne le contenu de la méthode Ajout (le contenu de la liste (nameList))*/
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
            listV= (ListView) findViewById(R.id.listP);
            /*Association de la liste view avec nameList et on créé la vue*/
            adapt= new ArrayAdapter(Pbiere.this, android.R.layout.simple_list_item_1, NameList);
            listV.setAdapter(adapt);



        }

    }



    }





