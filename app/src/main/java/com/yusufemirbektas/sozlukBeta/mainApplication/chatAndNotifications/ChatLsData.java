package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ChatLsData implements Parcelable {
    int userCode;
    int date;

    protected ChatLsData(Parcel in) {
        userCode = in.readInt();
        date = in.readInt();
    }

    public static final Creator<ChatLsData> CREATOR = new Creator<ChatLsData>() {
        @Override
        public ChatLsData createFromParcel(Parcel in) {
            return new ChatLsData(in);
        }

        @Override
        public ChatLsData[] newArray(int size) {
            return new ChatLsData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(userCode);
        dest.writeInt(date);
    }
}
