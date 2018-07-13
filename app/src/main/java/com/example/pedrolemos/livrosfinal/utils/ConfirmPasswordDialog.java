package com.example.pedrolemos.livrosfinal.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedrolemos.livrosfinal.R;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class ConfirmPasswordDialog extends DialogFragment {

    public interface OnConfirmPasswordListener{
        public void onConfirmPassword(String password);
    }
    OnConfirmPasswordListener onConfirmPasswordListener;

    TextView pass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password, container, false);

        TextView cancelDialog = view.findViewById(R.id.dialog_cancel);
        TextView confirmDialog = view.findViewById(R.id.dialog_confirm);

        pass = (TextView) view.findViewById(R.id.et_confirm_password_dialog);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = pass.getText().toString();
                if(!password.equals("")){
                    onConfirmPasswordListener.onConfirmPassword(password);
                    getDialog().dismiss();
                }else{
                    Toast.makeText(getActivity(), "You must provide a password", Toast.LENGTH_SHORT).show();
                }

            }
        });



        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            onConfirmPasswordListener = (OnConfirmPasswordListener) getActivity();
        }catch (ClassCastException c){
            Log.e("Erro", "onAttach: ClassCastException " + c.getMessage());
        }
    }
}
