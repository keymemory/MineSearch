package my.minesearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main5Activity extends AppCompatActivity {

    private Button startBtn;
    private Button addBtn;
    private TextView tvStatus;
    private TextView tvTime;
    private int num1 = 0;
    private static int scoreS;
    private static int itemT;
    Intent intent;
    String username;
    int num2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        setTitle("지뢰찾기 5단계");
        RelativeLayout.LayoutParams lp;
        timerStart();

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
        tvLabel.setText("지뢰찾기 5단계");
        cv.addView(tvLabel);


        tvLabel = new TextView(this);
        tvLabel.setLayoutParams(lp);
        tvLabel.setBackgroundColor(Color.BLACK);
        tvLabel.setTextColor(Color.WHITE);
        tvLabel.setHeight(70);
        tvLabel.setTextSize(15f);
        tvLabel.setGravity(Gravity.CENTER);
        tvLabel.setText("미션!! 안전한버튼 10개 클릭 | 폭탄 15개 조심!!");
        cv.addView(tvLabel);

        tvTime = new TextView(this);
        tvTime.setLayoutParams(lp);
        tvTime.setBackgroundColor(Color.BLACK);
        tvTime.setTextColor(Color.WHITE);
        tvTime.setHeight(80);
        tvTime.setTextSize(20f);
        tvTime.setGravity(Gravity.CENTER);
        tvTime.setText("경과시간 : 00 초");
        cv.addView(tvTime);

        intent = getIntent();
        int score = intent.getIntExtra("score", -1);
        int item = intent.getIntExtra("item", -2);
        int value = intent.getIntExtra("second", 0);
        String name = intent.getStringExtra("name");
        username = name;
        num1 = value;
        scoreS = score;
        itemT = item;
        num2 = timer_sec;

        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvStatus = new TextView(this);
        tvStatus.setLayoutParams(lp);
        tvStatus.setBackgroundColor(Color.WHITE);
        tvStatus.setTextColor(Color.BLACK);
        tvStatus.setHeight(90);
        tvStatus.setTextSize(18f);
        tvStatus.setGravity(Gravity.CENTER);
        tvStatus.setText("점수 : " + scoreS + "     |     코인 : " + itemT);
        cv.addView(tvStatus);

        gv = new RelativeLayout(this);
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        gv.setLayoutParams(lp);
        cv.addView(gv);


        int btnId = 0;
        int nb = 8;

        for (int ny = 0; ny < nb; ny++) {
            for (int nx = 0; nx < nb; nx++) {
                gv.addView(CreateButton(ny, nx, String.valueOf(btnId++)));
            }
        }

        shuffleArray();
        ResetButtons();
        bombCount = 0;
        btnCount=0;


    }


    private RelativeLayout gv;

    private Button CreateButton(int ny, int nx, String label) {
        int sw = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int nb = 8;
        int BtnWidth = sw / nb;


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(BtnWidth - 8, BtnWidth - 8);
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
            int btnId = Integer.valueOf(((Button) v).getText().toString());
            int btnColor;
            ImageView imageView = new ImageView(Main5Activity.this);
            imageView.setImageResource(R.drawable.boom);


            if (bombData[btnId] == 1) {
                btnColor = Color.RED;
                ++bombCount;
                ((Button) v).setBackgroundResource(R.drawable.angry);
                if (bombCount == 15) startBtn.setVisibility(View.VISIBLE);
                else if (bombCount == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main5Activity.this);
                    builder.setTitle(("실패"));
                    builder.setView(imageView);
                    builder.setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            stopTimer();
                            intent = new Intent(getApplicationContext(), ResultActivity.class);
                            intent.putExtra("score", scoreS);
                            intent.putExtra("second", num1 + timer_sec);
                            intent.putExtra("name", username);
                            intent.putExtra("stage", 5);
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
                btnColor = Color.WHITE;
                ++btnCount;
                    scoreS += 100;
                    itemT++;
                ((Button) v).setBackgroundResource(R.drawable.rool);
                    tvStatus.setText("점수 : " + (String.valueOf(scoreS)) + "     |     코인 : " + (String.valueOf(itemT)));
                if (btnCount >= 10) {
                    stopTimer();
                    intent = new Intent(getApplicationContext(), Main6Activity.class);
                    intent.putExtra("score", scoreS);
                    intent.putExtra("second", num1 + timer_sec);
                    intent.putExtra("item", itemT);
                    intent.putExtra("name", username);
                    startActivity(intent);
                }
                Log.d("test", (Integer.toString(btnCount)));
            }

            //((Button) v).setBackgroundColor(btnColor);
            ((Button) v).setEnabled(false);
        }


    };


    private void ResetButtons() {
        for (int i = gv.getChildCount() - 1; i > -1; --i) {
            Button btn = (Button) gv.getChildAt(i);
            btn.setBackgroundResource(R.drawable.sleep);
            btn.setEnabled(true);
        }
    }


    private int[] bombData = {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
            1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0};

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
    int timer_sec ;
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
                tvTime.setText(num1 + timer_sec + "초");
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
                if (itemT >= 4) {
                    btnCount += 3;
                    if(btnCount>=10) {
                        itemT-=4;
                        stopTimer();
                        intent = new Intent(getApplicationContext(), Main6Activity.class);
                        intent.putExtra("score", scoreS);
                        intent.putExtra("second", num1 + timer_sec);
                        intent.putExtra("item", itemT);
                        intent.putExtra("name", username);
                        startActivity(intent);
                    }
                    else{
                        btnCount -= 3;
                        AlertDialog.Builder builder = new AlertDialog.Builder(Main5Activity.this);
                        builder.setTitle(("버튼"));
                        builder.setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        int total = 7 - btnCount;
                        AlertDialog alert = builder.create();
                        alert.setTitle(("버튼을 " + total + "번 더 클릭하시오."));
                        alert.show();
                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main5Activity.this);
                    builder.setTitle(("코인"));
                    builder.setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    int total = 4 - itemT;

                    AlertDialog alert = builder.create();
                    alert.setTitle(("코인이 " + total + "개 부족합니다."));
                    alert.show();
                }
                return true;


            case R.id.next:
                if (itemT >= 5) {
                    itemT -= 5;
                    stopTimer();
                    intent = new Intent(getApplicationContext(), Main6Activity.class);
                    intent.putExtra("score", scoreS);
                    intent.putExtra("second", num1 + timer_sec);
                    intent.putExtra("item", itemT);
                    intent.putExtra("name", username);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main5Activity.this);
                    builder.setTitle(("코인"));
                    builder.setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    int total = 7 - itemT;

                    AlertDialog alert = builder.create();
                    alert.setTitle(("코인이 " + total + "개 부족합니다."));
                    alert.show();

                }
                return true;
        }
        return false;
    }


}
