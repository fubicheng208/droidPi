package com.work.mdroidpi;
import android.content.Context;
import android.util.AttributeSet;

import com.gc.materialdesign.views.ButtonRectangle;
/**
 * Created by fubicheng on 2016/4/17.
 */
public class mButton extends ButtonRectangle {
    //true对应绿色 low，false对应黄色 high
    private boolean _pressed = true;

    public mButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        _pressed = true;
    }

    public void change_Pressed(){
        if(_pressed)
            _pressed = false;
        else
            _pressed = true;
    }

    public void set_pressed(boolean b){
        _pressed = b;
    }

    public boolean get_Pressed(){
        return  _pressed;
    }


}
