package com.rohit.kt.android_photo_editor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class canvasActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO: Add Variables
    public static DrawingView drawView;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, cropBtn, rotateBtn;
    private LinearLayout paintLayout;
    private ImageButton imgView;
    private String color;
    private String timeStamp;
    private float Rotation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        if (cameraActivity.k == 9) {
            cameraActivity.k = 0;
            finish();
        }

        drawView = findViewById(R.id.drawing);
        paintLayout = findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        drawBtn = findViewById(R.id.draw_btn);
        eraseBtn = findViewById(R.id.erase_btn);
        newBtn = findViewById(R.id.new_btn);
        saveBtn = findViewById(R.id.save_btn);
        cropBtn = findViewById(R.id.crop_btn);
        rotateBtn = findViewById(R.id.rotate_btn);

        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        drawView.setBrushSize(20);
        drawBtn.setOnClickListener(this);
        eraseBtn.setOnClickListener(this);
        newBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        rotateBtn.setOnClickListener(this);
    }

    //TODO: if paint is clicked
    public void paintClicked(View view) {
        if (view != currPaint) {
            imgView = (ImageButton) view;
            color = view.getTag().toString();

            drawView.setColor(color);
            drawView.setErase(false);
            drawView.setBrushSize(drawView.getLastBrushSize());

            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint = (ImageButton) view;
        }
    }

    //TODO: Add Dialog!!! & set Listeners for buttons
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.draw_btn) {
            final Dialog brushDialog = new Dialog(canvasActivity.this);
            brushDialog.setContentView(R.layout.slider);
            brushDialog.show();

            final TextView titleView = brushDialog.findViewById(R.id.titleView);
            final SeekBar mSeekBar = brushDialog.findViewById(R.id.slider);
            Button okButton = brushDialog.findViewById(R.id.setTextBtn);
            titleView.setText(R.string.select_brush);

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    titleView.setText(String.valueOf(5 + progress + "px"));

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    titleView.setText(String.valueOf(5 + seekBar.getProgress() + "px"));
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    titleView.setText(R.string.select_brush);
                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(5 + mSeekBar.getProgress());
                    drawView.setLastBrushSize(5 + mSeekBar.getProgress());
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
        } else if (view.getId() == R.id.erase_btn) {
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setContentView(R.layout.slider);
            brushDialog.show();

            final TextView titleView = brushDialog.findViewById(R.id.titleView);
            final SeekBar mSeekBar = brushDialog.findViewById(R.id.slider);
            Button okButton = brushDialog.findViewById(R.id.setTextBtn);
            titleView.setText(R.string.select_eraser);

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    titleView.setText(String.valueOf(5 + progress + "px"));

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    titleView.setText(String.valueOf(5 + seekBar.getProgress() + "px"));
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    titleView.setText(R.string.select_eraser);
                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(5 + mSeekBar.getProgress());
                    brushDialog.dismiss();
                }
            });
        } else if (view.getId() == R.id.new_btn) {
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();
        } else if (view.getId() == R.id.save_btn) {
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.setDrawingCacheEnabled(true);
                    Matrix matrix = new Matrix();
                    matrix.setRotate(Rotation);
                    Bitmap b = drawView.getDrawingCache();
                    b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);

                    String imgSaved = MediaStore.Images.Media.insertImage(getContentResolver(), b, timeStamp, "drawing");

                    if (imgSaved != null)
                        Toast.makeText(getApplicationContext(), "Drawing saved to gallery!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Oops! Image could not be saved.", Toast.LENGTH_SHORT).show();
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
        } else if (view.getId() == R.id.crop_btn) {
            //TODO: Add crop body
        } else if (view.getId() == R.id.rotate_btn) {
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setContentView(R.layout.slider);
            brushDialog.show();

            final TextView titleView = brushDialog.findViewById(R.id.titleView);
            final SeekBar mSeekBar = brushDialog.findViewById(R.id.slider);
            Button okButton = brushDialog.findViewById(R.id.setTextBtn);
            titleView.setText(R.string.rotate);
            mSeekBar.setMax(360);

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    titleView.setText(String.valueOf(progress + "\u00B0"));

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    titleView.setText(String.valueOf(seekBar.getProgress() + "\u00B0"));
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    titleView.setText(R.string.rotate);
                    Rotation = seekBar.getProgress();
                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setRotation(mSeekBar.getProgress());
                    mSeekBar.setMax(45);
                    brushDialog.dismiss();
                }
            });
        }
    }
}
