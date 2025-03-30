package com.example.basuratrack;

import static android.app.ProgressDialog.show;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserTransactHistoryfrgmnt#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserTransactHistoryfrgmnt extends Fragment {
    String[] item = {"All", "For Quotation", "Finished", "Cancelled"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserTransactHistoryfrgmnt() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserTransactHistoryfrgmnt.
     */
    // TODO: Rename and change types and number of parameters
    public static UserTransactHistoryfrgmnt newInstance(String param1, String param2) {
        UserTransactHistoryfrgmnt fragment = new UserTransactHistoryfrgmnt();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_transac_history, container, false);

        autoCompleteTextView = view.findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<>(requireContext(), R.layout.list_items, item);
//
        autoCompleteTextView.setAdapter(adapterItems);
//
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(requireContext(), "Filtered: " + item, Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update toolbar when this fragment becomes active
        if (getActivity() instanceof UserHome) {
            ((UserHome) getActivity()).setupToolbar("Transaction History", true);
        }
    }

}