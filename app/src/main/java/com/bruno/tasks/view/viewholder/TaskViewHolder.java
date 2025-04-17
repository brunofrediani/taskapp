package com.bruno.tasks.view.viewholder;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    private final PriorityRepository mPriorityRepository;
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final TaskListener mListener;

    private final ImageView mImageComplete  = itemView.findViewById(R.id.image_complete);
    private final TextView mTextDescription = itemView.findViewById(R.id.text_description);
    private final TextView mTextPriority = itemView.findViewById(R.id.text_priority);
    private final TextView mTextDueDate = itemView.findViewById(R.id.text_duedate);

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

        this.mTextDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onListClick(task.getId());
            }
        });

        this.mTextDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle(R.string.remocao_de_tarefa)
                        .setMessage(R.string.remover_tarefa)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mListener.onDeleteClick(task.getId());
                            }
                        })
                        .setNeutralButton(R.string.cancelar, null).show();
                return false;
            }
        });

        this.mImageComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task.isComplete()){
                    mListener.onUndoClick(task.getId());
                } else {
                    mListener.onCompleteClick(task.getId());
                }
            }
        });
    }
}
