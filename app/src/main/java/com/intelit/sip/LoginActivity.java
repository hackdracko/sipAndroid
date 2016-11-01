package com.intelit.sip;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelit.dao.Usuario;

import android.content.Context;
import android.content.DialogInterface;
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
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private ConnectionSource connectionSource;
    private Dao<Usuario, Integer> usuarioDao;

    public static final String SessionPreference = "SessionPreference" ;
    public static final String IdKey = "idKey";
    public static final String NombreKey = "nombreKey";
    public static final String UserKey = "userKey";
    public static final String EmailKey = "emailKey";
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
                //Toast.makeText(LoginActivity.this, "asdas", Toast.LENGTH_SHORT).show();
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
        String url ="http://192.168.15.44:8000/sincronizacion/usuario";
        Log.i("RESPUESTA-->", "URL " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RESPUESTA-->", "EN LA RESPUESTA DEL SERVICIO " + response);
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                        final List<String> nombres = new ArrayList<String>();
                        List<String> array = new ArrayList<String>();
                        try {
                            StructureJson respuesta = objectMapper.readValue(response.toString(), StructureJson.class);
                            Log.i("RESPUESTA-->", "RESPUESTA DEL JSON " + respuesta.getStatus());
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
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void insertaAdmin(){
        Usuario user = new Usuario();
        user.setId_asesor(440);
        user.setUsuario("admin");
        user.setPassword("admin");
        user.setAdministrador(1);
        user.setActivo(1);
        user.setFecha_expiracion("lo ke sea");
        try {
            usuarioDao.create(user);
            System.out.println("Insertando Admin");
        } catch (SQLException e) {
            System.out.println("Error insertando Admin "+e);
        }
    }
    public void ingresar(){
        Button accionIngresar = (Button)findViewById(R.id.btnIngresar);
        accionIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView usuario = (TextView)findViewById(R.id.txtUsuario);
                TextView password = (TextView)findViewById(R.id.txtPassword);
                Log.i("User-->", usuario.getText().toString());
                Log.i("Pass-->", password.getText().toString());
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
                            editor.putInt(NombreKey, listaLogin.getId_asesor());
                            editor.putString(UserKey, listaLogin.getUsuario());
                            editor.putInt(EmailKey, listaLogin.getAdministrador());
                            editor.commit();
                        }
                        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //startActivity(intent);
                    }else{
                        Context context = getApplicationContext();
                        CharSequence text = "Usuario y/o Password Incorrectos. Si tu usuario es correcto es necesario realizar una sincronización";
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, text, duration).show();
                    }
                } catch (SQLException e) {
                    System.out.println("Error Select "+e);
                }
            }
        });
    }
}
