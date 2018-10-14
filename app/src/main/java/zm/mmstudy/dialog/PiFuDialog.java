package zm.mmstudy.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;
import zm.mmstudy.MainActivity;
import zm.mmstudy.R;
import zm.mmstudy.service.MainService;


/**
 * Created by zm on 2018/7/27.
 */

public class PiFuDialog extends DialogFragment {

    MainActivity mainActivity;
    // 回调接口
    public interface OnDialogListener
    {
        void onDialogInput(String option);
    }
    public OnDialogListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mainActivity= (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , Bundle savedInstanceState)
    {
        // 创建View, 指定布局XML
        View view = inflater.inflate(R.layout.dialog_hualun, container);


        final NumberPickerView numberPickerView= view.findViewById(R.id.picker);
        String[] piFus = {"天依","阿绫","言和","星尘","心华","蕾姆"};
        numberPickerView.setDisplayedValues(piFus);
        numberPickerView.setMinValue(0);
        numberPickerView.setMaxValue(piFus.length - 1);
        String nowPiFu = MainService.getLaoPo(mainActivity);
        for (int i = 0; i < piFus.length; i++) {
            if (piFus[i].equals(nowPiFu)) {
                numberPickerView.setValue(i);
                break;
            }
        }
        // 点击按钮时，关闭对话框
        Button button = (Button)view.findViewById(R.id.id_ok);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss(); // 关闭对话框

                // 返回给调用者
                if(listener != null)
                {

                    listener.onDialogInput( numberPickerView.getContentByCurrValue());
                }

            }
        });

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // 当对话框显示时，调整对话框的窗口位置
        Window window = getDialog().getWindow();
        if (window != null)
        {
            window.setBackgroundDrawable( new ColorDrawable(Color.WHITE));

            // 设置对话框的窗口显示
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount=0.3f; // 背景灰度
            lp.gravity = Gravity.BOTTOM; // 靠下显示
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }
}
