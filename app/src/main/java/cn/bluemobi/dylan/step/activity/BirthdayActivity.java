package cn.bluemobi.dylan.step.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.totcy.salelibrary.HorizontalScaleScrollView;

import java.util.List;

import cn.bluemobi.dylan.step.R;
import cn.bluemobi.dylan.step.constant.Constants;
import cn.bluemobi.dylan.step.step.pojo.BodyData;
import cn.bluemobi.dylan.step.step.utils.DbUtils;

/**
 * Created by wangchen on 2017/3/17.
 */

public class BirthdayActivity extends Activity implements View.OnClickListener, HorizontalScaleScrollView.OnScrollListener {
    private ImageView iv_left;
    private LinearLayout layout_titlebar;
    private ImageView male;
    private LinearLayout layout_female;
    private TextView tv_scale;
    private HorizontalScaleScrollView hsScale;
    private Button last;
    private Button next;
    private LinearLayout layout_rule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        initView();
    }

    private void initView() {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setOnClickListener(this);
        layout_titlebar = (LinearLayout) findViewById(R.id.layout_titlebar);
//        male = (ImageView) findViewById(R.id.male);
        if (Constants.sex.equals("男")) {
            findViewById(R.id.male).setVisibility(View.VISIBLE);
        } else if (Constants.sex.equals("女")) {
            findViewById(R.id.female).setVisibility(View.VISIBLE);
        }
        layout_female = (LinearLayout) findViewById(R.id.layout_female);
        tv_scale = (TextView) findViewById(R.id.tv_scale);
        hsScale = (HorizontalScaleScrollView) findViewById(R.id.hsScale);
        hsScale.setScrollListener(this);
        last = (Button) findViewById(R.id.last);
        next = (Button) findViewById(R.id.next);
        layout_rule = (LinearLayout) findViewById(R.id.layout_rule);

        last.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.last:
                Intent intent = new Intent(this, HeightActivity.class);
                if (Constants.sex.equals("男")) {
                    intent.putExtra("sex", 1);
                } else if (Constants.sex.equals("女")) {
                    intent.putExtra("sex", 0);
                }
                startActivity(intent);
                break;
            case R.id.next:
                save();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.iv_left:
                finish();
                break;
        }
    }

    @Override
    public void onScaleScroll(float scale) {

    }

    @Override
    public void onScaleScroll(int scale) {
        tv_scale.setText(scale + "年");
    }

    private void save() {
        if (DbUtils.getLiteOrm() == null) {
            DbUtils.createDb(this, "body");
        }
        List<BodyData> list = DbUtils.getQueryAll(BodyData.class);
        if (list.size() == 0 || list.isEmpty()) {
            BodyData data = new BodyData();
            data.setSex(Constants.sex);
            data.setHeight(Constants.height);
            data.setWeight(Constants.weight);
            data.setBirthday(tv_scale.getText().toString());
            DbUtils.insert(data);
        } else if (list.size() == 1) {
            BodyData data = list.get(0);
            data.setSex(Constants.sex);
            data.setHeight(Constants.height);
            data.setWeight(Constants.weight);
            data.setBirthday(tv_scale.getText().toString());
            DbUtils.update(data);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出设置个人资料吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        startActivity(new Intent(BirthdayActivity.this,MainActivity.class));
                        BirthdayActivity.this.finish();

                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }
}
