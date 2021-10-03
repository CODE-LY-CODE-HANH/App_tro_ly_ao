package com.example.assistant_basic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageButton btn_Nghe;
    EditText edt_View;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Context context = MainActivity.this;

    //Chuyển văn bản thàn giọng nói
    TextToSpeech textToSpeech;
    Button btn_NOI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Nghe = findViewById(R.id.img_btn_nnghe);
        edt_View = findViewById(R.id.edtView);
        btn_NOI = findViewById(R.id.btn_noi);

        btn_Nghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech_TO_TEXT();
            }
        });

        btn_NOI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( edt_View.getText().toString().isEmpty() ){
                    edt_View.setText("Xin chào mọi người . Tôi là ứng dụng trợ lý ảo !");
                    tts("Xin chào mọi người . Tôi là ứng dụng trợ lý ảo !");
                } else {
                    tts( edt_View.getText().toString() );
                }
            }
        });
    }

    public void speech_TO_TEXT(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault() );

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Đang nghe  --__-- ");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn\\'t support speech input" ,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void  tts(String text){
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
                if ( status != TextToSpeech.ERROR ){
                    textToSpeech.setLanguage( new Locale("vi_VN") );
                    textToSpeech.setSpeechRate((float) 1.5);
                    textToSpeech.speak(text , TextToSpeech.QUEUE_FLUSH , null);

                }
            }
        });
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {

                    List<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    edt_View.setText(text);

                }
                break;
            }

        }
    }

}