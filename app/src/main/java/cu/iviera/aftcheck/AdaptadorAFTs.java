package cu.iviera.aftcheck;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
        viewHolderAFTs.linea1.setText("No.Inv: "+listaAFTs.get(i).getInventario()+" --  Inmov: +"+listaAFTs.get(i).getInmovilizado());
        viewHolderAFTs.linea2.setText("C. Costo: "+listaAFTs.get(i).getCentroCosto()+" --  Area: +"+listaAFTs.get(i).getArea());
        viewHolderAFTs.linea3.setText("Descrip: "+listaAFTs.get(i).getDescripcion());
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
        TextView linea1, linea2, linea3;
        ImageView imgCheck;

        public ViewHolderAFTs(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            //Alimentar con la informacion existente
            linea1= (TextView) itemView.findViewById(R.id.tvLinea1);
            linea2= (TextView) itemView.findViewById(R.id.tvLinea2);
            linea3= (TextView) itemView.findViewById(R.id.tvLinea3);
            imgCheck= (ImageView) itemView.findViewById(R.id.imgCheck);
        }

    }
}


