package com.human.edu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(LoginActivity.this, "onDestroy상태6", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(LoginActivity.this, "onStop상태5", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(LoginActivity.this, "onPause상태4", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(LoginActivity.this, "onRsume상태3", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(LoginActivity.this, "onStart상태2", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(LoginActivity.this, "onCReate상태1", Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextID, editTextPassword;
                editTextID = findViewById(R.id.editTextID);
                editTextPassword = findViewById(R.id.editTextPassword);
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                mainIntent.putExtra("editTextID", editTextID.getText().toString());
                mainIntent.putExtra("editTextPassword", editTextPassword.getText().toString());
                startActivity(mainIntent);
            }
        });
    }

    public void gotoMain(View view) {
        EditText editTextID, editTextPassword;
        editTextID = view.findViewById(R.id.editTextID);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        //Toast.makeText(LoginActivity.this, editTextID.getText(),Toast.LENGTH_LONG).show();
        //로그인 버튼을 클릭했을때
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        //데이터를 입력해서
        mainIntent.putExtra("editTextID", editTextID.getText().toString());//아이디
        mainIntent.putExtra("editTextPassword", editTextPassword.getText().toString());//암호
        startActivity(mainIntent);//편지봉투(Intent)를 개봉 = 화면 불러오기실행
        finish();//LoginActivity화면을 종료(프로그램종료x)
    }
}