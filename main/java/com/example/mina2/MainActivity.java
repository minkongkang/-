package com.example.mina2;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Calendar Cal;
    GridView gridView;
    ArrayList<String> dayList;
    GridAdapter gridAdapter;
    int now_year;
    int now_month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//calendar인스턴스 설정
        Cal = Calendar.getInstance();

// 현재 연도, 월, 일 받기
        Intent intent = getIntent();
        now_year = intent.getIntExtra("year", -1);
        now_month = intent.getIntExtra("month", -1);

        if (now_year == -1 || now_month == -1) {
            now_year = Calendar.getInstance().get(Calendar.YEAR);
            now_month = Calendar.getInstance().get(Calendar.MONTH)+1;
            day = Calendar.getInstance().get(Calendar.DATE);
        }

        gridView = (GridView) findViewById(R.id.gridview);

        TextView year_month = findViewById(R.id.tv_date);
        year_month.setText(now_year + "년" + now_month + "월");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,
                        now_year+"/"+now_month+"/"+dayList.get(i),Toast.LENGTH_SHORT).show();
            }
        });

        dayList = new ArrayList<String>();

//이번달 1일 무슨요일인지 판단 Cal.set(Year,Month,Day)
        Cal.set(now_year, now_month-1, 1); //이번달 1일 set
        int startday = Cal.get(Calendar.DAY_OF_WEEK); //1일의 요일
// 1일 전 요일들에 공백채우기
        for (int i = 1; i < startday; i++) {
            dayList.add("");
        }
//현재 월에 끝일 구하기
        setCalDate(Cal.get(Calendar.MONTH)+1);

//어댑터 연결
        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter); // 어댑터를 그리스뷰 객체에 연결

//버튼 설정
        Button prev = findViewById(R.id.button2);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                //1월 이전은 년 -1,12월
                if(now_month < 1){
                    intent.putExtra("year",now_year-1);
                    intent.putExtra("month",11);
                }
                else{
                    intent.putExtra("year",now_year);
                    intent.putExtra("month",now_month-1);
                }
                startActivity(intent);
                finish();
            }
        });

        Button next = findViewById(R.id.button3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                //1월 이전은 년 +1,1월
                if(now_month > 10){
                    intent.putExtra("year",now_year+1);
                    intent.putExtra("month",0);
                }
                else{
                    intent.putExtra("year",now_year);
                    intent.putExtra("month",now_month+1);
                }
                startActivity(intent);
                finish();
            }
        });
    }

    // 해당 월에 표시할 일 수
    private void setCalDate(int now_month){
        Cal.set(Calendar.MONTH, now_month +1);

        for (int i = 0; i < Cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i+1));
        }
    }

    private class GridAdapter extends BaseAdapter {

        private List<String> list;

        private LayoutInflater inflater;

        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gridview_item, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.tv_item_gridview);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));

            return convertView;
        }
    }
    private class ViewHolder {
        TextView tvItemGridView;
    }

}