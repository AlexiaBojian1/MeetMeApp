package com.example.presence.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presence.R;
import com.example.presence.objects.Report;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ReportRecyclerAdapter extends FirestoreRecyclerAdapter<Report, ReportRecyclerAdapter.MyViewHolder> {
    private ReportRecyclerAdapter.OnItemClickListener listener;

    public ReportRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Report> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReportRecyclerAdapter.MyViewHolder holder, int position, @NonNull Report model) {
        holder.reportingUser.setText(model.getReporterPhoneNumber());
        holder.reportContentPreview.setText(model.getReportContent());
    }

    @NonNull
    @Override
    public ReportRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report, parent, false);
        return new ReportRecyclerAdapter.MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView reportContentPreview, reportingUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reportingUser = itemView.findViewById(R.id.reportingUser);
            reportContentPreview = itemView.findViewById(R.id.reportContentPreview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(ReportRecyclerAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
