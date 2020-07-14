package org.jzl.android.recyclerview;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.PositionType;
import org.jzl.android.recyclerview.data.DataBindingMatchPolicy;
import org.jzl.android.recyclerview.data.model.CyModel;
import org.jzl.lang.util.StringRandomUtils;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        AdapterConfigurator.of()
                .createItemView(R.layout.item_test, 2)
                .createItemView(R.layout.item_student_info, 1)

                .itemTypes(target -> (Student) target.getData(), (data, position) -> -1, 1)
                .dataBinding((context, holder, data) -> holder
                        .provide()
                        .setText(R.id.tv_name, data.name)
                        .setText(R.id.tv_sex, data.sex)
                        .setText(R.id.tv_age, data.age))
                .and()

                .itemTypes(target -> (String) target.getData(), (data, position) -> -1, 2)
                .dataBinding((context, holder, data) -> {
                    holder.provide().setText(R.id.tv_test, data);
                })
                .and()

                .dataBinding((context, holder, data) -> {
                    holder.itemView.setBackgroundResource(R.color.colorPrimaryDark);
                }, DataBindingMatchPolicy.MATCH_POLICY_HEADER)

                .dataBinding((context, holder, data) -> {
                    holder.itemView.setBackgroundResource(R.color.colorAccent);
                }, DataBindingMatchPolicy.MATCH_POLICY_FOOTER)

                .<String>dataBlock(PositionType.HEADER, 1, dataSource -> {
                    dataSource.addAll(2, randStr(), randStr());
                })
                .<Student>dataBlock(PositionType.HEADER, 2, dataSource -> {
                    dataSource.addAll(1, randStudent(), randStudent());
                })
                .<Student>dataBlock(PositionType.FOOTER, 3, dataSource -> {
                    dataSource.addAll(1, randStudent(), randStudent(), randStudent());
                })
                .dataBlock(PositionType.CONTENT, 3, dataSource -> {
                    Log.d("test", "dataSource:" + dataSource);
                    dataSource.addAll(1, randStudent(), randStudent(), randStudent());
                })
                .<String>dataBlock(PositionType.CONTENT, 2, dataSource -> {
                    Log.d("test", "dataSource:" + dataSource);
                    dataSource.addAll(2, randStr(), randStr(), randStr());
                })
                .attachToRecyclerView(findViewById(R.id.rv_test));
    }

    private CyModel randStudentCyModel() {
        return new CyModel(randStudent(), 1);
    }

    private CyModel randStudentString() {
        return new CyModel(StringRandomUtils.randomLowerString(5), 2);
    }

    private String randStr() {
        return StringRandomUtils.randomLowerString(5);
    }

    private Student randStudent() {
        Student student = new Student();
        student.name = StringRandomUtils.randomLowerString(10);
        student.sex = StringRandomUtils.randomString(new char[]{'男', '女'}, 1);
        student.age = StringRandomUtils.randomNumber(2);
        return student;
    }

    private static class Student {
        private static int ID = 1;
        private int id;
        private String name;
        private String sex;
        private String age;
    }
}