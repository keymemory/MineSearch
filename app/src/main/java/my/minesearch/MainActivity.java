package my.minesearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button startBtn;
    private Button addBtn;
    private TextView tvStatus;
    private TextView tvLabel3;
    private TextView tvTime;

    private int score = 0;
    private int item = 0;
    int num1 = 0;
    String username;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("지뢰찾기 1단계");
        RelativeLayout.LayoutParams lp, lp2, lp3;
        intent = getIntent();
        username = intent.getStringExtra("name");

        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout gameView = new RelativeLayout(this);
        gameView.setLayoutParams(lp);
        setContentView(gameView);


        LinearLayout cv = new LinearLayout(this);
        cv.setLayoutParams(lp);
        cv.setOrientation(LinearLayout.VERTICAL);
        cv.setBackgroundColor(Color.rgb(255, 255, 255));
        gameView.addView(cv);

        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView tvLabel = new TextView(this);
        tvLabel.setLayoutParams(lp);
        tvLabel.setBackgroundColor(Color.BLACK);
        tvLabel.setTextColor(Color.WHITE);
        tvLabel.setTextSize(18f);
        tvLabel.setGravity(Gravity.CENTER);
        tvLabel.setText("지뢰찾기 1단계");
        cv.addView(tvLabel);

        lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvLabel3 = new TextView(this);
        tvLabel3.setLayoutParams(lp3);
        tvLabel3.setBackgroundColor(Color.BLACK);
        tvLabel3.setTextColor(Color.WHITE);
        tvLabel3.setHeight(70);
        tvLabel3.setTextSize(15f);
        tvLabel3.setGravity(Gravity.CENTER);
        tvLabel3.setText("3개추가->코인4 | 다음단계->코인7");
        cv.addView(tvLabel3);

        tvTime = new TextView(this);
        tvTime.setLayoutParams(lp);
        tvTime.setBackgroundColor(Color.BLACK);
        tvTime.setTextColor(Color.WHITE);
        tvTime.setHeight(80);
        tvTime.setTextSize(20f);
        tvTime.setGravity(Gravity.CENTER);
        tvTime.setText("경과시간 : 00 초");
        cv.addView(tvTime);


        num1 = timer_sec;


        lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvStatus = new TextView(this);
        tvStatus.setLayoutParams(lp2);
        tvStatus.setBackgroundColor(Color.WHITE);
        tvStatus.setTextColor(Color.BLACK);
        tvStatus.setHeight(90);
        tvStatus.setTextSize(18f);
        tvStatus.setGravity(Gravity.CENTER);
        tvStatus.setText("점수 : " + score + "     |     코인 : " + item);
        cv.addView(tvStatus);


        startBtn = new Button(this);
        startBtn.setBackgroundResource(R.drawable.start2);
        startBtn.setLayoutParams(lp);
        tvStatus.setGravity(Gravity.CENTER);
        startBtn.setOnClickListener(BtnClick);
        cv.addView(startBtn);


        gv = new RelativeLayout(this);
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        gv.setLayoutParams(lp);
        cv.addView(gv);

        int btnId = 0;
        int nb = 4;

        for (int ny = 0; ny < nb; ny++) {
            for (int nx = 0; nx < nb; nx++) {
                gv.addView(CreateButton(ny, nx, String.valueOf(btnId++)));
            }
        }


    }


    private RelativeLayout gv;

    private Button CreateButton(int ny, int nx, String label) {
        int sw = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int nb = 4;
        int BtnWidth = sw / nb;


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(BtnWidth - 4, BtnWidth - 4);
        lp.topMargin = 2 + ny * BtnWidth;
        lp.leftMargin = 2 + nx * BtnWidth;
        Button btn = new Button(this);
        btn.setLayoutParams(lp);
        btn.setTextColor(Color.WHITE);
        btn.setBackgroundColor(Color.WHITE);
        btn.setTextSize(1);
        btn.setText(label);
        btn.setOnClickListener(BtnClick);
        btn.setEnabled(false);

        return btn;
    }


    private int bombCount = 0;
    private static int btnCount = 0;


    private View.OnClickListener BtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == startBtn) {
                shuffleArray();
                ResetButtons();
                timerStart();
                bombCount = 0;
                btnCount = 0;
                score = 0;
                item = 0;
                tvLabel3.setText("미션!! 안전한버튼 4개 클릭 | 폭탄 2개 조심!!");
                startBtn.setVisibility(View.GONE);
            } else {
                int btnId = Integer.valueOf(((Button) v).getText().toString());
                int btnColor;
                int btnBoom;
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setImageResource(R.drawable.boom);

                if (bombData[btnId] == 1) {
                    //btnColor = Color.RED;
                    ((Button) v).setBackgroundResource(R.drawable.angry);
                    ++bombCount;
                    if (bombCount == 2) startBtn.setVisibility(View.VISIBLE);
                    else if (bombCount == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(("실패"));
                        builder.setView(imageView);
                        builder.setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                stopTimer();
                                intent = new Intent(getApplicationContext(), ResultActivity.class);
                                intent.putExtra("score", score);
                                intent.putExtra("second", timer_sec);
                                intent.putExtra("name", username);
                                intent.putExtra("stage", 1);
                                startActivity(intent);
                                finish();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.setTitle(("              실패하셨습니다."));
                        alert.show();
                        stopTimer();
                    }


                } else {
                    //btnColor = Color.WHITE;
                    ++btnCount;
                    score += 100;
                    item++;
                    ((Button) v).setBackgroundResource(R.drawable.rool);

                    tvStatus.setText("점수 : " + (String.valueOf(score)) + "     |     코인 : " + (String.valueOf(item)));
                    if (btnCount >= 4) {
                        stopTimer();
                        intent = new Intent(getApplicationContext(), Main2Activity.class);
                        intent.putExtra("score", score);
                        intent.putExtra("item", item);
                        intent.putExtra("second", timer_sec);
                        intent.putExtra("name", username);
                        startActivity(intent);

                    }
                    Log.d("test", (Integer.toString(btnCount)));
                }

                //((Button) v).setBackgroundColor(btnColor);
                ((Button) v).setEnabled(false);
            }
        }

    };


    private void ResetButtons() {
        for (int i = gv.getChildCount() - 1; i > -1; --i) {
            Button btn = (Button) gv.getChildAt(i);
            btn.setBackgroundResource(R.drawable.sleep);
            btn.setEnabled(true);
        }

    }


    private int[] bombData = {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private void shuffleArray() {
        Random rnd = new Random();
        for (int i = gv.getChildCount() - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int first = bombData[index];
            int tmp = bombData[i];
            bombData[index] = tmp;
            bombData[i] = first;
        }
    }

    private TimerTask second;
    private TextView timer_text;
    private final Handler handler = new Handler();
    private int timer_sec = 0;
    private int count = 0;

    public void timerStart() {
        second = new TimerTask() {
            @Override
            public void run() {
                Log.i("Test", "Timer start");
                Update();
                timer_sec++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 1000);
    }

    protected void Update() {
        Runnable updater = new Runnable() {
            public void run() {
                tvTime.setText(timer_sec + "초");
            }
        };
        handler.post(updater);

    }

    public void stopTimer() {

        second.cancel();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem it) {
        switch (it.getItemId()) {
            case R.id.three:
                if (item >= 4) {
                    btnCount += 3;
                    if (btnCount >= 4) {
                        item -= 4;
                        stopTimer();
                        intent = new Intent(getApplicationContext(), Main2Activity.class);
                        intent.putExtra("score", score);
                        intent.putExtra("item", item);
                        intent.putExtra("second", timer_sec);
                        startActivity(intent);
                    } else {
                        btnCount -= 3;
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(("버튼"));
                        builder.setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        int total = 1 - btnCount;
                        AlertDialog alert = builder.create();
                        alert.setTitle(("버튼을 " + total + "번 더 클릭하시오."));
                        alert.show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(("코인"));
                    builder.setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    int total = 4 - item;

                    AlertDialog alert = builder.create();
                    alert.setTitle(("코인이 " + total + "개 부족합니다."));
                    alert.show();
                }
                return true;

            case R.id.next:
                btnCount++;
                if (item >= 5) {
                    item -= 5;
                    stopTimer();
                    intent = new Intent(getApplicationContext(), Main2Activity.class);
                    intent.putExtra("score", score);
                    intent.putExtra("item", item);
                    intent.putExtra("second", timer_sec);
                    startActivity(intent);
                } else {
                    btnCount--;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(("코인"));
                    builder.setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    int total = 7 - item;

                    AlertDialog alert = builder.create();
                    alert.setTitle(("코인이 " + total + "개 부족합니다."));
                    alert.show();

                }
                return true;
        }
        return false;
    }

}
