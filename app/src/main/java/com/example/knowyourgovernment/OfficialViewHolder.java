package com.example.knowyourgovernment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

class OfficialViewHolder extends RecyclerView.ViewHolder {
    TextView governmentPos;
    TextView nameAndPartyAssoc;
    ImageView politicianSeparator;
    public OfficialViewHolder(View view) {
        super(view);
        governmentPos = view.findViewById(R.id.government_position);
        nameAndPartyAssoc = view.findViewById(R.id.name_politician);
        politicianSeparator = view.findViewById(R.id.separate_politicians);
    }
}
