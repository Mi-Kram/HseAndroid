package com.example.homework_21;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.logging.FileHandler;

public class MainActivity extends AppCompatActivity {
    Gallery gallery;
    ActivityResultLauncher<Intent> chooseDirectoryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::onNewDirectory);

    ActivityResultLauncher<Intent> photoViewLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::onNewDirectory);

    final String[] IMAGE_EXTENSIONS = { ".png", ".jpg", ".jpeg", ".bmp" };

    RecyclerView imagesView;
    ImageGridAdapter imagesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        gallery = Gallery.getInstance();

        imagesView = findViewById(R.id.grid);
        imagesView.setLayoutManager(new GridLayoutManager(this, 3));
        imagesAdapter = new ImageGridAdapter(gallery.getImages());
        imagesAdapter.onImageClick = this::onImageClick;
        imagesView.setAdapter(imagesAdapter);
    }

    private void chooseNewDirectory() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        chooseDirectoryLauncher.launch(intent);
    }

    private void onImageClick(Bitmap bitmap) {
        int idx = 0;
        for (; idx < gallery.getImages().size(); idx++) {
            if (gallery.getImages().get(idx) == bitmap) break;
        }
        if (idx == gallery.getImages().size()) return;

        Intent intent = new Intent(this, ImageViewer.class);
        intent.putExtra("idx", idx);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        chooseNewDirectory();
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onNewDirectory(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK) return;

        Intent intent = result.getData();
        if (intent == null) return;

        ArrayList<Uri> files = readImageUris(intent.getData());
        gallery.getImages().clear();
        readImages(files, gallery.getImages());
        imagesAdapter.notifyDataSetChanged();
    }

    private ArrayList<Uri> readImageUris(Uri uriTree) {
        ArrayList<Uri> uriList = new ArrayList<>();
        if (uriTree == null) return uriList;

        // the uri from which we query the files
        Uri uriFolder = DocumentsContract.buildChildDocumentsUriUsingTree(uriTree, DocumentsContract.getTreeDocumentId(uriTree));

        try (Cursor cursor = this.getContentResolver().query(uriFolder,
                new String[]{DocumentsContract.Document.COLUMN_DOCUMENT_ID},
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // build the uri for the file
                    Uri uriFile = DocumentsContract.buildDocumentUriUsingTree(uriTree, cursor.getString(0));

                    if (hasImageExtension(uriFile)) {
                        uriList.add(uriFile);
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.d("mines", e.toString());
        }

        return uriList;
    }

    private void readImages(ArrayList<Uri> uris, ArrayList<Bitmap> images) {
        for (Uri uri : uris) {
            try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
                images.add(BitmapFactory.decodeStream(inputStream));
            } catch (Exception ex) {
                Log.d("mines", ex.toString());
            }
        }
    }

    private boolean hasImageExtension(Uri file) {
        String path;
        for (String ext : IMAGE_EXTENSIONS) {
            path = file.getPath();
            if (path == null) continue;

            if (path.toLowerCase().endsWith(ext)) {
                return true;
            }
        }

        return false;
    }
}