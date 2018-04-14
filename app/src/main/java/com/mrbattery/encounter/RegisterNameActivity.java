package com.mrbattery.encounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterNameActivity extends AppCompatActivity {

    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_input_error)
    TextView tvInputError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.next_button)
    public void next() {
        if (tvUserName.getText() != null){//如果用户昵称不为空
            Intent intent = new Intent(RegisterNameActivity.this, RegisterPasswordActivity.class);
            //用Bundle携带数据
            Bundle bundle = new Bundle();
            //传递user对象
            bundle.putSerializable("userName", String.valueOf(tvUserName.getText()));
            intent.putExtras(bundle);
            startActivity(intent);
        }else {
            Toast.makeText(this, R.string.error_field_required, Toast.LENGTH_SHORT).show();
        }
    }
}
