package com.shoppingstore.app.formcommon.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shoppingstore.app.R;
import com.shoppingstore.app.R.id;
import com.shoppingstore.app.R.layout;

/**
 * 创建等待加载
 * @author meicunzhi
 * @date 2015-11-28 下午8:32:59
 */
public class LoadingProgressDialog extends Dialog {

	public LoadingProgressDialog(Context context) {
		this(context, R.style.loading_dialog);
		// TODO Auto-generated constructor stub 
	}
	
	public LoadingProgressDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.loading_dialog_layout, null);
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.dialog_view);
		setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		
		RelativeLayout layout_loading = (RelativeLayout) view.findViewById(R.id.layout_loading);
		layout_loading.getBackground().setAlpha(90);
		// 不可以用“返回键”取消
		setCancelable(false);
		ImageView spaceshipImage = (ImageView) view.findViewById(R.id.imageview_loading); 
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation); 
	} 
}
