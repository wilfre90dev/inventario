package cu.iviera.aftcheck;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorAFTs extends RecyclerView.Adapter<AdaptadorAFTs.ViewHolderAFTs> {
    ArrayList<AFTs> listaAFTs;

    public AdaptadorAFTs(ArrayList<AFTs> listaAFTs) {
        this.listaAFTs = listaAFTs;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public AdaptadorAFTs.ViewHolderAFTs onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, null, false);
        return new ViewHolderAFTs(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull AdaptadorAFTs.ViewHolderAFTs viewHolderAFTs, int i) {
        String linea1= "<u><b>No.Inv:</b></u> "+listaAFTs.get(i).getInventario();
        String linea2= "<u><b>Area:</b></u> "+listaAFTs.get(i).getArea();
        String linea3= "<u><b>Inmov:</b></u> "+listaAFTs.get(i).getInmovilizado();
        String linea4= "<u><b>C. Costo:</b></u> "+ listaAFTs.get(i).getCentroCosto();
        String linea5= "<u><b>Descrip:</b></u> "+listaAFTs.get(i).getDescripcion();

        viewHolderAFTs.linea1.setText(Html.fromHtml(linea1));
        viewHolderAFTs.linea2.setText(Html.fromHtml(linea2));
        viewHolderAFTs.linea3.setText(Html.fromHtml(linea3));
        viewHolderAFTs.linea4.setText(Html.fromHtml(linea4));
        viewHolderAFTs.linea5.setText(Html.fromHtml(linea5));
        if (listaAFTs.get(i).isChecked) {
            viewHolderAFTs.imgCheck.setImageResource(R.drawable.checked);
        } else {
            viewHolderAFTs.imgCheck.setImageResource(R.drawable.nochecked);
        }
    }

    @Override
    public int getItemCount() {
        return listaAFTs.size();
    }

    public class ViewHolderAFTs extends RecyclerView.ViewHolder {
        TextView linea1, linea2, linea3, linea4, linea5;
        ImageView imgCheck;

        public ViewHolderAFTs(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            //Alimentar con la informacion existente
            linea1= (TextView) itemView.findViewById(R.id.tvLinea1);
            linea2= (TextView) itemView.findViewById(R.id.tvLinea2);
            linea3= (TextView) itemView.findViewById(R.id.tvLinea3);
            linea4= (TextView) itemView.findViewById(R.id.tvLinea4);
            linea5= (TextView) itemView.findViewById(R.id.tvLinea5);
            imgCheck= (ImageView) itemView.findViewById(R.id.imgCheck);
        }

    }
}


