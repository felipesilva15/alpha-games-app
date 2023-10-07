package senac.alphagames.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import senac.alphagames.R;

public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);

        // Define o fundo como transparente
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Retira das ações de cancelamento e dialog
        setCancelable(false);
        setOnCancelListener(null);

        // Define a view
        View view = LayoutInflater.from(context).inflate(R.layout.loading_diaglog, null);
        setContentView(view);
    }
}
