package com.mrbattery.encounter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrbattery.encounter.constant.Constant;
import com.mrbattery.encounter.constant.EPQTest;
import com.mrbattery.encounter.util.HttpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;
import static com.mrbattery.encounter.constant.Constant.getSERVER_IP;

public class EPQActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ll_epq_start)
    LinearLayout llEPQStart;
    @BindView(R.id.ll_epq_test)
    LinearLayout llEPQTest;
    @BindView(R.id.ll_epq_result)
    LinearLayout llEPQResult;

    @BindView(R.id.tv_test_num)
    TextView tvTestNum;
    @BindView(R.id.tv_test_content)
    TextView tvTestContent;

    @BindView(R.id.tv_e_score)
    TextView tvEScore;
    @BindView(R.id.tv_n_score)
    TextView tvNScore;
    @BindView(R.id.tv_p_score)
    TextView tvPScore;

    @BindView(R.id.tv_result_analyse)
    TextView tvResultAnalyse;

    int i = -1;//当前测试题序数（从0开始数）（初始值为-1，点击开始按钮后自加1为0）
    double eScore = 0;
    double nScore = 0;
    double pScore = 0;
    double lScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epq);
        //绑定ButterKnife
        ButterKnife.bind(this);
        //设置Toolbar为Actionbar
        setSupportActionBar(toolbar);
        //切换为测试开始
        switchEPQView(R.id.ll_epq_start);
    }


    @OnClick({R.id.start_button, R.id.yes_button, R.id.no_btn})
    public void makeScore(Button button) {
        switch (button.getId()) {
            case R.id.start_button:
                switchEPQView(R.id.ll_epq_test);
                Log.i(TAG, "测试开始！！");
                break;
            case R.id.yes_button:
                if (EPQTest.scoreRule[EPQTest.E][i] == EPQTest.positive) {
                    eScore++;
                    Log.i(TAG, "第" + (i + 1) + "题，是，E得1分/共" + eScore + "分");
                }
                if (EPQTest.scoreRule[EPQTest.N][i] == EPQTest.positive) {
                    nScore++;
                    Log.i(TAG, "第" + (i + 1) + "题，是，N得1分/共" + eScore + "分");
                }
                if (EPQTest.scoreRule[EPQTest.P][i] == EPQTest.positive) {
                    pScore++;
                    Log.i(TAG, "第" + (i + 1) + "题，是，P得1分/共" + eScore + "分");
                }
                if (EPQTest.scoreRule[EPQTest.L][i] == EPQTest.positive) {
                    lScore++;
                    Log.i(TAG, "第" + (i + 1) + "题，是，L得1分/共" + eScore + "分");
                }
                break;
            case R.id.no_btn:
                if (EPQTest.scoreRule[EPQTest.E][i] == EPQTest.negative) {
                    eScore++;
                    Log.i(TAG, "第" + (i + 1) + "题，否，E得1分/共" + eScore + "分");
                }
                if (EPQTest.scoreRule[EPQTest.N][i] == EPQTest.negative) {
                    nScore++;
                    Log.i(TAG, "第" + (i + 1) + "题，否，N得1分/共" + eScore + "分");
                }
                if (EPQTest.scoreRule[EPQTest.P][i] == EPQTest.negative) {
                    pScore++;
                    Log.i(TAG, "第" + (i + 1) + "题，否，P得1分/共" + eScore + "分");
                }
                if (EPQTest.scoreRule[EPQTest.L][i] == EPQTest.negative) {
                    lScore++;
                    Log.i(TAG, "第" + (i + 1) + "题，否，L得1分/共" + eScore + "分");
                }
                break;
            default:
                break;
        }
        if (i < EPQTest.EPQ.length - 1) {
            i++;
            String testNum = "第" + (i + 1) + "/" + EPQTest.EPQ.length + "题";
            tvTestNum.setText(testNum);
            tvTestContent.setText(EPQTest.EPQ[i]);

        } else {//题答完了
            Log.i(TAG, "测试粗分:\nE" + eScore + "\nN" + nScore + "\nP" + pScore + "\nL" + lScore);
            eScore = EPQTest.NormConverse(EPQTest.E, eScore);
            nScore = EPQTest.NormConverse(EPQTest.N, eScore);
            pScore = EPQTest.NormConverse(EPQTest.P, eScore);
            lScore = EPQTest.NormConverse(EPQTest.L, eScore);
            Log.i(TAG, "常模转换结果:\nE" + eScore + "\nN" + nScore + "\nP" + pScore + "\nL" + lScore);

            tvEScore.setText(String.valueOf((int) eScore));
            tvNScore.setText(String.valueOf((int) nScore));
            tvPScore.setText(String.valueOf((int) pScore));
            tvResultAnalyse.setText(EPQTest.resultAnalyse(eScore, nScore, pScore, lScore));
            commit(eScore, nScore, pScore, lScore);
        }
    }

    @OnClick(R.id.confirm_button)
    public void confirm() {
        finish();
    }

    private void commit(final double eScore, final double nScore, double pScore, double lScore) {
        String url = "http://" + getSERVER_IP() + ":8080/post_epq_score?" +
                "userID=" + Constant.getCurrUserID()
                + "&eScore=" + eScore
                + "&nScore=" + nScore
                + "&pScore=" + pScore
                + "&lScore=" + lScore;
        Log.i(TAG, url);
        HttpUtil.getDataAsync(url, this, new Runnable() {
            @Override
            public void run() {
                String responseData = HttpUtil.getResponseData();
                if (responseData.equals("success")) {
                    switchEPQView(R.id.ll_epq_result);
                    Log.i(TAG, "run: post successfully!!!");
                } else {
                    Toast.makeText(EPQActivity.this, "测试结果上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void switchEPQView(int llId) {
        switch (llId) {
            case R.id.ll_epq_start:
                llEPQStart.setVisibility(View.VISIBLE);
                llEPQTest.setVisibility(View.GONE);
                llEPQResult.setVisibility(View.GONE);
                break;
            case R.id.ll_epq_test:
                llEPQStart.setVisibility(View.GONE);
                llEPQTest.setVisibility(View.VISIBLE);
                llEPQResult.setVisibility(View.GONE);
                break;
            case R.id.ll_epq_result:
                llEPQStart.setVisibility(View.GONE);
                llEPQTest.setVisibility(View.GONE);
                llEPQResult.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    @OnClick(R.id.iv_close)
    public void cancel() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.icon_encounter)
                .setTitle("放弃测试吗？")
//                .setMessage("不保留更改吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        cancel();
    }
}
