package com.akarmo.pega;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class configView extends Activity {


    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private ComponentName deviceAdmin;
    private DevicePolicyManager devicePolicyManager;
    private static final int ACTIVATION_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);
         prefs = this.getSharedPreferences(
                "com.akarmo.pega", Context.MODE_PRIVATE);
        editor = prefs.edit();
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        deviceAdmin = new ComponentName(configView.this, DetectPasswordFailed.class);


        final EditText edtEmail = (EditText) findViewById(R.id.txtEmail);
        final CheckBox chkErroSenha = (CheckBox) findViewById(R.id.chkErrarSenha);
        final CheckBox chkSempreTiraFoto = (CheckBox) findViewById(R.id.chkTodoDesbl);
        final CheckBox chkEnviaEmail = (CheckBox) findViewById(R.id.chkEnvia);


        edtEmail.setText(prefs.getString("email","").toString());

        if(prefs.getBoolean("SendEmail", false)){
            chkEnviaEmail.setChecked(true);
        } else{
            chkEnviaEmail.setChecked(false);
        }

        if(prefs.getBoolean("TakeAfterWrongPass",false)){
            chkErroSenha.setChecked(true);
        }else{
            chkErroSenha.setChecked(false);
        }
        if(prefs.getBoolean("AlwaysTakePicture",false)){
            chkSempreTiraFoto.setChecked(true);
        }else{
            chkSempreTiraFoto.setChecked(false);
        }

        Button btnsalvar = (Button) findViewById(R.id.btnSalvar);

        btnsalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String str = edtEmail.getText().toString().trim();
                editor.putString("email", edtEmail.getText().toString());

                if (chkErroSenha.isChecked()) {
                    editor.putBoolean("TakeAfterWrongPass", true);
                } else {
                    editor.putBoolean("TakeAfterWrongPass", false);
                }
                if (chkSempreTiraFoto.isChecked()) {
                    editor.putBoolean("AlwaysTakePicture", true);
                } else {
                    editor.putBoolean("AlwaysTakePicture", false);
                }
                if (chkEnviaEmail.isChecked()) {
                    if(str != null && !str.isEmpty()) {
                        editor.putBoolean("SendEmail", true);
                        editor.commit();
                        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
                        startActivityForResult(intent, ACTIVATION_REQUEST);

                        Toast.makeText(configView.this, "Configurações salvas com sucesso!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(configView.this, "Endereço vazio ou inválido",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    editor.putBoolean("SendEmail", false);
                }



            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuracao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
