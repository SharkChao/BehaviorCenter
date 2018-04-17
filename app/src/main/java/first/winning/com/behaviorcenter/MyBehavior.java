package first.winning.com.behaviorcenter;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import static android.content.ContentValues.TAG;

/**
 * Created by Admin on 2018/4/17.
 */

public class MyBehavior extends CoordinatorLayout.Behavior<View>{
    //定义动画插值器，控制动画变化过程
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private float viewY;//控件移动的距离
    private boolean isAnimate;//动画是否在进行
    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        if(child.getVisibility() == View.VISIBLE&&viewY==0){
            //获取控件高度作为移动的距离
            viewY=child.getHeight();
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;//判断是否竖直滚动
    }

    //在嵌套滑动进行时，对象消费滚动距离前回调
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        //dy大于0是向上滚动 小于0是向下滚动
        Log.i(TAG, "onNestedPreScroll: "+dy +child.getVisibility());
        if (dy >=0&&!isAnimate&&child.getVisibility()==View.VISIBLE) {
            Log.i(TAG, "onNestedPreScroll: "+"向上滚动");
            hide(child);
        } else if (dy <0&&!isAnimate&&child.getVisibility()==View.GONE) {
            Log.i(TAG, "onNestedPreScroll: "+"向下滚动哦");

            show(child);
        }
    }
    //隐藏时的动画
    private void hide(final View view) {
        ViewPropertyAnimator animator = view.animate().translationY(viewY).setInterpolator(INTERPOLATOR).setDuration(200);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimate=true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
                isAnimate=false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                show(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }

    //显示时的动画
    private void show(final View view) {
        ViewPropertyAnimator animator = view.animate().translationY(0).setInterpolator(INTERPOLATOR).setDuration(200);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
                isAnimate=true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimate=false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                hide(view);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }
}
