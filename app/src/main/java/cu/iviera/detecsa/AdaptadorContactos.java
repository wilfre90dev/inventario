package cu.iviera.detecsa;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorContactos extends RecyclerView.Adapter<AdaptadorContactos.ViewHolderAFTs> {
    ArrayList<Contacto> listaContactos;

    public AdaptadorContactos(ArrayList<Contacto> listaContactos) {
        this.listaContactos = listaContactos;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public AdaptadorContactos.ViewHolderAFTs onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, null, false);
        return new ViewHolderAFTs(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull AdaptadorContactos.ViewHolderAFTs viewHolderAFTs, int i) {
        String linea=  listaContactos.get(i).getNombre();
        String linea1= "<u><b>Area:</b></u> "+ listaContactos.get(i).getArea();
        String linea2= "<u><b>Cargo:</b></u> "+ listaContactos.get(i).getCargo();
        String linea3= "<u><b>Ubicacion:</b></u> "+ listaContactos.get(i).getUbicacion();
        String linea4= "<u><b>Email:</b></u> "+ listaContactos.get(i).getEmail();
        String linea5= "<u><b>Fijo:</b></u> "+ listaContactos.get(i).getFijo();
        String linea6= "<u><b>Movil:</b></u> "+ listaContactos.get(i).getMovil();

        viewHolderAFTs.linea.setText(linea);
        viewHolderAFTs.linea1.setText(Html.fromHtml(linea1));
        viewHolderAFTs.linea2.setText(Html.fromHtml(linea2));
        viewHolderAFTs.linea3.setText(Html.fromHtml(linea3));
        viewHolderAFTs.linea4.setText(Html.fromHtml(linea4));
        viewHolderAFTs.linea5.setText(Html.fromHtml(linea5));
        viewHolderAFTs.linea6.setText(Html.fromHtml(linea6));
//        viewHolderAFTs.imgCheck.setImageResource(R.drawable.nochecked);

    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public class ViewHolderAFTs extends RecyclerView.ViewHolder {
        TextView linea, linea1, linea2, linea3, linea4, linea5, linea6;
        ImageView imgCheck;

        public ViewHolderAFTs(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            //Alimentar con la informacion existente
            linea= (TextView) itemView.findViewById(R.id.tvLinea);
            linea1= (TextView) itemView.findViewById(R.id.tvLinea1);
            linea2= (TextView) itemView.findViewById(R.id.tvLinea2);
            linea3= (TextView) itemView.findViewById(R.id.tvLinea3);
            linea4= (TextView) itemView.findViewById(R.id.tvLinea4);
            linea5= (TextView) itemView.findViewById(R.id.tvLinea5);
            linea6= (TextView) itemView.findViewById(R.id.tvLinea6);
//            imgCheck= (ImageView) itemView.findViewById(R.id.imgCheck);
        }

    }
}


