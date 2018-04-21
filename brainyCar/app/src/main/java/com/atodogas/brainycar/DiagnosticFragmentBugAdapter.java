package com.atodogas.brainycar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by belenvb on 21/04/2018.
 */

public class DiagnosticFragmentBugAdapter extends RecyclerView.Adapter<DiagnosticFragmentBugAdapter.MyViewHolder> {
    public ArrayList<BugEntity> bugs;

    public DiagnosticFragmentBugAdapter(ArrayList<BugEntity> bugs){
        this.bugs= bugs;
    }

    @Override
    public DiagnosticFragmentBugAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_diagnostic_bug_cardview, parent, false);
        return new DiagnosticFragmentBugAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(DiagnosticFragmentBugAdapter.MyViewHolder holder, int position) {
        holder.bugTitle.setText(bugs.get(position).getTitle());
        holder.bugDescription.setText(bugs.get(position).getDescription());
    }


    @Override
    public int getItemCount() {
        return bugs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView bugTitle, bugDescription;
        public MyViewHolder(View itemView) {
            super(itemView);
            bugTitle = itemView.findViewById(R.id.bugTitle);
            bugDescription = itemView.findViewById(R.id.bugDescription);
        }
    }
}
