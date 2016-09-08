package com.androidessence.cashcaretaker.dataTransferObjects;

import android.database.Cursor;
import android.os.Parcel;

import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Details about a transaction beyond the required info.
 *
 * Created by adam.mcneilly on 9/7/16.
 */
public class TransactionDetailsR extends TransactionR {
    private Category category;

    public TransactionDetailsR(Cursor cursor) {
        super(cursor);
        // Get category name
        String categoryName = cursor.getString(cursor.getColumnIndex(CCContract.CategoryEntry.COLUMN_DESCRIPTION));
        setCategory(new Category(getCategoryID(), categoryName));
    }

    public TransactionDetailsR(Parcel source) {
        super(source);
        setCategory((Category) source.readParcelable(Category.class.getClassLoader()));
    }

    public static final Creator<TransactionDetailsR> CREATOR = new Creator<TransactionDetailsR>() {
        @Override
        public TransactionDetailsR createFromParcel(Parcel source) {
            return new TransactionDetailsR(source);
        }

        @Override
        public TransactionDetailsR[] newArray(int size) {
            return new TransactionDetailsR[size];
        }
    };

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public static Creator<TransactionDetailsR> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(getCategory(), flags);
    }
}
