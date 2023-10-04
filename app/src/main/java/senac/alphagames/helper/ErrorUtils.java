package senac.alphagames.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import senac.alphagames.api.HttpServiceGenerator;
import senac.alphagames.model.ApiError;
import senac.alphagames.ui.login.LoginActivity;

public class ErrorUtils {
    public static ApiError parseError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter = HttpServiceGenerator.retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);

        ApiError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiError();
        }

        return error;
    }

    public static void showErrorMessage(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Erro");
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static boolean isValidResponse(Context context, Response<?> response) {
        if (response.isSuccessful()) {
            return true;
        }

        if (response.code() == 401 || response.code() == 403) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            context.startActivity(loginIntent);

            return false;
        }

//        ApiError apiError = parseError(response);
//        showErrorMessage(context, apiError.getMessage());

        showErrorMessage(context, "Ocorreu um erro. Tente novamente mais tarde.");
        return false;
    }
}
