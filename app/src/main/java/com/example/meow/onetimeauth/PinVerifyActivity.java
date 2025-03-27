package com.example.meow.onetimeauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meow.MainActivity;
import com.example.meow.R;

public class PinVerifyActivity extends AppCompatActivity {
    private EditText pinInput;
    private Button verifyPinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_verify);

        pinInput = findViewById(R.id.pinInput);
        verifyPinButton = findViewById(R.id.verifyPinButton);

        verifyPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPin = pinInput.getText().toString();
                PrefManager prefManager = new PrefManager(PinVerifyActivity.this);

                if (enteredPin.equals("2803")) {
                    prefManager.setUserType("A"); // Store A
                    System.out.println(prefManager.getUserType()+"user type aft pin=================");
                    prefManager.setFirstTime(false);
                    navigateToMain();
                } else if (enteredPin.equals("0306")) {
                    prefManager.setUserType("B"); // Store B
                    prefManager.setFirstTime(false);
                    navigateToMain();
                } else {
                    Toast.makeText(PinVerifyActivity.this, "Incorrect PIN, try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void navigateToMain() {
        startActivity(new Intent(PinVerifyActivity.this, MainActivity.class));
        finish();
    }
}
