package com.intelit.sip;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelit.dao.Usuario;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.intelit.json.syncUsers.StructureJson;
import com.intelit.json.syncUsers.SyncUser;
import com.intelit.volley.MySingleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private ConnectionSource connectionSource;
    private Dao<Usuario, Integer> usuarioDao;

    public static final String SessionPreference = "SessionPreference" ;
    public static final String IdKey = "idKey";
    public static final String IdAsesorKey = "idAsesorKey";
    public static final String UserKey = "userKey";
    public static final String FechaExpKey = "fechaExpKey";
    public String DATABASE_NAME = "sip";
    SharedPreferences sharedpreferences;

    {
        String storage = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + DATABASE_NAME;
        Log.i("STORAGE -->" , storage);
        if(connectionSource == null){
            Log.i("","Inicializando BD");
            String url = "jdbc:h2:" +
                    storage +
                    //";IFEXISTS=TRUE" +
                    ";FILE_LOCK=FS" +
                    ";PAGE_SIZE=1024" +
                    ";CACHE_SIZE=8192";
            try {
                connectionSource = new JdbcConnectionSource(url);
                usuarioDao = DaoManager.createDao(connectionSource, Usuario.class);
            } catch (SQLException e) {
                throw new RuntimeException("Problems initializing database objects", e);
            }
            try {
                TableUtils.dropTable(connectionSource, Usuario.class, true);
                TableUtils.createTableIfNotExists(connectionSource, Usuario.class);
                Log.i("","Creando Tabla");
            } catch (SQLException e) {
                Log.i("","Error creando tabla "+e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //insertaAdmin();
        ingresar();
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingUpdateButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog();
            }
        });
    }

    public void updateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder
                .setMessage("¿Estas seguro de Actualizar?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LoginActivity.this, "Comenzando Actualización. Asegurate de tener una buena conexión de datos o internet", Toast.LENGTH_SHORT).show();
                        syncUser();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void syncUser() {
        String url = AppConfig.URL_SYNC_USER;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RESPUESTA-->", "EN LA RESPUESTA DEL SERVICIO " + response);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                try {
                    StructureJson respuesta = objectMapper.readValue(response.toString(), StructureJson.class);
                    if(respuesta.getStatus().equals("ok")) {
                        for (SyncUser data : respuesta.getResults()) {
                            insertUser(Integer.parseInt(data.getIdAsesor()), data.getUsuario(), data.getPassword(), data.getAdministrador(), data.getActivo());
                        }
                        Toast.makeText(LoginActivity.this, "Se sincronizaron correctamente los usuarios intenta ingresar nuevamente.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Error al sincronizar al Usuario.", Toast.LENGTH_SHORT).show();
                Log.i("RESPUESTA-->", "ERROR EN LA PETICION " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                TextView usuario = (TextView)findViewById(R.id.txtUsuario);
                TextView password = (TextView)findViewById(R.id.txtPassword);
                params.put("user",usuario.getText().toString().trim());
                params.put("password",password.getText().toString().trim());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void insertUser(int id_asesor, String usuario, String password, int administrador, int activo){
        Usuario user = new Usuario();
        user.setId_asesor(id_asesor);
        user.setUsuario(usuario);
        user.setPassword(password);
        user.setAdministrador(administrador);
        user.setActivo(activo);
        user.setFecha_expiracion("test");
        try {
            usuarioDao.create(user);
            System.out.println("Insertando Usuario");
        } catch (SQLException e) {
            System.out.println("Error insertando Usuario "+e);
        }
    }
    public void ingresar(){
        Button accionIngresar = (Button)findViewById(R.id.btnIngresar);
        accionIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView usuario = (TextView)findViewById(R.id.txtUsuario);
                TextView password = (TextView)findViewById(R.id.txtPassword);
                QueryBuilder<Usuario, Integer> queryBuilder = usuarioDao.queryBuilder();
                try {
                    queryBuilder.where()
                            .eq(Usuario.USUARIO, usuario.getText().toString())
                            .and()
                            .eq(Usuario.PASSWORD, password.getText().toString())
                            .and()
                            .eq(Usuario.ACTIVO, 1);
                    PreparedQuery<Usuario> preparedQuery = queryBuilder.prepare();
                    List<Usuario> usuarioList = usuarioDao.query(preparedQuery);
                    System.out.println("----------------");
                    sharedpreferences = getSharedPreferences(SessionPreference, Context.MODE_PRIVATE);
                    if(usuarioList.size() > 0){
                        for (Usuario listaLogin: usuarioList) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putInt(IdKey, listaLogin.getId());
                            editor.putInt(IdAsesorKey, listaLogin.getId_asesor());
                            editor.putString(UserKey, listaLogin.getUsuario());
                            editor.putString(FechaExpKey, listaLogin.getFecha_expiracion());
                            editor.commit();
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Context context = getApplicationContext();
                        CharSequence text = "Usuario y/o Password Incorrectos. Si tu usuario es correcto es necesario realizar una sincronización";
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, text, duration).show();
                        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingUpdateButton);
                        floatingActionButton.show();
                    }
                } catch (SQLException e) {
                    System.out.println("Error Select "+e);
                }
            }
        });
    }
}
