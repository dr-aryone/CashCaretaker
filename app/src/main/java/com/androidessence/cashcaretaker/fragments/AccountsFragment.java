package com.androidessence.cashcaretaker.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.activities.AddAccountActivity;
import com.androidessence.cashcaretaker.adapters.AccountAdapter;
import com.androidessence.cashcaretaker.core.CoreRecyclerViewFragment;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Fragment that displays a list of accounts to the user.
 *
 * Created by adam.mcneilly on 9/8/16.
 */
public class AccountsFragment extends CoreRecyclerViewFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // UI Elements
    private AccountAdapter accountAdapter;
    private FloatingActionButton floatingActionButton;
    private TextView addFirstAccount;

    // Loader identifier for the CursorLoader.
    private static final int ACCOUNT_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (CoordinatorLayout) inflater.inflate(R.layout.fragment_accounts, container, false);

        getElements(root);
        setupRecyclerView(LinearLayoutManager.VERTICAL);
        setupFloatingActionButton();

        return root;
    }

    @Override
    protected void getElements(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.account_recycler_view);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_account_fab);
        addFirstAccount = (TextView) view.findViewById(R.id.add_first_account_text_view);
    }

    @Override
    protected void setupRecyclerView(int orientation) {
        super.setupRecyclerView(orientation);

        accountAdapter = new AccountAdapter(getActivity());
        recyclerView.setAdapter(accountAdapter);
    }

    /**
     * Prepares the FloatingActionButton of the fragment by setting its click listener.
     */
    private void setupFloatingActionButton(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddAccountActivity();
            }
        });
    }

    /**
     * Starts the Activity for adding a new account.
     */
    private void startAddAccountActivity(){
        Intent addAccount = new Intent(getActivity(), AddAccountActivity.class);
        startActivity(addAccount);
    }

    /**
     * Restart the CursorLoader when the fragment resumes.
     */
    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(ACCOUNT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case ACCOUNT_LOADER:
                return new CursorLoader(
                        getActivity(),
                        CCContract.AccountEntry.CONTENT_URI,
                        AccountAdapter.ACCOUNT_COLUMNS,
                        null,
                        null,
                        CCContract.AccountEntry.COLUMN_NAME
                );
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case ACCOUNT_LOADER:
                accountAdapter.swapCursor(data);
                // If we have no data, hide the recyclerview and show the text view
                recyclerView.setVisibility(data.getCount() == 0 ? View.GONE : View.VISIBLE);
                addFirstAccount.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()){
            case ACCOUNT_LOADER:
                accountAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }
}
