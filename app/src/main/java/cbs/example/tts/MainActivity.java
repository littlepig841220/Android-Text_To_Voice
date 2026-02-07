package cbs.example.tts;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private View rootView;
    private EditText editTextContent;
    private Button buttonToVoice;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.linearLayout);
        editTextContent = findViewById(R.id.editTextContent);
        buttonToVoice = findViewById(R.id.buttonToVoice);

        textToSpeech = new TextToSpeech(this, onInitListenerTTS);

        System.out.println(rootView.getPaddingBottom());
        System.out.println(rootView.getPaddingEnd());
        System.out.println(rootView.getPaddingLeft());
        System.out.println(rootView.getPaddingRight());
        System.out.println(rootView.getPaddingTop());
        System.out.println(rootView.getPaddingStart());
        ViewCompat.setOnApplyWindowInsetsListener(rootView, onApplyWindowInsetsListener);
        buttonToVoice.setOnClickListener(onClickListener);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        // shutdown tts
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private final TextToSpeech.OnInitListener onInitListenerTTS = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "This Language is not supported");
                }
                else {
                    Log.e(TAG, "Initialization Failed!");
                }
            }
        }
    };

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String data = editTextContent.getText().toString();

            textToSpeech.setPitch(1);
            textToSpeech.setSpeechRate(1);

            Bundle params = new Bundle();
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "tts1");

            textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, params, "tts1");
        }
    };

    private final OnApplyWindowInsetsListener onApplyWindowInsetsListener = new OnApplyWindowInsetsListener() {
        @NonNull
        @Override
        public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft() + systemBars.left,
                    systemBars.top,
                    v.getPaddingRight() + systemBars.right,
                    systemBars.bottom);
            return insets;
        }
    };
}