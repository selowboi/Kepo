package com.example.kepo.error;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kepo.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

public class Error_2 extends BottomSheetDialogFragment {

    private TextView tvErrorTitle;
    private TextView tvErrorMessage;
    private Button btnError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_error_login, container, false);

        btnError = v.findViewById(R.id.btn_errorMessage);

        btnError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }


}
