package zm.mmstudy.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import zm.mmstudy.MainActivity;
import zm.mmstudy.R;
import zm.mmstudy.activity.AboutMeActivity;
import zm.mmstudy.activity.UserFeedbackActivity;
import zm.mmstudy.dialog.PiFuDialog;
import zm.mmstudy.service.MainService;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    public View contentView;
    MainActivity mainActivity;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity= (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 创建界面
        contentView = inflater.inflate(R.layout.fragment_me, container, false);


        LinearLayout ll_chooselaopo = contentView.findViewById(R.id.ll_chooselaopo);
        ll_chooselaopo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                PiFuDialog dlg = new PiFuDialog();
                //回调接口
                dlg.listener = new PiFuDialog.OnDialogListener() {
                    @Override
                    public void onDialogInput(String option) {
                        MainService.setPiFu(mainActivity, option);
                    }
                };

                dlg.show(mainActivity.getSupportFragmentManager(), "PiFuDialog");

            }
        });


        LinearLayout ll_aboutme = contentView.findViewById(R.id.ll_aboutme);
        ll_aboutme.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, AboutMeActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout ll_sbsay = contentView.findViewById(R.id.ll_sbsay);
        ll_sbsay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, UserFeedbackActivity.class);
                startActivity(intent);


            }
        });

        LinearLayout ll_cheekversion = contentView.findViewById(R.id.ll_cheekversion);
        ll_cheekversion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               MainService.getResVersion(mainActivity);

            }
        });
        return contentView;
    }

}
