package com.example.research;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class FileDocumentAdapter extends RecyclerView.Adapter<FileDocumentAdapter.ViewHolder> {

    AssetManager getAssets;
    ItemClickListener clickListener;
    Context context;

    ArrayList<String> pdfs = new ArrayList<>();
    HashMap<String,String> allDownloads = new HashMap<>();
    HashMap<String,String> allLikes = new HashMap<>();
    HashMap<String,String> allRates = new HashMap<>();

    ArrayList<String> availablePDFs;

    String[] aPDFs;

    //Filtering pdfs them it to array list
    private void ArrayListToArray(ArrayList<String> availablePDFs){
        for(int i=0;i<availablePDFs.size();i++){
            aPDFs[i]=availablePDFs.get(i);
        }
    }
    private void ArrayToArrayList(String [] array){
        for (int i=0;i<array.length;i++){
            pdfs.add(array[i]);
        }
    }
    private void filterPDFS(){
        ArrayListToArray(availablePDFs);
        ArrayToArrayList(aPDFs);
    }
    //End of filtering

    public interface ItemClickListener{
        void onClick(int index,String name);
    }

    FileDocumentAdapter(ArrayList<String> availablePDFs,AssetManager getAssets,Context context, ItemClickListener clickListener){
        this.getAssets = getAssets;
        this.context = context;
        this.clickListener=clickListener;
        this.availablePDFs=availablePDFs;
        aPDFs = new String[availablePDFs.size()];
        filterPDFS();

    }
//    @NonNull
//    private boolean listAssetFiles(String path) {
//
//        String [] list;
//        try {
//            list = getAssets.list(path);
//            if (list.length > 0) {
//                // This is a folder
//                for (String file : list) {
//                    if (!listAssetFiles(path + "/" + file))
//                        return false;
//                    else {
//                        // This is a file
//                        // TODO: add file name to an array list
//                        pdfs.add(file);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            return false;
//        }
//
//        return true;
//    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.file_doucment_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException{
        byte[] buffer = new byte[1024];
        int read;
        while ((read=inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,read);
        }
    }
    private void copyAsset(String name){
        String dirpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReasearchFiles";
        File dir = new File(dirpath);

        if(!dir.exists()){
            dir.mkdirs();
        }

        AssetManager assetManager = getAssets;
        InputStream in = null;
        OutputStream ou = null;

        try{
            in = assetManager.open("pdfs/"+name);
            File outfile = new File(dirpath,name);
            ou = new FileOutputStream(outfile);
            copyFile(in,ou);
            Toast.makeText(context,"Download finished...\n Check ResearchFiles Folder",Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(context,"Download not finished...\n Check ResearchFiles Folder",Toast.LENGTH_SHORT).show();
        }finally {
            if(in!=null){
                try{
                    in.close();
                }catch (IOException f){
                    f.printStackTrace();
                }
            }
            if(ou!=null){
                try{
                    ou.close();
                }catch (IOException j){
                    j.printStackTrace();
                }
            }
        }
    }

    public int sumArray(ArrayList<String> array){
        int sum=0;
        for(String value:array){
            sum+=Integer.parseInt(value);
        }return sum;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final ArrayList<String> values = new ArrayList<>();
        final String pdfName = pdfs.get(viewHolder.getAdapterPosition());
        viewHolder.header.setText(pdfName);

        final Firebase firebase = new Firebase("https://research-a4f46.firebaseio.com/"+pdfName);
        firebase.child("Downloads").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                values.add(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        firebase.child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                values.add(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        firebase.child("Rate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                values.add(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        if (sumArray(values)>8){
            viewHolder.recommended.setText("Recommended");
        }else{
            viewHolder.recommended.setVisibility(View.GONE);
        }


        viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Firebase firebase = new Firebase("https://research-a4f46.firebaseio.com/"+pdfName+"/Downloads");
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String download = dataSnapshot.getValue(String.class);
                        allDownloads.put(pdfName,download);
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                String myD = allDownloads.get(pdfName);
                try{
                    firebase.setValue(String.valueOf(Integer.parseInt(myD)+1));
                    copyAsset(pdfName+".pdf");
                    Toast.makeText(context,"Downloaded",Toast.LENGTH_SHORT).show();
                }catch (NumberFormatException e){
                    Toast.makeText(context,"Check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.setAndroidContext(context);
                final Firebase firebase = new Firebase("https://research-a4f46.firebaseio.com/"+pdfName+"/Likes");
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String like = dataSnapshot.getValue(String.class);
                        allLikes.put(pdfName,like);
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                String myL = allLikes.get(pdfName);
                try{
                    firebase.setValue(String.valueOf(Integer.parseInt(myL)+1));
                }catch(NumberFormatException e){
                    Toast.makeText(context,"Check your internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.setAndroidContext(context);
                final Firebase firebase = new Firebase("https://research-a4f46.firebaseio.com/"+pdfName+"/Rate");
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       String rate = dataSnapshot.getValue(String.class);
                       allRates.put(pdfName,rate);
                   }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                   }
                });
                String myR = allDownloads.get(pdfName);
                try{
                firebase.setValue(String.valueOf(Integer.parseInt(myR)+1));}
                catch (NumberFormatException e){
                    Toast.makeText(context,"Check your internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return pdfs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView header,recommended;
        LinearLayout download;
        LinearLayout like,rate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            header = itemView.findViewById(R.id.header);
            download = itemView.findViewById(R.id.download);
            rate = itemView.findViewById(R.id.rate);
            like = itemView.findViewById(R.id.like);
            recommended = itemView.findViewById(R.id.recommend);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v){
            int index = getAdapterPosition();
            clickListener.onClick(index,header.getText().toString());
        }
    }
}
