package com.example.todolist;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNote;
    private FloatingActionButton buttonAddNote;
    private NotesAdapter notesAdapter;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initViews();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        notesAdapter = new NotesAdapter();

        notesAdapter.setOnNoteClickListener(note -> {

        });

        recyclerViewNote.setAdapter(notesAdapter);

        buttonAddNote.setOnClickListener(
                v -> startActivity(CreatingNoteActivity.newIntent(MainActivity.this))
        );

        viewModel.getNotes().observe(this, notes ->
            notesAdapter.setNotes(notes)
        );

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT
                ) {
                    @Override
                    public boolean onMove(
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target
                    ) {
                        return false;
                    }

                    @Override
                    public void onSwiped(
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            int direction
                    ) {
                        int position = viewHolder.getBindingAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) return;
                        Note note = notesAdapter.getNotes().get(position);
                        viewModel.remove(note);
                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(recyclerViewNote);
    }

    private void initViews(){
        recyclerViewNote = findViewById(R.id.recyclerViewNote);
        buttonAddNote = findViewById(R.id.buttonAddNote);
    }
}