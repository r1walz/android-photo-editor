package com.rohit.kt.android_photo_editor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class canvasActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    //TODO: Add Variables
    public static DrawingView drawView;
    private ImageButton currPaint, imgView, drawBtn, eraseBtn, newBtn, saveBtn, cropBtn, rotateBtn, textOpenBtn, emojiBtn;
    private LinearLayout paintLayout;
    private Button cancel;
    private String color, timeStamp, eText, gText;
    private float Rotation = 0;
    private Paint p;
    private EmojiconEditText emojiText;
    private boolean textOn = false, emojiOn = false;

    private Intent CropIntent;
    private Uri imageUri;

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
        textOpenBtn = findViewById(R.id.txtBox);
        emojiBtn = findViewById(R.id.emojiBtn);
        cancel = findViewById(R.id.cancel_btn);

        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        drawView.setBrushSize(20);
        drawBtn.setOnClickListener(this);
        eraseBtn.setOnClickListener(this);
        newBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        rotateBtn.setOnClickListener(this);
        textOpenBtn.setOnClickListener(this);
        emojiBtn.setOnClickListener(this);
        drawView.setOnTouchListener(this);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //TODO: if paint is clicked
    public void paintClicked(View view) {
        emojiOn = false;
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
            emojiOn = false;
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
            emojiOn = false;
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
            emojiOn = false;
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
            emojiOn = false;
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            drawView.setDrawingCacheEnabled(true);
            if(drawView.getDrawingCache()!=null)
                drawView.destroyDrawingCache();
            drawView.buildDrawingCache();
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

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
            emojiOn = false;
            drawView.setDrawingCacheEnabled(true);

            if(drawView.getDrawingCache()!=null)
                drawView.destroyDrawingCache();

            drawView.buildDrawingCache();

            String url = MediaStore.Images.Media.insertImage(getContentResolver(), drawView.getDrawingCache(), "tmp_" + System.currentTimeMillis(), "Drawing");
            imageUri = Uri.parse(url);

            CropIntent = CropImage.activity(imageUri).getIntent(canvasActivity.this);

            startActivityForResult(CropIntent, 2);

        } else if (view.getId() == R.id.rotate_btn) {
            emojiOn = false;
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
        } else if (view.getId() == R.id.txtBox) {
            emojiOn = false;
            textDialogAppear();
        } else if (view.getId() == R.id.emojiBtn) {
            emojiDialogAppear();
        }
    }

    private void textDialogAppear() {

        final Dialog dialog = new Dialog(canvasActivity.this);
        dialog.setContentView(R.layout.text_box);

        Button okBtn = dialog.findViewById(R.id.okBtn);
        final EditText input = dialog.findViewById(R.id.inputText);
        final SeekBar seekBar = dialog.findViewById(R.id.txtSlider);
        seekBar.setMax(95);
        final TextView titleTxt = dialog.findViewById(R.id.titleTxt);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textOn = true;
                p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setColor(drawView.drawPaint.getColor());
                p.setTextSize((float) (seekBar.getProgress() + 5) * getResources().getDisplayMetrics().density);

                gText = input.getText().toString();
                Rect dstRect = new Rect();
                p.getTextBounds(gText, 0, gText.length(), dstRect);

                if (!gText.equals(""))
                    Toast.makeText(canvasActivity.this, "Touch where you want to insert text.", Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                titleTxt.setText(String.valueOf(seekBar.getProgress() + 5 + "px"));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                titleTxt.setText(String.valueOf(seekBar.getProgress() + 5 + "px"));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                titleTxt.setText(R.string.input_text);
            }
        });

        dialog.show();
    }

    private void emojiDialogAppear() {
        final Dialog d = new Dialog(canvasActivity.this, android.R.style.Theme_Holo_Light_NoActionBar);
        d.setContentView(R.layout.emoji_chooser);
        View view = d.findViewById(R.id.root_view);
        emojiText = d.findViewById(R.id.emojicon_edit_text);

        final TextView titleText = d.findViewById(R.id.titleText);
        final SeekBar eSeekBar = d.findViewById(R.id.emoji_seek_bar);

        ImageView emojiImage = d.findViewById(R.id.emoji_btn);
        ImageView submitBtn = d.findViewById(R.id.submit_btn);
        EmojIconActions emojiIcons = new EmojIconActions(canvasActivity.this, view, emojiText, emojiImage);
        emojiIcons.ShowEmojIcon();
        emojiIcons.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float size = 5 + eSeekBar.getProgress();
                eText = emojiText.getText().toString();
                emojiOn = true;

                p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setColor(drawView.drawPaint.getColor());
                p.setTextSize(size * getResources().getDisplayMetrics().density);
                Rect dstRect = new Rect();
                p.getTextBounds(eText, 0, eText.length(), dstRect);

                if (!eText.equals(""))
                    Toast.makeText(canvasActivity.this, "Touch where you want to draw Emoji.", Toast.LENGTH_LONG).show();

                d.dismiss();
            }
        });

        eSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                titleText.setText(String.valueOf(seekBar.getProgress() + 5 + "px"));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                titleText.setText(String.valueOf(seekBar.getProgress() + 5 + "px"));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                titleText.setText(R.string.emoji_input);
            }
        });

        d.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            getContentResolver().delete(imageUri, null, null);
            try {
                imageUri = CropImage.getActivityResult(data).getUri();
                cameraActivity.image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                cameraActivity.k = 1;
                drawView.setBackground(new BitmapDrawable(cameraActivity.image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2 && resultCode == RESULT_CANCELED) {
            getContentResolver().delete(imageUri, null, null);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if (emojiOn && !eText.equals("")) {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                drawView.drawCanvas.drawText(eText, touchX, touchY, p);
        } else if (textOn && !gText.equals("")) {
            drawView.drawCanvas.drawText(gText, touchX, touchY, p);
            textOn = false;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawView.drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawView.drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    drawView.drawCanvas.drawPath(drawView.drawPath, drawView.drawPaint);
                    drawView.drawPath.reset();
                    break;
                default:
                    return false;
            }
        }

        drawView.invalidate();
        return true;
    }
}
