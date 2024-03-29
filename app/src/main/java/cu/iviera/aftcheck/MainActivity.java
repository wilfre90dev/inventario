package cu.iviera.aftcheck;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    private TextView tvInventario, tvInmovilizado, tvDescripcion, tvArea, tvCentroCosto;
    ArrayList<AFTs> listaAFTs=new ArrayList<>();
    ArrayList<String> listaCentroCosto=new ArrayList<>();
    ArrayList<String> listaAreas=new ArrayList<>();
    RecyclerView recyclerAFTs;

    Spinner spArea, spCentroCosto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        verificarYPedirPermisosDeCamara();


        Button btnEscanear = findViewById(R.id.btnEscanear);
        tvInventario = findViewById(R.id.tvInventario);
        tvInmovilizado = findViewById(R.id.tvInmovilizado);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvArea = findViewById(R.id.tvArea);
        tvCentroCosto = findViewById(R.id.tvCentroCosto);

        //Se define el TabHost
        TabHost th=(TabHost) findViewById(R.id.tabHost);

        //Configurando tab 1
        th.setup();
        TabHost.TabSpec ts1=th.newTabSpec("tab1");
        ts1.setContent(R.id.tabEscanear);
        ts1.setIndicator("ESCANEAR");
        th.addTab(ts1);

        //Configurando tab 2
        th.setup();
        TabHost.TabSpec ts2=th.newTabSpec("tab2");
        ts2.setContent(R.id.tabAfts);
        ts2.setIndicator("AFTs");
        th.addTab(ts2);

        //Configurando tab 3
        th.setup();
        TabHost.TabSpec ts3=th.newTabSpec("tab3");
        ts3.setContent(R.id.tabOpciones);
        ts3.setIndicator("OPCIONES");
        th.addTab(ts3);

        //Configuracion del RecyclerView de AFTs
        recyclerAFTs= (RecyclerView) findViewById(R.id.rvRecyclerId);
        recyclerAFTs.setLayoutManager(new LinearLayoutManager(this));

        //Anadir linea para separar items
        recyclerAFTs.addItemDecoration(new DividerItemDecoration(recyclerAFTs.getContext(), DividerItemDecoration.VERTICAL));

        spArea= (Spinner) findViewById(R.id.spArea);
        spCentroCosto= (Spinner) findViewById(R.id.spCentroCosto);

        LlenarSpinners();
        LlenarAFTs();

       ActualizarRecycler();

       spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               LlenarAFTs();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

    }

    public void LlenarAFTs() {
        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "afts.sqlite");
        try {
            dbHelper.importIfNotExist();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql= "SELECT * FROM afts WHERE afts.area ='" +spArea.getSelectedItem().toString()+ "' AND afts.centroCosto ='"+spCentroCosto.getSelectedItem().toString()+ "'";


        Cursor cursor = dbHelper.getWritableDatabase().rawQuery(sql, null);
        //TextView tv= findViewById(R.id.tvInventario);
        listaAFTs.clear();

        while (cursor.moveToNext()) {
            listaAFTs.add(new AFTs(cursor.getString(cursor.getColumnIndex("inventario")),
                    cursor.getString(cursor.getColumnIndex("inmovilizado")),
                    cursor.getString(cursor.getColumnIndex("centroCosto")),
                    cursor.getString(cursor.getColumnIndex("area")),
                    cursor.getString(cursor.getColumnIndex("descripcion")),
                    cursor.getInt(cursor.getColumnIndex("checked"))));
        }
    }

    //Llena los Spinners de las opciones con un una lista de areas y centros de costo
    public void LlenarSpinners() {

        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "afts.sqlite");
        try {
            dbHelper.importIfNotExist();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql = "SELECT DISTINCT afts.area, afts.centroCosto  FROM afts";

        Cursor cursor = dbHelper.getWritableDatabase().rawQuery(sql, null);

        while (cursor.moveToNext()) {
            if(!listaCentroCosto.contains(cursor.getString(cursor.getColumnIndex("centroCosto")))){
                listaCentroCosto.add(cursor.getString(cursor.getColumnIndex("centroCosto")));
            }
            listaAreas.add(cursor.getString(cursor.getColumnIndex("area")));
        }

        ArrayAdapter<String> spAreaAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listaAreas);
        spAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spArea.setAdapter(spAreaAdapter);

        ArrayAdapter<String> spCentroCostoAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listaCentroCosto);
        spAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCentroCosto.setAdapter(spCentroCostoAdapter);
    }

    public String[] CargarBD(String codigo){
        String inventario="No se encuentra";
        String inmovilizado="No se encuentra";
        String descripcion="No se encuentra";
        String checked="";
        String area="";
        String centroCosto="";
        String [] resultados=new String[6];

        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "afts.sqlite");
        try {
            dbHelper.importIfNotExist();
        } catch (Exception e) {
            e.printStackTrace();
        }

      String sql="SELECT DISTINCT * FROM afts WHERE afts.inventario = \'"+codigo+"\'";

//        String sql= "SELECT DISTINCT * FROM afts WHERE afts.inventario = '" +codigo+ "' AND afts.area ='" +spArea.getSelectedItem().toString()+ "' AND afts.centroCosto ='"+spCentroCosto.getSelectedItem().toString()+ "'";

        Cursor cursor=dbHelper.getWritableDatabase().rawQuery(sql,null);
        //TextView tv= findViewById(R.id.tvInventario);

        while (cursor.moveToNext()) {
            inventario = cursor.getString(cursor.getColumnIndex("inventario"));
            inmovilizado = cursor.getString(cursor.getColumnIndex("inmovilizado"));
            descripcion = cursor.getString(cursor.getColumnIndex("descripcion"));
            checked = cursor.getString(cursor.getColumnIndex("checked"));
            area = cursor.getString(cursor.getColumnIndex("area"));
            centroCosto = cursor.getString(cursor.getColumnIndex("centroCosto"));

            //System.out.println(provincia);
        }
        resultados[0] =inventario;
        resultados[1] =inmovilizado;
        resultados[2] =descripcion;
        resultados[3] = checked;
        resultados[4] = area;
        resultados[5] = centroCosto;

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
        tvCentroCosto.setText(municipio);
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
    // Envia un correo de contacto
   public void Contact(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","ines.viera@etecsa.cu,alexy.lorenzo@etecsa.cu", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contacto de AFT Check");
        startActivity(Intent.createChooser(intent, "Elija un cliente de correo:"));
    }

    // Busca directo en la base de datos segun el texto que tenga el EditText
    public void BuscarPorCodigo(String codigo){
        String [] datos= CargarBD(codigo);

        tvInventario.setText(codigo);
        tvInmovilizado.setText(datos[1]);
        tvDescripcion.setText(datos[2]);
        tvArea.setText(datos[4]);
        tvCentroCosto.setText(datos[5]);

        ActualizarAFTs(codigo);
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

    public void ActualizarAFTs (String codigo){
        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "afts.sqlite");
        try {
            dbHelper.importIfNotExist();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql="UPDATE afts SET checked=1 WHERE afts.inventario = \'"+codigo+"\'";

       // Cursor cursor=dbHelper.getWritableDatabase().rawQuery(sql,null);
        dbHelper.getWritableDatabase().execSQL(sql);
        LlenarAFTs();
        ActualizarRecycler();
    }

    public void ActualizarRecycler(){
        AdaptadorAFTs adaptadorAFTs=new AdaptadorAFTs(listaAFTs);
        recyclerAFTs.setAdapter(adaptadorAFTs);
        adaptadorAFTs.notifyDataSetChanged();
    }

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public void ResetAFTs(View view){
        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "afts.sqlite");
        try {
            dbHelper.importIfNotExist();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql="UPDATE afts SET checked=0 WHERE afts.checked = 1";

        // Cursor cursor=dbHelper.getWritableDatabase().rawQuery(sql,null);
        dbHelper.getWritableDatabase().execSQL(sql);
        LlenarAFTs();
        ActualizarRecycler();
        toastMsg("Estado de AFTS por defecto.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODIGO_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String codigo = data.getStringExtra("codigo");
                    if(codigo.contains(":")){
                        /*  No. Inventario: 20_242_005768
                            Inmovilizado: 300496964
                            Centro Costo: 50OP200243
                            Area: STA105
                            Descripcion: BEEPERS PIN 5159*/

                    //Divide el QR escaneado por saltos de linea, quedando en la primera posicion "No. Inventario: 20_242_005768", despues se elimina "No. Inventario: "
                        String inventario = codigo.split("\n")[0].replace("No. Inventario: ", "");
                        codigo=inventario;
                    }

                    String [] datos= CargarBD(codigo);

                    tvInventario.setText(codigo);
                    tvInmovilizado.setText(datos[1]);
                    tvDescripcion.setText(datos[2]);
                    tvArea.setText(datos[4]);
                    tvCentroCosto.setText(datos[5]);
                    ActualizarAFTs(codigo);
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
        Toast.makeText(MainActivity.this, "No puedes usar la camara si no das permiso", Toast.LENGTH_SHORT).show();
    }


}