package senac.alphagames.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ImageView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
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

    public static String formatAddress(Address address) {
        String formattedAddress = address.getENDERECO_LOGRADOURO() + ", " + address.getENDERECO_NUMERO();

        if (!address.getENDERECO_COMPLEMENTO().isEmpty()) {
            formattedAddress += " - " + address.getENDERECO_COMPLEMENTO();
        }

        formattedAddress += " - " + address.getENDERECO_CIDADE() + ", " + address.getENDERECO_ESTADO();

        return formattedAddress;
    }

    public static String formatCode(int code, int lenght) {
        if (lenght == 0) {
            lenght = 6;
        }

        String mask = "#%" + lenght + "s";

        return String.format(mask, code).replace(" ", "0");
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(date);
    }
}
