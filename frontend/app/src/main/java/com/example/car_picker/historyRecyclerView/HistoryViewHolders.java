package com.example.car_picker.historyRecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.car_picker.HistrySingle;
import com.example.car_picker.R;

public class HistoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView rideId;
    public TextView time;

    public HistoryViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        rideId = (TextView) itemView.findViewById(R.id.rideId);
        time = (TextView) itemView.findViewById(R.id.time);
    }

    @Override
    public void onClick(View view) {
        Intent i =new Intent(view.getContext(), HistrySingle.class);
        Bundle b = new Bundle();
        b.putString("rideId",rideId.getText().toString());
        i.putExtras(b);
        view.getContext().startActivity(i);
    }
}
