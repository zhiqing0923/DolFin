package com.example.dolfinloginsignup;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditExpenseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final int CAMERA_PERM_CODE =101;
    public static final int CAMERA_REQUEST_CODE=102;
    public static final int GALLERY_REQUEST_CODE = 105;
    String[] category = { "Food", "Fashion", "Transport", "Daily Goods", "Travel", "Rent", "Grocery", "Others"};
    private EditText ETname, ETdate, ETamount;
    private Spinner spinner;
    private Button BtnSaveExpense;
    private String expenseCategory;
    private String expenseName;
    private String expenseAmount;
    private String expenseDate;
    private String imageURL;
    String currentPhotoPath;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private Uri contentUri;
    ExpenseInfo expenseInfo;
    Dialog myDialog;
    Query query;
    String key;
    HashMap<String, Object> hashMap;
    public EditExpenseFragment() {
        // Required empty public constructor
    }

    public EditExpenseFragment(String expenseCategory, String expenseName, String expenseAmount, String expenseDate) {
        this.expenseCategory = expenseCategory;
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditExpenseFragment newInstance(String param1, String param2) {
        EditExpenseFragment fragment = new EditExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_expense, container, false);
        myDialog = new Dialog(getActivity());
        ETname = view.findViewById(R.id.ETname);
        ETamount = view.findViewById(R.id.ETamount);
        ETdate = view.findViewById(R.id.ETdate);

//        IVAddInvoice = view.findViewById(R.id.IVAddInvoice);
//        IVAddInvoice.getLayoutParams().height = 200;
//        IVAddInvoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShowPopup();
//            }
//        });

        spinner = view.findViewById(R.id.spinner);
        BtnSaveExpense = view.findViewById(R.id.BtnSaveExpense);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        //spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,category);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        // on below line we are initializing our variables.
        ETdate = view.findViewById(R.id.ETdate);

        Spinner expenseCategorySpinner = view.findViewById(R.id.spinner);
        EditText expenseNameEdt = view.findViewById(R.id.ETname);
        EditText expenseAmountEdt = view.findViewById(R.id.ETamount);
        EditText expenseDateEdt = view.findViewById(R.id.ETdate);
        //ImageView IVinvoice = view.findViewById(R.id.IVAddInvoice);


        expenseNameEdt.setText(expenseName);
        expenseAmountEdt.setText(expenseAmount);
        expenseDateEdt.setText(expenseDate);
        //IVinvoice.setImageURI(Uri.parse(imageURL));

        // on below line we are adding click listener
        // for our pick date button
        ETdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);



                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                ETdate.setText(dayOfMonth + " " + getMonthFormat(monthOfYear + 1) + " " + year);
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ExpenseInfo");
        query = databaseReference.orderByChild("expenseName").equalTo(expenseName);
        hashMap = new HashMap<>();
        expenseInfo = new ExpenseInfo();

        BtnSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!expenseName.equals(ETname.getText().toString())){
                    expenseInfo.setExpenseName(ETname.getText().toString());
                    hashMap.put("expenseName", ETname.getText().toString());
                    updateDatatoFirebase(hashMap);

                }else if(!expenseAmount.equals(ETamount.getText().toString())){
                    expenseInfo.setExpenseAmount(ETamount.getText().toString());
                    hashMap.put("expenseAmount", ETamount.getText().toString());
                    updateDatatoFirebase(hashMap);

                }else if(!expenseDate.equals(ETdate.getText().toString())) {
                    expenseInfo.setExpenseDate(ETdate.getText().toString());
                    hashMap.put("expenseDate", ETdate.getText().toString());
                    updateDatatoFirebase(hashMap);
                }

                //Toast.makeText(getActivity(), "testing done.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void updateDatatoFirebase (HashMap<String, Object> hashMap) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    key = dataSnapshot.getKey();
                }
                databaseReference.child(key).updateChildren(hashMap);

                Toast.makeText(getActivity(), "Updated.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Update failed." + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        return "JAN";
    }

    private String makeDateString(int day, int month, int year)
    {
        return day + " " + getMonthFormat(month + 1) + " " + year;
    }

//    private void uploadToFirebase(Uri uri) {
//        StorageReference fileRef = storageReference.child(System.currentTimeMillis()+"."+getFileExt(uri));
//        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Toast.makeText(getActivity(),"Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity(),"Uploading Fail", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void ShowPopup(){
//        ImageButton btnCamera;
//        ImageButton btnImage;
//        myDialog.setContentView(R.layout.pop_up);
//        btnCamera = myDialog.findViewById(R.id.cameraButton);
//        btnImage = myDialog.findViewById(R.id.imageButton);
//
//        btnCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                askCameraPermission();
//            }
//        });
//
//        btnImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(gallery,GALLERY_REQUEST_CODE);
//            }
//        });
//
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.getWindow().setGravity(Gravity.BOTTOM);
//        myDialog.show();
//    }
//
//    private void askCameraPermission() {
//        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
//        }else{
//            openCamera();
//        }
//    }
//
//    private void openCamera() {
//        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(camera, CAMERA_REQUEST_CODE);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == CAMERA_REQUEST_CODE){
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            IVAddInvoice.setImageBitmap(image);
//            File f = new File(currentPhotoPath);
//            currentPhotoPath = Uri.fromFile(f).getPath().toString();
//        }
//
//        if(requestCode==GALLERY_REQUEST_CODE){
//            contentUri = data.getData();
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            String imageFileName = "JPEG_"+timeStamp+"."+getFileExt(contentUri);
//            Log.d("tag","onActivityResult: Gallery Image Uri: "+ imageFileName);
//            IVAddInvoice.setImageURI(contentUri);
//            currentPhotoPath = contentUri.getPath().toString();
//        }
//    }
//
//    private String getFileExt(Uri contentUri) {
//        ContentResolver c = getActivity().getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(c.getType(contentUri));
//    }
//
//    public File createImageFile() throws IOException {
//        //create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_"+ timeStamp + "_";
//        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,
//                "jpg",
//                storageDir
//        );
//
//        //save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAMERA_PERM_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCamera();
//            } else {
//                Toast.makeText(getActivity(), "Camera Permission is Required to Use Camera", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


}