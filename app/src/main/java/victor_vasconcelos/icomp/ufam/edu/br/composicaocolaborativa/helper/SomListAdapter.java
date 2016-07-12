package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.helper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.R;
import victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.Som;

/**
 * Created by Victor Freitas Vasconcelos on 27/05/2016.
 */
public class SomListAdapter extends RecyclerView.Adapter<SomListAdapter.ViewHolder> {

    private List<Som> somList;
    private Context context;
    private OnDataSelected  onDataSelected;
    private int selected_position = -1;

    public SomListAdapter(Context context, OnDataSelected onDataSelected, List<Som> sons) {
        this.context = context;
        this.onDataSelected = onDataSelected;
        this.somList = sons;
    }

    @Override
    public SomListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.som_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SomListAdapter.ViewHolder holder, int position) {

        /*Som som = somList.get(position);*/

        final Som item = somList.get(position);

        if(selected_position == position){
            // Here I am just highlighting the background
            holder.itemView.setBackgroundColor(Color.DKGRAY);
        }else{
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        final ViewHolder aux = holder;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Updating old as well as new positions
                notifyItemChanged(selected_position);
                selected_position = aux.getAdapterPosition();
                notifyItemChanged(selected_position);

                // Do your another stuff for your onClick

                onDataSelected.onDataSelected(v, aux.getAdapterPosition());
            }
        });

        holder.tvNomeSom.setText(item.getNome());


    }

    @Override
    public int getItemCount() {
        return somList.size();
    }

    public static interface OnDataSelected {

        public void onDataSelected(View view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNomeSom;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatOnDataSelectedIfNecessary(v,getAdapterPosition());
                }
            });

            tvNomeSom = (TextView)view.findViewById(R.id.info_text);
        }
    }

    private void treatOnDataSelectedIfNecessary(View view, int position) {
        if(onDataSelected != null) {
            onDataSelected.onDataSelected(view, position);
        }
    }
}
