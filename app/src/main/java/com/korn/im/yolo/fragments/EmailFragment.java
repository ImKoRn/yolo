package com.korn.im.yolo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.korn.im.yolo.R;
import com.korn.im.yolo.loaders.DataLoader;

/**
 * Fragment for communication with agency
 * */
public class EmailFragment extends Fragment {
    private EditText nameView;
    private EditText emailView;
    private EditText phoneNumberView;
    private EditText textView;

    private Button sendBtn;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if(view == null) view = inflater.inflate(R.layout.fragment_mail, container, false);

        initUi(view);

        return view;
    }

    private void initUi(View view) {
        //TODO Change hardcoded string to resource
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendBtn.setEnabled(nameView.getError() == null && emailView.getError() == null &&
                        phoneNumberView.getError() == null && textView.getError() == null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        nameView = (EditText) view.findViewById(R.id.nameView);
        nameView.addTextChangedListener(textWatcher);
        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) nameView.setError("Empty");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailView = (EditText) view.findViewById(R.id.emailView);
        emailView.addTextChangedListener(textWatcher);
        emailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) emailView.setError("Empty");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isEmailValid(s.toString())) emailView.setError("Invalid");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phoneNumberView = (EditText) view.findViewById(R.id.phoneNumberView);
        phoneNumberView.addTextChangedListener(textWatcher);
        phoneNumberView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) phoneNumberView.setError("Empty");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textView = (EditText) view.findViewById(R.id.textView);
        textView.addTextChangedListener(textWatcher);
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) textView.setError("Empty");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        sendBtn = (Button) view.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.promptSendDialogTitle)
                        .setMessage(R.string.promptSendDialogMsg)
                        .setPositiveButton(R.string.promptPossitiveBtnText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "Hi", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.promptNegativeBtnText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "No(", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();
                alertDialog.show();*/
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
                intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
                intent.setData(Uri.parse("mailto:kor.wolf13@gmail.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
        });
    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        return email.matches(regExpn);
    }
}
