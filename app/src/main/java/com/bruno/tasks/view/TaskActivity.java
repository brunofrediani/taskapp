package com.bruno.tasks.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bruno.tasks.R;
import com.bruno.tasks.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private ViewHolder mViewHolder = new ViewHolder();
    private TaskViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Botão de voltar nativo
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //mapeamento elementos
        this.mViewHolder.editDescription = findViewById(R.id.edit_description);
        this.mViewHolder.spinnerPriority = findViewById(R.id.spinner_priority);
        this.mViewHolder.checkComplete = findViewById(R.id.check_complete);
        this.mViewHolder.buttonDate = findViewById(R.id.button_date);
        this.mViewHolder.buttonSave = findViewById(R.id.button_save);

        // ViewModel
        this.mViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        this.createEvents();

        // Cria observadores
        this.loadObservers();
    }

    // Botão de voltar nativo
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_date){
            this.showDatePicker();
        } else if (id == R.id.button_save){

        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day= calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this,this,year,month,day).show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,dayOfMonth);
        String date = this.mFormat.format(calendar.getTime());
        this.mViewHolder.buttonDate.setText(date);
    }


    /**
     * Observadores
     */
    private void loadObservers() {
    }

    private void createEvents() {
        this.mViewHolder.buttonDate.setOnClickListener(this);
        this.mViewHolder.buttonSave.setOnClickListener(this);
    }


    /**
     * ViewHolder
     */
    private static class ViewHolder {
        EditText editDescription;
        Spinner spinnerPriority;
        CheckBox checkComplete;
        Button buttonDate;
        Button buttonSave;
    }
}
