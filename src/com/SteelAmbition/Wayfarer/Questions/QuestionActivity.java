package com.SteelAmbition.Wayfarer.Questions;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.SteelAmbition.Wayfarer.R;
import com.SteelAmbition.Wayfarer.data.Question;
import com.actionbarsherlock.app.SherlockActivity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Joshua
 */
public class QuestionActivity extends SherlockActivity{

    public static Question question = null;
    LinearLayout radioGroupLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.question);
        radioGroupLayout = (LinearLayout)findViewById(R.id.radioGroup);

        if (question != null)
            createRadioButton(question);

        Button createSubjectButton = (Button) findViewById(R.id.btnContinue);

        final Activity activity = this;

        createSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

//                new CreateSubject(name.getText().toString(), activity).execute();
            }

        });
    }

    private void createRadioButton(Question ques) {
        List<String> answers = ques.getAnswers();
        final RadioButton[] radioButton = new RadioButton[answers.size()];
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        for(int i = 0; i < answers.size(); ++i) {
            radioButton[i] = new RadioButton(this);
            Button buttonView = new Button(this);
            buttonView.setText(answers.get(i));
            radioGroup.addView(radioButton[i]);
            radioGroup.addView(buttonView);
            radioButton[i].setText("Test" + i);
        }
        radioGroupLayout.addView(radioGroup);
    }
}
