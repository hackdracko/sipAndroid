package com.intelit.sip;

import com.intelit.dao.Usuario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private ConnectionSource connectionSource;
    private Dao<Usuario, Integer> usuarioDao;

    public static final String SessionPreference = "SessionPreference" ;
    public static final String IdKey = "idKey";
    public static final String NombreKey = "nombreKey";
    public static final String UserKey = "userKey";
    public static final String EmailKey = "emailKey";
    SharedPreferences sharedpreferences;

    {
        if(connectionSource == null){
            Log.i("","Inicializando BD");
            String url = "jdbc:h2:/data/data/" +
                    "com.intelit.sip" +
                    "/data/BdSip" +
                    ";IFEXISTS=TRUE" +
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
        insertaAdmin();
        ingresar();
    }

    public void insertaAdmin(){
        Usuario user = new Usuario();
        user.setNombre("Administrador");
        user.setUsuario("admin");
        user.setPassword("admin");
        user.setEmail("admin@admin.com.mx");
        user.setEstatus(1);
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
                            .eq(Usuario.ESTATUS, 1);
                    PreparedQuery<Usuario> preparedQuery = queryBuilder.prepare();
                    List<Usuario> usuarioList = usuarioDao.query(preparedQuery);
                    System.out.println("----------------");
                    sharedpreferences = getSharedPreferences(SessionPreference, Context.MODE_PRIVATE);
                    if(usuarioList.size() > 0){
                        for (Usuario listaLogin: usuarioList) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putInt(IdKey, listaLogin.getId());
                            editor.putString(NombreKey, listaLogin.getNombre());
                            editor.putString(UserKey, listaLogin.getUsuario());
                            editor.putString(EmailKey, listaLogin.getEmail());
                            editor.commit();
                        }
                        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //startActivity(intent);
                    }else{
                        Context context = getApplicationContext();
                        CharSequence text = "Usuario y/o Password Incorrectos";
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
