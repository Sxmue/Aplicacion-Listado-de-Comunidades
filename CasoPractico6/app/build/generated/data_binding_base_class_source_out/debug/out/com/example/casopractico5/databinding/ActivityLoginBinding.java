// Generated by view binder compiler. Do not edit!
package com.example.casopractico5.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.casopractico5.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnLogin;

  @NonNull
  public final CheckBox chkRecordar;

  @NonNull
  public final TextView logoGiratorio;

  @NonNull
  public final MotionLayout motionLayout;

  @NonNull
  public final TextView textView;

  @NonNull
  public final EditText txtName;

  @NonNull
  public final EditText txtPassword;

  private ActivityLoginBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnLogin,
      @NonNull CheckBox chkRecordar, @NonNull TextView logoGiratorio,
      @NonNull MotionLayout motionLayout, @NonNull TextView textView, @NonNull EditText txtName,
      @NonNull EditText txtPassword) {
    this.rootView = rootView;
    this.btnLogin = btnLogin;
    this.chkRecordar = chkRecordar;
    this.logoGiratorio = logoGiratorio;
    this.motionLayout = motionLayout;
    this.textView = textView;
    this.txtName = txtName;
    this.txtPassword = txtPassword;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnLogin;
      Button btnLogin = ViewBindings.findChildViewById(rootView, id);
      if (btnLogin == null) {
        break missingId;
      }

      id = R.id.chkRecordar;
      CheckBox chkRecordar = ViewBindings.findChildViewById(rootView, id);
      if (chkRecordar == null) {
        break missingId;
      }

      id = R.id.logoGiratorio;
      TextView logoGiratorio = ViewBindings.findChildViewById(rootView, id);
      if (logoGiratorio == null) {
        break missingId;
      }

      id = R.id.motionLayout;
      MotionLayout motionLayout = ViewBindings.findChildViewById(rootView, id);
      if (motionLayout == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      id = R.id.txtName;
      EditText txtName = ViewBindings.findChildViewById(rootView, id);
      if (txtName == null) {
        break missingId;
      }

      id = R.id.txtPassword;
      EditText txtPassword = ViewBindings.findChildViewById(rootView, id);
      if (txtPassword == null) {
        break missingId;
      }

      return new ActivityLoginBinding((ConstraintLayout) rootView, btnLogin, chkRecordar,
          logoGiratorio, motionLayout, textView, txtName, txtPassword);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
