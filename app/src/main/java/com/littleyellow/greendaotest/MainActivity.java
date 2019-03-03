package com.littleyellow.greendaotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.littleyellow.greendaotest.bean.Score;
import com.littleyellow.greendaotest.bean.User;
import com.littleyellow.greendaotest.db.ScoreDao;
import com.littleyellow.greendaotest.db.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView queryText;

    private TextView insertText;

    private TextView deleteText;

    private TextView updateText;

    private EditText nameEt;
    private EditText ageEt;

    private TextView query_parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GreenDao.init(getApplication());
        queryText = findViewById(R.id.query);
        insertText = findViewById(R.id.insert);
        deleteText = findViewById(R.id.delete);
        updateText = findViewById(R.id.updata);
        nameEt = findViewById(R.id.name_ev);
        ageEt = findViewById(R.id.age_ev);

        query_parent = findViewById(R.id.query_parent);
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        final UserDao userDao = GreenDao.getSession().getUserDao();
        queryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> list = userDao.loadAll();
                if(null != list) {
                    StringBuilder sb = new StringBuilder();
                    for (User user : list) {
                        sb.append(user.getName())
                                .append(",")
                                .append(user.getAge());
                                List<Score> scores = user.getScores();
                                if(null!=scores&&!scores.isEmpty()){
                                    sb.append(" 分数:");
                                    for (Score score:scores){
                                        sb.append(score.getName())
                                           .append(score.getScore())
                                                .append(",");
                                    }
                                }
                         sb.append("\n");
                    }
                    queryText.setText(sb.toString());
                }
            }
        });

        insertText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setName(nameEt.getText().toString());
                user.setAge(Integer.valueOf(ageEt.getText().toString()));

                List<Score> scores = new ArrayList<>();
                Score score = new Score();
                score.setUser(user);
                score.setName("语文");
                score.setScore(90);
                scores.add(score);

                score = new Score();
                score.setName("数学");
                score.setScore(96);
                score.setUser(user);
                scores.add(score);
                user.setScores(scores);
                userDao.insert(user);
            }
        });

        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    User user = new User();
                    user.setName(nameEt.getText().toString());
                    user.setAge(Integer.valueOf(ageEt.getText().toString()));
                    List<User> list = userDao.loadAll();
                    try {
                        user.setId(list.get(0).getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    userDao.delete(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setName(nameEt.getText().toString());
                user.setAge(Integer.valueOf(ageEt.getText().toString()));
                List<User> list = userDao.loadAll();
                try {
                    user.setId(list.get(0).getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                userDao.update(user);
            }
        });

        query_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ScoreDao scoreDao = GreenDao.getSession().getScoreDao();
                List<Score> scores = scoreDao.loadAll();
                if (null != scores) {
                    StringBuilder sb = new StringBuilder();
                    for (Score score : scores) {
                        sb.append(score.getName())
                                .append(",")
                                .append(score.getScore());
                        User user = score.getUser();
                        if (null != user) {
                            sb.append(" 所属:")
                                    .append(user.getId()).append(",")
                                    .append(user.getName());
                        }
                        sb.append("\n");
                        query_parent.setText(sb.toString());
                    }
                }
            }
        });
    }
}
