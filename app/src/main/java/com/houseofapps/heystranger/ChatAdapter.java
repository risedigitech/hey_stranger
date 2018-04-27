package com.houseofapps.heystranger;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.houseofapps.heystranger.ChatScreen1.insertImage;

/**
 * Created by abc on 20/04/2018.
 */

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    TextView rv_text_chat,rv_text_time,sd_text_chat,sd_text_time;
    Context context;
    List<ChatMessage> items;
    LayoutInflater inflater;
    ImageView img_rv,img_sd;
    Bitmap decodedByte;
    LinearLayout ll_chat_item;
    RecyclerView chat_recycler;

    public ChatAdapter(ChatScreen1 chatScreen1, List<ChatMessage> items, RecyclerView chat_recycler) {
        this.context = chatScreen1;
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.chat_recycler = chat_recycler;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
            rv_text_chat = (TextView) view.findViewById(R.id.rv_text_chat);
            rv_text_time = (TextView) view.findViewById(R.id.rv_text_time);
            img_rv = (ImageView)view.findViewById(R.id.img_rv);
            ll_chat_item = (LinearLayout) view.findViewById(R.id.ll_chat_item);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_view, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ChatMessage msg = items.get(position);
        Log.e("msg","abc_testing");
        Log.e("msg",""+msg.getChat_msg());
//        chat_recycler.requestFocus(View.FOCUS_DOWN);
        ll_chat_item.requestFocus();
//        rv_text_chat.requestFocus();
//        rv_text_time.requestFocus();
//        img_rv.requestFocus();
   //    ll_chat_item.getParent().requestChildFocus(ll_chat_item, ll_chat_item);
//        sd_text_chat.setText(""+msg.getChat_msg());
//        rv_text_time.setText(""+msg.getChat_timestamp());

        if (msg.getChat_username().equals(msg.getGet_token())){
            ll_chat_item.setGravity(Gravity.END);
        }
        else{
            ll_chat_item.setGravity(Gravity.LEFT);
        }

        if (msg.getData_type().equals("Image")){
            rv_text_chat.setVisibility(View.GONE);
            img_rv.setVisibility(View.VISIBLE);
            final byte[] decodedString = Base64.decode(msg.getChat_msg(), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img_rv.setImageBitmap(decodedByte);
            img_rv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            insertImage(context.getContentResolver(), decodedByte, msg.getChat_timestamp() , msg.getChat_timestamp());
            rv_text_time.setText(""+msg.getChat_timestamp());

            img_rv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ImageOpen.class);
                    intent.putExtra("decodedByte",decodedString);
                    context.startActivity(intent);
                }
            });

        }
        else if(msg.getData_type().equals("Text")){
            img_rv.setVisibility(View.GONE);
            rv_text_chat.setVisibility(View.VISIBLE);
            rv_text_chat.setText(""+msg.getChat_msg());
            rv_text_time.setText(""+msg.getChat_timestamp());
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public  void updateList(List<ChatMessage> list){
        items = list;
        notifyDataSetChanged();
    }

    public static final String insertImage(ContentResolver cr,
                                           Bitmap source,
                                           String title,
                                           String description) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }
        return stringUrl;
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     * @see MediaStore.Images.Media (StoreThumbnail private method)
     */
    private static final Bitmap storeThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND,kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID,(int)id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT,thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH,thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }



}



