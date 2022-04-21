package in.learncodewithrk.carparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Slot_booking extends AppCompatActivity {
    EditText Date,Name,Age;
    Calendar calendar;
    Button Appoint;
    TextView time;
    int t1Hour, t1Minute;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_booking);
        Date = findViewById(R.id.date1);
        Name=findViewById(R.id.name);
        Age=findViewById(R.id.age);
        Calendar calendar= Calendar.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Appoint=findViewById(R.id.appoint);
        time=findViewById(R.id.Time);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog= new TimePickerDialog(Slot_booking.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        t1Hour= hourOfDay;
                        t1Minute=minute;

                        calendar.set(0,0,0,t1Hour,t1Minute);

                        time.setText(DateFormat.format("hh:mm aa",calendar));


                    }
                },12,0,false
                );
                timePickerDialog.updateTime(t1Hour,t1Minute);
                timePickerDialog.show();
            }
        });

        Appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date =Date.getText().toString();
                String Timer = time.getText().toString();
                String AGE = Age.getText().toString();
                String NAME =Name.getText().toString();
                Map<String, Object > user = new HashMap<>();
                user.put("Name_of_Patient",NAME);
                user.put("Age_of_Patient",AGE);
                user.put("Appointment_Date",date);
                user.put("Appointment_Time",Timer);

                firestore.collection("Appointment")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(Slot_booking.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Slot_booking.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfmonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfmonth);
                updateCalender();
            }

            private void updateCalender() {
                String Format = "dd/MM/yy";
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat(Format, Locale.US);
                Date.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Slot_booking.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}
