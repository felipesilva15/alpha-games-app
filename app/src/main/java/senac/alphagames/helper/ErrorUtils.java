package senac.alphagames.helper;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import senac.alphagames.model.ApiError;
import senac.alphagames.ui.MainActivity;

public class ErrorUtils {
//    public static ApiError parseError(Response<?> response) {
//        Converter<ResponseBody, ApiError> converter = MainActivity.retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
//
//        ApiError error;
//
//        try {
//            error = converter.convert(response.errorBody());
//        } catch (IOException e) {
//            return new ApiError();
//        }
//
//        return error;
//    }
}
