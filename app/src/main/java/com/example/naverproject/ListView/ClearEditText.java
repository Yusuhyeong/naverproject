/*
 * ClearEditText
 * 2023.06.02
 */


package com.example.naverproject.ListView;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.naverproject.R;

// EditText의 입력시 입력한 내용을 삭제시켜주는 x버튼을 제작하기 위한 class
public class ClearEditText extends AppCompatEditText implements TextWatcher, View.OnTouchListener, View.OnFocusChangeListener {
        private Drawable clearDrawable;
        private OnFocusChangeListener onFocusChangeListener; // x버튼에 대한 커서 초점을 위한 변수
        private OnTouchListener onTouchListener; // x버튼 클릭시 일어나는 이벤트를 위한 변수
        private InputMethodManager manager;

        public ClearEditText(final Context context) {
                super(context);
                init();
        }

        public ClearEditText(final Context context, final AttributeSet attrs) {
                super(context, attrs);
                init();
        }

        public ClearEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                init();
        }

        @Override
        public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
                this.onFocusChangeListener = onFocusChangeListener;
        }

        @Override
        public void setOnTouchListener(OnTouchListener onTouchListener) {
                this.onTouchListener = onTouchListener;
        }


        @SuppressLint("ClickableViewAccessibility")
        private void init() {
                Drawable tempDrawable = ContextCompat.getDrawable(getContext(), R.drawable.btn_close);
                clearDrawable = DrawableCompat.wrap(tempDrawable);
                DrawableCompat.setTintList(clearDrawable, getHintTextColors());
                clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());

                setClearIconVisible(false);

                manager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);

                super.setOnTouchListener(this);
        }


        @Override
        public void onFocusChange(final View view, final boolean hasFocus) {
                if (hasFocus) {
                        setClearIconVisible(getText().length() > 0);
                } else {
                        setClearIconVisible(false);
                }

                if (onFocusChangeListener != null) {
                        onFocusChangeListener.onFocusChange(view, hasFocus);
                }
        }


        @Override
        public boolean onTouch(final View view, final MotionEvent motionEvent) { // x버튼 touch이벤트에 대한 text삭제 이벤트
                final int x = (int) motionEvent.getX();
                if (clearDrawable.isVisible() && x > getWidth() - getPaddingRight() - clearDrawable.getIntrinsicWidth()) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                setError(null);
                                setText(null);
                                showKeyboard();
                        }
                        return true;
                }

                if (onTouchListener != null) {
                        return onTouchListener.onTouch(view, motionEvent);
                } else {
                        return false;
                }
        }

        @Override
        public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if (isFocused()) {
                        setClearIconVisible(s.length() > 0);
                }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }


        private void setClearIconVisible(boolean visible) {
                clearDrawable.setVisible(visible, false);
                setCompoundDrawables(null, null, visible ? clearDrawable : null, null);
        }

        private void showKeyboard() {
                if (manager != null) {
                        manager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
                }
        }
}