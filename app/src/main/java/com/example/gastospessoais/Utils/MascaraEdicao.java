package com.example.gastospessoais.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class MascaraEdicao {


        public static final String FORMAT_CPF = "###.###.###-##";
        public static final String FORMAT_FONE = "(###)####-#####";
        public static final String FORMAT_CEP = "#####-###";
        public static final String FORMAT_DATE = "##/##/####";

        /**
         * Método que deve ser chamado para realizar a formatação
         *
         * @param ediTxt
         * @param mask
         * @return
         */
        public static TextWatcher mask(final EditText ediTxt, final String mask) {
            return new TextWatcher() {
                boolean isUpdating;
                String old = "";

                @Override
                public void afterTextChanged(final Editable s) {}

                @Override
                public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

                @Override
                public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                    final String str = MascaraEdicao.unmask(s.toString());
                    String mascara = "";
                    if (isUpdating) {
                        old = str;
                        isUpdating = false;
                        return;
                    }
                    int i = 0;
                    for (final char m : mask.toCharArray()) {
                        if (m != '#' && str.length() > old.length()) {
                            mascara += m;
                            continue;
                        }
                        try {
                            mascara += str.charAt(i);
                        } catch (final Exception e) {
                            break;
                        }
                        i++;
                    }
                    isUpdating = true;
                    ediTxt.setText(mascara);
                    ediTxt.setSelection(mascara.length());
                }
            };
        }

        public static String unmask(final String s) {
            return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
        }

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }


    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    }




