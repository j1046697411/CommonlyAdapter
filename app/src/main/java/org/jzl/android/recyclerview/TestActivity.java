package org.jzl.android.recyclerview;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.jzl.android.recyclerview.component.LayoutManagerComponent;
import org.jzl.android.recyclerview.config.AdapterConfigurator;
import org.jzl.android.recyclerview.core.PositionType;
import org.jzl.android.recyclerview.data.DataBindingMatchPolicy;
import org.jzl.android.recyclerview.data.DataSource;
import org.jzl.android.recyclerview.data.model.CAModel;
import org.jzl.android.recyclerview.data.model.IdAble;
import org.jzl.android.recyclerview.diff.CAModelDiffCallback;
import org.jzl.lang.util.RandomUtils;
import org.jzl.lang.util.StringRandomUtils;

public class TestActivity extends AppCompatActivity {

    private DataSource<Student> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        AdapterConfigurator.of()
                .createItemView(R.layout.item_test, 2)
                .createItemView(R.layout.item_student_info, 1, 3)
                .itemTypes(target -> (Student) target.getData(), (data, position) -> -1, 1, 3)
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
                    holder.itemView.setBackgroundColor(0xff00ff00);
                }, DataBindingMatchPolicy.ofItemTypes(3))

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
                .<Student>dataBlock(PositionType.CONTENT, 3, dataSource -> {
                    Log.d("test", "dataSource:" + dataSource);
                    dataSource.addAll(1, randStudent(), randStudent(), randStudent());
                    this.dataSource = dataSource;
                })
                .<String>dataBlock(PositionType.CONTENT, 2, dataSource -> {
                    Log.d("test", "dataSource:" + dataSource);
                    dataSource.addAll(2, randStr(), randStr(), randStr());
                })
                .layoutManager(LayoutManagerComponent::linearLayoutManager)
                .diff(target -> {
                    target.setDetectMoves(true).setDiffCallback(new CAModelDiffCallback());
                })
                .attachToRecyclerView(findViewById(R.id.rv_test));

        findViewById(R.id.btn_add).setOnClickListener(view -> {
            dataSource.addAll(3, randStudent());
        });
        findViewById(R.id.btn_random_add).setOnClickListener(view -> {
            if (!dataSource.isEmpty()){
                dataSource.remove(RandomUtils.random(dataSource.size()));
            }
        });

    }

    private CAModel randStudentCyModel() {
        return new CAModel(randStudent(), 1, RandomUtils.random(Integer.MAX_VALUE));
    }

    private CAModel randStudentString() {
        return new CAModel(StringRandomUtils.randomLowerString(5), 2, RandomUtils.random(Integer.MAX_VALUE));
    }

    private String randStr() {
        return StringRandomUtils.randomLowerString(RandomUtils.random(10, 100));
    }

    private Student randStudent() {
        Student student = new Student();
        student.name = StringRandomUtils.randomLowerString(10);
        student.sex = StringRandomUtils.randomString(new char[]{'男', '女'}, 1);
        student.age = StringRandomUtils.randomNumber(2);
        student.id = Student.ID++;
        return student;
    }

    private static class Student implements IdAble {
        private static int ID = 1;
        private int id;
        private String name;
        private String sex;
        private String age;

        @Override
        public long getItemId() {
            return id;
        }
    }
}