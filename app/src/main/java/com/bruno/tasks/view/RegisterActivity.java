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
import com.bruno.tasks.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private RegisterViewModel mRegisterViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botão de voltar nativo
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        this.mViewHolder.editName = findViewById(R.id.edit_name);
        this.mViewHolder.editEmail = findViewById(R.id.edit_email);
        this.mViewHolder.editPassword = findViewById(R.id.edit_password);
        this.mViewHolder.buttonCreate = findViewById(R.id.button_create);

        this.mViewHolder.buttonCreate.setOnClickListener(this);

        // Incializa variáveis
        this.mRegisterViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Cria observadores
        this.loadObservers();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_create){

            String name = this.mViewHolder.editName.getText().toString();
            String email = this.mViewHolder.editEmail.getText().toString();
            String password = this.mViewHolder.editPassword.getText().toString();

            this.mRegisterViewModel.create(name, email, password);
        }
    }

    private void loadObservers() {
        this.mRegisterViewModel.create.observe(this, feedback -> {
            if (feedback.isSuccess()) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), feedback.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * ViewHolder
     */
    private static class ViewHolder {
        EditText editName;
        EditText editEmail;
        EditText editPassword;
        Button buttonCreate;
    }
}
