package com.human.edu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.AsyncResponse;
import core.JsonConverter;
import core.PostResponseAsyncTask;

/**
 * 이 액티비티에서는 리사이클러뷰에 RestAPI Json 데이터를 바인딩 시키는 기능
 * List객체(Json데이터바인딩)-어댑터클래스(데이터와 뷰객체중간)-리사이클러뷰
 */
public class SubActivity extends AppCompatActivity {
    //리사이클러 뷰를 사용할 멤버변수(필드변수) 생성
    private RecyclerAdapter mRecyclerAdapter;
    private RecyclerAdapter mRecyclerAdapter2;
    private List mItemList = new ArrayList<MemberVO>();
    //리사이클러 레이아웃 뷰 멤버변수 생성
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    //어댑터에서 선택한 값 확인 변수(선택한 회원을 삭제하기 위해서)
    private String currentCursorId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        //객체 생성
        mRecyclerAdapter = new RecyclerAdapter(mItemList);
        mRecyclerAdapter2 = new RecyclerAdapter(mItemList);
        //리사이클러뷰xml과 어댑터클래스를 바인딩
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);//리사이클러 뷰의 높이를 고정.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mRecyclerAdapter);//데이터없는 빈 어댑터를 뷰화면에 바인딩시킴
        //리사이클러뷰xml2와 어댑터클래스 바인딩
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(mRecyclerAdapter2);

        getAllData();//1개의 메서드로 2개의 어댑터를 갱신합니다.
        mRecyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                MemberVO memberVO = (MemberVO) mItemList.get(position);
                currentCursorId = memberVO.getUser_id();
                //Toast.makeText(getApplicationContext(), "현재선택한 회원ID는"+currentCursorId,Toast.LENGTH_LONG).show();
                deleteUserData(position, currentCursorId);
            }
        });
        //선택한 회원 메일 보내기
        mRecyclerAdapter2.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                MemberVO memberVO = (MemberVO) mItemList.get(position);
                String userEmail = memberVO.getEmail();
                String userName = memberVO.getUser_name();
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("plain/text");
                String[] address = {};
                //put은 해시데이터에(key,value) 값을 입력할때 사용
                mailIntent.putExtra(Intent.EXTRA_EMAIL, address);
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "[사이트 공지사항]");
                mailIntent.putExtra(Intent.EXTRA_TEXT, userName+ "님 안녕하세요!");
                startActivity(mailIntent);//스마스폰의 메일앱을 띄웁니다.
            }
        });
    }

    //RestAPI 서버로 UserId를 전송해서 스프링앱의 사용자를 삭제하는 메서드
    private void deleteUserData(int position, String currentCursorId) {
        //삭제 대화상자에 보여줄 메세지를 만듭니다.
        String message = "해당 회원을 삭제 하시겠습니까?<br>" +
                "position : " + position + "<br />" +
                "회원ID : " + currentCursorId + "<br />";
        DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //RestAPI 삭제 호출 Call
                String requestUrl = "http://192.168.124.100:8080/android/delete/"+currentCursorId;
                PostResponseAsyncTask deleteTask = new PostResponseAsyncTask(SubActivity.this, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        if(output.equals("success")) {
                            Toast.makeText(SubActivity.this, "삭제 성공", Toast.LENGTH_LONG).show();
                            getAllData();
                        }else{
                            Toast.makeText(SubActivity.this, "삭제 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                deleteTask.execute(requestUrl);
            }
        };
        //삭제를 물어보는 다이얼로그를 생성.
        new AlertDialog.Builder(this).setTitle("선택된 회원을 삭제")
                .setMessage(Html.fromHtml(message))
                .setPositiveButton("삭제", deleteListener)
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();//취소 버튼을 눌렀을때 화면에서 치우기
                            }
                        }
                ).show();
    }

    //RestAPI 서버에서 전송받은 데이터를 리사이클러뷰 어댑터에 바인딩 시킴
    private void getAllData() {
        //RestAPI 서버와 비동기 통신 시작
        String requestUrl = "http://192.168.124.100:8080/android/list";
        HashMap postDataParams = new HashMap();
        postDataParams.put("mobile","android");
        List resultList = new ArrayList<>();//RestAPI에서 보내온 jSon데이터가 저장공간 생성
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(SubActivity.this, postDataParams, new AsyncResponse() {

            @Override
            public void processFinish(String output) {
                ArrayList<MemberVO> memberList = new JsonConverter<MemberVO>().toArrayList(output, MemberVO.class);
                //위 컨버트한 memberList변수를 어댑터에 바인딩 시키기(아래)
                for(MemberVO value: memberList) {
                    //resultList 에 1개 레코드씩 저장 -> 어댑터에 데이터 바인딩예정
                    //Log.i("RestAPI테스트: ", value.getUser_id());
                    String p_id = value.getUser_id();
                    String p_name = value.getUser_name();
                    String p_email = value.getEmail();
                    resultList.add(new MemberVO(p_id,p_name,p_email));
                }
                //화면출력
                mItemList.clear();
                mItemList.addAll(resultList);
                mRecyclerAdapter.notifyDataSetChanged();//어댑터 객체가 리프레시 됨.
                mRecyclerAdapter2.notifyDataSetChanged();//데이터변경사항을 공지.
            }
        });
        readTask.execute(requestUrl);//비동기 통신 시작명령.
    }
}