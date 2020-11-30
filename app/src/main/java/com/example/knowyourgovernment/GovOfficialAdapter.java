package com.example.knowyourgovernment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GovOfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {
    private static final String TAG = "OfficialAdapter";
    private List<Official> officialList;
    private MainActivity mainAct;

    GovOfficialAdapter(List<Official> oList,MainActivity main){
        this.officialList = oList;
        mainAct = main;
    }
    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemV = LayoutInflater.from(parent.getContext()).inflate(R.layout.officials_in_area,parent,false);
        itemV.setOnClickListener(mainAct);
        return new OfficialViewHolder(itemV);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {
        Official politician = officialList.get(position);

        String name = politician.getPoliticianName();
        String party = politician.getPoliticalParty();
        String politicalTitle = politician.getGovTitle();
        String nameAndPartyAssoc = name + "(" + party + ")";
        holder.governmentPos.setText(politicalTitle);
        holder.nameAndPartyAssoc.setText(nameAndPartyAssoc);
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
