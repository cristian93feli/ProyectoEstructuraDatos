package com.mycompany.proyectoestructuradatos.vista.componentes;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

public class PlaceholderTextField extends JTextField {
    private String placeholder;
    private Color placeholderColor = Color.GRAY; // Color del placeholder

    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;
        setText(placeholder);
        setForeground(placeholderColor);

        // Agregar un FocusListener para manejar la visibilidad del placeholder
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK); // Color del texto normal
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(placeholderColor);
                }
            }
        });
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
