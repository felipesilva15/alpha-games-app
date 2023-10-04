package senac.alphagames.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

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
        try {
            ResponseBody errorBody = response.errorBody();
            Log.i("ErrorUtils", response.raw().toString());

            if (errorBody != null) {
                String errorBodyString = errorBody.string();
                Gson gson = new Gson();
                return gson.fromJson(errorBodyString, ApiError.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ApiError();
    }

    public static void showErrorMessage(Context context, String message) {
        SharedUtils.showMessage(context, "Erro", message);
    }

    public static void validateUnsuccessfulResponse(Context context, Response<?> response) {
        if ((response.code() == 401 || response.code() == 403) && !(context instanceof LoginActivity)) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            context.startActivity(loginIntent);
            return;
        }

        ApiError apiError = parseError(response);
        showErrorMessage(context, apiError.getMessage());
    }
}
