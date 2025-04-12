package com.bruno.tasks.view.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bruno.tasks.R;
import com.bruno.tasks.service.listener.TaskListener;
import com.bruno.tasks.service.model.TaskModel;
import com.bruno.tasks.service.repository.PriorityRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    private PriorityRepository mPriorityRepository;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private TaskListener mListener;

    private ImageView mImageComplete  = itemView.findViewById(R.id.image_complete);
    private TextView mTextDescription = itemView.findViewById(R.id.text_description);
    private TextView mTextPriority = itemView.findViewById(R.id.text_priority);
    private TextView mTextDueDate = itemView.findViewById(R.id.text_duedate);

    public TaskViewHolder(@NonNull View itemView, TaskListener listener) {
        super(itemView);
        this.mListener = listener;
        this.mPriorityRepository = new PriorityRepository(itemView.getContext());
    }

    /**
     * Atribui valores aos elementos de interface e também eventos
     */

    public void bindData(TaskModel task) {
        this.mTextDescription.setText(task.getDescription());

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).parse(task.getDueDate());
            assert date != null;
            this.mTextDueDate.setText(this.mDateFormat.format(date));
        } catch (ParseException e) {
            this.mTextDueDate.setText("--");
        }

        String priority = this.mPriorityRepository.getDescription(task.getPriorityId());
        this.mTextPriority.setText(priority);

        if (task.isComplete()){
            this.mImageComplete.setImageResource(R.drawable.ic_done);
        } else {
            this.mImageComplete.setImageResource(R.drawable.ic_todo);
        }
        /*
        new AlertDialog.Builder(itemView.getContext())
                .setTitle(R.string.remocao_de_tarefa)
                .setMessage(R.string.remover_tarefa)
                .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // mListener.onDeleteClick(task.id);
                    }
                })
                .setNeutralButton(R.string.cancelar, null).show();*/


    }
}
