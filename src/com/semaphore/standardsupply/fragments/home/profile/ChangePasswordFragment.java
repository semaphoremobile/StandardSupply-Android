package com.semaphore.standardsupply.fragments.home.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.semaphore.standardsupply.R;
import com.semaphore.standardsupply.activity.HomeActivity;
import com.semaphore.standardsupply.activity.SSActivity;
import com.semaphore.standardsupply.fragments.BaseFragment;
import com.semaphore.standardsupply.handlers.home.profile.ChangePasswordHandler;

public class ChangePasswordFragment extends BaseFragment {

	EditText txtCurrentPassword;
	EditText txtNewPassword;
	EditText txtReconfirmPassword;
	Button btnChange;
	Button btnCancel;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_password, container,false);
        txtCurrentPassword =  (EditText) v.findViewById(R.id.txtCurrentPassword);
        txtNewPassword =  (EditText) v.findViewById(R.id.txtNewPassword);
        txtReconfirmPassword =  (EditText) v.findViewById(R.id.txtReconfirmPassword);
        btnChange = (Button) v.findViewById(R.id.btnChange);
        btnCancel = (Button) v.findViewById(R.id.btnCancel);
        
        btnChange.setOnClickListener(changeListener);
        btnCancel.setOnClickListener(cancelListener);
        return v;
    }
    
    @Override
	protected String getName() {
		return "Change Password";
	}
    
    public void onViewWillAppear() {
    	getActivity().getActionBar().setTitle("Change Password");
    }
    OnClickListener changeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(validate()){
				HomeActivity.hideKeyboard();
					
				ChangePasswordHandler handler = new ChangePasswordHandler((SSActivity) getActivity());
				handler.currentPassword = txtCurrentPassword.getText().toString();
				handler.newPassword = txtNewPassword.getText().toString();
				handler.request();
			}
		}
	};
	
	 OnClickListener cancelListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					HomeActivity.hideKeyboard();
					getActivity().getFragmentManager().popBackStack();
			}
		};
    
	private boolean validate(){
		boolean validated = true;
		return validated;
	}

}
