package com.coolstar.linearbubbletest;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    View btnAdd;
    LinearLayout danmakuContainer;
    private View.OnClickListener addBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String inputStr = editText.getText().toString();
            if(TextUtils.isEmpty(inputStr)){
                showLog("请输入弹幕内容再发送！");
                return;
            }
            addContentToDanmaku(inputStr);
        }
    };
    private LayoutTransition mTransitioner;

    /**
     * 发送弹幕内容到界面上
     * @param inputStr
     */
    private void addContentToDanmaku(String inputStr) {
        View inputView = createDanmakuView(inputStr);               //创建弹幕的背景及内容
        LinearLayout.LayoutParams inputLP = createDanmakuLP();      //自定义每一条弹幕的margin值
        danmakuContainer.addView(inputView,inputLP);
        inputView.postDelayed(autoRemoveSelf(inputView),5000);               //5秒后自动删除自己这条弹幕
    }

    private View createDanmakuView(String inputStr) {
        TextView textView = new TextView(this);
        textView.setText(inputStr);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setTextSize(getDip(10));
        textView.setBackgroundResource(getBgResId());
        textView.setCompoundDrawables(getHeadDrawable(),null,null,null);
        return textView;
    }

    private Drawable getHeadDrawable() {
        Drawable drawable;
        if(danmakuContainer.getChildCount()%2==0){
            drawable= getResources().getDrawable(R.drawable.myself_head);
        }else {
            drawable= getResources().getDrawable(R.drawable.vs_contact_head_detail);
        }
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, (int)getDip(40), (int)getDip(40));
        return drawable;
    }

    private int getBgResId() {
        if(danmakuContainer.getChildCount()%2==0){
            return R.drawable.chatfrom_bg_normal;
        }else {
            return R.drawable.chatfrom_bg_pressed;
        }
    }

    private LinearLayout.LayoutParams createDanmakuLP() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = (int) getDip(10);
        layoutParams.setMargins(margin,margin,margin,margin);
        return layoutParams;
    }

    private Runnable autoRemoveSelf(final View v) {
        return new Runnable() {
            @Override
            public void run() {
                danmakuContainer.removeView(v);
            }
        };
    }

    private void showLog(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        editText = (EditText)findViewById(R.id.etInput);
        btnAdd = findViewById(R.id.btnAdd);
        danmakuContainer = (LinearLayout)findViewById(R.id.danmakulayout);
        btnAdd.setOnClickListener(addBtnClickListener);

        initTransition(danmakuContainer);
        setTransition();
    }
    DisplayMetrics metrics;
    private float getDip(int value){
        if(metrics==null){
            metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,metrics);
    }

    /**
     * 自定义动画效果
     */
    private void setTransition() {
        /**
         * view出现时 view自身的动画效果
         */
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        ObjectAnimator multAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY, pvhZ)  //使用此种方法，可定义多属性同时修改的动画
                .setDuration(mTransitioner.getDuration(LayoutTransition.APPEARING));
        multAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                View view = (View) ((ObjectAnimator) animation).getTarget();  //使用动画监听器，主要是为了处理缩放的中心点修改到自己想要的位置
                view.setPivotX(0f);
                view.setPivotY(view.getHeight());
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mTransitioner.setAnimator(LayoutTransition.APPEARING, multAnim);

        /**
         * view 消失时，view自身的动画效果
         */
        pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        multAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY, pvhZ)
                .setDuration(mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
        multAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                View view = (View) ((ObjectAnimator) animation).getTarget();
                view.setPivotX(0f);
                view.setPivotY(0f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, multAnim);

        /**
         * view出现时，导致整个布局改变的动画
         */

        /**
         * view消失，导致整个布局改变时的动画
         */
    }

    /**
     * 初始化容器动画
     */
    private void initTransition(ViewGroup layout) {
        mTransitioner = new LayoutTransition();
        layout.setLayoutTransition(mTransitioner);
    }
}
