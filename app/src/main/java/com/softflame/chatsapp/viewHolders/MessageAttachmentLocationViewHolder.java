package com.softflame.chatsapp.viewHolders;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softflame.chatsapp.R;
import com.softflame.chatsapp.interfaces.OnMessageItemClick;
import com.softflame.chatsapp.models.Message;
import com.softflame.chatsapp.models.User;
import com.softflame.chatsapp.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by mayank on 11/5/17.
 */

public class MessageAttachmentLocationViewHolder extends BaseMessageViewHolder {
    TextView text;
    ImageView locationImage;
    LinearLayout ll;

    String staticMap = "https://maps.googleapis.com/maps/api/staticmap?center=%s,%s&zoom=16&size=512x512&format=jpg";
    String latitude, longitude;

    public MessageAttachmentLocationViewHolder(View itemView, OnMessageItemClick itemClickListener) {
        super(itemView, itemClickListener);
        text = itemView.findViewById(R.id.text);
        locationImage = itemView.findViewById(R.id.locationImage);
        ll = itemView.findViewById(R.id.container);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(true);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClick(false);
                return true;
            }
        });
        locationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Helper.CHAT_CAB && !TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                    Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(mapIntent);
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setData(Message message, int position,Message lastMessage) {
        super.setData(message, position,lastMessage);
        try {
            JSONObject placeData = new JSONObject(message.getAttachment().getData());
            text.setText(placeData.getString("address"));
            latitude = placeData.getString("latitude");
            longitude = placeData.getString("longitude");
            Glide.with(context).load(String.format(staticMap, latitude, longitude)).into(locationImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, message.isSelected() ? R.color.colorPrimary : R.color.colorBgLight));
        ll.setBackgroundColor(message.isSelected() ? ContextCompat.getColor(context, R.color.colorPrimary) : isMine() ? Color.WHITE : ContextCompat.getColor(context, R.color.colorBgLight));
    }
}
