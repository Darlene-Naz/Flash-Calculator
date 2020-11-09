package com.appsintown.calculator;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MainActivity extends AppCompatActivity
{
    String expression = "";
    EditText input;
    TextView output;
    ScriptEngineManager manager;
    private int openParenthesis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = new ScriptEngineManager();

        input = findViewById(R.id.input);
        input.setInputType(InputType.TYPE_NULL);
        input.setTextIsSelectable(true);
        output = findViewById(R.id.output);
    }

    public void addOperand(View view) {
        Button b = (Button) view;
        String opr = b.getText().toString();
        input.getText().append(opr);
        expression += opr;
        showOutput();
    }

    public void clear(View view) {
        input.setText("");
        output.setText("");
        expression = "";
        openParenthesis=0;
    }

    public void parentheses(View view) {
        int operationLength = expression.length();
        String last_char = operationLength!=0?expression.charAt(operationLength-1)+"":"";
        if (operationLength == 0)
        {
            input.getText().append("(");
            expression+="(";
            openParenthesis++;
        } else if (openParenthesis > 0 && operationLength > 0)
        {
            if(last_char.matches("\\d|\\)")){
                input.getText().append(")");
                expression+=")";
                openParenthesis--;
            }else if(last_char.matches("\\+|-|\\*|/|\\(")){
                input.getText().append("(");
                expression+="(";
                openParenthesis++;
            }
        } else if (openParenthesis == 0 && operationLength > 0)
        {
            if (last_char.matches("\\d")) {
                input.getText().append("x(");
                expression+="*(";
            } else
            {
                input.getText().append("(");
                expression+="(";
            }
            openParenthesis++;
        }
        showOutput();
    }

    public void percent(View view) {
        int operationLength = expression.length();
        String last_char = operationLength!=0?expression.charAt(operationLength-1)+"":"";
        if(last_char.matches("\\d")){
            input.getText().append("%");
            expression+="%";
        }
        output.setText("");
    }

    public void addOperator(View view) {
        Button b = (Button) view;
        String opr = b.getText().toString();
        input.getText().append(opr);
        switch (opr){
            case "\u00F7": opr = "/";
                break;
            case "x": opr = "*";
                break;
        }
        expression += opr;
        output.setText("");
    }

    public void evaluate(View view) {
        expression = output.getText().toString();
        input.setText(expression);
        output.setText("");
   }

    public void showOutput() {
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            Log.d("msg", expression);
            Object result = engine.eval(expression);
            output.setText(result.toString());
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public void addPoint(View view) {
        int operationLength = expression.length();
        String last_char = operationLength!=0?expression.charAt(operationLength-1)+"":"";
        if(last_char.matches("\\d")){
            input.getText().append(".");
            expression+=".";
        }else{
            input.getText().append("0.");
            expression+="0.";
        }
        showOutput();
    }
}
