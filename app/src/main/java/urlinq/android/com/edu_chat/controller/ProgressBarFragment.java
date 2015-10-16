package urlinq.android.com.edu_chat.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import urlinq.android.com.edu_chat.R;

/**
 * Created by Kai on 10/16/2015.
 */
public class ProgressBarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.progress_bar_layout, container, false);

        return v;

    }
}
