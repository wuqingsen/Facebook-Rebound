package com.example.yangyu.springanimationdemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private Button one, two, three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.animation_image);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
                One();
                break;
            case R.id.two:
                Two();
                break;
            case R.id.three:
                Three();
                break;
        }
    }

    //Facebook-Rebound
    private void One() {
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.chain_layout);
        viewGroup.setVisibility(View.VISIBLE);
        //创建SpringChain对象
        SpringChain springChain = SpringChain.create();
        //获取到子控件
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View view = viewGroup.getChildAt(i);

            springChain.addSpring(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    float value = (float) spring.getCurrentValue();
                    //只设置沿X轴
                    view.setTranslationX(value);
                }
            });
        }

        List<Spring> springs = springChain.getAllSprings();
        for (int i = 0; i < springs.size(); i++) {
            springs.get(i).setCurrentValue(1080);
        }

        //setControlSpringIndex：从第几个子view开始
        springChain.setControlSpringIndex(0);
        //设置结束的位置，
        springChain.getControlSpring().setEndValue(0);
    }

    //Facebook-Rebound弹簧
    private void Two() {
        //创建SpringSystem对象
        SpringSystem springSystem = SpringSystem.create();
        //添加到弹簧系统
        Spring spring = springSystem.createSpring();
        //动画开始的图片大小，1.0f：原图开始；增大则开始的图片也大
        spring.setCurrentValue(1.0f);
        //tension(拉力)值改成100(拉力值越小，晃动越慢)，friction(摩擦力)值改成1(摩擦力越小，弹动大小越明显)
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100, 1));
        //与上面一样，回弹效果更明显，但速度慢
//        spring.setSpringConfig(new SpringConfig(100, 1));
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                imageView.setScaleX(scale);
                imageView.setScaleY(scale);
            }
        });
        spring.setEndValue(0.1f);
    }

    //属性动画
    private void Three() {
        /**
         * 五个参数：
         * 1.要放大/缩小的控件
         * 2.方向：scaleX为沿X轴缩放，scaleY为沿Y轴缩放
         * 3.开始的大小：1.0f为原图开始
         * 4.放大/缩小的倍数：1.5f为放大1.5倍
         * 5.结束的大小：1.0f为原图结束
         */
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 1.5f, 1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 1.5f, 1f);
        AnimatorSet set = new AnimatorSet();
        //动画持续时间
        set.setDuration(1000);
        //动画弹动的速度，越大越慢
        set.setInterpolator(new SpringScaleInterpolator(0.3f));
        //X轴和Y轴同时开始
        set.playTogether(animatorX, animatorY);
        //动画开始
        set.start();
    }
}
