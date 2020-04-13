package com.softflame.chatsapp.viewHolders;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.softflame.chatsapp.R;
import com.softflame.chatsapp.interfaces.OnMessageItemClick;
import com.softflame.chatsapp.models.Attachment;
import com.softflame.chatsapp.models.AttachmentTypes;
import com.softflame.chatsapp.models.Message;
import com.softflame.chatsapp.models.User;
import com.softflame.chatsapp.utils.FileUtils;
import com.softflame.chatsapp.utils.Helper;
import com.softflame.chatsapp.utils.MyFileProvider;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by mayank on 11/5/17.
 */

public class MessageAttachmentAudioViewHolder extends BaseMessageViewHolder {
    TextView text;
    TextView durationOrSize;
    LinearLayout ll;
    ProgressBar progressBar;
    ImageView playPauseToggle;

    private Message message;
    private File file;

    public MessageAttachmentAudioViewHolder(View itemView, OnMessageItemClick itemClickListener) {
        super(itemView, itemClickListener);
        text = itemView.findViewById(R.id.text);
        durationOrSize = itemView.findViewById(R.id.duration);
        ll = itemView.findViewById(R.id.container);
        progressBar = itemView.findViewById(R.id.progressBar);
        playPauseToggle = itemView.findViewById(R.id.playPauseToggle);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Helper.CHAT_CAB)
                    downloadFile();
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
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setData(Message message, int position,Message lastMessage) {
        super.setData(message, position,lastMessage);
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, message.isSelected() ? R.color.colorPrimary : R.color.colorBgLight));
        ll.setBackgroundColor(message.isSelected() ? ContextCompat.getColor(context, R.color.colorPrimary) : isMine() ? Color.WHITE : ContextCompat.getColor(context, R.color.colorBgLight));
        Attachment attachment = message.getAttachment();
        this.message = message;

        boolean loading = message.getAttachment().getUrl().equals("loading");
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        playPauseToggle.setVisibility(loading ? View.GONE : View.VISIBLE);

        file = new File(Environment.getExternalStorageDirectory() + "/"
                +
                context.getString(R.string.app_name) + "/" + AttachmentTypes.getTypeName(message.getAttachmentType()) + (isMine() ? "/.sent/" : "")
                , message.getAttachment().getName());
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            try {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(context, uri);
                String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                int millis = Integer.parseInt(durationStr);
                durationOrSize.setText(TimeUnit.MILLISECONDS.toMinutes(millis) + ":" + TimeUnit.MILLISECONDS.toSeconds(millis));
                mmr.release();
            } catch (Exception e) {
            }
        } else
            durationOrSize.setText(FileUtils.getReadableFileSize(attachment.getBytesCount()));
        text.setText(message.getAttachment().getName());
    }

    //@OnClick(R.id.playPauseToggle)
    public void downloadFile() {
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = MyFileProvider.getUriForFile(context,
                    context.getString(R.string.authority),
                    file);
            intent.setDataAndType(uri, Helper.getMimeType(context, uri)); //storage path is path of your vcf file and vFile is name of that file.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } else if (!isMine() && !message.getAttachment().getUrl().equals("loading")) {
            broadcastDownloadEvent();
        } else {
            Toast.makeText(context, "File unavailable", Toast.LENGTH_SHORT).show();
        }
    }
}
