package com.example.presence.activities;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presence.R;
import com.example.presence.objects.RegularUser;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class UsersAdapter extends FirestoreRecyclerAdapter<
        RegularUser, UsersAdapter.UserViewholder> {
    private OnItemClickListener listener;

    public UsersAdapter(@NonNull FirestoreRecyclerOptions<RegularUser> options)
    {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewholder holder,
                                 @SuppressLint("RecyclerView") int position, @NonNull RegularUser model)
    {
        holder.nickName.setText(model.getNickname());
        holder.phoneNumber.setText(model.getPhoneNumber());
    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user, parent, false);
        return new UsersAdapter.UserViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class UserViewholder extends RecyclerView.ViewHolder {
        private final TextView nickName;
        private final TextView phoneNumber;
        public UserViewholder(@NonNull View itemView)
        {
            super(itemView);

            nickName = itemView.findViewById(R.id.nickName);
            phoneNumber = itemView.findViewById(R.id.phoneNumberText);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
