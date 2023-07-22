package com.codify92.reminderappmaterialdesign.Others;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codify92.reminderappmaterialdesign.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;
    private CharSequence finalDate = "";
    private TextView date;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        //TODO: Setting Views
        TextView save = v.findViewById(R.id.saveText);
        date = v.findViewById(R.id.enteredDateTV);
        EditText editText = v.findViewById(R.id.todoEditText);
        ImageView mPickDate = v.findViewById(R.id.pickDate);
        //TODO:Focusing on EditText When Launching
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        //TODO: Click Listeners
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredText = editText.getText().toString();
                if (enteredText.equals("")) {
                    Toast.makeText(getContext(), "No Text Found", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.onSaveClicked(enteredText + "", finalDate);
                    dismiss();
                }
            }
        });

        mPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDate = pickDate();
            }
        });

        return v;
    }

    private CharSequence pickDate() {

        final Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DAY = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, dayOfMonth);

                finalDate = DateFormat.format("MMM d, yyyy", calendar1);
                date.setText(finalDate);
                date.setVisibility(View.VISIBLE);
            }
        }, YEAR, MONTH, DAY);
        datePickerDialog.show();

        return finalDate;
    }

    //TODO:Interfaces
    public interface BottomSheetListener {
        void onSaveClicked(String EnteredText, CharSequence Date);

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "Must Implement Bottom Sheet Listener");
        }
    }
}
