// Generated by view binder compiler. Do not edit!
package com.example.casopractico5.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.casopractico5.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityEditComunityBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btncambiar;

  @NonNull
  public final Button btncancelar;

  @NonNull
  public final ImageView imgedit;

  @NonNull
  public final EditText txtbanderanuevo;

  private ActivityEditComunityBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button btncambiar, @NonNull Button btncancelar, @NonNull ImageView imgedit,
      @NonNull EditText txtbanderanuevo) {
    this.rootView = rootView;
    this.btncambiar = btncambiar;
    this.btncancelar = btncancelar;
    this.imgedit = imgedit;
    this.txtbanderanuevo = txtbanderanuevo;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityEditComunityBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityEditComunityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_edit_comunity, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityEditComunityBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btncambiar;
      Button btncambiar = ViewBindings.findChildViewById(rootView, id);
      if (btncambiar == null) {
        break missingId;
      }

      id = R.id.btncancelar;
      Button btncancelar = ViewBindings.findChildViewById(rootView, id);
      if (btncancelar == null) {
        break missingId;
      }

      id = R.id.imgedit;
      ImageView imgedit = ViewBindings.findChildViewById(rootView, id);
      if (imgedit == null) {
        break missingId;
      }

      id = R.id.txtbanderanuevo;
      EditText txtbanderanuevo = ViewBindings.findChildViewById(rootView, id);
      if (txtbanderanuevo == null) {
        break missingId;
      }

      return new ActivityEditComunityBinding((ConstraintLayout) rootView, btncambiar, btncancelar,
          imgedit, txtbanderanuevo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}