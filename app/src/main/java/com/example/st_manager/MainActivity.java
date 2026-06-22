package com.example.st_manager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.st_manager.data.Student;
import com.example.st_manager.data.Teacher;
import com.example.st_manager.ui.StudentAdapter;
import com.example.st_manager.ui.TeacherAdapter;
import com.example.st_manager.viewmodel.StudentViewModel;
import com.example.st_manager.viewmodel.TeacherViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private static final int MODE_STUDENT = 0;
    private static final int MODE_TEACHER = 1;

    private StudentViewModel studentViewModel;
    private TeacherViewModel teacherViewModel;
    private StudentAdapter studentAdapter;
    private TeacherAdapter teacherAdapter;
    private RecyclerView recyclerStudents;
    private RecyclerView recyclerTeachers;
    private TextInputLayout searchLayout;
    private EditText editSearch;
    private TextView textModuleTitle;
    private TextView textModuleSubtitle;
    private TextView textStudentCount;
    private TextView textTeacherCount;
    private TextView textCurrentCount;
    private TextView textEmptyTitle;
    private TextView textEmptyBody;
    private View emptyState;
    private ExtendedFloatingActionButton fabAdd;
    private int currentMode = MODE_STUDENT;
    private int studentCount = 0;
    private int teacherCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindViews();
        setupLists();
        setupViewModels();
        setupActions();
        updateMode(MODE_STUDENT);
    }

    private void bindViews() {
        MaterialButtonToggleGroup segmentGroup = findViewById(R.id.segmentGroup);
        segmentGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) {
                return;
            }
            if (checkedId == R.id.buttonStudents) {
                updateMode(MODE_STUDENT);
            } else if (checkedId == R.id.buttonTeachers) {
                updateMode(MODE_TEACHER);
            }
        });

        recyclerStudents = findViewById(R.id.recyclerStudents);
        recyclerTeachers = findViewById(R.id.recyclerTeachers);
        searchLayout = findViewById(R.id.searchLayout);
        editSearch = findViewById(R.id.editSearch);
        textModuleTitle = findViewById(R.id.textModuleTitle);
        textModuleSubtitle = findViewById(R.id.textModuleSubtitle);
        textStudentCount = findViewById(R.id.textStudentCount);
        textTeacherCount = findViewById(R.id.textTeacherCount);
        textCurrentCount = findViewById(R.id.textCurrentCount);
        textEmptyTitle = findViewById(R.id.textEmptyTitle);
        textEmptyBody = findViewById(R.id.textEmptyBody);
        emptyState = findViewById(R.id.emptyState);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setupLists() {
        studentAdapter = new StudentAdapter(new StudentAdapter.OnStudentActionListener() {
            @Override
            public void onEdit(Student student) {
                showStudentDialog(student);
            }

            @Override
            public void onDelete(Student student) {
                confirmDeleteStudent(student);
            }
        });
        teacherAdapter = new TeacherAdapter(new TeacherAdapter.OnTeacherActionListener() {
            @Override
            public void onEdit(Teacher teacher) {
                showTeacherDialog(teacher);
            }

            @Override
            public void onDelete(Teacher teacher) {
                confirmDeleteTeacher(teacher);
            }
        });

        configureRecyclerView(recyclerStudents, studentAdapter);
        configureRecyclerView(recyclerTeachers, teacherAdapter);
    }

    private void configureRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_list, getTheme()));
        recyclerView.addItemDecoration(decoration);
    }

    private void setupViewModels() {
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        teacherViewModel = new ViewModelProvider(this).get(TeacherViewModel.class);

        studentViewModel.getStudents().observe(this, students -> {
            studentCount = students == null ? 0 : students.size();
            studentAdapter.submitList(students);
            textStudentCount.setText(String.valueOf(studentCount));
            updateEmptyState();
        });

        teacherViewModel.getTeachers().observe(this, teachers -> {
            teacherCount = teachers == null ? 0 : teachers.size();
            teacherAdapter.submitList(teachers);
            textTeacherCount.setText(String.valueOf(teacherCount));
            updateEmptyState();
        });
    }

    private void setupActions() {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (currentMode == MODE_STUDENT) {
                    studentViewModel.setKeyword(s.toString());
                } else {
                    teacherViewModel.setKeyword(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        fabAdd.setOnClickListener(v -> {
            if (currentMode == MODE_STUDENT) {
                showStudentDialog(null);
            } else {
                showTeacherDialog(null);
            }
        });
    }

    private void updateMode(int mode) {
        currentMode = mode;
        editSearch.setText("");

        boolean studentMode = mode == MODE_STUDENT;
        recyclerStudents.setVisibility(studentMode ? View.VISIBLE : View.GONE);
        recyclerTeachers.setVisibility(studentMode ? View.GONE : View.VISIBLE);

        if (studentMode) {
            textModuleTitle.setText("学生管理");
            textModuleSubtitle.setText("添加、查看、修改、删除和条件查询学生资料");
            searchLayout.setHint("按姓名、学号、学院、班级或电话查询");
            fabAdd.setText("添加学生");
        } else {
            textModuleTitle.setText("教师管理");
            textModuleSubtitle.setText("添加、查看、修改、删除和条件查询教师资料");
            searchLayout.setHint("按姓名、工号、院系、职称或电话查询");
            fabAdd.setText("添加教师");
        }
        updateEmptyState();
    }

    private void updateEmptyState() {
        boolean studentMode = currentMode == MODE_STUDENT;
        int count = studentMode ? studentCount : teacherCount;
        textCurrentCount.setText(count + " 条");
        emptyState.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
        if (studentMode) {
            textEmptyTitle.setText("暂无学生资料");
            textEmptyBody.setText("点击右下角添加第一条学生记录");
        } else {
            textEmptyTitle.setText("暂无教师资料");
            textEmptyBody.setText("点击右下角添加第一条教师记录");
        }
    }

    private void showStudentDialog(Student editingStudent) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_student, null);
        TextInputLayout layoutNo = view.findViewById(R.id.layoutNo);
        TextInputLayout layoutName = view.findViewById(R.id.layoutName);
        EditText editNo = view.findViewById(R.id.editNo);
        EditText editName = view.findViewById(R.id.editName);
        EditText editGender = view.findViewById(R.id.editGender);
        EditText editCollege = view.findViewById(R.id.editCollege);
        EditText editClassName = view.findViewById(R.id.editClassName);
        EditText editPhone = view.findViewById(R.id.editPhone);

        if (editingStudent != null) {
            editNo.setText(editingStudent.getStudentNo());
            editName.setText(editingStudent.getName());
            editGender.setText(editingStudent.getGender());
            editCollege.setText(editingStudent.getCollege());
            editClassName.setText(editingStudent.getClassName());
            editPhone.setText(editingStudent.getPhone());
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(editingStudent == null ? "添加学生" : "修改学生")
                .setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton(editingStudent == null ? "添加" : "保存", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            clearErrors(layoutNo, layoutName);
            String no = valueOf(editNo);
            String name = valueOf(editName);
            if (!validateRequired(no, layoutNo, "请输入学号") || !validateRequired(name, layoutName, "请输入姓名")) {
                return;
            }

            if (editingStudent == null) {
                Student student = new Student(
                        no,
                        name,
                        valueOf(editGender),
                        valueOf(editCollege),
                        valueOf(editClassName),
                        valueOf(editPhone),
                        System.currentTimeMillis()
                );
                studentViewModel.insert(student);
                Toast.makeText(this, "学生已添加", Toast.LENGTH_SHORT).show();
            } else {
                editingStudent.setStudentNo(no);
                editingStudent.setName(name);
                editingStudent.setGender(valueOf(editGender));
                editingStudent.setCollege(valueOf(editCollege));
                editingStudent.setClassName(valueOf(editClassName));
                editingStudent.setPhone(valueOf(editPhone));
                studentViewModel.update(editingStudent);
                Toast.makeText(this, "学生信息已保存", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }));
        dialog.show();
    }

    private void showTeacherDialog(Teacher editingTeacher) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_teacher, null);
        TextInputLayout layoutNo = view.findViewById(R.id.layoutNo);
        TextInputLayout layoutName = view.findViewById(R.id.layoutName);
        EditText editNo = view.findViewById(R.id.editNo);
        EditText editName = view.findViewById(R.id.editName);
        EditText editGender = view.findViewById(R.id.editGender);
        EditText editDepartment = view.findViewById(R.id.editDepartment);
        EditText editTitle = view.findViewById(R.id.editTitle);
        EditText editPhone = view.findViewById(R.id.editPhone);

        if (editingTeacher != null) {
            editNo.setText(editingTeacher.getTeacherNo());
            editName.setText(editingTeacher.getName());
            editGender.setText(editingTeacher.getGender());
            editDepartment.setText(editingTeacher.getDepartment());
            editTitle.setText(editingTeacher.getTitle());
            editPhone.setText(editingTeacher.getPhone());
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(editingTeacher == null ? "添加教师" : "修改教师")
                .setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton(editingTeacher == null ? "添加" : "保存", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            clearErrors(layoutNo, layoutName);
            String no = valueOf(editNo);
            String name = valueOf(editName);
            if (!validateRequired(no, layoutNo, "请输入工号") || !validateRequired(name, layoutName, "请输入姓名")) {
                return;
            }

            if (editingTeacher == null) {
                Teacher teacher = new Teacher(
                        no,
                        name,
                        valueOf(editGender),
                        valueOf(editDepartment),
                        valueOf(editTitle),
                        valueOf(editPhone),
                        System.currentTimeMillis()
                );
                teacherViewModel.insert(teacher);
                Toast.makeText(this, "教师已添加", Toast.LENGTH_SHORT).show();
            } else {
                editingTeacher.setTeacherNo(no);
                editingTeacher.setName(name);
                editingTeacher.setGender(valueOf(editGender));
                editingTeacher.setDepartment(valueOf(editDepartment));
                editingTeacher.setTitle(valueOf(editTitle));
                editingTeacher.setPhone(valueOf(editPhone));
                teacherViewModel.update(editingTeacher);
                Toast.makeText(this, "教师信息已保存", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }));
        dialog.show();
    }

    private void confirmDeleteStudent(Student student) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("删除学生")
                .setMessage("确定删除“" + student.getName() + "”的资料吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("删除", (dialog, which) -> {
                    studentViewModel.delete(student);
                    Toast.makeText(this, "学生已删除", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void confirmDeleteTeacher(Teacher teacher) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("删除教师")
                .setMessage("确定删除“" + teacher.getName() + "”的资料吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("删除", (dialog, which) -> {
                    teacherViewModel.delete(teacher);
                    Toast.makeText(this, "教师已删除", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void clearErrors(TextInputLayout... layouts) {
        for (TextInputLayout layout : layouts) {
            layout.setError(null);
        }
    }

    private boolean validateRequired(String value, TextInputLayout layout, String error) {
        if (TextUtils.isEmpty(value)) {
            layout.setError(error);
            return false;
        }
        return true;
    }

    private String valueOf(EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}
