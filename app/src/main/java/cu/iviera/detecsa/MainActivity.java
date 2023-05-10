package cu.iviera.detecsa;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int CODIGO_INTENT = 2;
    private AutoCompleteTextView acNombre, acArea, acUbicacion, acCargo, acFijo, acMovil, acEmail;
    ArrayList<Contacto> listaContactos =new ArrayList<>();
    ArrayList<String> listaCentroCosto=new ArrayList<>();
    ArrayList<String> listaNombres=new ArrayList<>();
    ArrayList<String> listaAreas=new ArrayList<>();
    ArrayList<String> listaCargos=new ArrayList<>();
    ArrayList<String> listaUbicaciones=new ArrayList<>();
    ArrayList<String> listaMovil=new ArrayList<>();
    ArrayList<String> listaEmail=new ArrayList<>();
    ArrayList<String> listaFijos=new ArrayList<>();
    RecyclerView recyclerContactos;

    Spinner spArea, spCentroCosto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btnEscanear = findViewById(R.id.btnBuscar);
        acNombre= (AutoCompleteTextView) findViewById(R.id.acNombre);
        acArea= (AutoCompleteTextView) findViewById(R.id.acArea);
        acCargo= (AutoCompleteTextView) findViewById(R.id.acCargo);
        acUbicacion= (AutoCompleteTextView) findViewById(R.id.acUbicacion);
        acFijo= (AutoCompleteTextView) findViewById(R.id.acFijo);
        acMovil= (AutoCompleteTextView) findViewById(R.id.acMovil);
        acEmail= (AutoCompleteTextView) findViewById(R.id.acEmail);

//        tvInventario = findViewById(R.id.tvInventario);
//        tvInmovilizado = findViewById(R.id.tvInmovilizado);
//        tvDescripcion = findViewById(R.id.tvDescripcion);
//        tvArea = findViewById(R.id.tvArea);
//        tvCentroCosto = findViewById(R.id.tvCentroCosto);

        //Se define el TabHost
        TabHost th=(TabHost) findViewById(R.id.tabHost);

        //Configurando tab 1
        th.setup();
        TabHost.TabSpec ts1=th.newTabSpec("tab1");
        ts1.setContent(R.id.tabEscanear);
        ts1.setIndicator("BUSCAR");
        th.addTab(ts1);

        //Configurando tab 2
        th.setup();
        TabHost.TabSpec ts2=th.newTabSpec("tab2");
        ts2.setContent(R.id.tabAfts);
        ts2.setIndicator("CONTACTOS");
        th.addTab(ts2);

        //Configurando tab 3
        th.setup();
        TabHost.TabSpec ts3=th.newTabSpec("tab3");
        ts3.setContent(R.id.tabOpciones);
        ts3.setIndicator("OPCIONES");
        th.addTab(ts3);

        //Configuracion del RecyclerView de Contacto
        recyclerContactos= (RecyclerView) findViewById(R.id.rvRecyclerId);
        recyclerContactos.setLayoutManager(new LinearLayoutManager(this));

        //Anadir linea para separar items
        recyclerContactos.addItemDecoration(new DividerItemDecoration(recyclerContactos.getContext(), DividerItemDecoration.VERTICAL));

//        spArea= (Spinner) findViewById(R.id.spArea);
//        spCentroCosto= (Spinner) findViewById(R.id.spCentroCosto);

//        TODO
//        LlenarSpinners();
        IniciarAutocompletes();

//       ActualizarRecycler();

//        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                LlenarContactos();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }


    public void IniciarAutocompletes() {
        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "directorio.sqlite");
        try {
            dbHelper.importIfNotExist();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql= "SELECT * FROM directorio";


        Cursor cursor = dbHelper.getWritableDatabase().rawQuery(sql, null);
        //TextView tv= findViewById(R.id.tvInventario);
        listaContactos.clear();

        while (cursor.moveToNext()) {
            listaContactos.add(new Contacto(cursor.getString(cursor.getColumnIndex("nombre")),
                    cursor.getString(cursor.getColumnIndex("sap")),
                    cursor.getString(cursor.getColumnIndex("area")),
                    cursor.getString(cursor.getColumnIndex("cargo")),
                    cursor.getString(cursor.getColumnIndex("ubicacion")),
                    cursor.getString(cursor.getColumnIndex("fijo")),
                    cursor.getString(cursor.getColumnIndex("movil")),
                    cursor.getString(cursor.getColumnIndex("casa")),
                    cursor.getString(cursor.getColumnIndex("email"))
            ));
            listaNombres.add(cursor.getString(cursor.getColumnIndex("nombre")));
            listaAreas.add(cursor.getString(cursor.getColumnIndex("area")));
            listaCargos.add(cursor.getString(cursor.getColumnIndex("cargo")));
            listaUbicaciones.add(cursor.getString(cursor.getColumnIndex("ubicacion")));
            listaFijos.add(cursor.getString(cursor.getColumnIndex("fijo")));
            listaMovil.add(cursor.getString(cursor.getColumnIndex("movil")));
            listaEmail.add(cursor.getString(cursor.getColumnIndex("email")));
        }

        LlenarAutocomplete(listaNombres, acNombre);
        LlenarAutocomplete(listaAreas, acArea);
        LlenarAutocomplete(listaCargos, acCargo);
        LlenarAutocomplete(listaUbicaciones, acUbicacion);
        LlenarAutocomplete(listaFijos, acFijo);
        LlenarAutocomplete(listaMovil, acMovil);
        LlenarAutocomplete(listaEmail, acEmail);

    }



    public void LlenarAutocomplete(ArrayList<String> list, AutoCompleteTextView ac)
    {

        // Create a new LinkedHashSet
        Set<String> set = new LinkedHashSet<>();

        // Add the elements to set and remove null or empty strings
        set.addAll(list);
        set.remove(null);
        set.remove("");


        String[] arr = new String[set.size()];
        arr= set.toArray(arr);

        // Fill Autocomplete elements
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arr);
        ac.setAdapter(adapter);
    }




    //Llena los Spinners de las opciones con un una lista de areas y centros de costo
    public void LlenarSpinners() {

        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "directorio.sqlite");
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

        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "directorio.sqlite");
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

//        tvInventario.setText(codigo);
//        tvInmovilizado.setText(datos[1]);
//        tvDescripcion.setText(datos[2]);
//        tvArea.setText(datos[4]);
//        tvCentroCosto.setText(datos[5]);

        ActualizarContactos(codigo);
    }

    public void ActualizarContactos (String codigo){
        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "direcotorio.sqlite");
        try {
            dbHelper.importIfNotExist();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql="UPDATE Contactos SET checked=1 WHERE afts.inventario = \'"+codigo+"\'";

       // Cursor cursor=dbHelper.getWritableDatabase().rawQuery(sql,null);
        dbHelper.getWritableDatabase().execSQL(sql);
//        LlenarContactos();
        ActualizarRecycler();
    }

    public void ActualizarRecycler(){
        AdaptadorContactos adaptadorContactos =new AdaptadorContactos(listaContactos);
        recyclerContactos.setAdapter(adaptadorContactos);
        adaptadorContactos.notifyDataSetChanged();
    }

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public void ResetContactos(View view){
        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "directorio.sqlite");
        try {
            dbHelper.importIfNotExist();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql="UPDATE Contactos SET checked=0 WHERE afts.checked = 1";

        // Cursor cursor=dbHelper.getWritableDatabase().rawQuery(sql,null);
        dbHelper.getWritableDatabase().execSQL(sql);
//        LlenarContactos();
        ActualizarRecycler();
        toastMsg("Estado de Contactos por defecto.");
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

//                    tvInventario.setText(codigo);
//                    tvInmovilizado.setText(datos[1]);
//                    tvDescripcion.setText(datos[2]);
//                    tvArea.setText(datos[4]);
//                    tvCentroCosto.setText(datos[5]);
                    ActualizarContactos(codigo);
                }
            }
        }
    }




}