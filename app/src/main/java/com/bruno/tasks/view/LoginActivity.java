package com.bruno.tasks.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bruno.tasks.R;
import com.bruno.tasks.viewmodel.LoginViewModel;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final ViewHolder mViewHolder = new ViewHolder();
    private LoginViewModel mLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.mViewHolder.editEmail = findViewById(R.id.edit_email);
        this.mViewHolder.editPassword = findViewById(R.id.edit_password);
        this.mViewHolder.buttonLogin = findViewById(R.id.button_login);
        this.mViewHolder.txtRegister = findViewById(R.id.txt_register);



        // Incializa as variáveis
        this.mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        this.setListeners();

        // Cria observadores
        this.loadObservers();


        this.mLoginViewModel.isFingerprintAvailable();
    }

    private void openAuthentication() {
        //Executor
        Executor executor = ContextCompat.getMainExecutor(this);

        //Prompt
        BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startMain();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        //Biometricinfo
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Tasks")
                .setSubtitle("Desbloqueie seu celular para usar o aplicativo")
                .setDescription("Necessária autenticação biométrica para utilizar o aplicativo")
                .setNegativeButtonText("Cancelar")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void setListeners(){
        this.mViewHolder.buttonLogin.setOnClickListener(this);
        this.mViewHolder.txtRegister.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_login){
            String email = this.mViewHolder.editEmail.getText().toString();
            String password = this.mViewHolder.editPassword.getText().toString();

            this.mLoginViewModel.login(email, password);
        } else if (id == R.id.txt_register) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }
    }



    private void loadObservers() {
        this.mLoginViewModel.login.observe(this, feedback -> {
            if (feedback.isSuccess()){
                Toast.makeText(getApplicationContext(),"SUCESSO", Toast.LENGTH_SHORT).show();
                startMain();
            } else {
                Toast.makeText(getApplicationContext(),feedback.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        this.mLoginViewModel.fingerprint.observe(this, fingerprintAvailable -> {
            if (fingerprintAvailable){
                openAuthentication();
            }
        });
    }

    private void startMain(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
    /**
     * ViewHolder
     */
    private static class ViewHolder {
        EditText editEmail;
        EditText editPassword;
        Button buttonLogin;
        TextView txtRegister;
    }
}
