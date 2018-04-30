package com.harrysoft.burstcoinexplorer.accounts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.harrysoft.burstcoinexplorer.BurstExplorer;
import com.harrysoft.burstcoinexplorer.HSBurstExplorer;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.BurstUtils;

import java.math.BigInteger;

public class AccountsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        BurstExplorer burstExplorer = new HSBurstExplorer(getActivity());

        Button demoAccountButton = view.findViewById(R.id.button);
        Button demoTransactionButton = view.findViewById(R.id.button2);

        demoAccountButton.setOnClickListener((v) -> {
            try {
                burstExplorer.viewAccountDetails(BurstUtils.toNumericID("WEBR-T74Q-HQJY-8PUK4"));
            } catch (BurstUtils.ReedSolomon.DecodeException e) {
                e.printStackTrace();
            }
        });

        demoTransactionButton.setOnClickListener((v) -> {
            burstExplorer.viewTransactionDetailsByID(new BigInteger("10944065017260771197"));
        });

        return view;
    }
}
