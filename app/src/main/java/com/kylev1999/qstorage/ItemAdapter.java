package com.kylev1999.qstorage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemView> {

    ArrayList<Items> itemsArrayList = new ArrayList<>();

    public ItemAdapter(ArrayList<Items> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
    }

    @NonNull
    @Override
    public ItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ItemView(view);
    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemView holder, int position) {
        Items itemv = itemsArrayList.get(position);
        holder.item_name_text.setText(itemv.getName());
        holder.quantity_text.setText(itemv.getQuantity());

    }

    public class ItemView extends  RecyclerView.ViewHolder{

        TextView quantity_text, item_name_text;
        public ItemView(@NonNull View itemView) {
            super(itemView);

            quantity_text =  itemView.findViewById(R.id.quantity_text);
            item_name_text = itemView.findViewById(R.id.item_name_text);

        }
    }


}
