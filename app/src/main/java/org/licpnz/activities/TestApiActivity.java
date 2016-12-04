package org.licpnz.activities;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.licpnz.R;
import org.licpnz.api.Api;
import org.licpnz.api.internal.ByteList;
import org.licpnz.api.news.NewsApi;


public class TestApiActivity extends AppCompatActivity {

    Button action;
    RecyclerView rv;
    Adapter mAdapter;

    public ArrayList<String> mList;


    public void onButtonClick(View v){
        new ConnectionTest();
    }

    public void addString(String s){
        mList.add(s);
        if (mAdapter!=null)
            mAdapter.notifyItemInserted(mList.size()-1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new Adapter();
        mList = new ArrayList<String>();
        setContentView(R.layout.activity_test_api);
        action = (Button) findViewById(R.id.button);
        rv = (RecyclerView) findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this,1,false));
        rv.setAdapter(mAdapter);
        rv.setHasFixedSize(true);
        addString("this is test");

    }


    public class VHC extends RecyclerView.ViewHolder{

        TextView tv;

        public VHC(View v){
            super(v);
            //v.setElevation(2f);
            v.setLayoutParams(new RecyclerView.LayoutParams(-1,-2));
            tv = new TextView(TestApiActivity.this);
            ((CardView)v).addView(tv,-1,-2);
        }
    }


    public class Adapter extends RecyclerView.Adapter<VHC>{

        @Override
        public VHC onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VHC(new CardView(TestApiActivity.this));
        }

        @Override
        public void onBindViewHolder(VHC holder, int position) {
            holder.tv.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    public class ConnectionTest implements Api.DataConnectionReceiver{

        //ArrayList<byte[]> outputs;
        ByteList outputs;

        public long startMillis=0;

        public Handler h = new Handler(){
            public void handleMessage(Message msg){
                addString((String)msg.obj);
            }
        };
        public ConnectionTest(){
            super();
            outputs = new ByteList();// ArrayList<byte[]>();
            new Thread(){
                public void run(){
                    //Api.request("http://licpnz.ru/",ConnectionTest.this);
                    Api.init();
                    Api.getApi().getNewsApi().getNews(1,1);
                    //Api.request(Api.getHost()+"api/news.get.php",ConnectionTest.this);
                    //Api.request("http://192.168.1.38:8080/api/news.get.php",ConnectionTest.this);
                    //Api.request("http://licpnz.far.ru/api/news.get.php",ConnectionTest.this);
                }
            }.start();
        }

        @Override
        public void onReceive(byte[] in, int state) {
            if (state==1){
                outputs.add(in);
            }else{
                if (state==0) {
                    startMillis = System.currentTimeMillis();
                }else {
                    long time = System.currentTimeMillis() - startMillis;

                    outputs.add(in);
                    byte[] imp = outputs.toArray();
                    String m = new String(imp);
                    //m = m.toString();
                    //System.out.println(m);
                    //implode(outputs);
                    final String s =// "contains \\n:" + m.split("\n").length +
                            " read " + imp.length + " bytes in " + time + "milliseconds\n  " + m ;
                   // final String ss = s.replaceAll("\\n","\n");
                    //addString(s);
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                           // addString("sssokok");
                            addString(s);
                        }
                    });
                    /*for (byte b : imp) {
                        final String a = b + "\n";// + new String(imp.clone()));
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                addString(a);
                            }
                        });
                    }*/
                }
            }
        }

        public byte[] implode(ArrayList<byte[]> arr){
            int length = 0;
            int lastOffset = 0;
            for (byte[] b : arr)
                length+=b.length;
            byte[] out = new byte[length];
            for (int i = 0;i<arr.size();i++){
                int j = 0;
                final byte[] b = arr.get(i);
                int s = b.length;
                while (j<s){
                    out[lastOffset+j] = b[j];
                    j++;
                }
                lastOffset+=s;
            }
            arr = null;
            return out;
        }
    }
}
