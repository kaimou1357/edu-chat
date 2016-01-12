package urlinq.android.com.edu_chat.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urlinq.edu_chat.R;

/**
 * Created by Kai Mou on 1/12/2016.
 */
public class SubchatFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v  = inflater.inflate(R.layout.subchat_dropdown, container, false);
        return v;

    }

}
