package com.NamohTours.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.NamohTours.R;

public class EntertainMentActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnQuiz, btnGames;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertain_ment);

        toolbar = (Toolbar) findViewById(R.id.tourEnterToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnGames = (Button) findViewById(R.id.btnGames);
        btnQuiz = (Button) findViewById(R.id.btnQuiz);

        btnGames.setOnClickListener(this);
        btnQuiz.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onClick(View v) {


        int id = v.getId();

        if (id == R.id.btnQuiz) {
            Intent quiz = new Intent(EntertainMentActivity.this, WebUrl.class);
            quiz.putExtra("Web", "quiz");
            startActivity(quiz);
        }

        if (id == R.id.btnGames) {
            Intent games = new Intent(EntertainMentActivity.this, WebUrl.class);
            games.putExtra("Web", "games");
            startActivity(games);
        }
    }
}
