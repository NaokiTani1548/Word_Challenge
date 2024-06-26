package com.websarva.wings.word_challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView countLabel, questionLabel;
    private Button answerBtn1, answerBtn2, answerBtn3, answerBtn4;
    private String rightAnswer;
    private int rightAnswerCount = 0;
    private int quizCount = 1;
    static final private int total_COUNT = 10;
    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();
    ArrayList<String> allAnswers = new ArrayList<>(); // 全回答のリスト
    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int db_type = getIntent().getIntExtra("DB_TYPE", 2);


        countLabel = findViewById(R.id.countLabel);
        questionLabel = findViewById(R.id.questionLabel);
        answerBtn1 = findViewById(R.id.answerBtn1);
        answerBtn2 = findViewById(R.id.answerBtn2);
        answerBtn3 = findViewById(R.id.answerBtn3);
        answerBtn4 = findViewById(R.id.answerBtn4);

        answerBtn1.setOnClickListener(this);
        answerBtn2.setOnClickListener(this);
        answerBtn3.setOnClickListener(this);
        answerBtn4.setOnClickListener(this);

        helper = new DatabaseHelper(this, db_type);
        try {
            helper.createDatabase();
        } catch (IOException e) {
            throw new Error("Unable to create database");
        }

        loadQuizData(db_type);
        showNextQuiz();
    }

    private void loadQuizData(int type) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql;
        if(type == 1) {
            sql = "SELECT question, answer FROM quiz";
        }else {
            sql = "SELECT question, answer FROM quiz4";
        }
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            String question = cursor.getString(0);
            String answer = cursor.getString(1);

            // データの前処理
            if (question == null || answer == null || question.trim().isEmpty() || answer.trim().isEmpty()) {
                continue; // 無効なデータをスキップ
            }

            ArrayList<String> quiz = new ArrayList<>();
            quiz.add(question); // question
            quiz.add(answer); // correct answer
            quizArray.add(quiz);
            allAnswers.add(answer); // 全回答リストに追加
        }
        cursor.close();
        db.close();
    }

    @Override
    public void onClick(View view) {
        Button answerBtn = findViewById(view.getId());
        String btnText = answerBtn.getText().toString();

        String alertTitle;
        if (btnText.equals(rightAnswer)) {
            alertTitle = "正解!";
            rightAnswerCount++;
        } else {
            alertTitle = "不正解...";
        }
        new MaterialAlertDialogBuilder(this)
                .setTitle(alertTitle)
                .setMessage("答え : " + rightAnswer)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (quizCount == total_COUNT) {
                            Intent intent = new Intent(MainActivity.this, Result.class);
                            intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount);
                            startActivity(intent);
                        } else {
                            quizCount++;
                            showNextQuiz();
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void showNextQuiz() {

        countLabel.setText(getString(R.string.count_label, quizCount));

        Random random = new Random();
        int randomNum = random.nextInt(quizArray.size());

        ArrayList<String> quiz = quizArray.get(randomNum);

        questionLabel.setText(quiz.get(0));
        rightAnswer = quiz.get(1);

        ArrayList<String> options = new ArrayList<>(allAnswers);
        options.remove(rightAnswer); // 正解を除外
        Collections.shuffle(options); // シャッフル
        ArrayList<String> choices = new ArrayList<>();
        choices.add(rightAnswer);
        choices.add(options.get(0));
        choices.add(options.get(1));
        choices.add(options.get(2));
        Collections.shuffle(choices); // シャッフル

        // 解答ボタンに選択肢を表示
        answerBtn1.setText(choices.get(0));
        answerBtn2.setText(choices.get(1));
        answerBtn3.setText(choices.get(2));
        answerBtn4.setText(choices.get(3));

        // このクイズをquizArrayから削除
        quizArray.remove(randomNum);
    }
}
