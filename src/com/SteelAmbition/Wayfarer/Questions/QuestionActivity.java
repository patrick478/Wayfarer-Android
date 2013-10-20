package com.SteelAmbition.Wayfarer.Questions;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.SteelAmbition.Wayfarer.R;
import com.SteelAmbition.Wayfarer.data.Question;
import com.actionbarsherlock.app.SherlockActivity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Joshua
 */
public class QuestionActivity extends SherlockActivity{

    public static List<Question> questions = null;
    LinearLayout radioGroupLayout;
    RadioGroup radioGroup;

    public static void setQuestions(List<Question> q) {
        questions = q;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Question question = questions.get(getIntent().getIntExtra("questionNum", -1));
        setContentView(R.layout.question);
        radioGroupLayout = (LinearLayout)findViewById(R.id.radioGroup);

        TextView questionText = (TextView) findViewById(R.id.question);
        questionText.setText(question.getQuestion());

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
            //Button buttonView = new Button(this);
            //buttonView.setText(answers.get(i));
            radioGroup.addView(radioButton[i]);
            //radioGroup.addView(buttonView);
            radioButton[i].setText(answers.get(i));
        }
        radioGroupLayout.addView(radioGroup);
    }
}
