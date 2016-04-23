package com.work.mdroidpi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.Dialog;

public class settings extends AppCompatActivity {

    private EditText ed_ip;
    private EditText ed_port;
    private ButtonRectangle change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ed_ip = (EditText)findViewById(R.id.ip);
        ed_port = (EditText)findViewById(R.id.port);
        change = (ButtonRectangle)findViewById(R.id.change);
        //SharedPreferences存储ip和port信息
        SharedPreferences sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        ed_ip.setText(sp.getString("ip",""));
        ed_port.setText(sp.getString("port",""));

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1,s2;
                s1 = ed_ip.getText().toString();
                s2 = ed_port.getText().toString();
                if(!s1.equals("")&&!s2.equals("")){
                    SharedPreferences sp = getSharedPreferences("Settings", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("ip",s1);
                    editor.putString("port",s2);
                    editor.commit();
                    Dialog dialog = new Dialog(settings.this, "Congratulations", "Change successfully!");
                    dialog.show();
                }
                else{
                    Dialog dialog = new Dialog(settings.this, "ERROR", "The Ip or port can't be null!");
                    dialog.show();
                }
            }
        });

        //判断intent,以便确认是否需要提示DIALOG
        Intent intent = this.getIntent();
        if(intent.getBooleanExtra("TAG",false)==true){
            Dialog dialog = new Dialog(settings.this, "CAUTION", "You should first set your ip and port!");
            dialog.show();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
