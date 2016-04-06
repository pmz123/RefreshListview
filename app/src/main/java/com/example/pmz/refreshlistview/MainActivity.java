package com.example.pmz.refreshlistview;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;



import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private PullToRefreshListView listView;
    private ArrayList<String> list=new ArrayList<>();
    private ReListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //终于上传到GitHub上面去了
        setContentView(R.layout.activity_main);
        loadDate();
        listView= (PullToRefreshListView) findViewById(R.id.refresh_listView);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                new loadDataAsyncTask(MainActivity.this).execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new loadDataAsyncTask(MainActivity.this).execute();
            }
        });
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter=new ReListAdapter();
        listView.setAdapter(adapter);


    }

    static class loadDataAsyncTask extends AsyncTask<Void,Void,String>{


        private MainActivity activity;

        public loadDataAsyncTask(MainActivity activity){
            this.activity=activity;
        }
        @Override
        protected String doInBackground(Void... params) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            activity.loadDate();

            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if ("success".equals(s)){
                activity.adapter.notifyDataSetChanged();
                activity.listView.onRefreshComplete();
            }


        }
    }

    private void loadDate() {
        for (int i=0;i<10;i++){
            list.add("條目"+i);
        }

    }
    class ReListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView==null){
                holder=new ViewHolder();
                convertView= LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_item,null);
                holder.tv= (TextView) convertView.findViewById(R.id.item);

                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }

            holder.tv.setText(list.get(position));




            return convertView;
        }
        class ViewHolder{
            TextView tv;
        }
    }
}
