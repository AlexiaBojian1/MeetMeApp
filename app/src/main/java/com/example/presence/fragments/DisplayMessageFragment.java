package com.example.presence.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.presence.R;
import com.example.presence.helpers.LabelGetter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayMessageFragment} factory method to
 * create an instance of this fragment.
 */
public class DisplayMessageFragment extends Fragment {

    public DisplayMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_message, container, false);
        TextView messageTextView = view.findViewById(R.id.message_text_view);
        Bundle args = getArguments();
        if (args != null) {
            String message = args.getString(LabelGetter.messageLbl);
            messageTextView.setText(message);
        }
        return view;
    }
}
