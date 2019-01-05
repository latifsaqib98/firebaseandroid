package ims.hacker.secondfire;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Objects;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    Button signout,imagebtn;
    TextView welcome, email;
    int GALLERY_INTENT = 2;
    EditText emailedt;
    FirebaseStorage storage;
    StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        storage=FirebaseStorage.getInstance();

        mStorage = storage.getReference();

        signout= (Button) findViewById(R.id.signout);
        email=(TextView) findViewById(R.id.email);
        emailedt=(EditText)findViewById(R.id.emailedt);
        imagebtn = (Button) findViewById(R.id.imagebtn);


        email.setText(getText(R.id.emailedt).toString());
        signout.setOnClickListener(this);
        imagebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AccountActivity.this, MainActivity.class));
        }
        if (view.getId()==R.id.imagebtn)
        {
            Intent intent = new Intent(Intent.ACTION_PICK);
            File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String pictureDirectoryPath = pictureDirectory.getPath();
            Uri data = Uri.parse(pictureDirectoryPath);
            intent.setDataAndType(data, "image/*");

            startActivityForResult(intent, GALLERY_INTENT);

            StorageReference filepath = mStorage.child("Photos").child(data.getLastPathSegment());
            filepath.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AccountActivity.this, "Upload Done ", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
