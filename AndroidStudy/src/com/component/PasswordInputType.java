package com.component;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;

public class PasswordInputType implements TransformationMethod {

	@Override
	public CharSequence getTransformation(CharSequence source, View view) {
		// TODO Auto-generated method stub
		return new PasswordCharSequence(source);
	}

	 
	private class PasswordCharSequence implements CharSequence {
	    private CharSequence mSource;
	    public PasswordCharSequence(CharSequence source) {
	        mSource = source; // Store char sequence
	    }
	    public char charAt(int index) {
	        return '*'; // This is the important part
	    }
	    public int length() {
	        return mSource.length(); // Return default
	    }
	    public CharSequence subSequence(int start, int end) {
	        return mSource.subSequence(start, end); // Return default
	    }
	}


	@Override
	public void onFocusChanged(View view, CharSequence sourceText,
			boolean focused, int direction, Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		
	}

}
