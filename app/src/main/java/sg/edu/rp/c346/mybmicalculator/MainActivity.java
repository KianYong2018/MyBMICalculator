package sg.edu.rp.c346.mybmicalculator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText etWeight;
    EditText etHeight;
    Button btncalculate;
    Button btnReset;
    TextView tvDate;
    TextView tvBMI;
    TextView tvOutcome;

    String datetime;
    Float calculation;
    String outcome;

    Boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.editTextWeight);
        etHeight = findViewById(R.id.editTextHeight);
        btncalculate = findViewById(R.id.buttonCalculate);
        btnReset = findViewById(R.id.buttonReset);
        tvDate = findViewById(R.id.textViewDate);
        tvBMI = findViewById(R.id.textViewBMI);
        tvOutcome = findViewById(R.id.textViewOutcome);

        btncalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculation = Float.parseFloat(etWeight.getText().toString()) / Float.parseFloat(etHeight.getText().toString());
                Toast.makeText(MainActivity.this, calculation.toString(), Toast.LENGTH_SHORT).show();
                if(calculation < 18.5){
                    outcome = "You are underweight";
                }
                else if(calculation >=18.5 && calculation<=24.9){
                    outcome = "Your BMI is normal";
                }
                else if(calculation >=25 && calculation<=29.9){
                    outcome = "You are overweight";
                }
                else{
                    outcome = "You are obese";
                }
                tvOutcome.setText(outcome);
                etWeight.setText(null);
                etHeight.setText(null);

                tvDate.setText("Last Calculated Date: " + datetime);
                tvBMI.setText("Last Calculated BMI: " + calculation);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvBMI.setText("Last Calculated Date:");
                tvDate.setText("Last Calculated BMI:");
                tvOutcome.setText(null);
                clicked = true;
                /*SharedPreferences delete = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor del = delete.edit();
                del.clear();
                del.commit();*/
            }
        });

        Calendar now = Calendar.getInstance(); //Create a Calendar object with current date and time
        datetime = now.get(Calendar.DAY_OF_MONTH) + "/" + (now.get(Calendar.MONTH)+1) + "/" + now.get(Calendar.YEAR) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences retrieve = PreferenceManager.getDefaultSharedPreferences(this);
        String calculation = String.valueOf(retrieve.getFloat("BMI",0f));
        String date = String.valueOf(retrieve.getString("Date","No date found"));
        String out = retrieve.getString("Outcome","Failed");
        if(out.equalsIgnoreCase("Failed")){
            tvDate.setText("Last Calculated Date: ");
            tvBMI.setText("Last Calculated BMI: ");
            tvOutcome.setText(null);
        }
        else{
            tvDate.setText("Last Calculated Date: " + date);
            tvBMI.setText("Last Calculated BMI: " + calculation);
            tvOutcome.setText(out);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = preferences.edit();
        /*edit.putFloat("BMI",calculation);
        edit.putString("Date",datetime);
        edit.putString("Outcome",outcome);
        edit.commit();*/
        //Toast.makeText(getBaseContext(),calculation.toString(),Toast.LENGTH_SHORT).show();
        if(clicked){
            edit.clear();
            Toast.makeText(getBaseContext(),"Deleteing data stored",Toast.LENGTH_SHORT).show();
        }
        else{
            edit.putFloat("BMI",calculation);
            edit.putString("Date",datetime);
            edit.putString("Outcome",outcome);
        }
        edit.commit();
    }
}
