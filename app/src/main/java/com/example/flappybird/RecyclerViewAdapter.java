package com.example.flappybird;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    @NonNull

    private final ArrayList<String> mImageNames ;
    private final ArrayList<Integer> mImage ;
    private final ArrayList<Integer> mPrices ;
    private ArrayList<Button> mBuyButtons ;
    private ArrayList<Button> mSelectButtons ;
    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
    private final String userID=user.getUid();
    private final Context mContext;
    private ArrayList<String> avatarsArrayList=new ArrayList<>();



    public RecyclerViewAdapter(ArrayList<String> mImageNames, ArrayList<Integer> mImage, ArrayList<Integer> mPrice,
                               ArrayList<Button> mBuyButtons, ArrayList<Button> mSelectButtons, Context mContext) {
        this.mImageNames = mImageNames;
        this.mImage = mImage;
        this.mPrices = mPrice;
        this.mBuyButtons = mBuyButtons;
        this.mSelectButtons = mSelectButtons;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).asBitmap().load(mImage.get(position)).into(holder.image);
        holder.price.setText(String.valueOf(mPrices.get(position)));
        avatarsArrayList=AppHolder.getUser().getAvatarArrayList();
        for(int i = 0 ; i < AppHolder.getUser().getAvatarArrayList().size();i++){
            if(AppHolder.getUser().getAvatarArrayList().get(i).equals(mImageNames.get(position)))
            {
                holder.buyBtn.setVisibility(View.GONE);
                holder.selectBtn.setVisibility(View.VISIBLE);
            }
        }


        holder.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppHolder.getUser().getMoneyCount() >= mPrices.get(position)) {
                    reference.keepSynced(true);
                    reference.child(userID).child("moneyCount").setValue(AppHolder.getUser().getMoneyCount() - mPrices.get(position) );
                    AppHolder.user.setMoneyCount(AppHolder.getUser().getMoneyCount() - mPrices.get(position));
                    avatarsArrayList.add(String.valueOf(mImageNames.get(position)));
                    AppHolder.getUser().setAvatarArrayList(avatarsArrayList);
                    reference.child(userID).child("avatarArrayList").setValue(avatarsArrayList );

                    holder.buyBtn.setVisibility(View.GONE);
                    holder.selectBtn.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(mContext, "Sorry you don't have enough money", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.keepSynced(true);
                reference.child(userID).child("currentAvatar").setValue(mImageNames.get(position));
                                AppHolder.getUser().setCurrentAvatar(mImageNames.get(position));
                AppHolder.getBitmapControl().setSelectedFlyingBird(position);
                Toast.makeText(mContext,mImageNames.get(position)+" is selected", Toast.LENGTH_SHORT).show();
            }
        });
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        TextView price;
        Button buyBtn;
        Button selectBtn;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.price_tv);
            buyBtn = itemView.findViewById(R.id.buyBtn);
            selectBtn = itemView.findViewById(R.id.selectBtn);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
