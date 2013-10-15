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
    RadioGroup radioGroup;

    public static void setQuestion(Question q) {
        question = q;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.question);
        radioGroupLayout = (LinearLayout)findViewById(R.id.radioGroup);

        if (question != null)
            createRadioButton(question);

        Button continueButton = (Button) findViewById(R.id.btnContinue);

        final Activity activity = this;

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(radioGroup.getCheckedRadioButtonId()!=-1) {
                    int id= radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(id);
                    int radioId = radioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                    String selection = (String) btn.getText();
                    System.out.println(selection);
                }
                activity.finish();
//                new CreateSubject(name.getText().toString(), activity).execute();
            }

        });
    }

    private void createRadioButton(Question ques) {
        List<String> answers = ques.getAnswers();
        final RadioButton[] radioButton = new RadioButton[answers.size()];
        radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        for(int i = 0; i < answers.size(); ++i) {
            radioButton[i] = new RadioButton(this);
            Button buttonView = new Button(this);
            buttonView.setText(answers.get(i));
            radioGroup.addView(radioButton[i]);
            radioGroup.addView(buttonView);
            radioButton[i].setText(i);
        }
        radioGroupLayout.addView(radioGroup);
    }
}
