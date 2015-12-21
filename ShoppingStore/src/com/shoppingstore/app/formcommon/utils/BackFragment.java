package com.shoppingstore.app.formcommon.utils; 

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class BackFragment extends Fragment{
	protected BackFragmentInterface mBackHandledInterface;

	/**
	 * 所有继承BackFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
	 */
	protected abstract boolean onBackPressed();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!(getActivity() instanceof BackFragmentInterface)) {
			throw new ClassCastException(
					"Hosting Activity must implement BackHandledInterface");
		} else {
			this.mBackHandledInterface = (BackFragmentInterface) getActivity();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// 告诉FragmentActivity，当前Fragment在栈顶
		//mBackHandledInterface.setCurrentFragment(this);
	}
}
