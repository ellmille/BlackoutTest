package elle.blackouttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Elle on 5/12/2017 at 9:23 AM.
 */

public class ThemeActivity extends AppCompatActivity {
    Activity themeActivity;
    Button switchThemeButton, nextButton, prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set theme
        setTheme(ThemeSettings.getInstance().getCurrentTheme());

        setContentView(R.layout.activity_theme);

        switchThemeButton = (Button) findViewById(R.id.button4);
        switchThemeButton.setOnClickListener(switchThemeButtonListener);

        //set up View elements
        nextButton = (Button) findViewById(R.id.button5);
        nextButton.setOnClickListener(returnButtonListener);
        prevButton = (Button) findViewById(R.id.button7);
        prevButton.setOnClickListener(returnButtonListener);

        themeActivity = this;
    }

    private View.OnClickListener switchThemeButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            ThemeSettings.getInstance().findNextTheme();
            //restart activity
            themeActivity.recreate();
        }
    };
    private View.OnClickListener returnButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), BlackoutActivity.class);
            startActivity(intent);
        }
    };
}
