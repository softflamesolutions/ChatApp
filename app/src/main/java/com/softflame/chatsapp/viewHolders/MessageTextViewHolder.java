package com.softflame.chatsapp.viewHolders;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.softflame.chatsapp.utils.Helper;
import com.softflame.chatsapp.utils.KeyboardUtil;
import com.vanniktech.emoji.EmojiTextView;
import com.softflame.chatsapp.R;
import com.softflame.chatsapp.interfaces.OnMessageItemClick;
import com.softflame.chatsapp.models.Message;
import com.softflame.chatsapp.models.User;
import com.softflame.chatsapp.utils.GeneralUtils;
import com.softflame.chatsapp.utils.LinkTransformationMethod;

import java.util.HashMap;

import static com.softflame.chatsapp.adapters.MessageAdapter.MY;

/**
 * Created by mayank on 11/5/17.
 */

public class MessageTextViewHolder extends BaseMessageViewHolder {
    EmojiTextView text;
    LinearLayout ll;

    private Message message;
    String msg;
    private String roll = null;

    private Helper helper;
    private static int _4dpInPx = -1;

    public MessageTextViewHolder(View itemView, View newMessageView, OnMessageItemClick itemClickListener) {
        super(itemView, newMessageView, itemClickListener);

        helper = new Helper(itemView.getContext());
        roll = helper.getRoll();
        text = itemView.findViewById(R.id.text);
        ll = itemView.findViewById(R.id.container);

        text.setTransformationMethod(new LinkTransformationMethod());
        text.setMovementMethod(LinkMovementMethod.getInstance());
        if (_4dpInPx == -1) _4dpInPx = GeneralUtils.dpToPx(itemView.getContext(), 4);
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
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setData(Message message, int position,Message lastMessage) {
        super.setData(message, position,lastMessage);
        this.message = message;
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, message.isSelected() ? R.color.colorPrimary : R.color.colorBgLight));
        ll.setBackgroundColor(message.isSelected() ? ContextCompat.getColor(context, R.color.colorPrimary) : isMine() ? ContextCompat.getColor(context, R.color.light_blue) : ContextCompat.getColor(context, R.color.white));
        text.setText(message.getBody());

        final View sendContainer = ((Activity) itemView.getContext()).findViewById(R.id.sendContainer);
        ImageView startConversation =  ((Activity) itemView.getContext()).findViewById(R.id.end_con);

        if (getItemViewType() == MY) {
            animateView(position);
        }else {


            if(lastMessage.getBody()!=null && lastMessage.getBody().equalsIgnoreCase(context.getString(R.string.conversation_finish))
            && roll.equalsIgnoreCase(context.getString(R.string.patient))){
                sendContainer.setVisibility(View.GONE);

               startConversation.setVisibility(View.VISIBLE);
               startConversation.setImageDrawable(context.getDrawable(R.drawable.add_conversation));


            }else {
                if (roll.equalsIgnoreCase(context.getString(R.string.patient))){
                    sendContainer.setVisibility(View.VISIBLE);
                    startConversation.setVisibility(View.INVISIBLE);
                }



            }
        }
    }

    private void animateView(int position) {
        if (animate && position > lastPosition) {

            itemView.post(new Runnable() {
                @Override
                public void run() {

                    float originalX = cardView.getX();
                    final float originalY = itemView.getY();
                    int[] loc = new int[2];
                    newMessageView.getLocationOnScreen(loc);
                    cardView.setX(loc[0] / 2);
                    itemView.setY(loc[1]);
                    ValueAnimator radiusAnimator = new ValueAnimator();
                    radiusAnimator.setFloatValues(80, _4dpInPx);
                    radiusAnimator.setDuration(850);
                    radiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            cardView.setRadius((Float) animation.getAnimatedValue());
                        }
                    });
                    radiusAnimator.start();
                    cardView.animate().x(originalX).setDuration(900).setInterpolator(new DecelerateInterpolator()).start();
                    itemView.animate().y(originalY - _4dpInPx).setDuration(750).setInterpolator(new DecelerateInterpolator()).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            itemView.animate().y(originalY + _4dpInPx).setDuration(250).setInterpolator(new DecelerateInterpolator()).start();
                        }
                    }, 750);
                }
            });
            lastPosition = position;
//            setAnimation(getAdapterPosition());
            animate = false;
        }
    }

}
