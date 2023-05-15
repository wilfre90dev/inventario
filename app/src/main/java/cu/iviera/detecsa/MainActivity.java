package cu.iviera.detecsa;



import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int CODIGO_INTENT = 2;
    private AutoCompleteTextView acNombre, acSap, acArea, acUbicacion, acCargo, acFijo, acMovil, acEmail;
    ArrayList<Contacto> listaContactos =new ArrayList<>();
    ArrayList<String> listaNombres=new ArrayList<>();
    ArrayList<String> listaSap=new ArrayList<>();
    ArrayList<String> listaAreas=new ArrayList<>();
    ArrayList<String> listaCargos=new ArrayList<>();
    ArrayList<String> listaUbicaciones=new ArrayList<>();
    ArrayList<String> listaMovil=new ArrayList<>();
    ArrayList<String> listaEmail=new ArrayList<>();
    ArrayList<String> listaFijos=new ArrayList<>();
    RecyclerView recyclerContactos;
    Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnBuscar = findViewById(R.id.btnBuscar);
        acNombre= (AutoCompleteTextView) findViewById(R.id.acNombre);
        acSap= (AutoCompleteTextView) findViewById(R.id.acSap);
        acArea= (AutoCompleteTextView) findViewById(R.id.acArea);
        acCargo= (AutoCompleteTextView) findViewById(R.id.acCargo);
        acUbicacion= (AutoCompleteTextView) findViewById(R.id.acUbicacion);
        acFijo= (AutoCompleteTextView) findViewById(R.id.acFijo);
        acMovil= (AutoCompleteTextView) findViewById(R.id.acMovil);
        acEmail= (AutoCompleteTextView) findViewById(R.id.acEmail);

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

       IniciarAutocompletes();

       ActualizarRecycler();

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
            listaSap.add(cursor.getString(cursor.getColumnIndex("sap")));
            listaAreas.add(cursor.getString(cursor.getColumnIndex("area")));
            listaCargos.add(cursor.getString(cursor.getColumnIndex("cargo")));
            listaUbicaciones.add(cursor.getString(cursor.getColumnIndex("ubicacion")));
            listaFijos.add(cursor.getString(cursor.getColumnIndex("fijo")));
            listaMovil.add(cursor.getString(cursor.getColumnIndex("movil")));
            listaEmail.add(cursor.getString(cursor.getColumnIndex("email")));
        }

        LlenarAutocomplete(listaNombres, acNombre);
        LlenarAutocomplete(listaSap, acSap);
        LlenarAutocomplete(listaAreas, acArea);
        LlenarAutocomplete(listaCargos, acCargo);
        LlenarAutocomplete(listaUbicaciones, acUbicacion);
        LlenarAutocomplete(listaFijos, acFijo);
        LlenarAutocomplete(listaMovil, acMovil);
        LlenarAutocomplete(listaEmail, acEmail);

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, listaEmail);
        acEmail.setAdapter(adapter);

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


    public void BuscarContactos(View view) {
        AssetDatabaseHelper dbHelper = new AssetDatabaseHelper(getBaseContext(), "directorio.sqlite");
        try {
            dbHelper.importIfNotExist();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String selection="";

       if(!acArea.getText().toString().isEmpty()){
           selection+= " and directorio.area=\'"+acArea.getText().toString()+"\'";
       }
        if(!acSap.getText().toString().isEmpty()){
            selection+= " and directorio.sap=\'"+acSap.getText().toString()+"\'";
        }
        if(!acNombre.getText().toString().isEmpty()){
            selection+= " and directorio.nombre=\'"+acNombre.getText().toString()+"\'";
        }
        if(!acCargo.getText().toString().isEmpty()){
            selection+= " and directorio.cargo=\'"+acCargo.getText().toString()+"\'";
        }
        if(!acUbicacion.getText().toString().isEmpty()){
            selection+= " and directorio.ubicacion=\'"+acUbicacion.getText().toString()+"\'";
        }
        if(!acFijo.getText().toString().isEmpty()){
            selection+= " and directorio.fijo=\'"+acFijo.getText().toString()+"\'";
        }
        if(!acMovil.getText().toString().isEmpty()){
            selection+= " and directorio.movil=\'"+acMovil.getText().toString()+"\'";
        }
        if(!acEmail.getText().toString().isEmpty()){
            selection+= " and directorio.email=\'"+acEmail.getText().toString()+"\'";
        }

        if (!selection.isEmpty()) {
            selection = "SELECT * FROM directorio WHERE" + selection;

            if (selection.contains("WHERE and")) {
                selection = selection.replace("WHERE and", "WHERE");
            }


            Cursor cursor = dbHelper.getWritableDatabase().rawQuery(selection, null);
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
            }
            ActualizarRecycler();

            if (listaContactos.size() == 1) {
                toastMsg("¡Se ha encontrado un trabajador! \n Vaya a la pestaña Contactos para ver los resultados.");
            } else if (listaContactos.size() > 1) {
                toastMsg("¡Se han encontrado " + listaContactos.size() + " trabajadores! \n Vaya a la pestaña Contactos para ver los resultados.");
            } else {
                toastMsg("¡Ups, no se han encontrado trabajadores, intente de nuevo!");
            }
        }
    }


    public void Llamar(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ 58129882));
        startActivity(callIntent);
    }



    // Envia un correo de contacto
   public void Contact(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","ines.viera@etecsa.cu,secej.vpiv@etecsa.cu", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contacto de DETECSA");
        startActivity(Intent.createChooser(intent, "Elija un cliente de correo:"));
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

}