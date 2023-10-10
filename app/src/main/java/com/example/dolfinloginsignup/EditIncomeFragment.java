package com.example.dolfinloginsignup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditIncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditIncomeFragment extends Fragment {

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
    private EditText inputName, inputDate, inputAmount;
    private ImageButton addIncomeButton;
    private String incomeName;
    private String incomeAmount;
    private String incomeDate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Income income;
    Dialog myDialog;
    Query query;
    String key;
    HashMap<String, Object> hashMap;
    public EditIncomeFragment() {
        // Required empty public constructor
    }

    public EditIncomeFragment(String incomeName, String incomeAmount, String incomeDate) {

        this.incomeName = incomeName;
        this.incomeAmount = incomeAmount;
        this.incomeDate = incomeDate;
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
    public static EditIncomeFragment newInstance(String param1, String param2) {
        EditIncomeFragment fragment = new EditIncomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_income, container, false);
        myDialog = new Dialog(getActivity());
        inputName = view.findViewById(R.id.inputName);
        inputAmount = view.findViewById(R.id.inputAmount);
        inputDate = view.findViewById(R.id.inputDate);

//        IVAddInvoice = view.findViewById(R.id.IVAddInvoice);
//        IVAddInvoice.getLayoutParams().height = 200;
//        IVAddInvoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShowPopup();
//            }
//        });

        addIncomeButton = view.findViewById(R.id.addIncomeButton2);


        // on below line we are initializing our variables.
        inputDate = view.findViewById(R.id.inputDate);

        EditText incomeNameEdt = view.findViewById(R.id.inputName);
        EditText incomeAmountEdt = view.findViewById(R.id.inputAmount);
        EditText incomeDateEdt = view.findViewById(R.id.inputDate);

        incomeNameEdt.setText(incomeName);
        incomeAmountEdt.setText(incomeAmount);
        incomeDateEdt.setText(incomeDate);

        // on below line we are adding click listener
        // for our pick date button
        inputDate.setOnClickListener(new View.OnClickListener() {
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
                                inputDate.setText(dayOfMonth + " " + getMonthFormat(monthOfYear + 1) + " " + year);
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
        databaseReference = firebaseDatabase.getReference("Income");
        query = databaseReference.orderByChild("name").equalTo(incomeName);
        hashMap = new HashMap<>();
        income = new Income();

        addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!incomeName.equals(inputName.getText().toString())){
                    income.setName(inputName.getText().toString());
                    hashMap.put("name", inputName.getText().toString());
                    updateDatatoFirebase(hashMap);

                }else if(!incomeAmount.equals(inputAmount.getText().toString())){
                    income.setAmount(inputAmount.getText().toString());
                    hashMap.put("amount", inputAmount.getText().toString());
                    updateDatatoFirebase(hashMap);

                }else if(!incomeDate.equals(inputDate.getText().toString())) {
                    income.setDate(inputDate.getText().toString());
                    hashMap.put("date", inputDate.getText().toString());
                    updateDatatoFirebase(hashMap);
                }

                Toast.makeText(getActivity(), "testing done.", Toast.LENGTH_SHORT).show();
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