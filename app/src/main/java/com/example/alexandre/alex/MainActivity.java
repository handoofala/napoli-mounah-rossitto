package com.example.alexandre.alex;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    EditText mail;
    EditText nic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button but = (Button) findViewById(R.id.ButtonE);
        Button but1 = (Button) findViewById(R.id.Button1);
/* Lorsqu'on appuie sur le bouton but, on lance l'activité ScreenSliderActivity*/
      but.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(MainActivity.this, SliderActivity .class);
                  startActivity(intent);
              } });
/* Lorsqu'on appuie sur le bouton but, on lance l'activité Pbiere*/
          but1.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(MainActivity.this, Pbiere .class);
                  startActivity(intent);
              } });



    }
}

