package com.bruno.tasks.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bruno.tasks.R;
import com.bruno.tasks.service.constants.TaskConstants;
import com.bruno.tasks.service.listener.Feedback;
import com.bruno.tasks.service.model.PriorityModel;
import com.bruno.tasks.service.model.TaskModel;
import com.bruno.tasks.viewmodel.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final ViewHolder mViewHolder = new ViewHolder();
    private TaskViewModel mTaskViewModel;
    private final List<Integer> mListPriorityId = new ArrayList<>();
    private int mTaskId=0;

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
        this.mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        this.createEvents();

        // Cria observadores
        this.loadObservers();
        this.mTaskViewModel.getPriorityList();


        this.loadDataFromActivity();
    }

    // Botão de voltar nativo
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadDataFromActivity(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID);
            mViewHolder.buttonSave.setText(this.getString(R.string.update_task));
            this.mTaskViewModel.load(this.mTaskId);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_date){
            this.showDatePicker();
        } else if (id == R.id.button_save){
            this.handleSave();
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

    private void handleSave(){
        TaskModel task = new TaskModel();
        task.setId(this.mTaskId);
        task.setDescription(this.mViewHolder.editDescription.getText().toString());
        task.setComplete(this.mViewHolder.checkComplete.isChecked());
        task.setPriorityId(this.mListPriorityId.get(this.mViewHolder.spinnerPriority.getSelectedItemPosition()));
        task.setDueDate(this.mViewHolder.buttonDate.getText().toString());

        this.mTaskViewModel.save(task);
    }
    /**
     * Observadores
     */
    private void loadObservers() {
        this.mTaskViewModel.priorityList.observe(this, new Observer<List<PriorityModel>>() {
            @Override
            public void onChanged(List<PriorityModel> priorityModels) {
                loadSpinner(priorityModels);
            }
        });

        this.mTaskViewModel.taskLoad.observe(this, new Observer<TaskModel>() {
            @Override
            public void onChanged(TaskModel taskModel) {
                mViewHolder.editDescription.setText(taskModel.getDescription());
                mViewHolder.checkComplete.setChecked(taskModel.isComplete());
                mViewHolder.spinnerPriority.setSelection(getIndex(taskModel.getPriorityId()));


                //carregar a data
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(taskModel.getDueDate());
                    assert date != null;
                    mViewHolder.buttonDate.setText(mFormat.format(date));
                } catch (ParseException e) {
                    mViewHolder.buttonDate.setText("--");
                }
            }
        });

        this.mTaskViewModel.feedback.observe(this, new Observer<Feedback>() {
            @Override
            public void onChanged(Feedback feedback) {
                if (feedback.isSuccess()){
                    if (mTaskId == 0){
                        toast(getApplicationContext().getString(R.string.task_created));
                    } else {
                        toast(getApplicationContext().getString(R.string.task_updated));

                    }
                    finish();
                } else {
                    toast(feedback.getMessage());
                }
            }
        });
    }
    private int getIndex(int priorityId) {
        int index = 0;
        for (int i = 0; i < mListPriorityId.size(); i++) {
            if (mListPriorityId.get(i) == priorityId){
                index = i;
                break;
            }
        }
        return index;
    }

    private void loadSpinner(List<PriorityModel> list){
        List<String> priorityList = new ArrayList<>();
        for (PriorityModel priority : list){
            priorityList.add(priority.getDescription());
            this.mListPriorityId.add(priority.getId());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,priorityList);
        mViewHolder.spinnerPriority.setAdapter(spinnerAdapter);
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

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
