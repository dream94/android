package com.component;

import java.text.AttributedCharacterIterator.Attribute;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class PasswordEditText extends EditText {
	public PasswordEditText(Context ctx) {
		super(ctx);
		
	}
	public PasswordEditText(Context ctx, AttributeSet attr) {
		super(ctx, attr);
		this.setTransformationMethod(new PasswordInputType());
	}
	
	
	
}
