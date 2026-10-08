package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class CreatingNoteActivity extends AppCompatActivity {

    private EditText editTextNote;
    private Chip chipLow;
    private Chip chipMedium;
    private Chip chipHigh;
    private ChipGroup chipGroupPriority;
    private Button buttonSave;
    private CreatingNoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_creating_note);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(CreatingNoteViewModel.class);
        viewModel.getShouldCloseScreen().observe(
                this,
                shouldClose ->{
                    if(shouldClose)
                        finish();
                });

        initViews();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private void initViews(){
        editTextNote = findViewById(R.id.editTextNote);
        chipLow = findViewById(R.id.chipLow);
        chipMedium = findViewById(R.id.chipMedium);
        chipHigh = findViewById(R.id.chipHigh);
        chipGroupPriority = findViewById(R.id.chipGroupPriority);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void saveNote() {
        String text = editTextNote.getText().toString().trim();

        if (text.isEmpty()) {
            Toast.makeText(this, R.string.empty_field, Toast.LENGTH_SHORT).show();
            return;
        }

        int checkedId = chipGroupPriority.getCheckedChipId();
        if (checkedId == View.NO_ID) {
            Toast.makeText(this, R.string.not_priority, Toast.LENGTH_SHORT).show();
            return;
        }

        int priority = getPriority(checkedId);
        viewModel.saveNote(new Note(text, priority));
    }

    private int getPriority(int checkedId){
        if (checkedId == R.id.chipLow) return 0;
        if (checkedId == R.id.chipMedium) return 1;
        return 2;
    }

    public static Intent newIntent(Context context){
        return new Intent(context, CreatingNoteActivity.class);
    }
}