package com.human.edu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //클래스 안쪽에서 사용될 변수를 선언(아래) = 멤버변수
    Button btnTel, btnLogout;
    TextView textViewWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//화면렌더링=메모리로딩
        //--------------------------------------------------------
        //메인액티비티가 onCreate로 생성이 될때, 인텐트로 받은 값을 출력가능
        //기술참조: http://blog.naver.com/PostView.nhn?blogId=rkdwnsdud555&logNo=220292204654&redirect=Dlog&widgetTypeCall=true&directAccess=false
        Intent intent = new Intent(this.getIntent());
        String userId = intent.getStringExtra("editTextID");
        //Toast.makeText(MainActivity.this,"사용자ID는 : " + userId,Toast.LENGTH_LONG).show();
        String userPw = intent.getStringExtra("editTextPassword");
        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewWelcome.setText(userId + "님 환영합니다.");
        //--------------------------------------------------------
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
        btnTel = findViewById(R.id.btnTel);//onClick속성없이 사용.
        btnTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"전화걸기클릭!",Toast.LENGTH_LONG).show();
                Intent telIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("tel:010-1111-1111"));
                startActivity(telIntent);
                finish();//앱이 종료되는 것은아니고, 액티비티가 사라지는 기능.
                System.exit(0);//자바프로그램 강제종료방법 0 정상종료, 1 비정상종료
            }
        });
    }

    public void goToNaver(View view) {
        //디버그 : 작동확인, System.out.println();
        //jsp의 alert와 기능을 하는
        //Toast토스트(식빵 구워서 튀어나오는 모양)
        //Toast.makeText(getApplicationContext(),"네이버웹 클릭!",Toast.LENGTH_LONG).show();
        Intent naverIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://naver.com"));
        startActivity(naverIntent);//액티비티화면 실행
    }
}