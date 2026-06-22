package com.example.st_manager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.st_manager.R;
import com.example.st_manager.data.Student;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    public interface OnStudentActionListener {
        void onEdit(Student student);

        void onDelete(Student student);
    }

    private final List<Student> students = new ArrayList<>();
    private final OnStudentActionListener listener;

    public StudentAdapter(OnStudentActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.bind(students.get(position));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public void submitList(List<Student> list) {
        students.clear();
        if (list != null) {
            students.addAll(list);
        }
        notifyDataSetChanged();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView noView;
        private final TextView detailsView;
        private final TextView phoneView;
        private final MaterialButton editButton;
        private final MaterialButton deleteButton;
        private final OnStudentActionListener listener;

        StudentViewHolder(@NonNull View itemView, OnStudentActionListener listener) {
            super(itemView);
            this.listener = listener;
            nameView = itemView.findViewById(R.id.textName);
            noView = itemView.findViewById(R.id.textNo);
            detailsView = itemView.findViewById(R.id.textDetails);
            phoneView = itemView.findViewById(R.id.textPhone);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }

        void bind(Student student) {
            nameView.setText(student.getName());
            noView.setText("学号 " + valueOrDash(student.getStudentNo()));
            detailsView.setText(valueOrDash(student.getGender()) + " · " + valueOrDash(student.getCollege()) + " · " + valueOrDash(student.getClassName()));
            phoneView.setText("电话 " + valueOrDash(student.getPhone()));
            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(student);
                }
            });
            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(student);
                }
            });
        }

        private String valueOrDash(String value) {
            return value == null || value.trim().isEmpty() ? "-" : value.trim();
        }
    }
}
