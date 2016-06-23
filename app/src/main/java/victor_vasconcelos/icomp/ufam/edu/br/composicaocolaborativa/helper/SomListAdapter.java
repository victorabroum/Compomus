package victor_vasconcelos.icomp.ufam.edu.br.composicaocolaborativa.helper;

import android.content.Context;
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

        Som som = somList.get(position);
        holder.tvNomeSom.setText(som.getNome());

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
