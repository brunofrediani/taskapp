package com.bruno.tasks.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bruno.tasks.R;
import com.bruno.tasks.viewmodel.LoginViewModel;

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

        // Incializa as variáveis
        this.mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        this.setListeners();

        // Cria observadores
        this.loadObservers();

        this.verifyUserLogged();
    }
    private void setListeners(){
        this.mViewHolder.buttonLogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_login){
            String email = this.mViewHolder.editEmail.getText().toString();
            String password = this.mViewHolder.editPassword.getText().toString();

            this.mLoginViewModel.login(email, password);
        }
    }

    private void verifyUserLogged(){
        this.mLoginViewModel.verifyUserLogged();
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
        this.mLoginViewModel.userLogged.observe(this, logged -> {
            if (logged){
                startMain();
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
    }
}
