package my.minesearch;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ResultActivity extends AppCompatActivity {


    TextView textScore, textStage, textSecond, textName, textRank;
    private int num1 = 0;
    private int scoreS = 0;
    private int stage = 0;
    private int rank = 1;
    String username;
    myDBHelper myHelper;
    SQLiteDatabase sqlDB;
    Intent intent;
    ImageButton button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textScore = (TextView) findViewById(R.id.textScore);
        textName = (TextView) findViewById(R.id.textName);
        textSecond = (TextView) findViewById(R.id.textSecond);
        textStage = (TextView) findViewById(R.id.textStage);
        textRank = (TextView) findViewById(R.id.textRank);

        button1 = (ImageButton) findViewById(R.id.reset);
        button2 = (ImageButton) findViewById(R.id.main);

        intent = getIntent();

        username = intent.getStringExtra("name");
        stage = intent.getIntExtra("stage", 0);
        scoreS = intent.getIntExtra("score", -1);
        num1 = intent.getIntExtra("second", 0);


        myHelper = new myDBHelper(this);
        sqlDB = myHelper.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO userTBL1 VALUES ('" + username + "'," + stage + "," + scoreS + "," + num1 + ");");
        sqlDB.close();

        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM userTBL1 ORDER BY stage DESC, score DESC,second ASC", null);

        String strName = "이름" + "\r\n";
        String strStage = "단계" + "\r\n";
        String strScore = "점수" + "\r\n";
        String strSecond = "시간" + "\r\n";
        String strRank = "순위" + "\r\n";

        while (cursor.moveToNext()) {
            strRank += rank + "위\r\n";
            rank++;
            strName += cursor.getString(0) + "\r\n";
            strStage += cursor.getString(1) + "단계\r\n";
            strScore += cursor.getString(2) + "점\r\n";
            strSecond += cursor.getString(3) + "초\r\n";
        }
        textRank.setText(strRank);
        textName.setText(strName);
        textStage.setText(strStage);
        textScore.setText(strScore);
        textSecond.setText(strSecond);

        cursor.close();
        sqlDB.close();


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("DELETE FROM userTBL1 ");
                sqlDB.close();
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(intent);
            }
        });
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "gameDB", null, 2);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE  userTBL1 ( name CHAR PRIMARY KEY, stage INTEGER, score INTEGER , second INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);
        }
    }
}