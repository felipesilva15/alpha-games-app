package senac.alphagames.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ImageView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import senac.alphagames.R;
import senac.alphagames.model.Address;

public class SharedUtils {
    public static void showMessage(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static String formatToCurrency(double value) {
        // Crie uma instância do NumberFormat para formatar como moeda
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        // Configure o símbolo de moeda (R$) e a precisão
        currencyFormat.setCurrency(Currency.getInstance("BRL"));
        currencyFormat.setMaximumFractionDigits(2);
        currencyFormat.setMinimumFractionDigits(2);

        // Formate o valor e retorne como uma string
        return currencyFormat.format(value);
    }

    public static void setInvalidImageToImageView(ImageView image) {
        image.setImageResource(R.drawable.ic_image_not_supported);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setPadding(32, 32, 32, 32);
    }

    public static String formatAddres(Address address) {
        String formattedAddress = address.getENDERECO_LOGRADOURO() + ", " + address.getENDERECO_NUMERO();

        if (!address.getENDERECO_COMPLEMENTO().isEmpty()) {
            formattedAddress += " - " + address.getENDERECO_COMPLEMENTO();
        }

        formattedAddress += " - " + address.getENDERECO_CIDADE() + ", " + address.getENDERECO_ESTADO();

        return formattedAddress;
    }
}
