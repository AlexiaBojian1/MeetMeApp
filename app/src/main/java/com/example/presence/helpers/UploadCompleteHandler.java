package com.example.presence.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.presence.activities.HomeActivity;
import com.example.presence.helpers.LabelGetter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class UploadCompleteHandler implements OnCompleteListener {
    Context context;
    Class nextActivityClass;
    List<Parcelable> dataToPass;

    public UploadCompleteHandler(Context context, Class nextActivityClass,
                                 List<Parcelable> dataToPass) {
        this.context = context;
        this.nextActivityClass = nextActivityClass;
        this.dataToPass = dataToPass;
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {
            // go to next screen, passing data along:
            Intent nextIntent = new Intent(context, HomeActivity.class);
            nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            for (Parcelable p : dataToPass) {
                nextIntent.putExtra(LabelGetter.getLabel(p), p);
            }
            context.startActivity(nextIntent);
        } else {
            Toast.makeText(context,
                    "The database could not be reached. Try again later.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
