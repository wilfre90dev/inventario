package cu.iviera.aftcheck;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    private TextView tvCodigoLeido, tvProvincia, tvNombreSitio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        verificarYPedirPermisosDeCamara();


        Button btnEscanear = findViewById(R.id.btnEscanear);
        tvCodigoLeido = findViewById(R.id.tvInventario);
        tvProvincia = findViewById(R.id.tvInmovilizado);
        tvNombreSitio = findViewById(R.id.tvNombreSitio);

        TabHost th=(TabHost) findViewById(R.id.tabHost);
        th.setup();

        TabHost.TabSpec ts1=th.newTabSpec("tab1");
        ts1.setContent(R.id.tabEscanear);
        ts1.setIndicator("ESCANEAR");
        th.addTab(ts1);

        th.setup();
        TabHost.TabSpec ts2=th.newTabSpec("tab2");
        ts2.setContent(R.id.tabAfts);
        ts2.setIndicator("AFTs");
        th.addTab(ts2);

        th.setup();
        TabHost.TabSpec ts3=th.newTabSpec("tab3");
        ts3.setContent(R.id.tabOpciones);
        ts3.setIndicator("OPCIONES");
        th.addTab(ts3);
    }

    public String[] CargarBD(String codigo){
        String provincia="No existe ninguna provincia";
        String municipio="No existe ningún sitio";
        String [] resultados=new String[2];

        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "sitios.sqlite");
        try {
            dbHelper.importIfNotExist();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql="SELECT DISTINCT sitios.Provincia, sitios.Municipio FROM sitios WHERE sitios.BoxNo = \'"+codigo+"\'";

        Cursor cursor=dbHelper.getWritableDatabase().rawQuery(sql,null);
        //TextView tv= findViewById(R.id.tvInventario);

        while (cursor.moveToNext()) {
            provincia = cursor.getString(cursor.getColumnIndex("Provincia"));
            municipio = cursor.getString(cursor.getColumnIndex("Municipio"));

            //System.out.println(provincia);
        }
        resultados[0] =provincia;
        resultados[1] =municipio;

        return resultados;

    }

/*    public String[] CargarBD(String codigo){
        String provincia="No existe ninguna provincia";
        String municipio="No existe ningún municipio";
        String [] resultados=new String[2];

        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "sitios.sqlite");
        try {
            dbHelper.importIfNotExist();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // String sql="SELECT DISTINCT sitios.Provincia, sitios.Municipio FROM sitios WHERE sitios.BoxNo = \'"+codigo+"\'";
        String sql="SELECT DISTINCT sitios.Provincia, sitios.Municipio FROM sitios WHERE sitios.BoxNo=?";

        String [] select= {"Provincia", "Municipio"};
        String [] where={codigo};

        Cursor cursor=dbHelper.getWritableDatabase().query("sitios",select,"BoxNo=?",where, null,null,null, "1");

        //Cursor cursor=dbHelper.getWritableDatabase().rawQuery(sql, codigo,null);
        //TextView tv= findViewById(R.id.tvInventario);

        while (cursor.moveToNext()) {
            provincia = cursor.getString(cursor.getColumnIndex("Provincia"));
            municipio = cursor.getString(cursor.getColumnIndex("Municipio"));

            //System.out.println(provincia);
        }
        resultados[0] =provincia;
        resultados[1] =municipio;
        tvInventario.setText(codigo);
        tvInmovilizado.setText(provincia);
        tvNombreSitio.setText(municipio);
        return resultados;

    }*/

    // Abre la activity de tabEscanear
    private void escanear() {
        Intent i = new Intent(MainActivity.this, ActivityEscanear.class);
        startActivityForResult(i, CODIGO_INTENT);
    }

    // En dependencia del texto que tenga el boton v a tabEscanear o a buscar el resultado directo en la BD
    public void AccionPrincipal(View view) {
        EditText et=findViewById(R.id.editText);
        Button btnEscanear=findViewById(R.id.btnEscanear);

        if (btnEscanear.getText().equals("Escanear")) {
            if (!permisoCamaraConcedido) {
                Toast.makeText(MainActivity.this, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_SHORT).show();
                permisoSolicitadoDesdeBoton = true;
                verificarYPedirPermisosDeCamara();
                return;
            }
            escanear();
        }
        else{
            BuscarPorCodigo(et.getText().toString());
        }

    }

    // Busca directo en la base de datos segun el texto que tenga el EditText
    public void BuscarPorCodigo(String codigo){
        if(codigo.contains("+")){
            codigo=codigo.replace("+","");
        }

        String [] datos= CargarBD(codigo);

        tvCodigoLeido.setText(codigo);
        tvProvincia.setText(datos[0]);
        tvNombreSitio.setText(datos[1]);
    }

    // Cambia de estado entre Buscar y Escanear
    public void CambiarEstado(View view){
        Button btnEscanear=findViewById(R.id.btnEscanear);
        EditText et=findViewById(R.id.editText);
        if (btnEscanear.getText().equals("Escanear")){
            btnEscanear.setText("Buscar");
            //et.setVisibility(View.VISIBLE);
            et.setText("");
        }

        else
            btnEscanear.setText("Escanear");
        //et.setVisibility(View.INVISIBLE);
        et.setText("");
        btnEscanear.setFocusable(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String codigo = data.getStringExtra("codigo");
                    if(codigo.contains("+")){
                        codigo=codigo.replace("+","");
                    }

                    String [] datos= CargarBD(codigo);

                    tvCodigoLeido.setText(codigo);
                    tvProvincia.setText(datos[0]);
                    tvNombreSitio.setText(datos[1]);

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton) {
                        escanear();
                    }
                    permisoCamaraConcedido = true;
                } else {
                    permisoDeCamaraDenegado();
                }
                break;
        }
    }

    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }


    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(MainActivity.this, "No puedes tabEscanear si no das permiso", Toast.LENGTH_SHORT).show();
    }


}