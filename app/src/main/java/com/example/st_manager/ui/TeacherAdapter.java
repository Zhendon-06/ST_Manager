package com.example.st_manager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.st_manager.R;
import com.example.st_manager.data.Teacher;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {
    public interface OnTeacherActionListener {
        void onEdit(Teacher teacher);

        void onDelete(Teacher teacher);
    }

    private final List<Teacher> teachers = new ArrayList<>();
    private final OnTeacherActionListener listener;

    public TeacherAdapter(OnTeacherActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher, parent, false);
        return new TeacherViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        holder.bind(teachers.get(position));
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public void submitList(List<Teacher> list) {
        teachers.clear();
        if (list != null) {
            teachers.addAll(list);
        }
        notifyDataSetChanged();
    }

    static class TeacherViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView noView;
        private final TextView detailsView;
        private final TextView phoneView;
        private final MaterialButton editButton;
        private final MaterialButton deleteButton;
        private final OnTeacherActionListener listener;

        TeacherViewHolder(@NonNull View itemView, OnTeacherActionListener listener) {
            super(itemView);
            this.listener = listener;
            nameView = itemView.findViewById(R.id.textName);
            noView = itemView.findViewById(R.id.textNo);
            detailsView = itemView.findViewById(R.id.textDetails);
            phoneView = itemView.findViewById(R.id.textPhone);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }

        void bind(Teacher teacher) {
            nameView.setText(teacher.getName());
            noView.setText("工号 " + valueOrDash(teacher.getTeacherNo()));
            detailsView.setText(valueOrDash(teacher.getGender()) + " · " + valueOrDash(teacher.getDepartment()) + " · " + valueOrDash(teacher.getTitle()));
            phoneView.setText("电话 " + valueOrDash(teacher.getPhone()));
            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(teacher);
                }
            });
            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(teacher);
                }
            });
        }

        private String valueOrDash(String value) {
            return value == null || value.trim().isEmpty() ? "-" : value.trim();
        }
    }
}
