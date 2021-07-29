package my.minesearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity {

    ImageButton button1, button2;
    View dialogView;
    EditText dlgName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setTitle("지뢰찾기 메인화면");

        button1 = (ImageButton) findViewById(R.id.button1);
        button2 = (ImageButton) findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView = (View) View.inflate(MainMenu.this, R.layout.dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainMenu.this);
                dlg.setTitle("사용자 정보 입력");
                dlg.setIcon(R.drawable.user);
                dlg.setView(dialogView);


                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dlgName = (EditText) dialogView.findViewById(R.id.editId);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("name",dlgName.getText().toString());
                        startActivity(intent);
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                builder.setTitle(("종료"));
                builder.setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setCancelable(false).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle(("종료하시겠습니까?"));
                alert.show();
            }
        });

    }


}
